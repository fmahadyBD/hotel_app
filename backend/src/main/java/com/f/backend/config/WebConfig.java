package com.f.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${image.upload.dir}")
    private String uploadDir;

    // @Override
    // public void addCorsMappings(CorsRegistry registry) {

    //     registry.addMapping("/api/**")
    //             .allowedOrigins("http://localhost:4200")
    //             .allowedMethods("GET", "POST", "DELETE", "PUT", "oPTIONS")
    //             .allowedHeaders("*")
    //             .allowCredentials(true);
    // }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                // .addResourceLocations("file:" + uploadDir + "/");
                .addResourceLocations("classpath:/static/images/");

    }

}
