var mongoose = require('mongoose');

var userSchema = mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'users_data'
    },
    targetUserId:{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'users_data'
    }
});
var userModel = module.exports = mongoose.model('relations_data', userSchema);