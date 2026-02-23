package com.example.filmBooking.config;

import com.example.filmBooking.model.Customer;
import com.example.filmBooking.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
public class HashAllPasswordsRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(HashAllPasswordsRunner.class);

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public HashAllPasswordsRunner(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        List<Customer> all = customerRepository.findAll();
        int hashed = 0;
        for (Customer c : all) {
            String pwd = c.getPassword();
            if (pwd == null || pwd.isBlank()) continue;
            if (pwd.startsWith("$2a$") || pwd.startsWith("$2b$")) continue; // đã hash rồi
            c.setPassword(passwordEncoder.encode(pwd));
            customerRepository.save(c);
            hashed++;
            log.info("Đã hash mật khẩu cho customer: {}", c.getEmail());
        }
        if (hashed > 0) {
            log.info("Hash mật khẩu hoàn tất: {} tài khoản đã được cập nhật.", hashed);
        }
    }
}
