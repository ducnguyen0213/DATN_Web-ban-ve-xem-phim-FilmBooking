function validateDirector(event){
    const nameDirector = document.getElementById('nameDirector').value;
    const nameDirectorErr = document.getElementById('nameDirectorErr');
    var isValid = true;
    if(nameDirector.trim() === ''){
        nameDirectorErr.textContent = 'Tên đạo diễn không được trống';
        isValid =false;
    }else{
        nameDirectorErr.innerText = '';
    }
    if(!isValid){
        event.preventDefault();
    }

};
function checkNameDirector(event){
    const nameDirector = document.getElementById('nameDirector').value;
    const nameDirectorErr = document.getElementById('nameDirectorErr');
    var isValid = true;
    fetch('/movie/checkDirector?name=' + nameDirector)
        .then(response => response.json())
        .then(data => {
            if (data.isDuplicate) {
                nameDirectorErr.textContent = "Tên đạo diễn đã tồn tại";
                isValid = false;
            } else {
                nameDirectorErr.textContent = ""; // Xóa thông báo lỗi
            }
            if(!isValid){
                event.preventDefault();
            }
        })
        .catch(error => console.error('Lỗi: ' + error));
}
function validateMovieType(event){
    const nameMovieType = document.getElementById('nameMovieType').value;
    const nameMovieTypeErr = document.getElementById('nameMovieTypeErr');
    var isValid = true;
    if(nameMovieType.trim() === ''){
        nameMovieTypeErr.textContent = 'Tên thể loại phim không được trống';
        isValid = false;
    }
    else{
        nameMovieTypeErr.innerText = '';
    }
    if(!isValid){
        event.preventDefault();
    }
};
function checkNameMovieType(event){
    const nameMovieType = document.getElementById('nameMovieType').value;
    const nameMovieTypeErr = document.getElementById('nameMovieTypeErr');
    var isValid = true;
    fetch('/movie/checkMovieType?name=' + nameMovieType)
        .then(response => response.json())
        .then(data => {
            if (data.checkMovieType) {
                nameMovieTypeErr.textContent = "Tên thể loại phim đã tồn tại";
                // isValid = false;
            } else {
                nameMovieTypeErr.textContent = ""; // Xóa thông báo lỗi
            }
        })
        .catch(error => console.error('Lỗi: ' + error));
}
function validateLanguage(event){
    const nameLanguage = document.getElementById('nameLanguage').value;
    const nameLanguageErr = document.getElementById('nameLanguageErr');
    var isValid = true;
    if(nameLanguage.trim() === ''){
        nameLanguageErr.textContent = 'Tên ngôn ngữ không được để trống';
        isValid = false;
    }else{
        nameLanguageErr.innerText = '';
    }
    if(!isValid){
        event.preventDefault();
    }
};
function checkNameLanguage(event){
    const nameLanguage = document.getElementById('nameLanguage').value;
    const nameLanguageErr = document.getElementById('nameLanguageErr');
    var isValid = true;
    fetch('/movie/checkLanguage?name=' + nameLanguage)
        .then(response => response.json())
        .then(data => {
            if (data.isDuplicate) {
                nameLanguageErr.textContent = "Tên ngôn ngữ đã tồn tại";
                isValid = false;
            } else {
                nameLanguageErr.textContent = ""; // Xóa thông báo lỗi
            }
            if(!isValid){
                event.preventDefault();
            }
        })
        .catch(error => console.error('Lỗi: ' + error));
}

function validatePerformer(event){
    const namePerformer = document.getElementById('namePerformer').value;
    const namePerformerErr = document.getElementById('namePerformerErr');
    var isValid = true;
    if(namePerformer.trim() === ''){
        namePerformerErr.textContent = 'Tên diễn viên không được trống';
        isValid = false;
    }else {
        namePerformerErr.innerText = '';
    }
    if(!isValid){
        event.preventDefault()
    }
}
function checkNamePerformer(event){
    const namePerformer = document.getElementById('namePerformer').value;
    const namePerformerErr = document.getElementById('namePerformerErr');
    var isValid = true;
    fetch('/movie/checkPerformer?name=' + namePerformer)
        .then(response => response.json())
        .then(data => {
            if (data.isDuplicate) {
                namePerformerErr.textContent = "Tên diễn viên đã tồn tại";
                isValid = false;
            } else {
                namePerformerErr.textContent = ""; // Xóa thông báo lỗi
            }
            if(!isValid){
                event.preventDefault();
            }
        })
        .catch(error => console.error('Lỗi: ' + error));
}