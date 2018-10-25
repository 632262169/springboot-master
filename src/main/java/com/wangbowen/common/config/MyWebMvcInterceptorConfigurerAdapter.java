package com.wangbowen.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.wangbowen.common.interceptor.LogInterceptor;

/**
 * Created by Administrator on 2017/10/16.
 */
@Configuration
public class MyWebMvcInterceptorConfigurerAdapter extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //对来自/admin/** 这个链接来的请求进行拦截，并去除一些不需要记录日志的操作，如打开登录页、等
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/admin/**").
                excludePathPatterns("/admin/", "/admin", "/admin/quit", "/admin/index", "/admin/login",
                        "/admin/sys/user/", "/admin/sys/role/", "/admin/sys/menu/", "/admin/cms/article/",
                        "/admin/sys/log/", "/admin/**/list", "/admin/**/view",
                        "/admin/**/*Ajax",
                        "/admin/treeData/**");
    }
}
