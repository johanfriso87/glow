var videoModel = require("./../video/video.model");
var blogModel = require("./../blog/blog.model");
var mySelfProduct = require("./../product/myself.product.model");
var userModel = require("./../../models/users.model");
module.exports.fnGetDashboard = async (req,res,next)=>{
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
      
       var blogs = await blogModel.find({}).sort({'_id': -1}).limit(20);
       var users = await userModel.find({_id:{$ne:req.session.userId}}).sort({'_id': -1}).limit(100).lean();
       var savedProduct = await mySelfProduct.find({"userId":req.session.userId}).sort({'_id': -1})
       var uniqueVideos = [];
       var modifiedusers = [];
       var  userIdes = []
       var vIdes = []
       for (let index = 0; index < users.length; index++) {
            var userVideo = await videoModel.find({userId:users[index]._id.toString()}).populate('userId','name _id profilePic').sort({'_id': -1}).limit(20);
            if(userVideo.length > 1){
                users[index]["videos"] = userVideo;
                modifiedusers.push(users[index]);
                if(userIdes.indexOf(userVideo[0].userId._id.toString()) == -1){
                    uniqueVideos.push(userVideo[0]);
                    userIdes.push(userVideo[0].userId._id.toString())
                    vIdes.push(userVideo[0]._id.toString())
                }
               
            }
           
            if(modifiedusers.length > 19){
                break;
            }
       }
      

       var videos = await videoModel.find({'_id': {$nin: vIdes}}).populate('userId','name _id profilePic').sort({'_id': -1}).limit(20);
       var data = {
           videos:videos,
           users:modifiedusers,
           blogs:blogs,
           savedProduct:savedProduct,
           uniqueVideos:uniqueVideos
       }
       response.status = "success";
       response.data = data;
       response.msg = "";
       res.json(response);
    } catch (e) {
        console.log('Server error --> fnGetDashboard --> e', e);
        res.json(response);
    }
}