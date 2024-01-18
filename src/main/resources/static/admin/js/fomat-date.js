// var dateInput = document.getElementById("dateFomat");

const { log } = require("console");

// // Lắng nghe sự kiện khi giá trị của input thay đỗi
// dateInput.addEventListener("input", function() {
//   var selectedDate = new Date(dateInput.value); // Lấy giá trị ngày tháng từ input
//   var formattedDate = selectedDate.toLocaleDateString("en-GB"); // Định dạng ngày tháng thành "dd/MM/yyyy"

//   dateInput.value = formattedDate; // Gán giá trị mới đã định dạng trở lại cho input
// });
// var button = document.getElementById("myButton");

// if (button)  // Kiểm tra xem phần tử có tồn tại hay không
// {
//   function handleClick(){}  console.log("Button clicked");
//   button.addEventListener("click", handleClick);
// } else {
//   console.log("Button not found!"); // Thông báo nếu không tìm thấy phần tử
// }
function handledatechange() {
  var date_picker = document.getElementById("dateFomat").value;
  changeDateFomat(date_picker); //
  // alert('Date'+ date_picker + ': ' + changeDateFomat(date_picker));
}
function changeDateFomat(val) {
  const myArray = val.split("-");
  let year = myArray[0];
  let month = myArray[1];
  let day = myArray[2];
  let fomat = day + "-" + month + "-" + year;
  return fomat;
}
function formatDate() {
  var dateInput = document.getElementById("dateInput");
  // var date = new Date(dateInput.value);
  // var formattedDate = date.toLocaleDateString("en-CA");
  // dateInput.value = formattedDate;
  // var dateValue = "2022-01-15"; // ví dụ chuỗi ngày tháng "yyyy-MM-dd"
  var dateParts = dateInput.split("-");
  var formattedDate = dateParts[2] + "/" + dateParts[1] + "/" + dateParts[0];
  console.log(formattedDate);
}
// formate date input 8h - 2h
var currentDate = new Date();
currentDate.setDate(currentDate.getDate() + 1);
var currentDay = currentDate.toISOString().slice(0, 10);

var startTime = "08:00";
var endTime = "02:00";

var inputElement = document.createElement("input");
inputElement.type = "time";
inputElement.min = currentDay + "T" + startTime;
inputElement.max = currentDay + "T" + endTime;

document.body.appendChild(inputElement);

// addTimeInput();

const dateInput = document.getElementById("myTimeInput");
dateInput.addEventListener("input", function () {
  const selectedDate = new Date(dateInput.value);
  const formattedDate = selectedDate.toLocaleDateString("en-GB", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
  dateInput.value = formattedDate;
});

// hiển thị ngày khi chọn phim
function showDateMovie() {
  var phim = document.getElementById("date_time_movie").value;
  var dateTimeDiv = document.getElementById("date-movie");

  if (phim === "") {
    dateTimeDiv.style.display = "none";
  } else {
    dateTimeDiv.style.display = "block";
    console.log('block');
  }
}
const inputDate = document.getElementById("yourInput").value;
const date = new Date(inputDate);
const year = date.getFullYear();
const month = String(date.getMonth() + 1).padStart(2, '0');
const day = String(date.getDay()).padStart(2, '0');
const fomattedDate = `${month}-${day}-${year}`
inputDate.value = fomattedDate;

const inputDate1 = document.getElementById("yourInput1").value;
const date1 = new Date(inputDate1);
const year1 = date.getFullYear();
const month1 = String(date1.getMonth() + 1).padStart(2, '0');
const day1 = String(date1.getDay()).padStart(2, '0');
const fomattedDate1 = `${month1}-${day1}-${year1}`
inputDate1.value = fomattedDate1;