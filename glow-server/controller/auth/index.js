const express = require('express')
const router = express.Router()
var auth = require('./auth.controller');

router.post('/login_with_facebook', auth.fnLoginWithFacebook);
router.post('/login_with_apple', auth.fnLoginWithApple);
router.post('/login_with_google', auth.fnLoginWithGoogle);
router.post('/login_with_email', auth.fnLoginWithEmail);
router.all('/verify_otp', auth.fnVerifyEmail);
router.post('/login_with_email_password', auth.fnEmailPassowrdLogin);
router.post('/forgot_password', auth.fnForgotPassword);

// router.get('/login_with_facebook', auth.fnLoginWithFacebook);


module.exports = router