const express = require('express')
const router = express.Router()
var blog = require('./blog.controller');

router.post('/add', blog.fnAddBlog);
router.post('/get', blog.fnGetBlog);
router.post('/view', blog.fnViewBlog);
router.post('/update', blog.fnUpdateBlog);
// router.get('/login_with_facebook', auth.fnLoginWithFacebook);


module.exports = router