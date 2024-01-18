function validateFormMovie(event) {
    // Lấy các giá trị từ các trường dữ liệu
    const nameMovie = document.getElementById('nameMovie').value;
    const movieDurations = document.getElementById('movieDuration').value;
    const directors = document.getElementById('demo-multiple-select1').value; // Lấy giá trị từ đối tượng select
    const performers = document.getElementById('demo-multiple-select4').value; // Lấy giá trị từ đối tượng select
    const languages = document.getElementById('demo-multiple-select3').value; // Lấy giá trị từ đối tượng select
    const movieTypes = document.getElementById('demo-multiple-select2').value; // Lấy giá trị từ đối tượng select
    const trailers = document.getElementById('trailler').value;
    const descriptions = document.getElementById('description').value;
    const premiereDate = document.getElementById('premiereDate').value;
    const image = document.getElementById('image').value;
    const endDate = document.getElementById('endDate').value;

    // Lấy các phần hiển thị lỗi
    const nameError = document.getElementById('nameMovieErr');
    const movieDurationError = document.getElementById('movieDurationErr');
    const directorError = document.getElementById('directorErr');
    const performerError = document.getElementById('performersErr');
    const languageError = document.getElementById('languagesErr');
    const premieraDateErr = document.getElementById('premiereDateErr');
    const endDateErr = document.getElementById('endDateErr');
    const movieTypeError = document.getElementById('movieTypeErr');
    const trailerError = document.getElementById('traillerErr');
    const descriptionError = document.getElementById('descriptionErr');
    const imageErr = document.getElementById('imageErr');

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
    if (nameMovie.trim() === '') {
        nameError.textContent = 'Tên phim không được để trống';
        isValid = false;
    } else {
        nameError.textContent = '';
    }
    if (movieDurations.trim() === '') {
        movieDurationError.textContent = 'Thời lượng phim không được để trống';
        isValid = false;
    } else if (isNaN(movieDurations) || +movieDurations <= 0) {
        movieDurationError.textContent = 'Thời lượng phim không được âm';
        isValid = false;
    } else {
        movieDurationError.textContent = '';
    }
    if (directors.length === 0) { // Kiểm tra trường "Đạo diễn"
        directorError.textContent = 'Đạo diễn không được để trống';
        isValid = false;
    } else {
        directorError.textContent = '';
    }
    if (performers.length === 0) { // Kiểm tra trường "Diễn viên"
        performerError.textContent = 'Diễn viên không được để trống';
        isValid = false;
    } else {
        performerError.textContent = '';
    }
    if (languages.length === 0) { // Kiểm tra trường "Ngôn ngữ"
        languageError.textContent = 'Ngôn ngữ không được để trống';
        isValid = false;
    } else {
        languageError.textContent = '';
    }
    if (movieTypes.length === 0) { // Kiểm tra trường "Thể loại phim"
        movieTypeError.textContent = 'Thể loại phim không được để trống';
        isValid = false;
    } else {
        movieTypeError.textContent = ''; 
    }
    if (trailers.trim() === '') {
        trailerError.textContent = 'Trailer không được để trống';
        isValid = false;
    } else {
        trailerError.textContent = '';
    }
    if (image.trim() === '') {
        imageErr.textContent = "Hình ảnh không được để trống !";
        isValid = false;
    } else {
        imageErr.innerText = "";
    }
    if (premiereDate.trim() === '') {
        premieraDateErr.textContent = "Ngày bắt đầu không được để trống !";
        isValid = false;
    } else {
        premieraDateErr.innerText = "";
    }
    if (endDate.trim() === '') {
        endDateErr.textContent = "Ngày kết thúc không được để trống !";
        isValid = false;
    }
    // Kiểm tra ngày kết thúc > ngày ra mắt
    else if (new Date(endDate) <= new Date(premiereDate)) {
        endDateErr.textContent = 'Ngày kết thúc phải lớn hơn ngày ra mắt';
        isValid = false;
    } else {
        endDateErr.textContent = '';
    }

    // Nếu có bất kỳ lỗi nào, ngăn chặn sự kiện mặc định của form
    if (!isValid) {
        Toast.fire({
            icon: "error",
            title: "Thêm phim thất bại"
        });
        document.getElementById('loading-overlay').style.display = 'none';
        event.preventDefault();
    }else {
        Toast.fire({
            icon: "success",
            title: "Thêm phim thành công"
        });
        document.getElementById('loading-overlay').style.display = 'flex';
    }
}

function checkDuplicateMovieName() {
    var name = document.getElementById("nameMovie").value;
    var errorSpan = document.getElementById("nameMovieErr");

    // Gửi yêu cầu kiểm tra tính duy nhất của tên phim đến máy chủ
    // Sử dụng AJAX hoặc Fetch API

    // Ví dụ sử dụng Fetch API
    fetch('/movie/checkDuplicateName?name=' + name)
        .then(response => response.json())
        .then(data => {
            if (data.isDuplicate) {
                errorSpan.textContent = "Tên phim đã tồn tại";
            } else {
                errorSpan.textContent = ""; // Xóa thông báo lỗi
            }
        })
        .catch(error => console.error('Lỗi: ' + error));
}



// Lấy giá trị ngày ra mắt và ngày kết thúc từ trường th:value
var premiereDateValue = document.getElementById("premiereDate").value;
var endDateValue = document.getElementById("endDate").value;

// Định dạng lại ngày thành "mm/dd/yyyy" nếu giá trị hợp lệ
var formattedPremiereDate = formatDate(premiereDateValue);
var formattedEndDate = formatDate(endDateValue);

// Đặt giá trị đã định dạng lại hoặc giá trị mặc định và placeholder vào trường input
var premiereDateInput = document.getElementById("premiereDate");
premiereDateInput.value = formattedPremiereDate || "";
premiereDateInput.placeholder = "mm/dd/yyyy";

var endDateInput = document.getElementById("endDate");
endDateInput.value = formattedEndDate || "";
endDateInput.placeholder = "mm/dd/yyyy";

function formatDate(inputDate) {
    if (inputDate) {
        var date = new Date(inputDate);
        var month = (date.getMonth() + 1).toString().padStart(2, "0");
        var day = date.getDate().toString().padStart(2, "0");
        var year = date.getFullYear();
        return month + "/" + day + "/" + year;
    }
    return "";
}
