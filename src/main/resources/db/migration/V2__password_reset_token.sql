-- Bảng lưu token reset mật khẩu (dùng 1 lần, hết hạn 15 phút)
-- Index cho token được tạo tự động qua UNIQUE constraint
CREATE TABLE IF NOT EXISTS password_reset_token (
    id VARCHAR(36) PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    customer_id VARCHAR(36) NOT NULL,
    expiry_time DATETIME NOT NULL,
    CONSTRAINT fk_reset_customer FOREIGN KEY (customer_id) REFERENCES customer(ID) ON DELETE CASCADE
);
