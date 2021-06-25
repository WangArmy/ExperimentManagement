package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("auth");
        registry.addViewController("/login").setViewName("auth");
        registry.addViewController("/login.html").setViewName("auth");
        registry.addViewController("/main.html").setViewName("index");
        registry.addViewController("/teacher/main.html").setViewName("teacher/index");


        registry.addViewController("/admin/").setViewName("login");
        registry.addViewController("/admin/login").setViewName("login");
        registry.addViewController("/admin/login.html").setViewName("login");
        registry.addViewController("/admin/main.html").setViewName("manage");

        //registry.addViewController("/admin/stu.html").setViewName("manageStu");
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new MyLocaleResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login.html","/", "/login","/static/**", "/admin/","/admin/login" ,"/admin/login.html");
    }
}
