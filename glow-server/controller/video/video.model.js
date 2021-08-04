var mongoose = require('mongoose');

var userSchema = mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'users_data'
    },
    gif:{
        type:String,
        default:""
    },
    videoUrl: {
        type: String,
        default: ''
    },
    thumbnailUrl: {
        type: String,
        default: ''
    },
    productThumbnailUrl: {
        type: String,
        default: ""
    },
    asin: {
        type: String,
        default: ""
    },
    productName: {
        type: String,
        default: ""
    },
    productUrl: {
        type: String,
        default: ""
    },
    productId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'product_data'
    },
    views:{
        type:Number,
        default:0
    }
});
var userModel = module.exports = mongoose.model('videos_data', userSchema);