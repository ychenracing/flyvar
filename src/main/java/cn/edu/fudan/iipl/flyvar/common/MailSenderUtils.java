package cn.edu.fudan.iipl.flyvar.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class MailSenderUtils {

    @Autowired
    private JavaMailSender mailSender;

    public void send(String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("你好");
        mailMessage.setText("这个是一个通过Spring框架来发送邮件的小程序");
        mailMessage.setTo("9197****1@qq.com");
        mailSender.send(mailMessage);
    }

}
