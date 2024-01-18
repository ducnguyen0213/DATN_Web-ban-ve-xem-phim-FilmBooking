//package com.example.filmBooking.config;
//
//import com.example.filmBooking.interceptor.AuthInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//@Configuration
//public class MyAppConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AuthInterceptor())
//                .addPathPatterns("/user/**", "/profile")
//                .excludePathPatterns("/login", "/register", "/forgot-password", "/filmbooking/trangchu");
//    }
//}
