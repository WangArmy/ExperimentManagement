package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/*
* 国际化配置
* */
@Configuration
public class MyLocaleResolver implements LocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {

        String lan = request.getParameter("lan");
        Locale locale = Locale.getDefault();

        if(!StringUtils.isEmpty(lan)) {
            String[] split = lan.split("_");
            locale = new Locale(split[0], split[1]);
        }
        return locale;
    }


    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }

}
