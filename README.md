# FilmBooking

## Giới thiệu

Dự án: Xây dựng web bán vé xem phim

## Thông tin trường và khóa học

- Trường: Cao đẳng FPT Polytechnic
- Khóa: 17.3

## Thành viên dự án

- Hoàng Đức Toản
- Trần Thị Diệu Linh
- Nguyễn Hữu Đức
- Trần Văn Dương

## Giảng viên hướng dẫn

- Trần Tuấn Phong

## Chức năng chính

Dự án FilmBooking cung cấp một số chức năng chính như sau:

- Mua vé
- Thanh toán VNPAY
- Quét mã QR với admin xác nhận
- Tạo lịch chiếu
- Thêm phòng
- Chỉnh sửa sơ đồ ghế nhanh và excel
- Gửi mail xác nhận đơn hàng
- Thêm khuyến mãi với từng khách hàng
- Tích điểm point'
  
## Các công nghệ sử dụng

- **Ngôn ngữ:** Java Spring Boot - jdk 17
- **Cơ sở dữ liệu:** MySQL - MySQL Workbench
- **IDEA:** IntelliJ IDEA Ultimate - Version: 2020.3.4 - Windows x64 (exe)
- **Tool:** Swagger


## Phân quyền & Bảo mật (Spring Security 6)

- **Role:** USER (đặt vé), ADMIN (quản trị). Đăng ký mới mặc định USER.
- **Mật khẩu:** Hash BCrypt khi đăng ký/cập nhật; đăng nhập verify qua Spring Security (không so sánh equals trực tiếp).
- **URL:** USER được truy cập `/filmbooking/**`, `/show/**`, `/api/**`, thanh toán; ADMIN thêm `/admin/**`, `/cinema/**`, `/movie/**`, v.v. Chưa đăng nhập → redirect `/filmbooking/login`.

**Lần đầu chạy sau khi thêm Security:** Thêm cột `role` vào bảng `customer`:
```sql
ALTER TABLE customer ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'USER';
```
Tài khoản có sẵn mặc định USER; muốn có Admin thì cập nhật trong DB: `UPDATE customer SET role = 'ADMIN' WHERE email = 'admin@email.com';`  
Tài khoản cũ đang lưu mật khẩu plain text: đăng nhập sẽ lỗi; cần đăng ký lại hoặc cập nhật mật khẩu (sẽ được hash).

## Lưu ý

Dự án đã có file DB sử dựng MySQL, bạn cần chỉnh sửa lại thứ tự insert để có thể thêm các bảng không có khóa ngoại trước.

## Swagger

Khi chạy dự án, bạn có thể truy cập Swagger UI tại đường dẫn: [http://localhost:8080/swagger-ui/index.html#](http://localhost:8080/swagger-ui/index.html#)

## Cấu hình VNPAY (Sandbox)

Nếu gặp lỗi **"Website này chưa được phê duyệt"** khi thanh toán:

1. **Đăng ký merchant Sandbox VNPAY:** vào [https://sandbox.vnpayment.vn](https://sandbox.vnpayment.vn), đăng ký/đăng nhập và tạo website merchant để lấy **Mã website (vnp_TmnCode)** và **Chuỗi bí mật (vnp_HashSecret)** (cập nhật trong `VNPayConfig.java` nếu khác với mặc định).

2. **Đăng ký URL Return với VNPAY:** trong trang quản lý merchant Sandbox, vào mục **Cài đặt** (hoặc **Thông tin website**) và thêm **URL Return** / **Domain** đúng với URL ứng dụng của bạn:
   - Chạy local: thêm `http://localhost:8080` (và đảm bảo **Return URL** là `http://localhost:8080/vnpay-payment`).
   - Nếu Sandbox không chấp nhận localhost: dùng [ngrok](https://ngrok.com/) (`ngrok http 8080`), lấy URL dạng `https://xxxx.ngrok.io`, thêm vào cài đặt VNPAY, và trong `application.properties` đặt `vnpay.return-url-base=https://xxxx.ngrok.io`.


## Chức năng vẫn còn thiếu

Dự án vẫn đang trong quá trình phát triển và còn một số chức năng chưa hoàn thành, bao gồm:

- Báo lỗi khi đổi point quá số tiền (giới hạn số tiền cần thanh toán là 10k)
- Thiếu mã QR khi thanh toán xong (gửi về mail và bên thông tin cá nhân)
- In ra được dự kiến số suất chiếu sẽ được tạo
- Update thông tin cá nhân
- Đăng nhập với tư cách admin
- Tìm kiếm với multiselect của Performer, Director, Language, Movie_type


## Liên hệ và thắc mắc

Nếu có bất kỳ thắc mắc hoặc đóng góp nào về dự án, vui lòng liên hệ:

- **FB:** Đức Nguyễn - [Facebook](https://www.facebook.com/ducnguyen.231/)
- **Email:** ducnguyen1302cat@gmail.com
