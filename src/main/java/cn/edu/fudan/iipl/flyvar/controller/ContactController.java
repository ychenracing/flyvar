/**
 * ychen. Copyright (c) 2016年11月21日.
 */
package cn.edu.fudan.iipl.flyvar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.edu.fudan.iipl.flyvar.AbstractController;

/**
 * contact us controller
 * 
 * @author racing
 * @version $Id: ContactController.java, v 0.1 2016年11月21日 下午9:37:51 racing Exp $
 */
@Controller
public class ContactController extends AbstractController {

    private static final String CONTACT_JSP = "contact";

    @RequestMapping(value = { "/contact.htm" }, method = { RequestMethod.GET })
    public String showContactUs() {
        return CONTACT_JSP;
    }

}
