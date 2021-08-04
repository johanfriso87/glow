const express = require('express')
const router = express.Router()
var stickers = require('./stickers.controller');

router.post('/get', stickers.fnGetStickers);
router.post('/add', stickers.fnAddStickers);
router.post('/product_stickers_add', stickers.fnCreateStickerForProduct);


module.exports = router