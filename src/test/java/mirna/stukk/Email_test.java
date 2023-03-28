package mirna.stukk;

import mirna.stukk.Pojo.Message;
import mirna.stukk.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-27 22:47
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class Email_test {

    @Autowired
    private MessageService messageService;

    @Value("${FromEmail}")
    private String FromEmail;

    @Value("${ToEmail}")
    private String ToEmail;


    @Test
    public void testEmail(){
        messageService.send(Message.builder().id(1L).email("2682548155@qq.com").name("吴坤坤").message("你们做得真棒").createTime(new Date()).build(),FromEmail,ToEmail);
    }

}
