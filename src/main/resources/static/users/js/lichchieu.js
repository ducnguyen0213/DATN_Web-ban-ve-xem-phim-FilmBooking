



var selects = document.querySelectorAll('select');

selects.forEach(function(select) {
  select.addEventListener('focus', function() {
    select.size = 5;
    select.classList.add('fadeIn');
    select.classList.remove('fadeOut');
    select.style.backgroundColor = '#FFF';
  });

  select.addEventListener('blur', function() {
    select.size = 1;
    select.classList.add('fadeOut');
    select.classList.remove('fadeIn');
    select.style.backgroundColor = '#FFF';
  });

  select.addEventListener('change', function() {
    select.size = 1;
    select.blur();
    select.style.backgroundColor = '#FFF';
  });

  select.addEventListener('mouseover', function() {
    if(select.size === 1) {
      select.style.backgroundColor = 'rgb(247, 247, 247)';
    }
  });

  select.addEventListener('mouseout', function() {
    if(select.size === 1) {
      select.style.backgroundColor = '#FFF';
    }
  });
});

var currentDate = new Date();
var currentDay = currentDate.getDate();
var currentMonth = currentDate.getMonth() + 1;
var currentYear = currentDate.getFullYear();

var sevenDays = [];

for (var i = 0; i < 7; i++) {
  var nextDay = new Date(currentYear, currentMonth - 1, currentDay + i);

  var dateArray = [nextDay.getDate(), nextDay.getMonth() + 1,nextDay.getFullYear() ]; // Mảng chứa năm, tháng và ngày
  dateArray = dateArray.map(function(item) { // Chuyển đổi các phần tử trong mảng thành chuỗi và kiểm tra xem có cần thêm số 0 ở phía trước không
    return (item < 10 ? "0" : "") + item;
  });

  var dateString = dateArray.join('/'); // Xây dựng lại chuỗi ngày tháng

  sevenDays.push(dateString);
}

var selectElement = document.getElementById('startAt');

for (var j = 0; j < sevenDays.length; j++) {
  var optionElement = document.createElement('option');
  optionElement.value = sevenDays[j];
  optionElement.textContent = sevenDays[j];
  selectElement.appendChild(optionElement);
}

// Lấy tất cả các option trong dropdown
var options = document.getElementById('startAt').getElementsByTagName('option');

// Lặp qua từng option
for (var i = 0; i < options.length; i++) {
  var currentValue = options[i].value;

  // Kiểm tra xem giá trị đã được chọn trước đó chưa
  for (var j = 0; j < i; j++) {
    if (options[j].value === currentValue) {
      // Nếu đã được chọn trước đó, loại bỏ option này
      options[i].remove();
      i--; // Giảm chỉ số i để xử lý lại option mới sau khi loại bỏ
      break;
    }
  }
}

// const myData = document.getElementById("gio").textContent;
// var dateTime = new Date(myData);
// var hour = dateTime.getHours(); // Lấy giờ (0-23)
// var formattedHour = String(hour).padStart(2, '0');
// var minute = dateTime.getMinutes();
// var formattedMinute = String(minute).padStart(2, '0');
// var formattedTime = formattedHour + ":" + formattedMinute;
// document.getElementById("gio").innerHTML = formattedTime;
var gioElements = document.querySelectorAll("#gio");

gioElements.forEach(function(element) {
  var myData = element.textContent;
  var dateTime = new Date(myData);
  var hour = dateTime.getHours(); // Lấy giờ (0-23)
  var formattedHour = String(hour).padStart(2, '0');
  var minute = dateTime.getMinutes();
  var formattedMinute = String(minute).padStart(2, '0');
  var formattedTime = formattedHour + ":" + formattedMinute;
  element.innerHTML = formattedTime;
})
