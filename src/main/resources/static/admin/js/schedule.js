///
const selectBtn = document.querySelector(".select-btn"), items = document.querySelectorAll(".item");
const selectBtnRoom = document.querySelector(".select-btn-room"), itemsRoom = document.querySelectorAll(".itemRoom");


selectBtn.addEventListener("click", () => {
    selectBtn.classList.toggle("open");
});
selectBtnRoom.addEventListener("click", () => {
    selectBtnRoom.classList.toggle("open");
});
let selectedItems = []; // Mảng lưu trữ các phần tử đã chọn

items.forEach(item => {
    item.addEventListener("click", () => {
        item.classList.toggle("checked");
    });
});

itemsRoom.forEach(item => {
    item.addEventListener("click", () => {
        item.classList.toggle("checked");
    });
});

var selectedMovies = [];

function showSelectedMovies(checkbox) {
    var movieName = checkbox.parentNode.querySelector("span").textContent;

    if (checkbox.checked) {
        selectedMovies.push(movieName);
    } else {
        var index = selectedMovies.indexOf(movieName);
        if (index !== -1) {
            selectedMovies.splice(index, 1);
        }
    }

    var selectedMovieList = document.getElementById("selectedMovieList");
    selectedMovieList.innerHTML = "";

    for (var i = 0; i < selectedMovies.length; i++) {
        var listItem = document.createElement("li");
        listItem.textContent = (i + 1) + ". " + selectedMovies[i];
        selectedMovieList.appendChild(listItem);
    }

    var checkboxes = document.getElementsByName("checkboxItem");
    var selectedMoviesInput = [];

    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            selectedMoviesInput.push(checkboxes[i].value);
        }
    }

    var inputElement = document.querySelector('input[name="listMovieChecked"]');
    inputElement.value = selectedMoviesInput.join(',');

    console.log(selectedMoviesInput);
}

var selectedRoom = [];

function showSelectedRoom(checkbox) {
    var roomName = checkbox.parentNode.querySelector("span").textContent;

    if (checkbox.checked) {
        selectedRoom.push(roomName);
    } else {
        var index = selectedRoom.indexOf(roomName);
        if (index !== -1) {
            selectedRoom.splice(index, 1);
        }
    }

    var selectedRoomList = document.getElementById("selectedRoomList");
    selectedRoomList.innerHTML = "";

    for (var i = 0; i < selectedRoom.length; i++) {
        var listItem = document.createElement("li");
        listItem.textContent = (i + 1) + ". " + selectedRoom[i];
        selectedRoomList.appendChild(listItem);
    }

    var checkboxes = document.getElementsByName("checkboxItemRoom");
    var selectedRoomInput = [];

    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            selectedRoomInput.push(checkboxes[i].value);
        }
    }

    var inputElement = document.querySelector('input[name="listRoomChecked"]');
    inputElement.value = selectedRoomInput.join(',');

    console.log(selectedRoomInput);
}


function validateSchedule(event) {
    var startTime = document.getElementById('startTime').value;
    var endTime = document.getElementById('endTime').value;
    var danhSachPhim = document.getElementById('danhSachPhim').value;
    var startTimeErr = document.getElementById('startTimeErr');
    var endTimeErr = document.getElementById('endTimeErr');
    var danhSachPhimErr = document.getElementById('danhSachPhimErr');
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

    if (startTime.trim() === '') {
        startTimeErr.textContent = "Ngày bắt đầu không để trống !";
        isValid = false;
    } else {
        startTimeErr.innerText = "";
    }
    if (endTime.trim() === '') {
        endTimeErr.textContent = "Ngày kết thúc không được trống !";
        isValid = false;
    } else if (new Date(endTime) <= new Date(startTime)) {
        endTimeErr.textContent = "Ngày kết thúc phải lớn hơn ngày bắt đầu !";
        isValid = false;
    } else {
        endTimeErr.innerText = "";
    }
    if (danhSachPhim.trim() === '') {
        danhSachPhimErr.textContent = "Phải chọn ít nhất 1 phim !";
        isValid = false;
    } else {
        danhSachPhimErr.innerText = "";
    }
    document.getElementById('loading-overlay').style.display = 'flex';
}

