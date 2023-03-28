package mirna.stukk.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mirna.stukk.Pojo.Message;
import mirna.stukk.mapper.MessageMapper;
import mirna.stukk.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-27 22:25
 **/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //指定输出的时间格式

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JavaMailSender javaMailSender;

    public boolean isEmail(String email){ //判断Email合格不合格
        String s = "^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$";
        Pattern p = Pattern.compile(s);
        Matcher matcher = p.matcher(email);
        boolean b = matcher.matches();
        return b ? true:false;
    }

    @Async("taskExecutor")
    public void send(Message message, String FromEmail, String ToEmail){
        if(message.getCreateTime() == null){
            message.setCreateTime(new Date());
        }
//        插入数据
        this.save(message);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("管理员大大！！！又有人给你发留言啦");
        simpleMailMessage.setText("词条留言来自于用户: " + message.getName() +"\n"+"如果您想回复用户，可以通过TA的邮箱联系: "+message.getEmail()+"\n"+
                "用户留言内容: "+message.getMessage()+"\n"+"留言时间: "+ format.format(message.getCreateTime()));
        simpleMailMessage.setFrom(FromEmail);
        simpleMailMessage.setTo(ToEmail);
        javaMailSender.send(simpleMailMessage);
        String key = "Email:"+message.getEmail();
        redisTemplate.opsForValue().set(key,"1");
        redisTemplate.expire(key,1, TimeUnit.MINUTES);
//        将这个email缓存一下，不要重复发送...
    }

}
