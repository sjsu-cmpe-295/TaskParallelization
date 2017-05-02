function graph(taskData, value,minDate) {
	console.log(value + taskData);
	console.log(value);
	console.log(minDate);
	var date = minDate.split('-');
	var year = date[0];
	var month = date[1];
	var day = date[2].split('T')[0];
	var time = date[2].split('T')[1].split(':');
	var hr = time[0];
	var min = time[1];
	var sec = time[2].split('.')[0];
	console.log(year,month,day);
	// var month = minDate.getUTCMonth() + 1; 
	// var day = minDate.getUTCDate();
	// var year = minDate.getUTCFullYear();
	Highcharts.chart(value, {
	    credits: {
	      enabled: false
		},
	    title: {
	        text: value
	    },
	    xAxis: {
	        type: 'datetime',
	        dateTimeLabelFormats: {
	            day: '%d %b %H:%M:%S'
	        }
	    },
	    yAxis: {
	        title: {
	            text: value
	        }
	    },
	    legend: {
	        layout: 'vertical',
	        align: 'right',
	        verticalAlign: 'middle'
	    },
	    series: [{
	        name: value,
	        data: taskData,
	        pointStart: Date.UTC(parseInt(year),parseInt(month)-1,parseInt(day),parseInt(hr),parseInt(min),parseInt(sec)),
	        pointInterval: 60000 
	    }]
	 // plotOptions: {
    //     series: {
    //         pointStart: 2010
    //     }
    // },
	});

}


// }

// function generateRandom() {
//           var dataArray = [];
//           for(i=0; i<48; i++) {
//             var randomValue = Math.floor(Math.random() * (40 - 20)) + 20;
//             dataArray.push(randomValue);
//           }
//           return dataArray;
//         }
