package com.example.filmBooking.controller;

import com.example.filmBooking.model.Customer;
import com.example.filmBooking.repository.BillRepository;
import com.example.filmBooking.security.CustomUserDetails;
import com.example.filmBooking.service.CustomerService;
import com.example.filmBooking.service.impl.CustomerServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/filmbooking")
public class LoginController {

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private CustomerService service;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private HttpServletRequest request;

    @GetMapping(value = "/login")
    public String hienThiFormLogin() {
        return "users/DangNhap";
    }

    @GetMapping("/dangky")
    public String showRegistrationForm(Model model) {
        // create model object to store form data
        Customer customer1 = new Customer();
        model.addAttribute("customer1", customer1);
        return "users/DangKy";
    }

    // handler method to handle user registration form submit request
    @PostMapping("/dangky/save")
    public String registration(@Valid @ModelAttribute("customer1") Customer customer1,
                               BindingResult result,
                               Model model, RedirectAttributes ra) {
        Customer existingUser = service.findByEmail(customer1.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "Đã có tài khoản được đăng ký với cùng một email");
        }

        if (result.hasErrors()) {
            model.addAttribute("customer1", customer1);

            return "users/DangKy";
        }
        service.save(customer1);
        ra.addFlashAttribute("successMessage", "Đăng ký tài khoản thành công!!!");
        return "redirect:/filmbooking/dangky";
    }


    @PostMapping("/login")
    public String login(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "password") String password,
            HttpServletRequest request,
            Model model,
            RedirectAttributes ra
    ) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(auth);
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            Customer customer = userDetails.getCustomer();
            HttpSession sessionLogin = request.getSession();
            sessionLogin.setAttribute("customer", customer);
            return "redirect:/filmbooking/trangchu";
        } catch (Exception e) {
            // Fallback: mật khẩu trong DB có thể vẫn là plain (chưa chạy HashAllPasswordsRunner)
            Customer customer = service.findByEmail(email);
            if (customer != null && customer.getPassword() != null) {
                String stored = customer.getPassword();
                boolean plainMatch = !stored.startsWith("$2a$") && !stored.startsWith("$2b$")
                        && stored.equals(password);
                if (plainMatch) {
                    // Đăng nhập thành công; hash lại mật khẩu và lưu để lần sau dùng BCrypt
                    customer.setPassword(passwordEncoder.encode(password));
                    service.update(customer.getId(), customer);
                    CustomUserDetails userDetails = new CustomUserDetails(customer);
                    Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    HttpSession sessionLogin = request.getSession();
                    sessionLogin.setAttribute("customer", customer);
                    return "redirect:/filmbooking/trangchu";
                }
            }
            ra.addFlashAttribute("ERR_LOGIN", "Sai email hoặc mật khẩu");
            return "redirect:/filmbooking/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/filmbooking/login";
    }


}
