var mongoose = require('mongoose');

var blogSchema = mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'users_data'
    },
    title:{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'videos_data'
    },
    piucture:{
        type:'string',
        default:""
    },
    content:{
        type:String,
        default:""
    }
});
var blogModel = module.exports = mongoose.model('blogs_data', blogSchema);