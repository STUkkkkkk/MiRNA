package mirna.stukk.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mirna.stukk.Pojo.Message;
import mirna.stukk.config.Result;
import mirna.stukk.service.MessageService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-27 22:15
 **/
@RestController
@RequestMapping("/email")
@Api(tags = "发送邮件的接口")
public class EmailController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MessageService messageService;

    @Value("${FromEmail}")
    private String FromEmail;

    @Value("${ToEmail}")
    private String ToEmail;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/sendMessage")
    @ApiOperation(value = "发送用户的留言信息")
    public Result<Boolean> sendMessage(@RequestBody Message message){
        String key = "Email:"+message.getEmail();
        Object value = redisTemplate.opsForValue().get(key);
        if(value != null){
            return Result.error("555","操作频繁，请1分钟后再尝试");
        }
        String email = message.getEmail();
        if(!messageService.isEmail(email)){
            return Result.error("555","邮箱错误");
        }
        rabbitTemplate.convertAndSend("email","message", JSON.toJSONString(message));
//
        return Result.success(true);
    }

}
