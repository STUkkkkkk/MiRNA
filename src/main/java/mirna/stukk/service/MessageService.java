package mirna.stukk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import mirna.stukk.Pojo.Message;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-27 22:24
 **/
public interface MessageService extends IService<Message> {

    public boolean isEmail(String email);
    public void send(Message message, String FromEmail, String ToEmail);
}
