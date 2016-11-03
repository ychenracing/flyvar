package cn.edu.fudan.iipl.flyvar.common;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Template;

@Component
public class MailSenderUtils {

    @Autowired
    private JavaMailSender       mailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    public void sendText(String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("你好");
        mailMessage.setText("这个是一个通过Spring框架来发送邮件的小程序");
        mailMessage.setTo("9197****1@qq.com");
        mailSender.send(mailMessage);
    }

    public void sendFreemarkerTemplate() {
        MimeMessage msg = null;
        try {
            msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "utf-8");
            helper.setFrom("flyvar@sina.cn");
            helper.setTo("315838650@qq.com");
            helper.setSubject(MimeUtility.encodeText("flyvar sample result", "utf-8", "B"));
            helper.setText(getMailText(), true); // true表示text的内容为html

            // 添加内嵌文件，第1个参数为cid标识这个文件,第2个参数为资源
            helper.addInline("welcomePic", new File(
                "/Users/racing/Documents/workspace/flyvar/src/main/webapp/WEB-INF/static/images/flyvar.png")); // 附件内容

            // 这里的方法调用和插入图片是不同的，解决附件名称的中文问题
            File file = new File("/Users/racing/Downloads/jquery-2.2.4.js");
            helper.addAttachment(MimeUtility.encodeWord(file.getName()), file);
        } catch (Exception e) {
            // throw new RuntimeException("error happens", e);
            e.printStackTrace();
        }
        mailSender.send(msg);
        System.out.println("邮件发送成功...");
    }

    /**
     * 通过模板构造邮件内容，参数content将替换模板文件中的${content}标签。
     */
    private String getMailText() throws Exception {
        // 通过指定模板名获取FreeMarker模板实例
        Template template = freeMarkerConfigurer.getConfiguration()
            .getTemplate("queryBySample.ftl");

        // FreeMarker通过Map传递动态数据
        Map<String, String> map = new HashMap<String, String>();
        map.put("sampleName", "hehesample"); // 注意动态数据的key和模板标签中指定的属性相匹配
        map.put("filePath", "heheFilePath");

        // 解析模板并替换动态数据，最终content将替换模板文件中的${content}标签。
        String htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        return htmlText;
    }

}
