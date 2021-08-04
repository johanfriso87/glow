var mongoose = require('mongoose');

var userSchema = mongoose.Schema({
    name: {
        type: String,
        default: ''
    },
    userName: {
        type: String,
        default: ''
    },
    email:{
        type:String,
        default:''
    },
    otp:{
        type:String,
        default:""
    },
    about:{
        type:String,
        default:''
    },
    website:{
        type:String,
        default:''
    },
    birthDate:{
        type:Date,
    },
    skinTone:{
        type:String
    },
    skinType:{
        type:String
    },
    hairColor:{
        type:String
    },
    hairTexture:{
        type:String
    },
    hairType:{
        type:String
    },
    eyeColor:{
        type:String
    },
    skinCareInterests: [{
        type: String
    }],
    hairCareInterests: [{
        type: String
    }],
    appleId:{
        type:String,
        default:''
    },
    googleId:{
        type:String,
        default:''
    },
    facebookId:{
        type:String,
        default:''
    },
    password: {
        type: String,
        default: '',
    },
    phone: {
        type: String,
        default: ''
    },
    usertype: {
        type: String,
        default: 'USER'
    },
    registerTime: {
        type: Date,
    },
    profilePic:{
        type:String,
    },
    isVerified: {
        type: Boolean,
        default: true
    },
    status: {
        type: Number,
        default: 0
    }
});
var userModel = module.exports = mongoose.model('users_data', userSchema);