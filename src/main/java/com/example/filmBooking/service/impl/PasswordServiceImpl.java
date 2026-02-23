package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.Customer;
import com.example.filmBooking.model.PasswordResetToken;
import com.example.filmBooking.repository.PasswordResetTokenRepository;
import com.example.filmBooking.service.CustomerService;
import com.example.filmBooking.service.PasswordService;
import com.example.filmBooking.util.EmailHtmlUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Implement quên mật khẩu (token + email) và đổi mật khẩu (khi đã đăng nhập).
 */
@Service
public class PasswordServiceImpl implements PasswordService {

    private static final int TOKEN_VALID_MINUTES = 15;

    private final CustomerService customerService;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public PasswordServiceImpl(CustomerService customerService,
                              PasswordResetTokenRepository tokenRepository,
                              PasswordEncoder passwordEncoder,
                              JavaMailSender mailSender) {
        this.customerService = customerService;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Override
    @Transactional
    public void sendResetPasswordEmail(String email) {
        Customer customer = customerService.findByEmail(email);
        if (customer == null) {
            // Không tiết lộ email có tồn tại hay không — không gửi gì, không báo lỗi
            return;
        }
        // Chỉ giữ một token mới: xóa hết token cũ của customer
        tokenRepository.deleteAllByCustomerId(customer.getId());

        String tokenValue = UUID.randomUUID().toString();
        Instant expiry = Instant.now().plusSeconds(TOKEN_VALID_MINUTES * 60L);

        PasswordResetToken token = PasswordResetToken.builder()
                .token(tokenValue)
                .customer(customer)
                .expiryTime(expiry)
                .build();
        tokenRepository.save(token);

        String resetLink = baseUrl + "/filmbooking/reset-password?token=" + tokenValue;

        String body = EmailHtmlUtil.paragraph("Xin chào " + customer.getName() + ",")
                + EmailHtmlUtil.paragraph("Bạn đã yêu cầu đặt lại mật khẩu. Vui lòng nhấn nút bên dưới trong vòng " + TOKEN_VALID_MINUTES + " phút để tạo mật khẩu mới.")
                + EmailHtmlUtil.buttonLink(resetLink, "Đặt lại mật khẩu")
                + EmailHtmlUtil.paragraph("Nếu bạn không yêu cầu, hãy bỏ qua email này.");
        String footer = "— FilmBooking";
        String html = EmailHtmlUtil.wrap("Đặt lại mật khẩu", body, footer);

        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setTo(customer.getEmail());
            helper.setSubject("[FilmBooking] Đặt lại mật khẩu");
            helper.setText(html, true);
            mailSender.send(msg);
        } catch (MessagingException e) {
            throw new RuntimeException("Không gửi được email đặt lại mật khẩu", e);
        }
    }

    @Override
    public boolean isTokenValid(String token) {
        if (token == null || token.isBlank()) return false;
        Optional<PasswordResetToken> opt = tokenRepository.findByToken(token);
        return opt.isPresent() && opt.get().getExpiryTime().isAfter(Instant.now());
    }

    @Override
    @Transactional
    public boolean resetPasswordWithToken(String token, String newPassword) {
        if (newPassword == null || newPassword.length() < 8) return false;
        Optional<PasswordResetToken> opt = tokenRepository.findByToken(token);
        if (opt.isEmpty() || opt.get().getExpiryTime().isBefore(Instant.now())) {
            return false;
        }
        PasswordResetToken resetToken = opt.get();
        Customer customer = resetToken.getCustomer();
        customer.setPassword(passwordEncoder.encode(newPassword));
        customerService.update(customer.getId(), customer);
        tokenRepository.deleteByToken(token);
        return true;
    }

    @Override
    @Transactional
    public String changePassword(String customerId, String currentPassword, String newPassword, String confirmPassword) {
        Customer customer = customerService.findById(customerId);
        if (customer == null) return "Tài khoản không tồn tại.";

        if (!passwordEncoder.matches(currentPassword, customer.getPassword())) {
            return "Mật khẩu hiện tại không đúng.";
        }
        if (newPassword == null || newPassword.length() < 8) {
            return "Mật khẩu mới phải có ít nhất 8 ký tự.";
        }
        if (!newPassword.equals(confirmPassword)) {
            return "Xác nhận mật khẩu không khớp.";
        }
        if (passwordEncoder.matches(newPassword, customer.getPassword())) {
            return "Mật khẩu mới phải khác mật khẩu hiện tại.";
        }

        customer.setPassword(passwordEncoder.encode(newPassword));
        customerService.update(customer.getId(), customer);
        return null;
    }
}
