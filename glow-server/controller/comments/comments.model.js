var mongoose = require('mongoose');

var userSchema = mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'users_data'
    },
    videoId:{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'videos_data'
    },
    replay:[{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'comments_data'
    }],
    comment:{
        type:String,
        default:""
    }
});
var userModel = module.exports = mongoose.model('comments_data', userSchema);