// const seat_manager_container = document.querySelector('.seat_manager_container');
// const seat_manager = document.querySelector('.seat_row .seat_manager:not(.sold)');
// const counts = document.getElementById("count");
// const total = document.getElementById("total");
// const
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
    }else {
        nameCinemaErr.innerText = "";
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



document.querySelectorAll('#buttonSeatt').forEach(function (button) {
    button.addEventListener('click', function () {
        var seatRow = this.parentElement.querySelector('button').innerText.trim().charAt(0);
        console.log(seatRow);
        // Gọi ra thẻ input
        document.getElementById("line").value =seatRow;

        var maxSeatNumber = this.parentElement.children.length;
        document.getElementById("number").value =maxSeatNumber;
    });
});