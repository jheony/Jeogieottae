package com.example.jeogieottae.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ForwardedHeaderFilter;

public class WebMvcConfig {
    @Bean
    FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        FilterRegistrationBean<ForwardedHeaderFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ForwardedHeaderFilter());
        bean.addUrlPatterns("/accommodations/**");
        return bean;
    }
}
