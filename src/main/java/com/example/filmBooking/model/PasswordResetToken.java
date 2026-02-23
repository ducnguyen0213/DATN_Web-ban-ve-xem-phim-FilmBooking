package com.example.filmBooking.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Entity lưu token reset mật khẩu.
 * Quan hệ Many-to-One với Customer (một customer có thể có nhiều token theo thời gian, nhưng chỉ dùng 1 lần).
 */
@Entity
@Table(name = "password_reset_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true, length = 255)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "expiry_time", nullable = false)
    private Instant expiryTime;
}
