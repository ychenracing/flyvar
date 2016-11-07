package cn.edu.fudan.iipl.flyvar.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.edu.fudan.iipl.flyvar.common.FileUtils;
import cn.edu.fudan.iipl.flyvar.service.MailSenderService;
import freemarker.template.Template;

/**
 * @author yorkchen
 * @version $Id: MailSenderServiceImpl.java, v 0.1 2016年11月07日 下午4:17:30 racing Exp $
 */
@Service
public class MailSenderServiceImpl implements MailSenderService {

    private static final Logger    logger = LoggerFactory.getLogger(MailSenderServiceImpl.class);

    @Value("${spring.mail.username:flyvar@sina.cn}")
    private String                 sender;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private JavaMailSender         mailSender;

    @Autowired
    private FreeMarkerConfigurer   freeMarkerConfigurer;

    /* 
     * @see cn.edu.fudan.iipl.flyvar.service.MailSenderService#send(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void send(String subject, String text, String receiver) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject(subject);
            mailMessage.setText(text);
            mailMessage.setTo(receiver);
            mailMessage.setFrom(sender);
            mailSender.send(mailMessage);
        } catch (Exception ex) {
            logger.error("文本邮件发送失败！", ex);
            throw new RuntimeException("系统错误！邮件发送失败！");
        }
    }

    /* 
     * @see cn.edu.fudan.iipl.flyvar.service.MailSenderService#send(java.lang.String, java.lang.String, java.util.Map, java.lang.String, java.util.List, java.util.List)
     */
    @Override
    public void send(String subject, String templateFile, Map<String, Object> params,
                     String receiver, List<Pair<String, String>> imagePairs,
                     List<String> attachmentFilePaths) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "utf-8");
            System.out.println("sender:" + sender);
            helper.setFrom(sender);
            helper.setTo(receiver);
            helper.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));

            // 通过指定模板名获取FreeMarker模板实例
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templateFile);
            String mailText = FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
            helper.setText(mailText, true);

            // 添加内嵌文件，第1个参数为cid标识这个文件，第2个参数为资源
            if (!CollectionUtils.isEmpty(imagePairs)) {
                imagePairs.stream().forEach(imagePair -> {
                    try {
                        helper.addInline(imagePair.getLeft(),
                            Paths.get(FileUtils.getFlyvarRoot(), imagePair.getRight()).toFile());
                    } catch (MessagingException ex) {
                        logger.error("添加邮件内嵌图片失败！imagePair=" + imagePair, ex);
                    }
                });
            }

            if (!CollectionUtils.isEmpty(attachmentFilePaths)) {
                attachmentFilePaths.stream().forEach(attachmentFilePath -> {
                    File file = Paths.get(FileUtils.getFlyvarRoot(), attachmentFilePath).toFile();
                    try {
                        // 这里的方法调用和插入图片是不同的，解决附件名称的中文问题
                        helper.addAttachment(MimeUtility.encodeWord(file.getName()), file);
                    } catch (UnsupportedEncodingException | MessagingException ex) {
                        logger.error("添加邮件附件失败！attachmentFilePath=" + attachmentFilePath, ex);
                    }
                });
            }

            mailSender.send(msg);
            logger.info("模板邮件发送成功！receiver={}", receiver);
        } catch (Exception ex) {
            logger.error("模板邮件发送失败！", ex);
            throw new RuntimeException("系统错误！邮件发送失败！");
        }

    }

    /* 
     * @see cn.edu.fudan.iipl.flyvar.service.MailSenderService#asyncSend(java.lang.String, java.lang.String, java.util.Map, java.lang.String, java.util.List, java.util.List)
     */
    @Override
    public void asyncSend(String subject, String templateFile, Map<String, Object> params,
                          String receiver, List<Pair<String, String>> imagePairs,
                          List<String> attachmentFilePaths) {
        taskExecutor.execute(
            () -> send(subject, templateFile, params, receiver, imagePairs, attachmentFilePaths));
    }

}
