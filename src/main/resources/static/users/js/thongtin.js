$('.unmask').on('click', function () {
    var inputField = $(this).prev('input');
    var currentType = inputField.attr('type');

    if (currentType === 'password') {
        inputField.attr('type', 'text');
    } else {
        inputField.attr('type', 'password');
    }

    return false;
});

var priceElements = document.querySelectorAll("#price,#price1,#priceCho,#priceCho1, #priceSum,#priceHuy, #pricee");

// Loop through each element and format its content to VND
priceElements.forEach(function (element) {
    var price = parseFloat(element.innerHTML);
    var formattedPrice = new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(price);
    // Set the formatted price as the inner HTML of the element
    element.innerHTML = formattedPrice;
});

// Lấy tất cả các phần tử có id là "poin"
var elements = document.querySelectorAll("#poin, #poinn, #poinnn");

// Duyệt qua từng phần tử và thực hiện việc bỏ hết số 0 sau dấu chấm rồi chuyền giá trị ngược lại
elements.forEach(function (element) {
    var originalNumber = element.innerHTML;
    var number = parseFloat(originalNumber);
    var roundedNumber = number.toFixed(0);
    element.innerHTML = roundedNumber;
});



