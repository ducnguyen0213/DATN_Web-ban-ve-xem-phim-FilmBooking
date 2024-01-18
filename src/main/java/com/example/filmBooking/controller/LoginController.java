package com.example.filmBooking.controller;

import com.example.filmBooking.model.Customer;
import com.example.filmBooking.repository.BillRepository;
import com.example.filmBooking.service.CustomerService;
import com.example.filmBooking.service.impl.CustomerServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
            HttpServletRequest request, Model model
    ) {
//        Customer customer = customerService.findByEmail(email);?
        Customer customer = customerService.findByEmail(email);
        HttpSession sessionLogin = request.getSession();

        if (customer == null || !customer.getPassword().equals(password)) {
            sessionLogin.setAttribute("ERR_LOGIN", "Sai tên tài khoản hoặc mật khẩu");
            return "redirect:/filmbooking/login";
        } else {
            sessionLogin.setAttribute("customer", customer);
            String soldTicketsCountBill = billRepository.countSoldTicket(customer.getId());
            model.addAttribute("soldTicketsCountBill", soldTicketsCountBill);

            return "redirect:/filmbooking/trangchu";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("customer", null);
        return "redirect:/filmbooking/login";
    }


}
