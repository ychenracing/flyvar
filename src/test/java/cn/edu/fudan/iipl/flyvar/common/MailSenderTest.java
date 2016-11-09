package cn.edu.fudan.iipl.flyvar.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.edu.fudan.iipl.flyvar.service.FlyvarMailSenderService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:cn/edu/fudan/iipl/flyvar/root/root.xml",
                                    "classpath:cn/edu/fudan/iipl/flyvar/mvc/spring-mvc.xml" })
public class MailSenderTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private JavaMailSender          mailSender;

    @Autowired
    private FlyvarMailSenderService flyvarMailSenderService;

    @Test
    public void sendText() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("你好");
        mailMessage.setText("这个是一个通过Spring框架来发送邮件的小程序");
        mailMessage.setTo("315838650@qq.com");
        mailMessage.setFrom("flyvar@sina.cn");
        mailSender.send(mailMessage);
    }

    @Test
    public void sendFreemarkerTemplate() {
    }

}
