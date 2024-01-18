var spanElement = document.getElementById("spanId");

// Tạo một sự kiện click cho thẻ <span>
spanElement.addEventListener("click", function() {
    // Tải lại trang web
    location.reload();
});



// lấy ngày
//
// function updateDiv() {
//     var selectedValue = document.getElementById("listDate").value;
//     document.getElementById("myDivDate").textContent = selectedValue;
//     var link = document.getElementById("myLink");
//
//     var url = "&startAt=" + selectedValue.trim()
//
//     link.href += url;
//     console.log(selectedValue)
//
// }
// window.addEventListener("DOMContentLoaded", function() {
//     updateDiv();
// });
document.addEventListener('DOMContentLoaded', function() {
    var selectedDate = document.getElementById("listDate").value;
    var dateObject = new Date(selectedDate);
    var options = { weekday: 'long' };
    var dayOfWeek = new Intl.DateTimeFormat('vi-VN', options).format(dateObject);
    document.getElementById("resultDiv").innerText = dayOfWeek;
});

document.getElementById("listDate").addEventListener('change', function() {
    var selectedDate = this.value;
    var dateObject = new Date(selectedDate);
    var options = { weekday: 'long' };
    var dayOfWeek = new Intl.DateTimeFormat('vi-VN', options).format(dateObject);
    document.getElementById("resultDiv").innerText = dayOfWeek;
});
function updateDiv() {
    var selectedValue = document.getElementById("listDate").value;
    document.getElementById("myDivDate").textContent = selectedValue;
}

window.addEventListener("DOMContentLoaded", function() {
    document.getElementById("myLink").addEventListener("click", function() {
        var link = document.getElementById("myLink");
        var selectedValue = document.getElementById("listDate").value;
        var url = "&startAt=" + encodeURIComponent(selectedValue.trim());
        link.href += url;
        updateDiv();
    });
    updateDiv();
});
<!--    Lấy ra giờ chiếu -->
function displayData(button) {
    // Lấy dữ liệu từ button
    var buttonData = button.textContent;
    var startTime = button.querySelector("#time").innerText;
        console.log(startTime)
    var roomInfo = button.querySelector("#room").innerText;
    console.log(roomInfo);

    // Chọn phần tử div bằng id
    var myDiv = document.getElementById("myDiv");
    var room = document.getElementById("phong");
    var link = document.getElementById("myLink");
    // Cập nhật nội dung của div với dữ liệu đã chọn
    myDiv.textContent = startTime;
    room.textContent = roomInfo;

    var url = "&startTime=" + startTime.trim() + "&nameRoom=" + roomInfo.trim()

    link.href += url;
}



let select = document.querySelector('select');

select.addEventListener('focus', () => {
    select.size = 5;
    select.classList.add('fadeIn');
    select.classList.remove('fadeOut');
    select.style.backgroundColor = '#FFF';
});

select.addEventListener('blur', () => {
    select.size = 1;
    select.classList.add('fadeOut');
    select.classList.remove('fadeIn');
    select.style.backgroundColor = '#FFF';
});

select.addEventListener('change', () => {
    select.size = 1;
    select.blur();
    select.style.backgroundColor = '#FFF';
});

select.addEventListener('mouseover', () => {
    if (select.size == 1) {
        select.style.backgroundColor = 'rgb(247, 247, 247)';
    }
});
select.addEventListener('mouseout', () => {
    if (select.size == 1) {
        select.style.backgroundColor = '#FFF';
    }
});
//