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

- **Ngôn ngữ:** Java Spring Boot
- **Cơ sở dữ liệu:** MySQL - MySQL Workbench
- **IDEA:** IntelliJ IDEA Ultimate - Version: 2020.3.4 - Windows x64 (exe)
- **Tool:** Swagger


## Lưu ý

Dự án đã có file DB sử dựng MySQL, bạn cần chỉnh sửa lại thứ tự insert để có thể thêm các bảng không có khóa ngoại trước.

## Swagger

Khi chạy dự án, bạn có thể truy cập Swagger UI tại đường dẫn: [http://localhost:8080/swagger-ui/index.html#](http://localhost:8080/swagger-ui/index.html#)


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
