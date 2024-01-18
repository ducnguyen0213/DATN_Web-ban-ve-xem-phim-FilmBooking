
function Noti({ icon = 'success', text, timer = 4000 }) {
    var noti_con = document.createElement('div');
    var noti_alert = document.createElement('div');
    var noti_icon = document.createElement('div');
    noti_icon.setAttribute('class', 'Noti_icon')
    noti_con.setAttribute('class', 'Noti_con');
    noti_alert.setAttribute('class', 'noti_alert');
    document.body.appendChild(noti_con);
    document.querySelector('.Noti_con').prepend(noti_alert);
    noti_alert.innerHTML = '<p>' + text + '</p>';
    noti_alert.append(noti_icon);
    if (icon == 'success') {
        noti_icon.style.background = '#00b972';
        noti_icon.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" preserveAspectRatio="xMidYMid meet" viewBox="0 0 24 24"><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><path stroke-dasharray="60" stroke-dashoffset="60" d="M3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12Z"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.5s" values="60;0"/></path><path stroke-dasharray="14" stroke-dashoffset="14" d="M8 12L11 15L16 10"><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.6s" dur="0.2s" values="14;0"/></path></g></svg>`;

    } else if (icon == 'info') {
        noti_icon.style.background = '#0395FF';
        noti_icon.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" preserveAspectRatio="xMidYMid meet" viewBox="0 0 24 24"><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-width="2"><path stroke-dasharray="60" stroke-dashoffset="60" d="M12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3Z"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.5s" values="60;0"/></path><path stroke-dasharray="20" stroke-dashoffset="20" d="M8.99999 10C8.99999 8.34315 10.3431 7 12 7C13.6569 7 15 8.34315 15 10C15 10.9814 14.5288 11.8527 13.8003 12.4C13.0718 12.9473 12.5 13 12 14"><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.6s" dur="0.4s" values="20;0"/></path></g><circle cx="12" cy="17" r="1" fill="currentColor" fill-opacity="0"><animate fill="freeze" attributeName="fill-opacity" begin="1s" dur="0.2s" values="0;1"/></circle></svg>`;

    } else if (icon == 'danger' || icon == 'error') {
        noti_icon.style.background = '#FF032C';
        noti_icon.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" preserveAspectRatio="xMidYMid meet" viewBox="0 0 24 24"><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-width="2"><path stroke-dasharray="60" stroke-dashoffset="60" d="M12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3Z"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.5s" values="60;0"/></path><path stroke-dasharray="8" stroke-dashoffset="8" d="M12 12L16 16M12 12L8 8M12 12L8 16M12 12L16 8"><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.6s" dur="0.2s" values="8;0"/></path></g></svg>`;
    } else {
        noti_icon.style.background = '#00b972';
        noti_icon.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" preserveAspectRatio="xMidYMid meet" viewBox="0 0 24 24"><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><path stroke-dasharray="60" stroke-dashoffset="60" d="M3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12Z"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.5s" values="60;0"/></path><path stroke-dasharray="14" stroke-dashoffset="14" d="M8 12L11 15L16 10"><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.6s" dur="0.2s" values="14;0"/></path></g></svg>`;

    }

    setTimeout(() => {
        noti_alert.remove();
    }, timer);
}
function success() {
    Noti({
        text: 'Thành công',
        icon: 'success',
        timer: 2500
    })
}
function info() {
    Noti({
        text: 'Sửa thành công',
        icon: 'info',
        timer: 5000
    })
}
function danger() {
    Noti({
        text: 'Thất bại',
        icon: 'danger',
        timer: 5000
    })
}

// Noti({
//     text: 'Dương đây nhe',
//     icon: 'success',
//     timer: 7000
// })
// Noti({
//     text: 'người yêu dương tên linh nhe',
//     icon: 'info',
//     timer: 7000
// })
// Noti({
//     text: 'hê sờ lố',
//     icon: 'danger',
//     timer: 7000
// })

// let notifications = document.querySelector('.thong_bao');
// let success = document.getElementById('success');
// let error = document.getElementById('error');
// let warning = document.getElementById('warning');
// let info = document.getElementById('info');
//
// function createToast(type, icon, title, text){
//     let newToast = document.createElement('div');
//     newToast.innerHTML = `
//             <div class="toast ${type}">
//                 <i class="${icon}"></i>
//                 <div class="content">
//                     <div class="title">${title}</div>
//                     <span>${text}</span>
//                 </div>
//                 <i class="fa-solid fa-xmark" onclick="(this.parentElement).remove()"></i>
//             </div>`;
//     notifications.appendChild(newToast);
//     newToast.timeOut = setTimeout(
//         ()=>newToast.remove(), 5000
//     )
// };
// success.addEventListener("click",function(){
//     let type = 'success';
//     let icon = 'fa-solid fa-circle-check';
//     let title = 'Success';
//     let text = 'This is a success toast.';
//     createToast(type, icon, title, text);
// });
// // error.onclick = function(){
// //     let type = 'error';
// //     let icon = 'fa-solid fa-circle-exclamation';
// //     let title = 'Error';
// //     let text = 'This is a error toast.';
// //     createToast(type, icon, title, text);
// // }
// warning.addEventListener("click",function(){
//     let type = 'warning';
//     let icon = 'fa-solid fa-triangle-exclamation';
//     let title = 'Warning';
//     let text = 'This is a warning toast.';
//     createToast(type, icon, title, text);
// });
// info.addEventListener("click",function(){
//     let type = 'info';
//     let icon = 'fa-solid fa-circle-info';
//     let title = 'Info';
//     let text = 'This is a info toast.';
//     createToast(type, icon, title, text);
// })