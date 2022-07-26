package edu.poly.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import edu.poly.shop.interceptor.AdminAuthenticationInterceptor;

public class AuthenticationInterceptorConfig implements WebMvcConfigurer{
	@Autowired
	private AdminAuthenticationInterceptor adminAuthenticationInterceptor;
	
//	@Autowired
//	private SiteAuthenticationInterceptor siteAuthenticationInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(adminAuthenticationInterceptor).addPathPatterns("/admin/**");
		
		//registry.addInterceptor(siteAuthenticationInterceptor).addPathPatterns("/site/**");
	}
}
