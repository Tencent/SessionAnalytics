package com.session.path.data.userpath.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author v_liuyuxing
 * @version 0.1
 * @date 2021/3/16 11:35 上午
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("POST", "GET", "PATCH", "DELETE", "PUT", "OPTIONS")
                .allowedOrigins("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
