var mongoose = require('mongoose');

var userSchema = mongoose.Schema({
    url:{
        type:"string",
        default:""
    }
});
var userModel = module.exports = mongoose.model('stickers_data', userSchema);