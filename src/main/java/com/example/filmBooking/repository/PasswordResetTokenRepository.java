package com.example.filmBooking.repository;

import com.example.filmBooking.model.PasswordResetToken;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository cho bảng password_reset_token.
 */
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {

    Optional<PasswordResetToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.token = :token")
    void deleteByToken(@Param("token") String token);

    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.customer.id = :customerId AND t.expiryTime < :now")
    void deleteExpiredByCustomerId(@Param("customerId") String customerId, @Param("now") Instant now);

    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.customer.id = :customerId")
    void deleteAllByCustomerId(@Param("customerId") String customerId);
}
