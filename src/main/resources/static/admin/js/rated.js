function showConfirmationBeforeValidation(event) {
    if (confirm('Bạn có chắc chắn muốn thêm và sửa rated không?')) {
        // Người dùng đã xác nhận, tiến hành kiểm tra dữ liệu và gửi form
        validateForm(event);
    } else {
        // Người dùng đã hủy xác nhận, không làm gì cả
        event.preventDefault(); // Ngăn chặn việc gửi form khi hủy
    }
}

function validateFormRated(event) {
    // Lấy các giá trị từ các trường dữ liệu
    const ratedCode = document.getElementById('ratedCode').value;

    const ratedCodeError = document.getElementById('ratedCodeErr');
    var descriptionRated = document.getElementById('descriptionRated').value;
    var descriptionRatedErr = document.getElementById('descriptionRatedErr');

    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
        }
    });
    // Đặt các biến kiểm tra
    let isValid = true;

    // Kiểm tra trường rỗng
    if (ratedCode.trim() === '') {
        ratedCodeError.textContent = 'Mã rated không được để trống';
        isValid = false;
    } else {
        ratedCodeError.innerText = "";
    }

    if(descriptionRated.trim() === ''){
        descriptionRatedErr.textContent = "Mô tả không được để trống !";
        isValid = false;
    }else {
        descriptionRatedErr.innerText = "";
    }

    // Nếu có bất kỳ lỗi nào, ngăn chặn sự kiện mặc định của form
    if (!isValid) {
        event.preventDefault();
    }
}