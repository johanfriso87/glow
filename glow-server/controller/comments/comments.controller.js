var commentsModel = require("./comments.model");
var likesModel = require("./../likes/likes.model");
var usersModel = require("./../../models/users.model");

module.exports.fnGetComments = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var videoId = req.body.videoId;
        videoId = (videoId) ? videoId.trim() : null;
        if(videoId){
            var comments =  await commentsModel.find({videoId:videoId}).populate('userId','name _id profilePic').populate('replay.userId','name _id profilePic').populate('replay','userId comment');
            await usersModel.populate(comments,{path:'replay.userId',select: 'name _id profilePic',});
            var isLiked = false;
            var likeData = await likesModel.findOne({videoId:videoId,userId:req.session.userId}).lean();
            console.log(likeData)
            isLiked = likeData && likeData._id ? true : false;
            response.data = comments;
            response.msg = "";
            response.status = "success";
            response.isLiked = isLiked;
            console.log("blah blah");
            console.log(response);
            res.json(response);
        }else{
            response.msg = "Invalid Parameters.";
            res.json(response);
        }   
    } catch (e) {
        console.log('Server error --> fnGetComments --> e', e);
        res.json(response);
    }
}

module.exports.fnAddComment = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {   
        var comment = req.body.comment;
        var videoId = req.body.videoId;
        comment = (comment) ? comment.trim() : null;
        videoId = (videoId) ? videoId.trim() : null;
        if (comment && videoId) {
          var commentData = await commentsModel({
                userId:req.session.userId,
                comment:comment,
                videoId:videoId
            }).save()
            response.data = commentData;
            response.status = "success";
            response.msg = "";
            res.json(response);
        } else {
            response.msg = "Invalid Parameters.";
            res.json(response);
        }

    } catch (e) {
        console.log('Server error --> fnAddComment --> e', e);
        res.json(response);
    }
}

module.exports.fnAddReplay = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var commentId = req.body.commentId;
        var comment = req.body.comment;
        commentId = (commentId) ? commentId.trim() : null;
        comment = (comment) ? comment.trim() : null;

        if(commentId && comment){
            var savedComment =  await commentsModel({comment:comment,userId:req.session.userId}).save();
            console.log(savedComment);
             await commentsModel.findByIdAndUpdate(commentId , { $push: { replay: savedComment._id  } },);
            response.data = savedComment;
            response.msg = "";
            response.status = "success";
            res.json(response);
        }else{
            response.msg = "Invalid Parameters.";
            res.json(response);
        }
    } catch (e) {
        console.log('Server error --> fnAddReplay --> e', e);
        res.json(response);
    }
}

module.exports.fnRemoveComment = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var commentId = req.body.commentId;
        commentId = (commentId) ? commentId.trim() : null;
            if(commentId){
                await commentsModel.findOneAndRemove({userId:req.session.userId,_id:commentId})
                response.msg = "";
                response.status = "success";
                res.json(response);
            }else{
                response.msg = "Invalid Parameters.";
                res.json(response);
            }
    } catch (e) {
        console.log('Server error --> fnRemoveComment --> e', e);
        res.json(response);
    }
}

module.exports.fnRemoveCommentReplay = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var commentId = req.body.commentId;
        var replayId = req.body.replayId;
        commentId = (commentId) ? commentId.trim() : null;
        replayId = (replayId) ? replayId.trim() : null;
            if(commentId && replayId){
                await commentsModel.findOneAndRemove({userId:req.session.userId,_id:replayId})
                await commentsModel.findByIdAndUpdate(commentId,{ $push: { replay: replayId  } })
                response.msg = "";
                response.status = "success";
                res.json(response);
            }else{
                response.msg = "Invalid Parameters.";
                res.json(response);
            }
    } catch (e) {
        console.log('Server error --> fnRemoveComment --> e', e);
        res.json(response);
    }
}