const express = require('express')
var mongoose = require('mongoose'); 
var bodyParser = require('body-parser')
var cors = require('cors');
const authController = require('./controller/auth');
const publicController = require('./controller/public');
const blogController = require('./controller/blog');
const profileController = require('./controller/profile');
const videoController = require('./controller/video');
const dashboardController = require('./controller/dashboard');
const relationController = require('./controller/relation');
const likesController = require('./controller/likes');
const commentsController = require('./controller/comments');
const productController = require('./controller/product');
const stickersController = require('./controller/stickers');
const authMiddleware = require('./middleware/auth.middleware');
var http = require('http');
var path = require('path');
var cors = require('cors');
var debug = require('debug')('learn:server');
var AES_KEY = '6fnhkgo71s0caeqma6ojjftu4n1m1d85';
var crypto = require("crypto-js");
var session = require('express-session')({
	secret: "@#$%^&*(LKJLKSALYUQWEMJQWN<MNQDKLJHSALKJDHAUISDIUDYSASHDAM<SD",
	saveUninitialized: false,
	resave: false
});

// var mongoclient = 'mongodb://15.206.172.214:27017/supergreat_app_db'
var mongoclient = 'mongodb://localhost:27017/supergreat_app_db'
// mongoclient = "mongodb+srv://parth:IrnKlHN1eztSqYjb@supergreat.gsxkv.mongodb.net/supergreat?retryWrites=true&w=majority";

mongoose.connect(mongoclient, { useNewUrlParser: true });
const port = 3333;
const app = express()
app.use(session);
app.use(cors());
app.use( express.json( {
    extended: true
} ) );

app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');
app.use(express.static('uploads'));
app.use('/auth', authController);
app.use('/public', publicController);
app.use('/stickers', stickersController);
app.use('/blog', blogController);
app.use('/*',authMiddleware.fnAuthMiddleware);
app.use('/profile', profileController);
app.use('/product', productController);
app.use('/video', videoController);
app.use('/dashboard', dashboardController);
app.use('/relation', relationController);
app.use('/like', likesController);
app.use('/comment', commentsController);
// var server = http.createServer(app, session);

// server.listen(port)
mongoose.connection.once('open', function () {
    app.listen(port, () => console.log('App listening on port' +port))

})

