package cn.edu.fudan.iipl.flyvar.service.impl;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import cn.edu.fudan.iipl.flyvar.common.PathUtils;
import cn.edu.fudan.iipl.flyvar.service.FlyvarMailSenderService;
import cn.edu.fudan.iipl.flyvar.service.MailSenderService;

@Service
public class FlyvarMailSenderServiceImpl implements FlyvarMailSenderService {

    private static final Logger logger                  = LoggerFactory
        .getLogger(FlyvarMailSenderServiceImpl.class);

    private static final String FLYVAR_INDEX_IMAGE_PATH = "WEB-INF/static/images/flyvar.png";

    private static final String SAMPLE_TEMPLATE         = "mail/sample.ftl";

    private static final String ANNOTATION_TEMPLATE     = "mail/annotation.ftl";

    private static final String FILTER_TEMPLATE         = "mail/filter.ftl";

    private static final String QUERY_TEMPLATE          = "mail/query.ftl";

    @Autowired
    private MailSenderService   mailSenderService;

    @Autowired
    private PathUtils           pathUtils;

    /*
     * @see cn.edu.fudan.iipl.flyvar.service.FlyvarMailSenderService#sendSnpSample(java.util.List,
     * java.lang.String)
     */
    @Override
    public void sendSnpSample(List<String> sampleNames, String receiver) {
        String subject = "Sample(s) from FlyVar";
        String templateFilePath = SAMPLE_TEMPLATE;
        String indexImageFilePath = FLYVAR_INDEX_IMAGE_PATH;
        Pair<String, String> imagePair = new ImmutablePair<String, String>("flyvar-index",
            indexImageFilePath);
        List<String> samplePaths = sampleNames.stream()
            .map(sampleName -> Paths.get(pathUtils.getSnpSamplesPath(), sampleName).toString())
            .collect(Collectors.toList());
        Map<String, Object> params = Maps.newHashMap();
        params.put("domain", pathUtils.getDomain());
        mailSenderService.asyncSend(subject, templateFilePath, params, receiver,
            Arrays.asList(imagePair), samplePaths);
    }

    /* 
     * @see cn.edu.fudan.iipl.flyvar.service.FlyvarMailSenderService#sendAnnotateResults(java.util.Map, java.lang.String)
     */
    @Override
    public void sendAnnotateResults(Map<String, Object> emailParams, String receiver) {
        String subject = "Annotate result(s) from FlyVar";
        String templateFilePath = ANNOTATION_TEMPLATE;
        String indexImageFilePath = FLYVAR_INDEX_IMAGE_PATH;
        Pair<String, String> imagePair = new ImmutablePair<String, String>("flyvar-index",
            indexImageFilePath);
        emailParams.put("domain", pathUtils.getDomain());
        mailSenderService.send(subject, templateFilePath, emailParams, receiver,
            Arrays.asList(imagePair), null);
    }

    /* 
     * @see cn.edu.fudan.iipl.flyvar.service.FlyvarMailSenderService#sendFilterResults(java.util.Map, java.lang.String)
     */
    @Override
    public void sendFilterResults(Map<String, Object> emailParams, String receiver) {
        String subject = "Filter result(s) from FlyVar";
        String templateFilePath = FILTER_TEMPLATE;
        String indexImageFilePath = FLYVAR_INDEX_IMAGE_PATH;
        Pair<String, String> imagePair = new ImmutablePair<String, String>("flyvar-index",
            indexImageFilePath);
        emailParams.put("domain", pathUtils.getDomain());
        mailSenderService.send(subject, templateFilePath, emailParams, receiver,
            Arrays.asList(imagePair), null);
    }

    @Override
    public void sendQueryResults(Map<String, Object> emailParams, String receiver) {
        String subject = "Query result(s) from FlyVar";
        String templateFilePath = QUERY_TEMPLATE;
        String indexImageFilePath = FLYVAR_INDEX_IMAGE_PATH;
        Pair<String, String> imagePair = new ImmutablePair<String, String>("flyvar-index",
            indexImageFilePath);
        emailParams.put("domain", pathUtils.getDomain());
        mailSenderService.send(subject, templateFilePath, emailParams, receiver,
            Arrays.asList(imagePair), null);
    }

}
