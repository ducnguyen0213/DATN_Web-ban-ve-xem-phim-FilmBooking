package com.example.filmBooking.config;

import com.example.filmBooking.service.impl.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        AntPathRequestMatcher.antMatcher("/api/**"),
                        AntPathRequestMatcher.antMatcher("/vnpay-payment"),
                        AntPathRequestMatcher.antMatcher("/submitOrder")))
                .authorizeHttpRequests(auth -> auth
                        // Cho phép tất cả: đăng nhập, đăng ký, tài nguyên tĩnh (CSS/JS/img của trang user)
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/filmbooking/login"),
                                AntPathRequestMatcher.antMatcher("/filmbooking/forgot-password"),
                                AntPathRequestMatcher.antMatcher("/filmbooking/reset-password"),
                                AntPathRequestMatcher.antMatcher("/filmbooking/dangky"),
                                AntPathRequestMatcher.antMatcher("/filmbooking/dangky/save"),
                                AntPathRequestMatcher.antMatcher("/filmbooking/trangchu"),
                                AntPathRequestMatcher.antMatcher("/filmbooking/phimchieu"),
                                AntPathRequestMatcher.antMatcher("/filmbooking/lichchieu"),
                                AntPathRequestMatcher.antMatcher("/filmbooking/search/schedule"),
                                AntPathRequestMatcher.antMatcher("/filmbooking/movie/edit/**"),
                                AntPathRequestMatcher.antMatcher("/filmbooking/rank-and-membership"),
                                AntPathRequestMatcher.antMatcher("/filmbooking"),
                                AntPathRequestMatcher.antMatcher("/users/**"),
                                AntPathRequestMatcher.antMatcher("/admin/login"),
                                AntPathRequestMatcher.antMatcher("/admin/assets/**"),
                                AntPathRequestMatcher.antMatcher("/admin/css/**"),
                                AntPathRequestMatcher.antMatcher("/admin/js/**"),
                                AntPathRequestMatcher.antMatcher("/css/**"),
                                AntPathRequestMatcher.antMatcher("/js/**"),
                                AntPathRequestMatcher.antMatcher("/images/**"),
                                AntPathRequestMatcher.antMatcher("/webjars/**"),
                                AntPathRequestMatcher.antMatcher("/error"),
                                // API lịch/ghế/dịch vụ dùng nội bộ bởi RestTemplate khi xem trang đặt vé (tránh trả HTML login)
                                AntPathRequestMatcher.antMatcher("/api/ticket/show/**")).permitAll()
                        // Chỉ ADMIN: các URL quản trị
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/admin/**"),
                                AntPathRequestMatcher.antMatcher("/cinema/**"),
                                AntPathRequestMatcher.antMatcher("/room/**"),
                                AntPathRequestMatcher.antMatcher("/seat/**"),
                                AntPathRequestMatcher.antMatcher("/movie/**"),
                                AntPathRequestMatcher.antMatcher("/schedule/**"),
                                AntPathRequestMatcher.antMatcher("/ticket/**"),
                                AntPathRequestMatcher.antMatcher("/bill/**"),
                                AntPathRequestMatcher.antMatcher("/service/**"),
                                AntPathRequestMatcher.antMatcher("/promotion/**"),
                                AntPathRequestMatcher.antMatcher("/statistical/**"),
                                AntPathRequestMatcher.antMatcher("/general-setting/**"),
                                AntPathRequestMatcher.antMatcher("/home/**"),
                                AntPathRequestMatcher.antMatcher("/customer/**"),
                                AntPathRequestMatcher.antMatcher("/rated/**"),
                                AntPathRequestMatcher.antMatcher("/rank/**"),
                                AntPathRequestMatcher.antMatcher("/seatType/**")).hasRole("ADMIN")
                        // Đã đăng nhập (USER hoặc ADMIN): trang user + API + thanh toán
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/filmbooking/**"),
                                AntPathRequestMatcher.antMatcher("/show/**"),
                                AntPathRequestMatcher.antMatcher("/submitOrder"),
                                AntPathRequestMatcher.antMatcher("/vnpay-payment"),
                                AntPathRequestMatcher.antMatcher("/api/**")).authenticated()
                        .anyRequest().authenticated()
                )
                // Tắt form login mặc định; dùng custom login trong LoginController / AdminLoginController
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable())
                .authenticationProvider(authenticationProvider())
                // Lưu SecurityContext vào session sau khi đăng nhập (cần cho redirect)
                .securityContext(sec -> sec.requireExplicitSave(false))
                // Chưa login: nếu đang vào khu vực admin/home → /admin/login, còn lại → /filmbooking/login
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            String path = request.getRequestURI() != null ? request.getRequestURI() : "";
                            String ctx = request.getContextPath();
                            if (path.startsWith(ctx + "/admin") || path.startsWith(ctx + "/home")) {
                                response.sendRedirect(ctx + "/admin/login");
                            } else {
                                response.sendRedirect(ctx + "/filmbooking/login");
                            }
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            if (request.getUserPrincipal() != null) {
                                response.sendRedirect(request.getContextPath() + "/filmbooking/trangchu");
                            } else {
                                response.sendRedirect(request.getContextPath() + "/filmbooking/login");
                            }
                        })
                );

        return http.build();
    }
}
