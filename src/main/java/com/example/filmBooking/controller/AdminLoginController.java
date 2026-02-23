package com.example.filmBooking.controller;

import com.example.filmBooking.model.Customer;
import com.example.filmBooking.model.Role;
import com.example.filmBooking.security.CustomUserDetails;
import com.example.filmBooking.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Đăng nhập riêng cho Admin. URL: /admin/login.
 * Chỉ tài khoản có role ADMIN mới đăng nhập được; USER đăng nhập tại /filmbooking/login.
 */
@Controller
@RequestMapping("/admin")
public class AdminLoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String form() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletRequest request,
            RedirectAttributes ra
    ) {
        Customer customer = customerService.findByEmail(email);
        if (customer == null) {
            ra.addFlashAttribute("ERR_LOGIN", "Sai email hoặc mật khẩu.");
            return "redirect:/admin/login";
        }
        if (customer.getRole() != Role.ADMIN) {
            ra.addFlashAttribute("ERR_LOGIN", "Tài khoản không có quyền quản trị. Vui lòng đăng nhập tại trang khách hàng.");
            return "redirect:/admin/login";
        }
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(auth);
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            HttpSession session = request.getSession();
            session.setAttribute("customer", userDetails.getCustomer());
            return "redirect:/home";
        } catch (Exception e) {
            String stored = customer.getPassword();
            boolean plainMatch = stored != null && !stored.startsWith("$2a$") && !stored.startsWith("$2b$") && stored.equals(password);
            if (plainMatch) {
                customer.setPassword(passwordEncoder.encode(password));
                customerService.update(customer.getId(), customer);
                CustomUserDetails userDetails = new CustomUserDetails(customer);
                Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
                HttpSession session = request.getSession();
                session.setAttribute("customer", customer);
                return "redirect:/home";
            }
            ra.addFlashAttribute("ERR_LOGIN", "Sai email hoặc mật khẩu.");
            return "redirect:/admin/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/admin/login";
    }
}
