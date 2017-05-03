var clientIP = "localhost";
var task = {tasks: []};
var sensorSelected = false;
// ,{sensor:"humidity", time:"10"}];
// var task={sensor:"humidity", time:"5"};
// var task={sensor:"both", time:"5"};

var socket = io('http://' + clientIP + ':1301');
socket.on('connect_error', function () {
    console.log('Connection Failed');
});
$("#btnSubmit").button().click(function () {
    task = {tasks: []};

    //add isTP flag
    if ($("input[name='tpRadio']:checked").val() == "Without Task Parallelism")
        task["isTP"] = "false";
    else
        task["isTP"] = "true";

    //Populate tasks
    var childDivs = document.getElementById('task-wrapper').getElementsByClassName('form-group');
    for (i = 0; i < childDivs.length; i++) {
        var childDiv = childDivs[i];
        if (childDiv.getElementsByTagName("button")[0].innerText.toLowerCase() !== "choose a sensor ") {
            // console.log("inside " + childDiv.getElementsByTagName("button")[0].innerText.toLowerCase());
            task["tasks"].push({
                "sensor": childDiv.getElementsByTagName("button")[0].innerText.toLowerCase(),
                "startTime": childDiv.getElementsByTagName("input")[0].value.toLowerCase(),
                "endTime": childDiv.getElementsByTagName("input")[1].value.toLowerCase(),
                "type": "dataPoints"
            });
            task["tasks"].push({
                "sensor": childDiv.getElementsByTagName("button")[0].innerText.toLowerCase(),
                "startTime": childDiv.getElementsByTagName("input")[0].value.toLowerCase(),
                "endTime": childDiv.getElementsByTagName("input")[1].value.toLowerCase(),
                "type": "metrics"
            });
        }
    }
    console.log("task is " + JSON.stringify(task));
    document.getElementById("humidityMetricsRow").style.display='none';
    document.getElementById("temperatureMetricsRow").style.display='none';
    document.getElementById("Temperature").style.display='none';
    document.getElementById("Humidity").style.display='none';

    document.getElementById("response").innerHTML = "";
    if (task["tasks"].length > 0) {
        socket.emit('submitTask', task);
        swal("Task submitted!");
    }
    else
        swal("No Tasks selected!");
});

function changeDDText(object) {
    // console.log("text is "+$(object).text());

    object.parentElement.parentElement.parentElement.firstChild.nextSibling.innerText = $(object).text();

}
function removeTask(object) {
    // console.log("text is "+$(object).text());
    object.parentElement.parentElement.remove();
}
function showTime(object) {
    // console.log("text is "+$(object).text());
    $("#" + object.id).datetimepicker();
}


socket.on('getOutput', function (data) {
    if (data) {
        console.log("Output received ");
        document.getElementById("response").innerHTML = document.getElementById("response").textContent + JSON.stringify(data);

        //Update graphs and metrics
        drawSensorGraphs(data);


    }
});
socket.on('submitSuccessful', function (data) {
    console.log("submitSuccessful " + socket.id);
    console.log(data);
    if (data == "true")
        alert("Task submitted");
    else
        alert("Task submission failed");

});

