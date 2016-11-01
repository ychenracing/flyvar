/**
 * ychen. Copyright (c) 2016年10月23日.
 */
package cn.edu.fudan.iipl.flyvar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Controller基类，包含一些通用方法。
 * 
 * @author racing
 * @version $Id: AbstractController.java, v 0.1 2016年10月23日 上午10:57:44 racing Exp $
 */
public abstract class AbstractController {

    public static final Logger logger = LoggerFactory.getLogger(AbstractController.class);

    protected void checkReferer(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String referer = request.getHeader("Referer");
        Assert.isTrue(StringUtils.isNotBlank(userAgent) && StringUtils.isNotBlank(referer)
                      && referer.contains("flyvar"),
            "非法访问！");
    }

    protected String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.isBlank(ip) || ip.equalsIgnoreCase("unknown")) {
            ip = "127.0.0.1";
        }
        return ip;
    }

}
