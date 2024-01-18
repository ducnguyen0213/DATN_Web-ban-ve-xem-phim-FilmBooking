function validateSeat(event) {
    var numberSeat = document.getElementById('numberSeat').value;
    var numberSeatErr = document.getElementById('numberSeatErr');
    var lineSeat = document.getElementById('lineSeat').value;
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
    var isValid = true;

    if (numberSeat.trim() === '') {
        numberSeatErr.textContent = "Số lượng hàng ghế không được trống !";
        isValid = false;
    } else if (isNaN(numberSeat) || numberSeat < 0) {
        numberSeatErr.textContent = "Sô lượng hàng không được âm !";
        isValid = false;
    } else if (isNaN(numberSeat) || numberSeat >= 9) {
        numberSeatErr.textContent = "Số lượng ghế lớn nhất là 8 !"
    } else {
        numberSeatErr.innerText = "";
    }
    if (lineSeat.trim() === '') {
        lineSeatErr.textContent = "Hàng ghế không được trống !";
        isValid = false;
    } else if (isNaN(lineSeat) || lineSeat < 0) {
        lineSeatErr.textContent = "Hàng ghế không được âm !";
        isValid = false;
    } else if (isNaN(lineSeat) || lineSeat >= 6) {
        lineSeatErr.textContent = "Hàng ghế lớn nhất là 5 !";
        isValid = false;
    } else {
        lineSeatErr.innerText = "";
    }
    if (!isValid) {
        Toast.fire({
            icon: "error",
            title: "Thêm ghế thất bại"
        });
        document.getElementById('loading-overlay').style.display = 'none';
        event.preventDefault();
    }else {
        Toast.fire({
            icon: "success",
            title: "Thêm ghế thành công"
        })
        document.getElementById('loading-overlay').style.display = 'flex';
    }
}

