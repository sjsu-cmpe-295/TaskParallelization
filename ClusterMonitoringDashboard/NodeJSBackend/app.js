var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var querystring = require('querystring');
var request = require('request');
var mysql=require('./routes/mysql');
const http = require('http');

var index = require('./routes/index');
var users = require('./routes/users');

var app = express();
var avg,min,max,count;
var avgT,minT,maxT,countT;
var humidityMinDate;
var tempMinDate;
var TemperatureValues;
var humidityValues;
var metricsHashMap = {};
var isStartingUp=true;
var nodeUpdated=true;
var HumidityData;


//Node details
var nodes;
var taskId = 0;
var masterIp;
var sockets;
var taskIdtoSocketIdMap=[];
//var app = express();
var server = require('http').Server(app);
var io = require('socket.io')(server);
server.listen(1301);

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));


app.use(bodyParser.json({limit: '50mb'}));
app.use(bodyParser.urlencoded({limit: '50mb'}));

app.use(bodyParser.urlencoded({extended: true}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));


app.use('/', index);
app.use('/users', users);

//creates a socket with the client and updates UI
io.on('connection', function (socket) {
    console.log("socket initiated sid: " + socket.id);
    sockets=io.sockets;
    // console.log(socket);
    // if (nodes)
    socket.emit('clusterStats', nodes);

    socket.emit('temperatureStats',TemperatureValues,tempMinDate);
    socket.emit('humidityStats',humidityValues,humidityMinDate);
    socket.emit('humidityMetrics', avg,min,max,count);
    socket.emit('tempMetrics', avgT,minT,maxT,countT);
    socket.emit('clusterMetrics', metricsHashMap);

    socket.on('submitTask', function (data) {
        // console.log("submitted task " + JSON.stringify(data));
        console.log("masterip " + masterIp);
        taskId++;
        taskIdtoSocketIdMap[taskId]=socket.id;//temporary
        console.log("socket id "+socket.id+" taskid "+taskId);//temporary
        data["id"]=taskId;
        // console.log("data is "+JSON.stringify(data));

        var options = {
            method: 'post',
            body: data, // Javascript object
            json: true, // Use,If you are sending JSON data
            url: 'http://'+masterIp+':8080/submitTask',
            headers: {
                // Specify headers, If any
            }
        }

        request(options, function (err, res, body) {
            if (err) {
                console.log('Error :', err)
                return
            }
            console.log(' Body :', body)

        });


    });


});

//Receives an update from the master node and updates all UI clients(sockets)
app.post('/updateCluster', function (req, res) {
    console.log("updateCluster accessed");
    // console.log("request is " + JSON.stringify(req.body));

    nodes = req.body;
    masterIp = nodes.nodes[0].ip;
    console.log("master node set as "+masterIp);
    io.sockets.emit('clusterStats', nodes);

    res.sendStatus(200);
});

app.post('/updateMetrics',function (req,res){
    console.log("updateMetrics accessed");
    console.log("request is "+JSON.stringify(req.body));
    metrics = new Array();
    var json_obj = req.body;
    console.log("json_obj "+json_obj);
    metrics.push(json_obj.cpu);
    metrics.push(json_obj.memoryUsage);
    metrics.push(json_obj.netWorkIn);
    metrics.push(json_obj.netWorkOut);
    metricsHashMap[json_obj.ip] = metrics;
    console.log("metricsHashMap "+JSON.stringify(metricsHashMap));
    io.sockets.emit('clusterMetrics', metricsHashMap);

   res.sendStatus(200);

});

app.post('/updateGraph',function (req,res){
    var humidityDates = [];
    var tempDates = [];
    var humidityValues = [];
    var TemperatureValues = [];
    var humidityMetrics;
    var tempMetrics;
   
    // console.log("updateTemperatureGraph accessed");
    // console.log("request is " + JSON.stringify(req.body));
    var json_obj = req.body;
    var output = json_obj.output;
    // console.log(output);
    if(output['humidityDataPoints'].length!=0){
        HumidityData = output.humidityDataPoints;
        for(var key in HumidityData){
            humidityDates.push(new Date(key.split(' ')[0]));
            humidityValues.push(HumidityData[key]);
            humidityMinDate=new Date(Math.min.apply(null,humidityDates));

        }
        console.log(humidityMinDate);
        console.log(humidityValues);
        io.sockets.emit('humidityStats', humidityValues,humidityMinDate);
    }
    if(output['humidityMetrics']!=null){
        humidityMetrics = output.humidityMetrics;
        avg = humidityMetrics['average'];
        min = humidityMetrics['minimum'];
        max = humidityMetrics['maximum'];
        count = humidityMetrics['count'];
        console.log(avg,min,max,count);
        io.sockets.emit('humidityMetrics', avg,min,max,count);
    }
    if(output['temperatureDataPoints'].length!=0){
        TempData = output.temperatureDataPoints;
        for(var key in TempData){
            tempDates.push(new Date(key));
            TemperatureValues.push(TempData[key]);
            tempMinDate=new Date(Math.min.apply(null,tempDates));
        }
        console.log(TemperatureValues);
        console.log(tempMinDate);
        io.sockets.emit('temperatureStats', TemperatureValues,tempMinDate);
    }
    if(output['temperatureMetrics']!=null){
        tempMetrics = output.temperatureMetrics;
        avgT = tempMetrics['average'];
        minT = tempMetrics['minimum'];
        maxT = tempMetrics['maximum'];
        countT = tempMetrics['count'];
        console.log(avgT,minT,maxT,countT);
        io.sockets.emit('tempMetrics', avgT,minT,maxT,countT);
    }
    
    

    res.sendStatus(200);

});

