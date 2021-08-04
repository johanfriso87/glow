const express = require('express')
const router = express.Router()
var public = require('./public.controller');

router.all('/videoshare', public.fnShareVideo);

// router.get('/login_with_facebook', auth.fnLoginWithFacebook);


module.exports = router
// IrnKlHN1eztSqYjb