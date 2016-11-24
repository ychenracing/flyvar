package cn.edu.fudan.iipl.flyvar.service;

import java.util.List;
import java.util.Map;

public interface FlyvarMailSenderService {

    /**
     * query by sample中，发送sample邮件给用户
     * @param sampleNames
     * @param receiver
     */
    public void sendSnpSample(List<String> sampleNames, String receiver);

    /**
     * send an email to user with annotate results. 
     * 
     * @param emailParams
     * @param receiver
     */
    public void sendAnnotateResults(Map<String, Object> emailParams, String receiver);

    /**
     * send an email to user with filter results. 
     * 
     * @param emailParams
     * @param receiver
     */
    public void sendFilterResults(Map<String, Object> emailParams, String receiver);

    /**
     * send an email to user with query results. 
     * 
     * @param emailParams
     * @param receiver
     */
    public void sendQueryResults(Map<String, Object> emailParams, String receiver);

}
