function validateFormRoom(event) {
    // Lấy các giá trị từ các trường dữ liệu
    const height = document.getElementById('height').value;
    const heightErr = document.getElementById('heightErr');

    var width = document.getElementById('width').value;
    var widthErr = document.getElementById('widthErr');

    var other_equipment = document.getElementById('other_equipment').value;
    var other_equipmentErr = document.getElementById('other_equipmentErr');

    var description = document.getElementById('description').value;
    var descriptionErr = document.getElementById('descriptionErr');

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
    if (height.trim() === '') {
        heightErr.textContent = 'Không được để trống';
        isValid = false;
    } else {
        heightErr.innerText = "";
    }

    if(width.trim() === ''){
        widthErr.textContent = "Không được để trống !";
        isValid = false;
    }else {
        widthErr.innerText = "";
    }


    if(other_equipment.trim() === ''){
        other_equipmentErr.textContent = "Không được để trống !";
        isValid = false;
    }else {
        other_equipmentErr.innerText = "";
    }

    if(description.trim() === ''){
        descriptionErr.textContent = "Không được để trống !";
        isValid = false;
    }else {
        descriptionErr.innerText = "";
    }
    // Nếu có bất kỳ lỗi nào, ngăn chặn sự kiện mặc định của form
    if (!isValid) {
        event.preventDefault();
    }
}

function updateFormRoom(event){
    var nameRoom = document.getElementById('nameRoom').value;
    var nameRoomErr = document.getElementById('nameRoomErr');
    var descriptionRoom = document.getElementById('descriptionRoom').value;
    var descriptionRoomErr = document.getElementById('descriptionRoomErr');
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
    if(nameRoom.trim() === ''){
        nameRoomErr.textContent = "Tên phòng không được trống !";
        isValid = false;
    }else {
        nameRoomErr.innerText = "";
    }if(descriptionRoom.trim() === ''){
        descriptionRoomErr.textContent = "Mô tả trống";
        isValid = false
    }else {
        descriptionRoomErr.innerText = "";
    }
    if (!isValid) {
        event.preventDefault(); // This line prevents the default behavior without any visible indication
    }
}
document.addEventListener('DOMContentLoaded', function() {
    var button = document.querySelector('.btn-outline-success');
    var divToShow = document.getElementById('addSeat');

    button.addEventListener('click', function() {
        divToShow.style.display = 'block';
    });
});