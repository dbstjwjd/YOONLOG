package com.project.team;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private String restaurantPath = "C:/uploads/restaurant";

    private String reviewPath = "C:/uploads/review";


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/restaurant/image/**")
                .addResourceLocations("file:" + restaurantPath + "/");

        registry.addResourceHandler("/review/image/**")
                .addResourceLocations("file:" + reviewPath + "/");
    }
}
