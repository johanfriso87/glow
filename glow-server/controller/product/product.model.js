var mongoose = require('mongoose');
var productSchema = mongoose.Schema({
    name: {
        type: String,
        default: ''
    },
    image:{
        type:String,
        default:''
    },
    sticker:{
        type:String,
        default:""
    },
    shopUrl:{
        type:String,
        default:''
    },
    brand:{
        type:String,
        default:''
    }
})
var productModel = module.exports = mongoose.model('product_data', productSchema);
