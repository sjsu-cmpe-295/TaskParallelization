var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
const http = require('http');

var index = require('./routes/index');
var users = require('./routes/users');
// var trying = require('./views/try.html');

//Node details
var nodes;
var isStartingUp=true;
var nodeUpdated=true;
var app = express();

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

app.get('/getMasterSlaveStatus', function (req, response) {
    console.log("getMasterSlaveStatus accessed");

    var options = {
        host: '127.0.0.1',
        port: 8080,
        path: '/getMasterSlaveStatus',
        method: 'GET'
    };
    // var response;
    http.request(options, function (res) {
        console.log('STATUS: ' + res.statusCode);
        console.log('HEADERS: ' + JSON.stringify(res.headers));
        res.setEncoding('utf8');
        res.on('data', function (chunk) {
            console.log('BODY: ' + chunk);
            response.send(chunk);

        });
    }).end();

});


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

///////////////


app.post('/updateCluster', function (req, res) {
    console.log("updateCluster accessed");
    console.log("request is "+JSON.stringify(req.body));

    nodeUpdated=true;
    nodes=req.body;
    res.sendStatus(200);
});

app.get('/getNodeDetails/:pageReload?', function (req, res) {
    console.log("getNodeDetails accessed");
    // console.log(req.query.pageReload);
    // Website you wish to allow to connect
    res.setHeader('Access-Control-Allow-Origin', null);

    // Request methods you wish to allow
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');

    // Request headers you wish to allow
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type');
    //
    // // Set to true if you need the website to include cookies in the requests sent
    // // to the API (e.g. in case you use sessions)
    res.setHeader('Access-Control-Allow-Credentials', true);

    if( nodeUpdated || req.query.pageReload)
    {
        console.log("response is "+JSON.stringify(nodes));
        res.send(nodes);
        // isStartingUp=false;
        nodeUpdated=false;
    }
    else
        res.sendStatus(200);

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
