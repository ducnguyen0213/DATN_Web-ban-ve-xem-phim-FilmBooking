const html = document.documentElement;
const body = document.body;
const menuLinks = document.querySelectorAll(".admin-menu a");
const collapseBtn = document.querySelector(".admin-menu .collapse-btn");
const toggleMobileMenu = document.querySelector(".toggle-mob-menu");
const switchInput = document.querySelector(".switch input");
const switchLabel = document.querySelector(".switch label");
const switchLabelText = switchLabel.querySelector("span:last-child");
const collapsedClass = "collapsed";
const lightModeClass = "light-mode";

/*TOGGLE HEADER STATE*/
collapseBtn.addEventListener("click", function () {
    body.classList.toggle(collapsedClass);
    this.getAttribute("aria-expanded") == "true"
        ? this.setAttribute("aria-expanded", "false")
        : this.setAttribute("aria-expanded", "true");
    this.getAttribute("aria-label") == "collapse menu"
        ? this.setAttribute("aria-label", "expand menu")
        : this.setAttribute("aria-label", "collapse menu");
});

/*TOGGLE MOBILE MENU*/
toggleMobileMenu.addEventListener("click", function () {
    body.classList.toggle("mob-menu-opened");
    this.getAttribute("aria-expanded") == "true"
        ? this.setAttribute("aria-expanded", "false")
        : this.setAttribute("aria-expanded", "true");
    this.getAttribute("aria-label") == "open menu"
        ? this.setAttribute("aria-label", "close menu")
        : this.setAttribute("aria-label", "open menu");
});

/*SHOW TOOLTIP ON MENU LINK HOVER*/
for (const link of menuLinks) {
    link.addEventListener("mouseenter", function () {
        if (
            body.classList.contains(collapsedClass) &&
            window.matchMedia("(min-width: 768px)").matches
        ) {
            const tooltip = this.querySelector("span").textContent;
            this.setAttribute("title", tooltip);
        } else {
            this.removeAttribute("title");
        }
    });
}

/*TOGGLE LIGHT/DARK MODE*/
if (localStorage.getItem("dark-mode") === "false") {
    html.classList.add(lightModeClass);
    switchInput.checked = false;
    switchLabelText.textContent = "Light";
}

switchInput.addEventListener("input", function () {
    html.classList.toggle(lightModeClass);
    if (html.classList.contains(lightModeClass)) {
        switchLabelText.textContent = "Light";
        localStorage.setItem("dark-mode", "false");
    } else {
        switchLabelText.textContent = "Dark";
        localStorage.setItem("dark-mode", "true");
    }
});

//custom
$(document).ready(function () {
    $('.sub-btn').click(function () {
        $(this).next('.dropdown_menu').slideToggle();
        $(this).find('.dropdown').toggleClass('rotate');
    })
});
$(document).ready(function () {
    $('.sub-btn_one').click(function () {
        $(this).next('.dropdown_menu_one').slideToggle();
        $(this).find('.dropdown_one').toggleClass('rotate');
    })
});
$(document).ready(function () {
    $('.sub-btn_two').click(function () {
        $(this).next('.dropdown_menu_two').slideToggle();
        $(this).find('.dropdown_two').toggleClass('rotate');
    })
});
// $(document).ready(function () {
//   $('.sub-btn_three').click(function () {
//     $(this).next('.dropdown_menu_three').slideToggle();
//     $(this).find('.dropdown_three').toggleClass('rotate'); 
//   })
// });
//Phân trang
var paginationList = document.querySelector(".pagination");
//
// Get the page links
var pageLinks = paginationList.querySelectorAll(".page-link");

var maxVisiblePages = pageLinks;
var currentPage =  20;

var totalPages = pageLinks.length;
// Check the number of page links
if (totalPages > 10) {
    // Remove extra page links after index 5
    for (var i = 5; i < totalPages- 2; i++) {
        pageLinks[i].style.display = "none";
    }
}
if (currentPage - maxVisiblePages / 2 > 1) {
    var firstEllipsis = document.createElement("li");
    firstEllipsis.innerHTML = "...";
    // paginationList.insertBefore(firstEllipsis, pageLinks[currentPage - Math.floor(maxVisiblePages / 2)]);
}

if (currentPage + maxVisiblePages / 2 < totalPages) {
    var lastEllipsis = document.createElement("li");
    console.log(lastEllipsis);
    lastEllipsis.innerHTML = "...";
    // paginationList.insertBefore(lastEllipsis, pageLinks[currentPage + Math.floor(maxVisiblePages / 2) + 1]);
}

//
// var totalPages = pageLinks.length;
//
// // Số trang tối đa muốn hiển thị
// var maxVisiblePages = 10;
//
// // Xác định trang hiện tại (đây là ví dụ, bạn cần cái này từ backend hoặc cách khác)
// var currentPage = 3;
//
// // Số trang tối đa muốn hiển thị
// // Check the number of page links
// if (totalPages > maxVisiblePages) {
//     if (currentPage - maxVisiblePages / 2 > 1) {
//         var firstEllipsis = document.createElement("li");
//         firstEllipsis.innerHTML = "...";
//         // paginationList.insertBefore(firstEllipsis, pageLinks[currentPage - Math.floor(maxVisiblePages / 2)]);
//     }
//
//     if (currentPage + maxVisiblePages / 2 < totalPages) {
//         var lastEllipsis = document.createElement("li");
//         lastEllipsis.innerHTML = "...";
//         // paginationList.insertBefore(lastEllipsis, pageLinks[currentPage + Math.floor(maxVisiblePages / 2) + 1]);
//     }
// }


// pageLinks[currentPage - 1].classList.add("active");
/////////////////////////////////////////////
function overLoad() {
    document.getElementById('loading-overlay').style.display = 'flex';
}



