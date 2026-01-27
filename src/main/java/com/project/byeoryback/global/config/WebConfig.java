package com.project.byeoryback.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String envOrigins = System.getenv("CORS_ALLOWED_ORIGINS");
        String[] allowedOrigins;

        if (envOrigins != null && !envOrigins.isBlank()) {
            String[] envOriginArray = envOrigins.split(",");
            allowedOrigins = new String[envOriginArray.length + 2];
            System.arraycopy(envOriginArray, 0, allowedOrigins, 0, envOriginArray.length);
            allowedOrigins[envOriginArray.length] = "http://localhost:5173";
            allowedOrigins[envOriginArray.length + 1] = "https://byeory.vercel.app";
        } else {
            allowedOrigins = new String[] {
                    "http://localhost:5173",
                    "https://byeory.vercel.app"
            };
        }

        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
