
const multer = require('multer');
const constants = require('./../../const')
var videoModel = require('./video.model');
const path = require('path');
var shortid = require("shortid");
var likesModel = require("./../likes/likes.model");
const usersModel = require('../../models/users.model');
var gify =require('gify')
var nodecmd = require('node-cmd');
var promise = require('bluebird');
const getAsync = promise.promisify(nodecmd.get, {
    multiArgs: true,
    context: nodecmd
});
var storageForVideo = multer.diskStorage({

    destination: function (req, file, cb) {
        if(file.fieldname === "video"){
            cb(null, path.join(constants.VIDEO_PATH))

        }
        if(file.fieldname === "thumbnail"){
            console.log("thumbnail");
            cb(null, path.join(constants.THUMB_PATH))
            console.log("thumbnail");

        }

        // cb(null, path.join(__dirname, constants.VIDEO_PATH))
    },
    filename: function (req, file, cb) {
        if(file.fieldname === "video"){
            var extension = file.originalname.split('.');
            extension = extension[extension.length - 1];
            req.body.fileName=shortid.generate().toString()+"_"+ file.fieldname + '_' + Date.now() + '.' + extension
            cb(null, req.body.fileName)
        }

        if(file.fieldname === "thumbnail"){
            var extension = file.originalname.split('.');
            extension = extension[extension.length - 1];
            req.body.thumbnailFilePath= shortid.generate().toString()+"_"+file.fieldname + '_' + Date.now() + '.' + extension
            cb(null, req.body.thumbnailFilePath)
        }
       
    }
})
var uploadVideo = multer({ storage: storageForVideo }).fields([{name:"video",maxCount:"1"},{name:"thumbnail",maxCount:1}]);

module.exports.fnUploadVideo =async (req,res,next) =>  {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        uploadVideo(req, res, async (e1) => {
            console.log(req.body.isNew)
            if(req.body.isNew){
                if(!e1){
                    var nodeCommand = "ffmpeg -i "+constants.VIDEO_PATH+req.body.fileName+" -vcodec libx264 -profile:v main -level 3.1 -preset medium -crf 23 -x264-params ref=4 -acodec copy -movflags +faststart "+constants.VIDEO_PATH+"processed_"+req.body.fileName;
                    getAsync(nodeCommand).then(rrresult=>{
                        console.log(rrresult)
                        var gifName = shortid.generate()+Date.now() + '.gif'
                        console.log(gifName);
                        gify(constants.VIDEO_PATH+req.body.fileName, constants.GIF_PATH + gifName ,{width:120,duration:3},async function(err)  {
                            if (!err) {
                                var savedData =  await videoModel({userId:req.session.userId,
                                    videoUrl:"videos/"+"processed_"+ req.body.fileName,
                                    productThumbnailUrl:req.body.productThumbnailUrl,
                                    asin:req.body.asin,
                                    productName:req.body.productName,
                                    productUrl:req.body.productUrl,
                                    gif:"gif/"+ gifName,
                                    thumbnailUrl:"thumbnail/"+ req.body.thumbnailFilePath,
                                }).save();
                                savedData = await videoModel.findById(savedData._id.toString())
                                // savedData.populate("productId","");
                                response.status = "success";
                                response.msg ="video uploded";
                                response.data = savedData;
                                res.json(response);
                            }else{
                                console.log('Server error --> fnUploadVideo --> err', err);
                                res.json(response);
                            }
                            
                        });
                   
                    })
                
                  
                      
      
                   
                }else{
                    console.log('Server error --> fnUploadVideo --> e1', e1);
                    res.json(response);
                }
            }else{
                response.msg = "you are using old app."
                res.json(response);
            }
           
        })
    } catch(e){
        console.log('Server error --> fnUploadVideo --> e', e);
        res.json(response);
    }
}


module.exports.fnGetUserVideo = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var data = await videoModel.find({userId:req.session.userId}).sort({"_id":-1});
        response.status = "success";
        response.msg = "";
        response.data = data;
        res.json(response)
    }catch(e){
        console.log('Server error --> fnGetUserVideo --> e', e);
        res.json(response);
    }
}

module.exports.fnGetRevideVideoProduct = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var asin = req.body.asin;
        asin = (asin) ? asin.trim() : null;
        if(asin){
            var videoData = await videoModel.find({asin:asin}).populate('userId','name _id profilePic').sort({'_id': -1}).limit(20).lean();
        }else{
            response.msg = "Invalid Parameters.";
            res.json(response);
        }
    } catch(e) {
        console.log('Server error --> fnGetUserVideo --> e', e);
        res.json(response);
    }
}

module.exports.fnUpdateVideo = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        uploadVideo(req, res, async (e1) => {
            if(!e1){

                var savedData =  await videoModel.findByIdAndUpdate(req.body.videoId,{userId:req.session.userId,
                    videoUrl:"videos/"+ req.body.fileName,
                    thumbnailUrl:"thumbnail/"+ req.body.thumbnailFilePath,
                }).save();
                savedData = await videoModel.findById(req.body.videoId.toString());
                // savedData.populate("productId","");
                response.status = "success";
                response.msg ="video uploded";
                response.data = savedData;
                res.json(response);
            }else{
                console.log('Server error --> fnUpdateVideo --> e1', e1);
                res.json(response);
            }
        })
    } catch(e){
        console.log('Server error --> fnUpdateVideo --> e', e);
        res.json(response);
    }
}

module.exports.fnViewVideo = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try{
        var _id = req.body._id;
        _id = (_id) ? _id.trim() : null;
        if(_id){
            var videoData = await videoModel.findByIdAndUpdate(_id, {$inc: { views: 1 } });
            var isLiked = false;
            var likeData = await likesModel.findOne({videoId:_id,userId:req.session.userId}).lean();
            var likeDataa = await likesModel.find({videoId:_id}).lean();
            if( likeData && likeData._id){
                isLiked = true;
            }
            response.data["isLiked"] = isLiked;
            response.msg = "";
            response.status = "success";
            response.data = {"isLiked": isLiked}
            response["isLiked"] =  isLiked;
            res.json(response);
        }else{
            response.msg = "Invalid Parameters.";
            res.json(response);
        }
    } catch(e) {
        console.log('Server error --> fnViewVideo --> e', e);
        res.json(response);
    }
}

module.exports.fnGetTargetUserVideo = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var userId = req.body.userId;
        userId = (userId) ? userId.trim() : null;
        var page = req.body.page;
        var dataPerPage = req.body.dataPerPage;
        dataPerPage = dataPerPage ? dataPerPage : 20;
        page = page ? page : 0;
        if(userId){
            var data =  await videoModel.find({userId:userId}).populate('userId','name _id profilePic').sort({'_id': -1}).skip(dataPerPage * page).limit(dataPerPage).lean();
            response.data = data;
            response.status = "success";
            response.msg = "";
            res.json(response)
        } else {
            response.msg = "Invalid Parameters.";
            res.json(response);
        }
    } catch (e){
        console.log('Server error --> fnGetTargetUserVideo --> e', e);
        res.json(response);
    }
}