package com.its.web;

import com.its.web.controller.login.VerifyCodeServlet;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * its-spb-web启动入口<br>
 * scanBasePackages:修改自动扫描包的Package,默认只会扫描同级目录及子目录
 */
@SpringBootApplication(scanBasePackages = "com.its")
@MapperScan("com.its.core.mybatis.dao.mapper")
public class ItsSpbWebApplication {

    /**
     * 注册图形验证码Servlert
     */
    @Bean
    public ServletRegistrationBean<VerifyCodeServlet> getVerifyCodeServlet() {
        ServletRegistrationBean<VerifyCodeServlet> registrationBean = new ServletRegistrationBean<VerifyCodeServlet>(new VerifyCodeServlet());
        registrationBean.addUrlMappings("/verifyCodeServlet");
        registrationBean.addInitParameter("num", "4");
        registrationBean.addInitParameter("content", "a");
        return registrationBean;
    }


    public static void main(String[] args) {
        SpringApplication.run(ItsSpbWebApplication.class, args);
    }
}
