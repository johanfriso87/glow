const express = require('express')
const router = express.Router()
var likes = require('./likes.controller');

router.post('/add', likes.fnLikeVideo);
router.post('/remove', likes.fnRemoveLikedVideo);
router.post('/get_liked_video', likes.fnGetLikedVideo);


module.exports = router