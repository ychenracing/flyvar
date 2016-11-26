/**
 * ychen. Copyright (c) 2016年11月25日.
 */
package cn.edu.fudan.iipl.flyvar.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

/**
 * FlyVar security configuration
 * 
 * @author racing
 * @version $Id: FlyvarSecurityConfigurer.java, v 0.1 2016年11月25日 下午8:57:10 racing Exp $
 */
@Configuration
@EnableWebMvcSecurity
public class FlyvarSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll();
    }
}
