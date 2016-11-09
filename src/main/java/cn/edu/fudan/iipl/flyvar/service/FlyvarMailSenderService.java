package cn.edu.fudan.iipl.flyvar.service;

import java.util.List;

public interface FlyvarMailSenderService {

    /**
     * query by sample中，发送sample邮件给用户
     * @param sampleNames
     * @param receiver
     */
    public void sendSnpSample(List<String> sampleNames, String receiver);

}
