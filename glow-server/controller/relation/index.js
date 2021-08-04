const express = require('express')
const router = express.Router()
var relation = require('./relation.controller');

router.post('/add_to_favorite', relation.fnAddToFavorite);
router.post('/remove_from_favorite', relation.fnRemoveFromFavorite);
router.post('/get-favorite-video', relation.fnGetFavoriteVideo);
router.post('/get-favorite-users', relation.fnGetSubsribedUser);


module.exports = router