function updateSchedule(event) {

    var startTimeUpdate = document.getElementById('startTimeUpdate').value;
    var startTimeUpdateErr = document.getElementById('startTimeUpdateErr');
    var endDateUpdate = document.getElementById('endTimeUpdate').value;
    var endDateUpdateErr = document.getElementById('endTimeUpdateErr');
    var priceLichChieuErr = document.getElementById('priceLichChieuErr');
    var priceLichChieu = document.getElementById('priceLichChieu').value;
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
    if (startTimeUpdate.trim() === '') {
        startTimeUpdateErr.textContent = "Ngày bắt đầu không để trống !";
        isValid = false;
    } else {
        startTimeUpdateErr.innerText = "";
    }
    if (endDateUpdate.trim() === '') {
        endDateUpdateErr.textContent = "Ngày kết thúc không được trống !";
        isValid = false;
    } else if (new Date(endDateUpdate) <= new Date(startTimeUpdate)) {
        endDateUpdateErr.textContent = "Ngày kết thúc phải lớn hơn ngày bắt đầu !";
        isValid = false;
    } else {
        endDateUpdateErr.innerText = "";
    }
    if (priceLichChieu.trim() === '') {
        priceLichChieuErr.textContent = "Giá không được để trống !";
        isValid = false;
    } else if (isNaN(priceLichChieu) || priceLichChieu < 0) {
        priceLichChieuErr.textContent = "Giá không được âm !";
        isValid = false;
    } else {
        priceLichChieuErr.innerText = "";
    }
    if (!isValid) {
        Toast.fire({
            icon: "error", title: "Sửa lịch chiếu thất bại"
        });
        document.getElementById('loading-overlay').style.display = 'none';
        event.preventDefault();
    } else {
        Toast.fire({
            icon: "success", title: "Thêm lịch chiếu thành công"
        });
        document.getElementById('loading-overlay').style.display = 'flex';
    }

}

var currentDate = new Date();
var currentDay = currentDate.getDate();
var currentMonth = currentDate.getMonth() + 1;
var currentYear = currentDate.getFullYear();

var sevenDays = [];

for (var i = 0; i < 7; i++) {
    var nextDay = new Date(currentYear, currentMonth - 1, currentDay + i);

    var dateArray = [nextDay.getDate(), nextDay.getMonth() + 1, nextDay.getFullYear()]; // Mảng chứa năm, tháng và ngày
    dateArray = dateArray.map(function (item) { // Chuyển đổi các phần tử trong mảng thành chuỗi và kiểm tra xem có cần thêm số 0 ở phía trước không
        return (item < 10 ? "0" : "") + item;
    });

    var dateString = dateArray.join('/'); // Xây dựng lại chuỗi ngày tháng

    sevenDays.push(dateString);
}

var selectElement = document.getElementById('startAt');

for (var j = 0; j < sevenDays.length; j++) {
    var optionElement = document.createElement('option');
    optionElement.value = sevenDays[j];
    optionElement.textContent = sevenDays[j];
    selectElement.appendChild(optionElement);
}

document.getElementById("addButton").addEventListener("click", function (event) {
    if (document.getElementById("selectedMovieList").children.length === 0) {
        alert("Error: Chọn phim trước khi thêm");
        event.preventDefault(); // Prevent the default link behavior
    } else if (document.getElementById("selectedRoomList").children.length === 0) {
        alert("Error: Chọn phòng trước khi thêm");
        event.preventDefault(); // Prevent the default link behavior
    } else {
        var confirmAdd = confirm('Bạn muốn thêm lịch chiếu mới!'); // Show a confirmation dialog
        if (!confirmAdd) {
            event.preventDefault(); // Prevent the default link behavior if the user cancels
        }
    }
});