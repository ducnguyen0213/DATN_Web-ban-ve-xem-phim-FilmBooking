

function validateFormRated(event) {
    // Lấy các giá trị từ các trường dữ liệu

    var lineSeat = document.getElementById('inputNumber').value;
    var lineSeatErr = document.getElementById('lineSeatErr');



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
    if (lineSeat.trim() === '') {
        lineSeatErr.textContent = 'Vui lòng nhập số hàng ghế!!!';
        isValid = false;
    } else {
        lineSeatErr.innerText = "";
    }


    // Nếu có bất kỳ lỗi nào, ngăn chặn sự kiện mặc định của form
    if (!isValid) {
        event.preventDefault();
    }
}

function showContinueButton() {
    document.getElementById("addSeat").style.display = "none";
    document.getElementById("addSeatExcel").style.display = "block";
    document.getElementById("showDivButton").style.display = "none";
    document.getElementById("showDivButtonQuayLai").style.display = "block";
    document.getElementById("showDivButtonTaoGhe").style.display = "block";
    document.getElementById("addSeatSeat").style.display = "none";

}
function showContinueButtonQuayLai() {
    document.getElementById("addSeat").style.display = "block";
    document.getElementById("addSeatExcel").style.display = "none";
    document.getElementById("showDivButton").style.display = "block";
    document.getElementById("showDivButtonQuayLai").style.display = "none";
    document.getElementById("showDivButtonTaoGhe").style.display = "block";
    document.getElementById("addSeatSeat").style.display = "none";
}
function showContinueButtonSeat() {
    document.getElementById("addSeat").style.display = "none";
    document.getElementById("addSeatExcel").style.display = "none";
    document.getElementById("showDivButton").style.display = "block";
    document.getElementById("showDivButtonQuayLai").style.display = "block";
    document.getElementById("showDivButtonTaoGhe").style.display = "none";
    document.getElementById("addSeatSeat").style.display = "block";
}


function validateCinema(event){
    var nameCinema = document.getElementById('line').value;
    var nameCinemaErr = document.getElementById('lineErr');
    var addressCinema = document.getElementById('number').value;
    var addressCinemaErr = document.getElementById('numbertErr');
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
    var isValid = true;

    if(nameCinema.trim() === ''){
        nameCinemaErr.textContent = "Không được để trống !";
        isValid = false;
    } else if(/\d/.test(nameCinema)){
        nameCinemaErr.textContent = "Hàng ghế không được chứa số !";
        isValid = false;
    } else {
        nameCinemaErr.textContent = "";
    }
    if(addressCinema.trim() === ''){
        addressCinemaErr.textContent = "Không được để trống !";
        isValid = false;
    }else {
        addressCinemaErr.innerText = "";
    }
    if (!isValid) {
        event.preventDefault(); // This line prevents the default behavior without any visible indication
    }
}