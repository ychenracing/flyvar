/**
 * ychen. Copyright (c) 2016年10月22日.
 */
package cn.edu.fudan.iipl.flyvar.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;

import cn.edu.fudan.iipl.flyvar.AbstractController;
import cn.edu.fudan.iipl.flyvar.common.DateUtils;
import cn.edu.fudan.iipl.flyvar.model.VisitLog;
import cn.edu.fudan.iipl.flyvar.service.VisitService;

/**
 * 首页Controller
 * 
 * @author racing
 * @version $Id: HomeController.java, v 0.1 2016年10月22日 下午9:43:28 racing Exp $
 */
@Controller
public class HomeController extends AbstractController {

    private static final Logger logger   = LoggerFactory.getLogger(HomeController.class);

    private static final String HOME_JSP = "index";

    @Autowired
    private VisitService        visitService;

    @RequestMapping(value = { "/", "/index.htm" }, method = { RequestMethod.GET })
    public String home(HttpServletRequest request) {
        return HOME_JSP;
    }

    @RequestMapping(value = { "/visit.json" }, method = { RequestMethod.POST })
    @ResponseBody
    public Map<String, Object> visit(HttpServletRequest request) {
        checkReferer(request);
        Map<String, Object> result = Maps.newHashMap();
        VisitLog visitLog = new VisitLog(getClientIP(request),
            DateUtils.formatGeneral(DateUtils.current()));
        HttpSession session = request.getSession();
        if (session == null) {
            logger.info("新访问记录: visitLog={}", visitLog);
            visitService.visit(visitLog);
        }
        result.put("visit", visitService.getTotalVisitTime());
        return result;
    }

}
