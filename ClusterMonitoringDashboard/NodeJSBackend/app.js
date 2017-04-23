var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
const http = require('http');


var index = require('./routes/index');
var users = require('./routes/users');

//Node details
var nodes;

var metricsHashMap = {};
var isStartingUp=true;
var nodeUpdated=true;

var app = express();
var server = require('http').Server(app);
var io = require('socket.io')(server);
server.listen(1301);

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));


app.use('/', index);
app.use('/users', users);

//creates a socket with the client and updates UI
io.on('connection', function (socket) {
    console.log("socket initiated sid: " + socket.id);
    // console.log(socket);
    // if (nodes)
    socket.emit('clusterStats', nodes);
    // socket.on('my other event', function (data) {
    //     console.log(data);
    // });


});

//Receives an update from the master node and updates all UI clients(sockets)
app.post('/updateCluster', function (req, res) {
    console.log("updateCluster accessed");
    console.log("request is " + JSON.stringify(req.body));

    nodes = req.body;
    io.sockets.emit('clusterStats', nodes);

    res.sendStatus(200);
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

<<<<<<< HEAD
=======
///////////////
//[{"ip":"127.6.5.4","cpu":"0.9","memoryUsage":"0.9","netWorkIn":"0.9","netWorkOut":"0.9"}]
>>>>>>> 6a64db5... Added API changes for cluster metrics


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
    res.sendStatus(200);

});

app.get('/getMetrics',function (req,res){
    console.log("getMetrics accessed");

});




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
