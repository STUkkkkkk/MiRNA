package mirna.stukk.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import mirna.stukk.Pojo.Message;
import mirna.stukk.service.MessageService;
import org.springframework.amqp.core.ExchangeTypes;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-04-09 14:04
 **/
@Component
@Slf4j
public class EmailMQListener {

    @Autowired
    private MessageService messageService;

    @Value("${FromEmail}")
    private String FromEmail;

    @Value("${ToEmail}")
    private String ToEmail;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "emailMessageQueue"), //指的是用来发送用户留言的信息
            exchange = @Exchange(name = "email"),
            key = "message"))
    public void EmailListener(String message){
        JSONObject jsonObject = JSON.parseObject(message);
        log.info("接收到了 emailMessageQueue 里的消息："+ jsonObject.get("email"));
        messageService.send(Message.builder().email(jsonObject.get("email").toString()).name(jsonObject.get("name").toString()).message(jsonObject.get("message").toString()).build(), FromEmail, ToEmail);
    }



}
