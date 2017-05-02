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
            console.log("inside " + childDiv.getElementsByTagName("button")[0].innerText.toLowerCase());
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
        console.log("Output received " + JSON.stringify(data));
        document.getElementById("response").innerHTML = document.getElementById("response").textContent + JSON.stringify(data);
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