const express = require('express')
const router = express.Router()
var product = require('./product.controller');

router.post('/get_from_url', product.fnGetProductFromUrl);
router.post('/search', product.fnSearchProduct);
router.post('/get_reviews', product.fnGetRevideVideoProduct);
router.post('/add_to_myself',product.fnAddToMySelf);
router.post('/get_myself_product',product.fnGetMySelfProduct);
router.post('/add_to_my_product',product.fnAddToMyProduct);
router.post('/get_my_product',product.fnGetMyProduct);

module.exports = router