const express = require('express')
const router = express.Router()
var comments = require('./comments.controller');

router.post('/add', comments.fnAddComment);
router.post('/get', comments.fnGetComments);
router.post('/remove', comments.fnRemoveComment);
router.post('/add_replay', comments.fnAddReplay);
router.post('/remove_replay', comments.fnRemoveCommentReplay);


module.exports = router