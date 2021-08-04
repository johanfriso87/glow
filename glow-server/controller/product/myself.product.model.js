var mongoose = require('mongoose');
var productSchema = mongoose.Schema({
    asin: {
        type: String,
        default: ''
    },
    title:{
        type:String,
        default:''
    },
    url:{
        type:String,
        default:""
    },
    images:[{
        type:String,
        default:''
    }],
    brand:{
        type:String,
        default:''
    },
    userId:{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'users_data'
    },
    isMyself:{
        type:Boolean,
        default:false
    }
})
var productModel = module.exports = mongoose.model('myself_product_data', productSchema);
