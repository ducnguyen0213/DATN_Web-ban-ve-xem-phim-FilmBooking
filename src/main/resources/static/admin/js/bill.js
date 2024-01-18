function xacNhanHoaDon(event){
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
    if (!isValid) {
        Toast.fire({
            icon: "error",
            title: "Xác nhận hóa đơn thất bại"
        });
        document.getElementById('loading-overlay').style.display = 'none';
        event.preventDefault();
    } else {
        Toast.fire({
            icon: "success",
            title: "Xác nhận hóa đơn thành công"
        }).then(function () {
                window.location.reload();
            }
        );
        document.getElementById('loading-overlay').style.display = 'flex';
    }
}

const dateContainers = document.querySelectorAll('.input-container');
dateContainers.forEach(dateContainer => {
    const dateInput = dateContainer.querySelector('.date-field');
    if (dateInput) {
        dateContainer.addEventListener('click', function (event) {
            dateInput.select();
        });
    }
});

/* ----------------------------------------------------------------------------- */
/* -- Automatically set the date for check-in (today) and checkout (tomorrow) -- */
/* ----------------------------------------------------------------------------- */
document.addEventListener("DOMContentLoaded", function () {
    const dateCheckin = document.getElementById("date-checkin");
    const dateCheckout = document.getElementById("date-checkout");
    const today = new Date();
    const tomorrow = new Date(today); tomorrow.setDate(tomorrow.getDate() + 1);
    dateCheckin.valueAsDate = today;
    dateCheckout.valueAsDate = tomorrow;
    dateCheckin.addEventListener("input", function () {
        const checkinDate = dateCheckin.valueAsDate;
        const checkoutDate = dateCheckout.valueAsDate;
        if (checkinDate > checkoutDate) {
            const newCheckoutDate = new Date(checkinDate);
            newCheckoutDate.setDate(newCheckoutDate.getDate() + 1);
            dateCheckout.valueAsDate = newCheckoutDate;
        }
    });
    dateCheckout.addEventListener("input", function () {
        const checkinDate = dateCheckin.valueAsDate;
        const checkoutDate = dateCheckout.valueAsDate;
        if (checkoutDate < checkinDate) {
            const newCheckinDate = new Date(checkoutDate);
            newCheckinDate.setDate(newCheckinDate.getDate() - 1);
            dateCheckin.valueAsDate = newCheckinDate;
        }
    });
});




/* -- TESTKIT BUTTON -- */
//