package cn.edu.fudan.iipl.flyvar.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

/**
 * 邮件发送服务
 * 
 * @author racing
 * @version $Id: MailSenderService.java, v 0.1 2016年11月07日 下午3:20:30 racing Exp $
 */
public interface MailSenderService {

    /**
     * 发送文字邮件
     */
    public void send(String subject, String text, String receiver);

    /**
     * 发送模板邮件
     * @param subject 邮件主题
     * @param templateFile 模板文件路径，相对于/WEB-INF/的地址
     * @param params 传递给模板文件的参数
     * @param receiver 接收者地址
     * @param imagePairs 可选参数，邮件内嵌图片Pair，left为模板中该图片的cid，right为图片相对于项目根目录的路径，如“WEB-INF/static/images/flyvar.png”
     * @param attachmentFilePaths 可选参数，附件文件相对于项目根目录的路径，如“WEB-INF/static/js/jquery-2.2.4.js”
     */
    public void send(String subject, String templateFile, Map<String, Object> params,
                     String receiver, List<Pair<String, String>> imagePairs,
                     List<String> attachmentFilePaths);

    /**
     * 异步发送模板邮件
     * @param subject 邮件主题
     * @param templateFile 模板文件路径，相对于/WEB-INF/的地址
     * @param params 传递给模板文件的参数
     * @param receiver 接收者地址
     * @param imagePairs 可选参数，邮件内嵌图片Pair，left为模板中该图片的cid，right为图片相对于项目根目录的路径，如“WEB-INF/static/images/flyvar.png”
     * @param attachmentFilePaths 可选参数，附件文件相对于项目根目录的路径，如“WEB-INF/static/js/jquery-2.2.4.js”
     */
    public void asyncSend(String subject, String templateFile, Map<String, Object> params,
                          String receiver, List<Pair<String, String>> imagePairs,
                          List<String> attachmentFilePaths);
}
