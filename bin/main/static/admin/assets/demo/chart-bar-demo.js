// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';

// Bar Chart Example
var ctx = document.getElementById("myBarChart");
var myBarChart;

function initChart(labels, data, maxValue) {
  console.log("차트 초기화:", {
    labels: labels,
    data: data,
    maxValue: maxValue
  });
  
  // 이전 차트가 있다면 파괴
  if (myBarChart) {
    myBarChart.destroy();
  }
  
  // 데이터가 없으면 기본값 설정
  if (!data || data.length === 0) {
    data = [0];
    labels = ["데이터 없음"];
    maxValue = 1;
  }
  
  // 최대값을 계산하여 적절한 Y축 범위 설정
  var calculatedMax = Math.max.apply(null, data);
  // 최대값에 여유를 주기 위해 20% 증가시킴
  maxValue = Math.ceil(calculatedMax * 1.2);
  // 최소값은 5 이상으로 설정
  maxValue = Math.max(maxValue, 5);
  
  myBarChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: labels,
      datasets: [{
        label: "가입자 수",
        backgroundColor: "rgba(40, 121, 255, 0.8)",
        borderColor: "rgba(40, 121, 255, 1)",
        borderWidth: 1,
        data: data,
      }],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      layout: {
        padding: {
          left: 10,
          right: 10,
          top: 20,
          bottom: 10
        }
      },
      scales: {
        xAxes: [{
          time: {
            unit: 'day'
          },
          gridLines: {
            display: false
          },
          ticks: {
            maxTicksLimit: 10,
            padding: 5,
            callback: function(value) {
              // 날짜 형식을 간략하게 표시 (MM-DD 형식)
              if (value && value.length >= 10) {
                return value.substring(5); // "2025-04-18" -> "04-18"
              }
              return value;
            }
          }
        }],
        yAxes: [{
          ticks: {
            min: 0,
            max: maxValue,
            maxTicksLimit: 6,
            padding: 5,
            beginAtZero: true,
            stepSize: 1, // 정수 단위로 눈금 표시
            callback: function(value) {
              if (Math.floor(value) === value) {
                return value + '명';
              }
            }
          },
          gridLines: {
            color: "rgba(0, 0, 0, 0.1)",
            zeroLineColor: "rgba(0, 0, 0, 0.25)",
            drawBorder: false
          },
          scaleLabel: {
            display: true,
            labelString: '가입자 수'
          }
        }],
      },
      legend: {
        display: false
      },
      tooltips: {
        backgroundColor: "rgba(0, 0, 0, 0.7)",
        titleFontStyle: 'bold',
        titleMarginBottom: 10,
        xPadding: 15,
        yPadding: 15,
        intersect: false,
        mode: 'index',
        caretPadding: 10,
        callbacks: {
          title: function(tooltipItems, data) {
            return tooltipItems[0].xLabel; // 날짜를 타이틀로 표시
          },
          label: function(tooltipItem, data) {
            return '가입자: ' + tooltipItem.yLabel + '명';
          }
        }
      },
      animation: {
        duration: 1000,
        easing: 'easeOutQuart'
      }
    }
  });
}
