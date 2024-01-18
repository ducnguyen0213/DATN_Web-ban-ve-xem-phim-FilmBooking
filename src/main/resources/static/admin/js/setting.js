function validateSetting(event) {
    var openTime = document.getElementById('open_time').value;
    var cloesTime = document.getElementById('close_time').value;
    var showTime = document.getElementById('show_time').value;
    var showTimeGiao = document.getElementById('show_time_giao').value;
    var paymentTime = document.getElementById('payment_time').value;
    var priceChangeTime = document.getElementById('price_change_time').value;
    var weekendPriceIncrease = document.getElementById('weekend_price_increase').value;
    var weekdayPriceIncrease = document.getElementById('weekday_price_increase').value;
    var openTimeErr = document.getElementById('open_time_err');
    var closeTimeErr = document.getElementById('close_time_err');
    var showTimeErr = document.getElementById('show_time_err');
    var showTimeGiaoErr = document.getElementById('show_time_giao_err');
    var paymentTimeErr = document.getElementById('payment_time_err');
    var priceChangeTimeErr = document.getElementById('price_change_time_err');
    var weekendPriceIncreaseErr = document.getElementById('weekend_price_increase_err');
    var weekdayPriceIncreaseErr = document.getElementById('weekday_price_increase_err');
    var isValid = true;

    if (openTime.trim() === '') {
        openTimeErr.textContent = "Thời gian mở cửa không được trống !";
        isValid = false;
    } else {
        openTimeErr.innerText = "";
    }
    if (cloesTime.trim() === '') {
        closeTimeErr.textContent = "Thời gian đóng cửa không được trống !";
        isValid = false;
    } else {
        closeTimeErr.innerText = "";
    }
    if (showTime.trim() === '') {
        showTimeErr.textContent = "Giá cố định không được trống !";
        isValid = false;
    } else if (isNaN(showTime) || showTimeGiao < 0) {
        showTimeErr.textContent = "Giá cố định không được âm !";
        isValid = false;
    } else {
        showTimeErr.innerText = "";
    }
    if (showTimeGiao.trim() === '') {
        showTimeGiaoErr.textContent = "Thời gian giao ca giữa các suất chiếu không được trống !";
        isValid = false;
    } else if (isNaN(showTimeGiao) || showTimeGiao < 0) {
        showTimeGiaoErr.textContent = "Thời gian giao ca không được âm !";
        isValid = false;
    } else {
        showTimeGiaoErr.innerText = "";
    }
    if (paymentTime.trim() === '') {
        paymentTimeErr.textContent = "Thời gian chờ thanh toán không được trống";
        isValid = false;
    } else if (isNaN(paymentTime) || paymentTime < 0) {
        paymentTimeErr.textContent = "Thời gian chờ thanh toán không được âm !";
        isValid = false;
    } else {
        paymentTimeErr.innerText = "";
    }
    if (priceChangeTime.trim() === '') {
        priceChangeTimeErr.textContent = "Thời gian thay đổi giá không được trống !";
        isValid = false;
    } else {
        priceChangeTimeErr.innerText = "";
    }
    if (weekendPriceIncrease.trim() === '') {
        weekendPriceIncreaseErr.textContent = "Phần trăm tăng giá cuối tuần không để trống và có thể nhập là 0 !";
        isValid = false;
    } else if (isNaN(weekendPriceIncrease) || weekendPriceIncrease < 0) {
        weekendPriceIncreaseErr.textContent = "Phần trăm tăng giá cuối tuần không được âm !";
        isValid = false;
    } else {
        weekendPriceIncreaseErr.innerText = "";
    }
    if (weekdayPriceIncrease.trim() === '') {
        weekdayPriceIncreaseErr.textContent = "Phần trăm tăng giá ngày thường không được trống và có thể nhập là 0 !";
        isValid = false;
    } else if (isNaN(weekdayPriceIncrease) || weekdayPriceIncrease < 0) {
        weekdayPriceIncreaseErr.textContent = "Phần trăm tăng giá ngày thường không được âm !";
        isValid = false;
    } else {
        weekdayPriceIncreaseErr.innerText = "";
    }
    if (!isValid) {
        document.getElementById('loading-overlay').style.display = 'none';
        event.preventDefault();
    } else {
        document.getElementById('loading-overlay').style.display = 'flex';
    }
}