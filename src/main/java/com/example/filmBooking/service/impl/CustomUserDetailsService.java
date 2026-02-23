package com.example.filmBooking.service.impl;

import com.example.filmBooking.model.Customer;
import com.example.filmBooking.repository.CustomerRepository;
import com.example.filmBooking.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Load user từ DB theo email (username) để Spring Security dùng khi kiểm tra đăng nhập.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findEmail(username);
        if (customer == null) {
            throw new UsernameNotFoundException("Không tìm thấy tài khoản với email: " + username);
        }
        return new CustomUserDetails(customer);
    }
}
