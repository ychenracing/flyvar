/**
 * ychen. Copyright (c) 2016年10月23日.
 */
package cn.edu.fudan.iipl.flyvar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;

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
            "Invalid access！");
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
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ip) || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ip) || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.isBlank(ip) || ip.equalsIgnoreCase("unknown")) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    protected String getClientMACAddress(String ip) {
        String macAddress = "";
        try {
            // 如果为127.0.0.1，则获取本地MAC地址。  
            String loopbackAddress = "127.0.0.1";
            if (loopbackAddress.equals(ip)) {
                InetAddress inetAddress = InetAddress.getLocalHost();
                // 貌似此方法需要JDK1.6。  
                byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
                //下面代码是把mac地址拼装成String。
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    if (i != 0) {
                        sb.append("-");
                    }
                    // mac[i] & 0xFF 是为了把byte转化为正整数。
                    String s = Integer.toHexString(mac[i] & 0xFF);
                    sb.append(s.length() == 1 ? 0 + s : s);
                }
                // 把字符串所有小写字母改为大写成为正规的mac地址并返回。
                return sb.toString().trim().toUpperCase();
            }
            // 获取非本地IP的MAC地址。
            Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
            String line = "";
            String macAddressPrefix = "MAC Address = ";
            try (InputStreamReader isr = new InputStreamReader(p.getInputStream());
                    BufferedReader br = new BufferedReader(isr)) {
                while ((line = br.readLine()) != null) {
                    if (line != null) {
                        int index = line.indexOf(macAddressPrefix);
                        if (index != -1) {
                            macAddress = line.substring(index + macAddressPrefix.length()).trim()
                                .toUpperCase();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Achieve client mac address error!", ex);
        }
        return macAddress;
    }

}
