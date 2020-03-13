package com.its.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.its.web.common.interceptor.LoginInterceptor;

/**
  * Description: ItsSpbWebMvcConfig
  * Company: tzz
  * @Author: tzz
  * Date: 2019/07/10 18:22
  */
@Configuration
public class ItsSpbWebMvcConfig implements WebMvcConfigurer {

	/**登录拦截器*/
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //设置默认跳转首页
        registry.addViewController("/").setViewName("/WEB-INF/index.jsp");
        //SpringBoot方式addViewControllers实现传统Controller跳转JSP文件
        //用户管理列表页面
        registry.addViewController("/sysUser/toSysUserManage").setViewName("sysUser/sysUserManage");
        //角色管理列表页面
        registry.addViewController("/sysRole/toSysRoleManage").setViewName("sysRole/sysRoleManage");
        //报表管理列表页面
        registry.addViewController("/sysReport/toSysReportManage").setViewName("sysReport/sysReportManage");
        //mongoDB管理列表页面
        registry.addViewController("/mongoDB/toMongoDBManager").setViewName("mongoDB/mongoDBManager");
    }


	/**
	 * 添加拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> excludePath = new ArrayList<>();
		// 添加不需要拦截的URL
		excludePath.add("/**/*.js");
		excludePath.add("/**/*.css");
		excludePath.add("/**/*.html");
		excludePath.add("/**/*.htm");
		excludePath.add("/**/*.properties");
		excludePath.add("/**/*.jpg");
		excludePath.add("/**/*.JPG");
		excludePath.add("/**/*.png");
		excludePath.add("/**/*.gif");
		excludePath.add("/**/*.jpeg");
		excludePath.add("/**/*.pdf");
		excludePath.add("/**/*.txt");
		//添加登录拦截器
		registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(excludePath);
	}
	
	/**
     * 添加格式化转换器

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new Formatter<Date>() {
            @Override
            public String print(Date date, Locale locale) {
                return null;
            }
            @Override
            public Date parse(String s, Locale locale) throws ParseException {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);
            }

        });
    }*/
}
