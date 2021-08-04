var relationModel = require("./relation.model");
var videoModel = require("./../video/video.model");
module.exports.fnAddToFavorite = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var targetUserId = req.body.targetUserId;
        targetUserId = (targetUserId) ? targetUserId.trim() : null;
        if( targetUserId ){
            var relationData =  await relationModel.findOne({targetUserId:targetUserId,userId:req.session.userId}).lean();
            if(relationData && relationData._id){
                response.status = "success";
                response.data = relationData;
                response.msg = "";
                res.json(response);
            }else{
                await relationModel({
                    userId : req.session.userId,
                    targetUserId : targetUserId,
                }).save({ lean: true }, (e2, savedRelationData) => {
                    if(!e2){
                        response.status = "success";
                        response.data = savedRelationData;
                        response.msg = "";
                        res.json(response);
                    }else{
                        console.log('Server error --> fnAddToFavorite --> e2', e2);
                        res.json(response);
                    }
                  
                })
            }
        } else {
            response.msg = "Invalid Parameters.";
            res.json(response);
        }
    } catch (e) {
        console.log('Server error --> fnAddToFavorite --> e', e);
        res.json(response);
    }
}

module.exports.fnRemoveFromFavorite = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var targetUserId = req.body.targetUserId;
        targetUserId = (targetUserId) ? targetUserId.trim() : null;
        if(targetUserId){
            await relationModel.findOneAndRemove({userId:req.session.userId,targetUserId:targetUserId});
            response.status = "success";
            response.msg = "Favorite remove.";
            res.json(response);
        }else{
            response.msg = "Invalid Parameters.";
            res.json(response);
        }
    } catch (e) {
        console.log('Server error --> fnRemoveFromFavorite --> e', e);
        res.json(response);
    }
}

module.exports.fnGetFavoriteVideo = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var page = req.body.page;
        var dataPerPage = req.body.dataPerPage;
        dataPerPage = dataPerPage ? dataPerPage : 20;
        page = page ? page : 0;
        var getFollowingUsers = await relationModel.find({userId:req.session.userId}).select("_id").lean();
        var followingUsers = [];
        for (let index = 0; index < getFollowingUsers.length; index++) {
            const element = getFollowingUsers[index];
            followingUsers.push(element._id.toString())
            
        }
        var data = await videoModel.find({ "userId":{"$in":followingUsers}}).populate('userId','name _id profilePic').sort({'_id': -1}).skip(dataPerPage * page).limit(dataPerPage).lean();
        response.data = data;
        response.status = "success";
        response.msg = "";
        res.json(response)
    }catch(e){
        console.log('Server error --> fnRemoveFromFavorite --> e', e);
        res.json(response);
    }
}

module.exports.fnGetSubsribedUser = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var getFollowingUsers = await relationModel.find({userId:req.session.userId}).select("targetUserId").populate("targetUserId","").lean();
        var users = [];
        for (let index = 0; index < getFollowingUsers.length; index++) {
            if(getFollowingUsers[index].targetUserId != null)
            users.push(getFollowingUsers[index].targetUserId);
            
        }
        response.data = users;
        response.status = "success";
        response.msg = "";
        res.json(response)
    }catch(e){
        console.log('Server error --> fnGetSubsribedUser --> e', e);
        res.json(response);
    }
}