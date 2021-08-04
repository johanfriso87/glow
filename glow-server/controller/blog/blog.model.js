var mongoose = require('mongoose');

var blogSchema = mongoose.Schema({
    title:{
        type:String,
        default:""
    },
    picture:{
        type:String,
        default:""
    },
    content:{
        type:String,
        default:""
    },
    views:{
        type:Number,
        default:0
    }
});
var blogModel = module.exports = mongoose.model('blogs_data', blogSchema);