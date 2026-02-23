package com.example.filmBooking.controller;

import com.example.filmBooking.model.Customer;
import com.example.filmBooking.repository.BillRepository;
import com.example.filmBooking.service.PasswordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller xử lý Quên mật khẩu và Đổi mật khẩu (user).
 */
@Controller
@RequestMapping("/filmbooking")
public class PasswordController {

    private final PasswordService passwordService;
    private final BillRepository billRepository;

    public PasswordController(PasswordService passwordService, BillRepository billRepository) {
        this.passwordService = passwordService;
        this.billRepository = billRepository;
    }

    // ---------- Quên mật khẩu ----------
    @GetMapping("/forgot-password")
    public String forgotPasswordForm() {
        return "users/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPasswordSubmit(@RequestParam String email, RedirectAttributes ra) {
        passwordService.sendResetPasswordEmail(email);
        // Luôn hiển thị cùng một thông báo (không tiết lộ email có tồn tại hay không)
        ra.addFlashAttribute("forgotMessage", "Nếu email tồn tại trong hệ thống, bạn sẽ nhận được link đặt lại mật khẩu.");
        return "redirect:/filmbooking/forgot-password";
    }

    // ---------- Reset mật khẩu (từ link trong email) ----------
    @GetMapping("/reset-password")
    public String resetPasswordForm(@RequestParam(required = false) String token, Model model, RedirectAttributes ra) {
        if (token == null || token.isBlank() || !passwordService.isTokenValid(token)) {
            ra.addFlashAttribute("ERR_LOGIN", "Link không hợp lệ hoặc đã hết hạn. Vui lòng yêu cầu lại.");
            return "redirect:/filmbooking/login";
        }
        model.addAttribute("token", token);
        return "users/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPasswordSubmit(
            @RequestParam String token,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes ra) {
        if (!newPassword.equals(confirmPassword)) {
            ra.addFlashAttribute("error", "Xác nhận mật khẩu không khớp.");
            ra.addFlashAttribute("token", token);
            return "redirect:/filmbooking/reset-password?token=" + token;
        }
        if (newPassword.length() < 8) {
            ra.addFlashAttribute("error", "Mật khẩu mới phải có ít nhất 8 ký tự.");
            ra.addFlashAttribute("token", token);
            return "redirect:/filmbooking/reset-password?token=" + token;
        }
        boolean success = passwordService.resetPasswordWithToken(token, newPassword);
        if (!success) {
            ra.addFlashAttribute("ERR_LOGIN", "Link không hợp lệ hoặc đã hết hạn.");
            return "redirect:/filmbooking/login";
        }
        ra.addFlashAttribute("successMessage", "Đặt lại mật khẩu thành công. Vui lòng đăng nhập.");
        return "redirect:/filmbooking/login";
    }

    // ---------- Đổi mật khẩu (khi đã đăng nhập) ----------
    @GetMapping("/change-password")
    public String changePasswordForm(HttpServletRequest request, Model model) {
        Customer customer = (Customer) request.getSession().getAttribute("customer");
        if (customer == null) {
            return "redirect:/filmbooking/login";
        }
        model.addAttribute("customer", customer);
        model.addAttribute("soldTicketsCountBill", billRepository.countSoldTicket(customer.getId()));
        return "users/change-password";
    }

    @PostMapping("/change-password")
    public String changePasswordSubmit(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            HttpServletRequest request,
            RedirectAttributes ra) {
        Customer customer = (Customer) request.getSession().getAttribute("customer");
        if (customer == null) {
            return "redirect:/filmbooking/login";
        }
        String error = passwordService.changePassword(customer.getId(), currentPassword, newPassword, confirmPassword);
        if (error != null) {
            ra.addFlashAttribute("error", error);
            return "redirect:/filmbooking/change-password";
        }
        // Đăng xuất sau khi đổi mật khẩu thành công
        HttpSession session = request.getSession();
        session.invalidate();
        SecurityContextHolder.clearContext();
        ra.addFlashAttribute("successMessage", "Đổi mật khẩu thành công. Vui lòng đăng nhập lại.");
        return "redirect:/filmbooking/login";
    }
}
