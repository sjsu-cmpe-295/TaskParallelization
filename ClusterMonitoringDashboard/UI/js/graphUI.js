var clientIP = "localhost";
var nodes = null, links = [];
//    var pageReload = true;
//    getClientIP();
//    console.log("clientIP "+clientIP);

var socket = io('http://' + clientIP + ':1301');
socket.on('connect_error', function () {
    console.log('Connection Failed');
    document.getElementById("alertBoxDiv").classList.add('alert-danger');
    document.getElementById("alertBoxDiv").classList.remove('alert-info');

    document.getElementById("alertMessage").innerHTML = "Error connecting to server.";
    document.getElementById("alertDiv").style.display = 'block';
});

socket.on('humidityMetrics',function(avg,min,max,count){
    //graph(data,'temperature',minDate);
    console.log(avg,min,max,count);
    if(avg){
        var avgdiv = document.getElementById('Havg');
        avgdiv.innerHTML = "AVERAGE HUMIDITY: "+avg;
    }else{
        var avgdiv = document.getElementById('Havg');
        avgdiv.innerHTML = "Not calculated yet";
    }
    if(min){
        var mindiv = document.getElementById('Hmin');
        mindiv.innerHTML = "MINIMUM HUMIDITY: " +min;
    }else{
        var mindiv = document.getElementById('Hmin');
        mindiv.innerHTML = "Not calculated yet";
    }
    if(max){
        var maxdiv = document.getElementById('Hmax');
        maxdiv.innerHTML = "MAXIMUM HUMIDITY: "+max;
    }else{
        var maxdiv = document.getElementById('Hmax');
        maxdiv.innerHTML = "Not calculated yet";
    }
    if(count){
        var countdiv = document.getElementById('Hcount');
        countdiv.innerHTML = "TOTAL DATAPOINTS COUNT: "+count;
    }else{
        var countdiv = document.getElementById('Hcount');
        countdiv.innerHTML = "Not calculated yet";
    }
    
});


socket.on('tempMetrics',function(avg,min,max,count){
    //graph(data,'temperature',minDate);
    console.log("Temp: "+avg,min,max,count);
    if(avg){
        var avgdiv = document.getElementById('avg');
        avgdiv.innerHTML = "AVERAGE TEMPERATURE: "+avg;
    }else{
        alert('in else')
        document.getElementById('avg').style.display = 'none';
        
    }
    if(min){
        var mindiv = document.getElementById('min');
        mindiv.innerHTML = "MINIMUM TEMPERATURE: " +min;
    }else{
       document.getElementById('min').style.display = 'none';
    }
    if(max){
        var maxdiv = document.getElementById('max');
        maxdiv.innerHTML = "MAXIMUM TEMPERATURE: "+max;
    }else{
        document.getElementById('max').style.display = 'none';
        
    }
    if(count){
        var countdiv = document.getElementById('count');
        countdiv.innerHTML = "TOTAL DATAPOINTS COUNT: "+count;
    }else{
        document.getElementById('count').style.display = 'none';

    }
    
});

socket.on('temperatureStats',function(data,minDate){
    console.log(data);
    console.log(minDate);
    //graph(data,'temperature',minDate);
    if(data){
        graph(data,'temperature',minDate);
    }
    else{
        document.getElementById("alertBoxDiv").classList.add('alert-info');
        document.getElementById("alertBoxDiv").classList.remove('alert-danger');
        document.getElementById("alertMessage").innerHTML = "No Data Received Yet!";
        document.getElementById("alertDiv").style.display = 'block';
        d3.select("svg").remove();

    }
});

socket.on('humidityStats',function(data,minDate){
    console.log(data);
    console.log(minDate);
   // graph(data,'humidity',minDate);
    if(data){
        graph(data,'humidity',minDate);
    }
    else{
        document.getElementById("alertBoxDiv").classList.add('alert-info');
        document.getElementById("alertBoxDiv").classList.remove('alert-danger');
        document.getElementById("alertMessage").innerHTML = "No Data Received Yet!";
        document.getElementById("alertDiv").style.display = 'block';
        d3.select("svg").remove();

    }
});

function getClientIP() {
    //    $.get("http://jsonip.com/", function(response) {
//        clientIP=response.ip;
//        console.log(response.ip);
//    }, "jsonp");

    var RTCPeerConnection = /*window.RTCPeerConnection ||*/ window.webkitRTCPeerConnection || window.mozRTCPeerConnection;

    if (RTCPeerConnection) (function () {
        var rtc = new RTCPeerConnection({iceServers: []});
        if (1 || window.mozRTCPeerConnection) {      // FF [and now Chrome!] needs a channel/stream to proceed
            rtc.createDataChannel('', {reliable: false});
        }
        ;

        rtc.onicecandidate = function (evt) {
            // convert the candidate to SDP so we can run it through our general parser
            // see https://twitter.com/lancestout/status/525796175425720320 for details
            if (evt.candidate) grepSDP("a=" + evt.candidate.candidate);
        };
        rtc.createOffer(function (offerDesc) {
            grepSDP(offerDesc.sdp);
            rtc.setLocalDescription(offerDesc);
        }, function (e) {
            console.warn("offer failed", e);
        });


        var addrs = Object.create(null);
        addrs["0.0.0.0"] = false;
        function updateDisplay(newAddr) {
            if (newAddr in addrs) return;
            else addrs[newAddr] = true;
            var displayAddrs = Object.keys(addrs).filter(function (k) {
                return addrs[k];
            });
            console.log("displayAddrs " + displayAddrs);
            clientIP = displayAddrs[0];
        }

        function grepSDP(sdp) {
            var hosts = [];
            sdp.split('\r\n').forEach(function (line) { // c.f. http://tools.ietf.org/html/rfc4566#page-39
                if (~line.indexOf("a=candidate")) {     // http://tools.ietf.org/html/rfc4566#section-5.13
                    var parts = line.split(' '),        // http://tools.ietf.org/html/rfc5245#section-15.1
                        addr = parts[4],
                        type = parts[7];
                    if (type === 'host') updateDisplay(addr);

                } else if (~line.indexOf("c=")) {       // http://tools.ietf.org/html/rfc4566#section-5.7
                    var parts = line.split(' '),
                        addr = parts[2];
                    updateDisplay(addr);
                }
            });
        }
    })(); 
}