//Gets the output from the master node and updates the client that requested it
app.post('/getOutput', function (req, res) {
    console.log("getOutput accessed");
    console.log("output is " + JSON.stringify(req.body));

    //Emit to all sockets
    // io.sockets.emit('getOutput', req.body);

    if(req.body["Error"]!=null)
        io.sockets.emit('getOutput', req.body);
    else {
        console.log("task id is " + req.body.id);
        console.log("from map socket id is " + taskIdtoSocketIdMap[req.body.id]);

        io.to(taskIdtoSocketIdMap[req.body.id]).emit('getOutput', req.body);

        var data = {};
        data['id'] = req.body.id;
        data['output'] = JSON.stringify(req.body.output);
        mysql.query('INSERT INTO tasks SET ?', data, function (err, res) {
            if (err) {
                // throw err;
                console.log("error " + err);
            }
            console.log(res);

        });
    }
    res.sendStatus(200);
});

/*
 * Get Tasks
 */
app.get('/getTasks',function (req,res) {
    console.log("reached getTasks");
    // Website you wish to allow to connect
    res.setHeader('Access-Control-Allow-Origin', "*");

    // Request methods you wish to allow
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');

    // Request headers you wish to allow
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type');
    //
    // // Set to true if you need the website to include cookies in the requests sent
    // // to the API (e.g. in case you use sessions)
    res.setHeader('Access-Control-Allow-Credentials', true);
    var query = "select * from tasks";
    console.log(query);
    mysql.query(query, function (err, result) {
        if (err) {

            console.log("error");
            throw err;
        }
        else
        {
            console.log(result);
            res.send(result);
        }


    })
});


//Test Node-Spring rest call
// app.get('/getNodeDetails', function (req, response) {
//     console.log("getNodeDetails accessed");
//
//     var options = {
//         host: '10.0.0.3',
//         port: 8080,
//         path: '/getNodeDetails',
//         method: 'GET'
//     };
//     // var response;
//     http.request(options, function (res) {
//         console.log('STATUS: ' + res.statusCode);
//         console.log('HEADERS: ' + JSON.stringify(res.headers));
//         res.setEncoding('utf8');
//         res.on('data', function (chunk) {
//             console.log('BODY: ' + chunk);
//             response.send(chunk);
//
//         });
//     }).end();
//
// });


// Long polling(Set interval) usage for getNodeDetails
// app.get('/getNodeDetails/:pageReload?', function (req, res) {
//     console.log("getNodeDetails accessed");
//     // console.log(req.query.pageReload);
//     // Website you wish to allow to connect
//     res.setHeader('Access-Control-Allow-Origin', "*");
//
//     // Request methods you wish to allow
//     res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');
//
//     // Request headers you wish to allow
//     res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type');
//     //
//     // // Set to true if you need the website to include cookies in the requests sent
//     // // to the API (e.g. in case you use sessions)
//     res.setHeader('Access-Control-Allow-Credentials', true);
//
//     if( nodeUpdated || req.query.pageReload)
//     {
//         console.log("response is "+JSON.stringify(nodes));
//         res.send(nodes);
//         // isStartingUp=false;
//         nodeUpdated=false;
//     }
//     else
//         res.sendStatus(200);
//
// });


// catch 404 and forward to error handler
app.use(function (req, res, next) {
    // if(!err) return next();
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});


// error handler
app.use(function (err, req, res, next) {
    // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // render the error page
    res.status(err.status || 500);
    res.render('error');
});

app.post('/trypost', function (request, res) {
    console.log("reached dashboardCost");
    res.send(200);
});


module.exports = app;
