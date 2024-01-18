var dateList = [];
// Lấy ngày hiện tại

var currentDate = new Date();
// Lặp qua 7 ngày gần nhất và thêm chúng vào mảng

for (var i = 0; i < 7; i++) {
  var tempDate = new Date();
  tempDate.setDate(currentDate.getDate() - i);
  dateList.push(tempDate);
}
// Đổi định dạng ngày thành chuỗi và đưa vào mảng kết quả
console.log(myDinh);
console.log(mipec);
console.log(thanhXuan);

var resultList = dateList.map(function (date) {
  return date.toISOString().slice(0, 10);
});
var myChart = echarts.init(document.getElementById("myChart"));
var option = {
  title: {
    text: "Doanh thu của cả 3 rạp trong 7 ngày gần nhất",
  },
  tooltip: {
    trigger: "axis",
  },
  legend: {
    data: ["Rainfall", "Evaporation"],
  },
  toolbox: {
    show: true,
    feature: {
      dataView: { show: true, readOnly: false },
      magicType: { show: true, type: ["line", "bar"] },
      restore: { show: true },
      saveAsImage: { show: true },
    },
  },
  calculable: true,
  xAxis: [
    {
      type: "category",
      // prettier-ignore
      data: resultList,
    },
  ],
  yAxis: [
    {
      type: "value",
    },
  ],
  series: [
    {
      name: "Thanh Xuân",
      type: "bar",
      data: thanhXuan,
      markPoint: {
        data: [
          { type: "max", name: "Max" },
          { type: "min", name: "Min" },
        ],
      },
      markLine: {
        data: [{ type: "average", name: "Avg" }],
      },
    },
    {
      name: "Mỹ Đình",
      type: "bar",
      data: myDinh,
      markPoint: {
        data: [
          { name: "Max", value: 182.2, xAxis: 7, yAxis: 183 },
          { name: "Min", value: 2.3, xAxis: 11, yAxis: 3 },
        ],
      },
      markLine: {
        data: [{ type: "average", name: "Avg" }],
      },
    },
    {
      name: "Mipec Tower",
      type: "bar",
      data: mipec,
      markPoint: {
        data: [
          { name: "Max", value: 182.2, xAxis: 7, yAxis: 183 },
          { name: "Min", value: 2.3, xAxis: 11, yAxis: 3 },
        ],
      },
      markLine: {
        data: [{ type: "average", name: "Avg" }],
      },
    },
  ],
};
myChart.setOption(option);

// for (var i = 0; i < revenueTicket.length; i++) {
//     var cinemaName= i.cinemaName;
//     var totalMoney= i.totalMoney;
// }
var dataChartOne = [];
for (var i = 0; i < revenueTicket.length; i++) {
  var totalMoney = revenueTicket[i][0]; // Access the total money
  var cinemaName = revenueTicket[i][1]; // Access the cinema name
  dataChartOne.push({ value: totalMoney, name: cinemaName });
  console.log(dataChartOne);
  // Process or use the data as needed
}
var myChartOne = echarts.init(document.getElementById("myChartOne"));
var optionOne = {
  title: {
    text: "Doanh thu từ việc bán vé của 3 rạp",
  },
  tooltip: {
    trigger: "item",
  },
  legend: {
    top: "5%",
    left: "center",
  },
  series: [
    {
      name: "Rạp",
      type: "pie",
      radius: ["40%", "70%"],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: "#fff",
        borderWidth: 2,
      },
      label: {
        show: false,
        position: "center",
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 40,
          fontWeight: "bold",
        },
      },
      labelLine: {
        show: false,
      },
      data: dataChartOne,
    },
  ],
};
myChartOne.setOption(optionOne);
////////////////////////////////////////////////////////////////////////

var dataChartTwo = [];
var dataChartTwo2 = [];
for (var i = 0; i < revenueService.length; i++) {
  var totalMoney2 = revenueService[i][1]; // Access the total money
  var ServiceName = revenueService[i][0]; // Access the cinema name
  // dataChartTwo.push(totalMoney2);
  // dataChartTwo2.push(ServiceName);
  dataChartTwo.push({ value: totalMoney2, name: ServiceName });
}
var myChartTwo = echarts.init(document.getElementById("myChartTwo"));
var optionTwo = {
  title: {
    text: "Top 7 đồ ăn cho doanh thu cao nhất",
  },
  tooltip: {
    trigger: "item",
  },
  legend: {
    top: "5%",
    left: "center",
  },
  series: [
    {
      name: "Đồ ăn",
      type: "pie",
      radius: ["40%", "70%"],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: "#fff",
        borderWidth: 2,
      },
      label: {
        show: false,
        position: "center",
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 40,
          fontWeight: "bold",
        },
      },
      labelLine: {
        show: false,
      },
      data: dataChartTwo,
    },
  ],
};
myChartTwo.setOption(optionTwo);
