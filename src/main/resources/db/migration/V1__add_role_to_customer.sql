-- Thêm cột role cho phân quyền USER / ADMIN. Chạy 1 lần khi nâng cấp (nếu đã có cột thì bỏ qua).
ALTER TABLE customer ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'USER';
