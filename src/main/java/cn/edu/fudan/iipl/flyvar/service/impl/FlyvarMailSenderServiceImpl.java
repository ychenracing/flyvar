package cn.edu.fudan.iipl.flyvar.service.impl;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import cn.edu.fudan.iipl.flyvar.common.DateUtils;
import cn.edu.fudan.iipl.flyvar.service.FlyvarMailSenderService;
import cn.edu.fudan.iipl.flyvar.service.MailSenderService;

@Service
public class FlyvarMailSenderServiceImpl implements FlyvarMailSenderService {

    @Autowired
    private MailSenderService mailSenderService;

    /* 
     * @see cn.edu.fudan.iipl.flyvar.service.FlyvarMailSenderService#sendSnpSample(java.util.List, java.lang.String)
     */
    @Override
    public void sendSnpSample(List<String> sampleNames, String receiver) {
        String subject = "Sample(s) from FlyVar";
        String templateFilePath = "mail/sample.ftl";
        String indexImageFilePath = "WEB-INF/static/images/flyvar.png";
        Pair<String, String> imagePair = new ImmutablePair<String, String>("flyvar-index",
            indexImageFilePath);
        String sampleFolder = "WEB-INF/file/snpSamples";
        List<String> samplePaths = sampleNames.stream()
            .map(sampleName -> Paths.get(sampleFolder, sampleName).toString())
            .collect(Collectors.toList());
        mailSenderService.asyncSend(subject, templateFilePath, getSampleParamMap(), receiver,
            Arrays.asList(imagePair), samplePaths);
    }

    private Map<String, Object> getSampleParamMap() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("date", DateUtils.formatGeneral(DateUtils.currentDate()));
        return map;
    }

}
