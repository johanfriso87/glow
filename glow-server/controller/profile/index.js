const express = require('express')
const router = express.Router()
var profile = require('./profile.controller');

router.post('/update', profile.fnUpdateProfile);
router.post('/get', profile.fnGetProfile);
router.post('/add-profile-picture', profile.fnUpdateProfilePic);
router.post('/get-target-profile', profile.fnGetUserProfile);

module.exports = router