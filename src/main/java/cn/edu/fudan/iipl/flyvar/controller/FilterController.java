/**
 * ychen. Copyright (c) 2016年11月13日.
 */
package cn.edu.fudan.iipl.flyvar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.edu.fudan.iipl.flyvar.AbstractController;
import cn.edu.fudan.iipl.flyvar.common.AnnovarUtils;
import cn.edu.fudan.iipl.flyvar.common.PathUtils;
import cn.edu.fudan.iipl.flyvar.form.FilterForm;
import cn.edu.fudan.iipl.flyvar.service.AnnotateService;
import cn.edu.fudan.iipl.flyvar.service.CacheService;
import cn.edu.fudan.iipl.flyvar.service.SampleNameService;

/**
 * variation filter controller
 * 
 * @author racing
 * @version $Id: FilterController.java, v 0.1 2016年11月13日 下午10:31:07 racing Exp $
 */
@Controller
public class FilterController extends AbstractController {

    private static final Logger logger           = LoggerFactory.getLogger(FilterController.class);

    private static final String FILTER_JSP       = "filter/filter";

    private static final String QUERY_RESULT_JSP = "filter/result";

    @Autowired
    private PathUtils           pathUtils;

    @Autowired
    private SampleNameService   sampleNameService;

    @Autowired
    private AnnotateService     annotateService;

    @Autowired
    private AnnovarUtils        annovarUtils;

    @Autowired
    private CacheService        cacheService;

    @RequestMapping(value = { "/filter.htm" }, method = { RequestMethod.GET })
    public String showAnnotate(Model model) {
        model.addAttribute("filterForm", new FilterForm());
        return FILTER_JSP;
    }
}
