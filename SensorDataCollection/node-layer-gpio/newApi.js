var express = require('express');
var bodyParser = require('body-parser');
var cookieParser = require('cookie-parser');

var app = express();
app.use('/nifty-v2.2', express.static('nifty-v2.2/template'));

app.set('view engine', 'ejs');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(cookieParser());

app.get("/raspberryApi",  function (request, response) {
    console.log(request.query);
});


app.listen(3000, function () {
    console.log('Sensor Data collector listening at 3000');
});

