function validateCinema(event){
    var nameCinema = document.getElementById('nameCinema').value;
    var nameCinemaErr = document.getElementById('nameCinemaErr');
    var addressCinema = document.getElementById('addressCinema').value;
    var addressCinemaErr = document.getElementById('addressCinemaErr');
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
        nameCinemaErr.textContent = "Tên rạp không được để trống !";
        isValid = false;
    }else {
        nameCinemaErr.innerText = "";
    }
    if(addressCinema.trim() === ''){
        addressCinemaErr.textContent = "Địa chỉ rạp không được để trống !";
        isValid = false;
    }else {
        addressCinemaErr.innerText = "";
    }
    if (!isValid) {
        event.preventDefault(); // This line prevents the default behavior without any visible indication
    }
}