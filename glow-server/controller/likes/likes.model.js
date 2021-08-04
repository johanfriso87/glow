var mongoose = require('mongoose');

var userSchema = mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'users_data'
    },
    videoId:{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'videos_data'
    }
});
var userModel = module.exports = mongoose.model('likes_data', userSchema);