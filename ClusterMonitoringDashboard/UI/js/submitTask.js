var clientIP = "localhost";
var task={tasks:[{sensor:"temperature", time:"5"},{sensor:"humidity", time:"7"}]};
// ,{sensor:"humidity", time:"10"}];
// var task={sensor:"humidity", time:"5"};
// var task={sensor:"both", time:"5"};

var socket = io('http://' + clientIP + ':1301');
socket.on('connect_error', function () {
    console.log('Connection Failed');
});
$("#btnSubmit").button().click(function(){

    // var data = $('#taskDetails').serializeArray();
    // console.log(JSON.stringify(data));
    document.getElementById("response").innerHTML="";
    socket.emit('submitTask', task);
    swal("Task submitted!");
});
$(function(){

    $(".dropdown-menu li a").click(function(){

        $("#sensor:first-child").text($(this).text());
        $("#sensor:first-child").val($(this).text());

    });

});


socket.on('getOutput', function (data) {
    if (data) {
        console.log("Output received "+JSON.stringify(data));
        document.getElementById("response").innerHTML=document.getElementById("response").textContent+JSON.stringify(data);
    }
});
socket.on('submitSuccessful', function (data) {
    console.log("submitSuccessful "+socket.id);
    console.log(data);
    if(data=="true")
    alert("Task submitted");
    else
    alert("Task submission failed");

});