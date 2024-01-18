
function validateForm(event) {

    var name = document.getElementById('name').value;
    var nameError = document.getElementById('nameFvl');
    var price = document.getElementById('price').value;
    var priceErr = document.getElementById('priceFvl');
    var description = document.getElementById('description').value;
    var descriptionErr = document.getElementById('descriptionFvl');
    var image = document.getElementById('image').value;
    var imageErr = document.getElementById('imageFvl');


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
    if (name.trim() === '') {
        nameError.textContent = "Tên đồ ăn không được để trống !"
        isValid = false;
    } else {
        nameError.innerText = "";
    }

    if (price.trim() === '') {
        priceErr.textContent = "Giá đồ ăn không được để trống !"
        isValid = false;
    } else if (isNaN(price) || price < 0) {
        priceErr.textContent = "Giá đồ ăn không được âm !"
        isValid = false;
    } else {
        priceErr.innerText = "";
    }
    if (description.trim() === '') {
        descriptionErr.textContent = "Mô tả không được để trống !"
        isValid = false;
    } else {
        descriptionErr.innerText = "";
    }
    // if (image.trim() === '') {
    //     imageErr.textContent = "Hình ảnh không được để trống !"
    //     isValid = false;
    // } else {
    //     imageErr.innerText = "";
    // }
    if (!isValid) {

        event.preventDefault();
    }
}