$(document).ready(function () {

    $("#addButton").click(function () {

        var id = ($('#task-wrapper .form-group').length + 1).toString();
        $('#task-wrapper').append('<div class="form-group">' +
            ' <label class="control-label col-xs-2" for="sensor">Sensor:</label> ' +
            '<div class="dropdown col-xs-1"> ' +
            '<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown"' +
            'id="sensor' + id + ' name="sensor" >Choose a sensor <span class="caret"></span> ' +
            '</button> ' +
            '<ul class="dropdown-menu" > ' +
            '<li><a href="#" onclick="changeDDText(this)">Temperature</a></li> ' +
            '<li><a href="#" onclick="changeDDText(this)">Humidity</a></li> ' +
//                '<li><a href="#" onclick="changeDDText(this)">Temperature & Humidity</a></li> ' +
            '</ul> ' +
            '</div> ' +
            '<label class="control-label col-xs-2" for="time">Start Time:</label> ' +
            '<div class="col-xs-2"> ' +
            '<input type="number" min="1" class="form-control" id="time' + id + ' onclick="showTime("this)" ' +
            'placeholder="Enter time"> ' +
            '</div>' +
            '<label class="control-label col-xs-2 " for="endTime">End Time:</label> ' +
            '<div class="col-xs-2"> ' +
            '<input type="text" class="form-control" id="endTime" ' +
            'placeholder="Enter time" > ' +
            '</div> ' +
            '<div class="col-xs-1 "> ' +
            '<input type="button" class="btn btn-danger" value="-" id="removeButton"' + id + ' onclick="removeTask(this)"/> ' +
            '</div>' +
            '</div>');

    });

});
$(function(){

    $("#sensor1DD li a").click(function(){
        // console.log("$(this).text() "+$(this).text());
        // console.log("$('#sensor').text() "+$('#sensor2').text());

        if($(this).text()==$('#sensor2').text()) {
            swal("Please select a different sensor!");
            // $('#sensor').text("Choose a sensor ");
            $('#sensor').html("Choose a sensor <span class='caret'></span>");
        }
    });
    $("#sensor2DD li a").click(function(){
        // console.log("$(this).text() "+$(this).text());
        // console.log("$('#sensor').text() "+$('#sensor').text());

        if($(this).text()==$('#sensor').text())
        {
            swal("Please select a different sensor!");
            $('#sensor2').html("Choose a sensor <span class='caret'></span>");
        }
    });
});

$('#startTime').datetimepicker({format: 'YYYY-MM-DD HH:mm:ss'});
$('#endTime').datetimepicker({format: 'YYYY-MM-DD HH:mm:ss'});
$('#startTime2').datetimepicker({format: 'YYYY-MM-DD HH:mm:ss'});
$('#endTime2').datetimepicker({format: 'YYYY-MM-DD HH:mm:ss'});

function drawSensorGraphs(data) {
    var humidityDates = [];
    var tempDates = [];
    var humidityValues = [];
    var TemperatureValues = [];
    var humidityMetrics;
    var tempMetrics;

    // console.log("updateTemperatureGraph accessed");
    // console.log("request is " + JSON.stringify(req.body));
    var json_obj = data;
    var output = json_obj.output;
    // console.log("humidityDataPoints count "+Object.keys(output.humidityDataPoints).length);
    if(output.humidityDataPoints && Object.keys(output.humidityDataPoints).length>0){
        // console.log('in humdity points');
        HumidityData = output.humidityDataPoints;
        for(var key in HumidityData){
            humidityDates.push(new Date(key.split(' ')[0]));
            humidityValues.push(HumidityData[key]);
            humidityMinDate=new Date(Math.min.apply(null,humidityDates));

        }
        // console.log(humidityMinDate);
        // console.log(humidityValues);
        showhumidityStats(humidityValues,humidityMinDate.toISOString());
        // io.sockets.emit('humidityStats', humidityValues,humidityMinDate);
    }
    // console.log("humidityMetrics count "+Object.keys(output.humidityMetrics).length);
    if(output.humidityMetrics!=null){
        // console.log('in humdity metrics');
        humidityMetrics = output.humidityMetrics;
        avg = humidityMetrics['average'];
        min = humidityMetrics['minimum'];
        max = humidityMetrics['maximum'];
        count = humidityMetrics['count'];

        // console.log(avg,min,max,count);
        showhumidityMetrics(avg,min,max,count);
        // io.sockets.emit('humidityMetrics', avg,min,max,count);
    }
    if(Object.keys(output.temperatureDataPoints).length>0){
        TempData = output.temperatureDataPoints;
        for(var key in TempData){
            tempDates.push(new Date(key));
            TemperatureValues.push(TempData[key]);
            tempMinDate=new Date(Math.min.apply(null,tempDates));
        }
        // console.log(TemperatureValues);
        // console.log(tempMinDate);
        showtemperatureStats(TemperatureValues,tempMinDate.toISOString());
        // io.sockets.emit('temperatureStats', TemperatureValues,tempMinDate);
    }
    if(output['temperatureMetrics']!=null){
        tempMetrics = output.temperatureMetrics;
        avgT = tempMetrics['average'];
        minT = tempMetrics['minimum'];
        maxT = tempMetrics['maximum'];
        countT = tempMetrics['count'];
        // console.log(avgT,minT,maxT,countT);
        showtempMetrics(avgT,minT,maxT,countT);
        // io.sockets.emit('tempMetrics', avgT,minT,maxT,countT);
    }

}

