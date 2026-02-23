package com.example.filmBooking.service;

/**
 * Service xử lý quên mật khẩu và đổi mật khẩu.
 */
public interface PasswordService {

    /**
     * Gửi email reset mật khẩu nếu email tồn tại. Không tiết lộ email có trong hệ thống hay không.
     * @param email email đăng ký
     */
    void sendResetPasswordEmail(String email);

    /**
     * Kiểm tra token hợp lệ và chưa hết hạn.
     */
    boolean isTokenValid(String token);

    /**
     * Đặt lại mật khẩu bằng token (sau khi user mở link trong email).
     * Token dùng 1 lần, xóa sau khi dùng.
     * @return true nếu thành công
     */
    boolean resetPasswordWithToken(String token, String newPassword);

    /**
     * Đổi mật khẩu khi đã đăng nhập (verify mật khẩu hiện tại).
     * @return message lỗi nếu có, null nếu thành công
     */
    String changePassword(String customerId, String currentPassword, String newPassword, String confirmPassword);
}
