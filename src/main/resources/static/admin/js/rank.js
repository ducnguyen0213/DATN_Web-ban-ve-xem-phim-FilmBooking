function validateRank(event){
    var nameRank = document.getElementById('nameRank').value;
    var nameRankErr = document.getElementById('nameRankErr');
    var descriptionRank = document.getElementById('desciptionRank').value;
    var descriptionRankErr = document.getElementById('desciptionRankErr');
    var pointRank = document.getElementById('pointRank').value;
    var pointRankErr = document.getElementById('pointRankErr');
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

    if(nameRank.trim() === ''){
        nameRankErr.textContent = "Tên hạng không được để trống !";
        isValid = false;
    }else {
        nameRankErr.innerText = "";
    }
    if(pointRank.trim() === ''){
        pointRankErr.textContent = "Điểm hạng không được để trống !";
        isValid = false;
    }else if(isNaN(pointRank) || pointRank < 0){
        pointRankErr.textContent = "Điểm set hạng không được âm !"
        isValid = false;
    }else {
        pointRankErr.innerText = "";
    }
    if(descriptionRank.trim() === ''){
        descriptionRankErr.textContent = "Mô tả không được để trống !";
        isValid = false;
    }else {
        descriptionRankErr.innerText = "";
    }
    if (!isValid) {

        event.preventDefault();
    }
}