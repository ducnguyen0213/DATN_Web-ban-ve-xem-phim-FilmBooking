function updateRegions() {
  var list = document.getElementById("list").value;
  var regionSelect = document.getElementById("region");

  // Xóa tất cả các tùyọn hiện có
  while (regionSelect.options.length > 0) {
    regionSelect.remove(0);
  }

  if (list === "all") {
    var regions = [""];
  } else if (list === "Service") {
    var regions = ServiceList;
  } else if (list === "movie") {
    var regions = movieList;
  } else if (list === "schedule") {
    var regions = scheduleList;
  } else {
    var regions = ["Vui lòng chọn 1 mục"]; // Nếu không chọn mục nào
  }
  console.log();

  // Thêm các tùy chọn vào danh sách khu vực
  var option = document.createElement("option");
  option.text = "Tất cmn cả";
  regionSelect.add(option);

  for (var i = 0; i < regions.length; i++) {
    var option = document.createElement("option");
    option.text = regions[i].name;
    option.value = regions[i].id;
    regionSelect.add(option);
  }
}

function updateChart() {
  var selectedType = document.getElementById("list").value;
  var selectedValue = document.getElementById("region").value;

  if (selectedType == "Service") {
  } else {
  }
}

var myChartThree = echarts.init(document.getElementById("myChartThree"));
var dataChartThreeX = [];
var dataChartThreeY = [];
for (var i = 0; i < revenueMovie.length; i++) {
  var totalMoneyMovie = revenueMovie[i][1]; // Access the total money
  var movieNameRe = revenueMovie[i][0]; // Access the cinema name
  dataChartThreeY.push(totalMoneyMovie);
  dataChartThreeX.push(movieNameRe);
}
optionThree = {
  xAxis: {
    type: "category",
    data: dataChartThreeX,
  },
  yAxis: {
    type: "value",
  },
  series: [
    {
      data: dataChartThreeY,
      type: "bar",
    },
  ],
};

myChartThree.setOption(optionThree);

/* -- Select the input when clicking inside the input container -- */
/* --------------------------------------------------------------- */
const dateContainers = document.querySelectorAll(".input-container");
dateContainers.forEach((dateContainer) => {
  const dateInput = dateContainer.querySelector(".date-field");
  if (dateInput) {
    dateContainer.addEventListener("click", function (event) {
      dateInput.select();
    });
  }
});

/* ----------------------------------------------------------------------------- */
/* -- Automatically set the date for check-in (today) and checkout (tomorrow) -- */
/* ----------------------------------------------------------------------------- */
document.addEventListener("DOMContentLoaded", function () {
  const dateCheckin = document.getElementById("date-checkin");
  const dateCheckout = document.getElementById("date-checkout");
  const today = new Date();
  const tomorrow = new Date(today);
  tomorrow.setDate(tomorrow.getDate() + 1);
  dateCheckin.valueAsDate = today;
  dateCheckout.valueAsDate = tomorrow;
  dateCheckin.addEventListener("input", function () {
    const checkinDate = dateCheckin.valueAsDate;
    const checkoutDate = dateCheckout.valueAsDate;
    if (checkinDate > checkoutDate) {
      const newCheckoutDate = new Date(checkinDate);
      newCheckoutDate.setDate(newCheckoutDate.getDate() + 1);
      dateCheckout.valueAsDate = newCheckoutDate;
    }
  });
  dateCheckout.addEventListener("input", function () {
    const checkinDate = dateCheckin.valueAsDate;
    const checkoutDate = dateCheckout.valueAsDate;
    if (checkoutDate < checkinDate) {
      const newCheckinDate = new Date(checkoutDate);
      newCheckinDate.setDate(newCheckinDate.getDate() - 1);
      dateCheckin.valueAsDate = newCheckinDate;
    }
  });
});

/* -- TESTKIT BUTTON -- */
// document.querySelector('.test').addEventListener('click', function () {
//     const reservationBox = document.querySelector('.reservation-box');
//     const button = document.querySelector('.test');
//     reservationBox.classList.toggle('small');
//     button.classList.toggle('small');
// });
