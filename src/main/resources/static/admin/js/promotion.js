function validatePPromotion(event) {
    var codePromotion = document.getElementById('codePromotion').value;
    var namePromotion = document.getElementById('namePromotion').value;
    var percentPromotion = document.getElementById('percentPromotion').value;
    var quantityPromotion = document.getElementById('quantityPromotion').value;
    var startDatePromotion = document.getElementById('startDatePromotion').value;
    var endDatePromotion = document.getElementById('endDatePromotion').value;
    var codePromotionErr = document.getElementById('codePromotionErr');
    var namePromotionErr = document.getElementById('namePromotionErr');
    var percentPromotionErr = document.getElementById('percentPromotionErr');
    var quantityPromotionErr = document.getElementById('quantityPromotionErr');
    var startDatePromotionErr = document.getElementById('startDatePromotionErr');
    var endDatePromotionErr = document.getElementById('endDatePromotionErr');
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
    if(codePromotion.trim() === ''){
        codePromotionErr.textContent = "Mã khuyến mãi không được để trống !";
        isValid = false;
    }else {
        codePromotionErr.innerText = "";
    }
    if(namePromotion.trim() === ''){
        namePromotionErr.textContent = "Tên khuyển mãi không được để trống !";
        isValid = false;
    }else {
        namePromotionErr.innerText = "";
    }
    if (percentPromotion.trim() === ''){
        percentPromotionErr.textContent = "Phần trăm không được để trống !";
        isValid = false;
    }else if(isNaN(percentPromotion) || percentPromotion < 0 || percentPromotion > 100){
        percentPromotionErr.textContent = "Phần trăm phải nằm trong khoảng từ 0 đến 100!";
        isValid = false;
    }
    else{
        percentPromotionErr.innerText = "";
    }
    if(quantityPromotion.trim() === ''){
        quantityPromotionErr.textContent = "Số lượng không được để trống !";
        isValid = false;
    }else if(isNaN(quantityPromotion) || quantityPromotion < 0){
        quantityPromotionErr.textContent = "Số lượng không được âm !";
        isValid = false;
    }else {
        quantityPromotionErr.innerText = "";
    }
    if(startDatePromotion.trim() === ''){
        startDatePromotionErr.textContent = "Ngày bắt đầu không được để trống !";
        isValid = false;
    }else{
        startDatePromotionErr.innerText = "";
    }
    if(endDatePromotion.trim() === ''){
        endDatePromotionErr.textContent = "Ngày kết thúc không được để trống !";
        isValid = false;
    }else if(new Date(endDatePromotion) <= new Date(startDatePromotion)){
        endDatePromotionErr.textContent = "Ngày kết thúc phải lớn hơn ngày bắt đầu !";
        isValid = false;
    }
    else {
        endDatePromotionErr.innerText = "";
    }
    if (!isValid) {
        event.preventDefault(); // This line prevents the default behavior without any visible indication
    }
}
