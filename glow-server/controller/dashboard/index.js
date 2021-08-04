const express = require('express')
const router = express.Router()
var dashboard = require('./dashboard.controller');

router.post('/get', dashboard.fnGetDashboard);


module.exports = router