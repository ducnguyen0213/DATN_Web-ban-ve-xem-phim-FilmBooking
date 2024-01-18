
function validateSeatType(event){
    var nameSeatType = document.getElementById('name').value;
    var nameErr = document.getElementById('nameErr');
    var surcharge = document.getElementById('surcharge').value;
    var surchargeErr = document.getElementById('surchargeErr');
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

    if(nameSeatType.trim() === ''){
        nameErr.textContent = "Không được để trống !";
        isValid = false;
    }else {
        nameErr.innerText = "";
    }
    if(surcharge.trim() === '') {
        surchargeErr.textContent = "Không được để trống !";
        isValid = false;
    }else if(isNaN(surcharge) || +surcharge < 0){
        surchargeErr.textContent = "Phụ phí không thể < 0 !";
        isValid = false;
    }else {
        surchargeErr.innerText = "";
    }
    if (!isValid) {
        event.preventDefault(); // This line prevents the default behavior without any visible indication
    }
}