var clientIP = "localhost";
var nodes = null, links = [];
//    var pageReload = true;
//    getClientIP();
//    console.log("clientIP "+clientIP);

var socket = io('http://' + clientIP + ':1301');
socket.on('connect_error', function () {
    console.log('Socket connection failed');
    document.getElementById("alertBoxDiv").classList.add('alert-danger');
    document.getElementById("alertBoxDiv").classList.remove('alert-info');

    document.getElementById("alertMessage").innerHTML = "Error connecting to server.";
    document.getElementById("alertDiv").style.display = 'block';
});

socket.on('clusterStats', function (data) {
    if (data) {
        console.log(data);
        document.getElementById("alertDiv").style.display = 'none';
        var new_tbody = document.createElement('tbody');
        var old_tbody = document.getElementById("nodeTable").tBodies[0];
        nodes = [];
        links = [];

        for (var i in data.nodes) {
            // console.log("inside for");

            //Update graph
            d3.select("svg").remove();
            // console.log(data.nodes[i].state);
            if(data.nodes[i].state=="ACTIVE") {
                nodes.push({id: data.nodes[i].id, reflexive: false});
                if (i > 0)
                    links.push({source: nodes[0], target: nodes[nodes.length-1], left: false, right: true});
            }
            // console.log(nodes);
            // console.log(links);

            //Update table
            var row = new_tbody.insertRow(i);
            cell1 = row.insertCell(0);
            cell1.innerHTML = data.nodes[i].id;
            cell1 = row.insertCell(1);
            cell1.innerHTML = data.nodes[i].ip;
            cell1 = row.insertCell(2);
            cell1.innerHTML = data.nodes[i].isMaster;
            cell1 = row.insertCell(3);
            cell1.innerHTML = data.nodes[i].state;
        }
        drawGraph(nodes, links);
        old_tbody.parentNode.replaceChild(new_tbody, old_tbody);
    }
    else {
        document.getElementById("alertBoxDiv").classList.add('alert-info');
        document.getElementById("alertBoxDiv").classList.remove('alert-danger');
        document.getElementById("alertMessage").innerHTML = "No cluster has been formed, yet!";
        document.getElementById("alertDiv").style.display = 'block';
        d3.select("svg").remove();
        var new_tbody = document.createElement('tbody');
        var old_tbody = document.getElementById("nodeTable").tBodies[0];
        old_tbody.parentNode.replaceChild(new_tbody, old_tbody);

    }
});

//        socket.emit('my other event', { my: 'data' });

// Set interval implementation
//    setInterval(function () {
//        console.log("polling NodeJS");
//        $.ajax({
//            type: "GET",
//            url: pageReload==false ? "http://"+clientIP+":1300/getNodeDetails":"http://"+clientIP+":1300/getNodeDetails?pageReload=true",
//            useDefaultXhrHeader: false, //important, otherwise its not working
//            success: function (result) {
//                console.log("call successful");
//                console.log(result);
//                pageReload=false;
//                if (result.nodes) {
//                    var new_tbody = document.createElement('tbody');
//                    var old_tbody = document.getElementById("nodeTable").tBodies[0];
//                    nodes=[];
//                    links=[];
//
//                    for (var i in result.nodes) {
//                        console.log("inside for");
//
//                        //Update graph
//                        d3.select("svg").remove();
//                        nodes.push({id: result.nodes[i].id, reflexive: false});
//                        if (i > 0)
//                            links.push({source: nodes[0], target: nodes[i], left: false, right: true});
//
//                        console.log(nodes);
//                        console.log(links);
//
//                        //Update table
//                        var row = new_tbody.insertRow(i);
//                        cell1 = row.insertCell(0);
//                        cell1.innerHTML = result.nodes[i].id;
//                        cell1 = row.insertCell(1);
//                        cell1.innerHTML = result.nodes[i].ip;
//                        cell1 = row.insertCell(2);
//                        cell1.innerHTML = result.nodes[i].isMaster;
//                    }
//                    drawGraph(nodes, links);
//                    old_tbody.parentNode.replaceChild(new_tbody, old_tbody);
//                }
//                else
//                    console.log("no changes");
//            }
//        });
//    }, 2000);

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
    })(); else {
//        clientIP="<code>ifconfig | grep inet | grep -v inet6 | cut -d\" \" -f2 | tail -n1</code>";

//        document.getElementById('list').nextSibling.textContent = "In Chrome and Firefox your IP should display automatically, by the power of WebRTCskull.";
    }

}