// const selectBtn = document.querySelector(".select-btn"),
//     items = document.querySelectorAll(".item");
//
// selectBtn.addEventListener("click", () => {
//     selectBtn.classList.toggle("open");
// });
//
// items.forEach(item => {
//     item.addEventListener("click", () => {
//         item.classList.toggle("checked");
//
//         let checked = document.querySelectorAll(".checked"),
//             btnText = document.querySelector(".btn-text");
//
//         if(checked && checked.length > 0){
//             btnText.innerText = `${checked.length} Phim đã chọn`;
//         }else{
//             btnText.innerText = "Danh sách phim";
//         }
//     });
// })
// document.addEventListener("DOMContentLoaded", function() {
//     var checkboxForm = document.getElementById("checkboxForm");
//     var selectedItems = document.getElementById("themVaoList");
//     console.log(selectedItems)
//     checkboxForm.addEventListener("change", function() {
//         selectedItems.innerHTML = "";
//         var checkedItems = checkboxForm.querySelectorAll('input[name="checkboxItem"]:checked');
//         console.log(checkedItems)
//         for (var i = 0; i < checkedItems.length; i++) {
//             var item = checkedItems[i].value;
//             var li = document.createElement("li");
//             li.textContent = item;
//             selectedItems.appendChild(li);
//         }
//         console.log(selectedItems)
//     });
// });