function showhumidityMetrics(avg,min,max,count)
{
    //graph(data,'temperature',minDate);
    console.log("inside showhumidityMetrics");
    // console.log("humidity metrics "+avg,min,max,count);
    if(avg){
        var avgdiv = document.getElementById('Havg');
        avgdiv.innerHTML = "Average humidity: "+avg;
    }
    else{
        var avgdiv = document.getElementById('Havg');
        avgdiv.innerHTML = "Not calculated yet";
    }
    if(min){
        var mindiv = document.getElementById('Hmin');
        mindiv.innerHTML = "Minimum Humidity: " +min;
    }else{
        var mindiv = document.getElementById('Hmin');
        mindiv.innerHTML = "Not calculated yet";
    }
    if(max){
        var maxdiv = document.getElementById('Hmax');
        maxdiv.innerHTML = "Maximum Humidity: "+max;
    }else{
        var maxdiv = document.getElementById('Hmax');
        maxdiv.innerHTML = "Not calculated yet";
    }
    if(count){
        var countdiv = document.getElementById('Hcount');
        countdiv.innerHTML = "Total datapoints: "+count;
    }else{
        var countdiv = document.getElementById('Hcount');
        countdiv.innerHTML = "Not calculated yet";
    }
    document.getElementById("humidityMetricsRow").style.display='block';
}


function showtempMetrics(avg,min,max,count){
    //graph(data,'temperature',minDate);
    console.log("inside tempMetrics  ");
    // console.log("temperature metrics: "+avg,min,max,count);
    if(avg){
        var avgdiv = document.getElementById('avg');
        avgdiv.innerHTML = "Average Temperature: "+avg;
    }else{
        // alert('in else')
        document.getElementById('avg').style.display = 'none';

    }
    if(min){
        var mindiv = document.getElementById('min');
        mindiv.innerHTML = "Minimum Temperature: " +min;
    }else{
        document.getElementById('min').style.display = 'none';
    }
    if(max){
        var maxdiv = document.getElementById('max');
        maxdiv.innerHTML = "Maximum Temperature: "+max;
    }else{
        document.getElementById('max').style.display = 'none';

    }
    if(count){
        var countdiv = document.getElementById('count');
        countdiv.innerHTML = "Total datapoints: "+count;
    }else{
        document.getElementById('count').style.display = 'none';

    }
    document.getElementById("temperatureMetricsRow").style.display='block';
    // document.getElementById("humidityMetricsRow").style.display='block';

}

function showtemperatureStats(data,minDate){

    console.log("inside showtemperatureStats");
    // console.log(data);
    // console.log(minDate);
    //graph(data,'temperature',minDate);
    if(data){
        graph(data,'Temperature',minDate);
        document.getElementById("Temperature").style.display='block';

    }
}

function showhumidityStats(data,minDate){
    console.log("inside showhumidityStats");
    // console.log(data);
    // console.log(minDate);
    // graph(data,'humidity',minDate);
    if(data){
        graph(data,'Humidity',minDate);
        document.getElementById("Humidity").style.display='block';
    }
}