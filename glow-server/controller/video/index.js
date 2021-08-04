const express = require('express')
const router = express.Router()
var video = require('./video.controller');

router.post('/upload', video.fnUploadVideo);
router.post('/update', video.fnUpdateVideo);
router.post('/get_user_video', video.fnGetUserVideo);
router.post('/view',video.fnViewVideo)
router.post('/get-target-user-video',video.fnGetTargetUserVideo)

module.exports = router