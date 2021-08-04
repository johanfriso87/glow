var userModel = require("./../../models/users.model");
const constants = require('./../../const')
const multer = require('multer');
const path = require('path');
var storageForProfilePic = multer.diskStorage({

    destination: function (req, file, cb) {
        cb(null, path.join(constants.PROFILE_PICTURE_PATH))
    },
    filename: function (req, file, cb) {
        var extension = file.originalname.split('.');
        extension = extension[extension.length - 1];
        req.body.fileName =file.fieldname + '-' + Date.now() + '.' + extension;
        cb(null, file.fieldname + '-' + Date.now() + '.' + extension)
    }
})
var uploadProfile = multer({ storage: storageForProfilePic }).single('profile');

module.exports.fnGetProfile = async (req, res, next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var userId = req.session.userId;
        if (userId) {
            var userData = await userModel.findById(userId).lean();
            if(userData && userData._id){
                delete userData.password;
                userData.token = req.headers.authorization.split(' ')[1];
                response.status = 'success';
                response.data = userData;
                response.msg = '';
                res.json(response);
            }else{
                console.log('Server error --> does not found any relevent user data');
                response.msg = "Invalid data.";
                res.json(response);
            }
        } else {
            console.log('Server error --> user ID not found');
            res.json(response);
        }
    }
    catch (e) {
        console.log('Server error --> fnGetProfile --> e', e);
        res.json(response);
    }
}

module.exports.fnUpdateProfile = async (req, res, next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var name = req.body.name;
        var userName = req.body.userName;
        var email = req.body.email;
        var about = req.body.about;
        var website = req.body.website;
        var birthDate = req.body.birthDate;
        var skinTone = req.body.skinTone;
        var skinType = req.body.skinType;
        var hairColor = req.body.hairColor;
        var hairTexture = req.body.hairTexture;
        var hairType = req.body.hairType;
        var eyeColor = req.body.eyeColor;
        var skinCareInterests = req.body.skinCareInterests;
        var hairCareInterests = req.body.hairCareInterests;
        var userId = req.session.userId;
        var userData = {
            name:name,
            userName:userName,
            email:email,
            about:about,
            website:website,
            birthDate:birthDate,
            skinTone:skinTone,
            skinType:skinType,
            hairColor:hairColor,
            hairTexture:hairTexture,
            hairType:hairType,
            eyeColor:eyeColor,
            skinCareInterests:skinCareInterests,
            hairCareInterests:hairCareInterests
        }

        var userData = await userModel.findByIdAndUpdate(userId,userData);
        userData = await userModel.findById(userId);
        delete userData.password;
        response.status = 'success';
        response.data = userData;
        response.msg = '';
        res.json(response);
    } catch (e) {
        console.log('Server error --> fnUpdateProfile --> e', e);
        res.json(response);
    }
}

module.exports.fnUpdateProfilePic = async (req, res, next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        uploadProfile(req, res, async (e1) => {
            if(!e1){
                await userModel.findByIdAndUpdate(req.session.userId,{profilePic:"profiles/"+ req.body.fileName})
                response.status = "success";
                response.msg ="";
                response.data = {profilePic:"profiles/" + req.body.fileName}
                res.json(response);
            }else{
                console.log('Server error --> fnUpdateProfilePic --> e1', e1);
                res.json(response);
            }
          
        })
    } catch (e){
        console.log('Server error --> fnUpdateProfilePic --> e', e);
        res.json(response);
    }
} 

module.exports.fnGetUserProfile = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var userId = req.body.userId;
        userId = (userId) ? userId.trim() : null;
        if(userId){
            var userData = await userModel.findById(userId).lean();
            if(userData && userData._id){
                delete userData.password;
                response.status = 'success';
                response.data = userData;
                response.msg = '';
                res.json(response);
            }else{
                console.log('Server error --> does not found any relevent user data');
                response.msg = "Invalid user data.";
                res.json(response);
            }
        }else{
            response.msg = "Invalid Parameters.";
            res.json(response);
        }
    } catch (e){
        console.log('Server error --> fnGetUserProfile --> e', e);
        res.json(response);
    }
}  