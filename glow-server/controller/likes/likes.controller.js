var likesModel = require("./likes.model");
var usersModel = require("./../../models/users.model");
var _ = require('lodash');
module.exports.fnLikeVideo = async (req, res, next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var videoId = req.body.videoId;
        console.log(videoId)
        videoId = (videoId) ? videoId.trim() : null;
        if (videoId) {
            console.log(req.session.userId);
            console.log(req.session.userId);
            console.log(req.session.userId);
            console.log(req.session.userId);
            console.log(req.session.userId);
            console.log(req.session.userId);
            console.log(req.session.userId);
            console.log(req.session.userId);
            console.log(req.session.userId);
            var userVideolikesData = await likesModel.findOne({ videoId: videoId, userId: req.session.userId }).lean();
            if (userVideolikesData && userVideolikesData._id) {
                response.msg = "Video liked.";
                response.status = "success.";
                res.json(response);
            } else {
                await likesModel({ userId: req.session.userId, videoId: videoId }).save();
                response.msg = "Video liked.";
                response.status = "success.";
                res.json(response);
            }
        } else {
            response.msg = "Invalid Parameters.";
            res.json(response);
        }

    } catch (e1) {
        console.log('Server error --> fnLikeVideo --> e1', e1);
        res.json(response);
    }
}

module.exports.fnRemoveLikedVideo = async (req, res, next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var videoId = req.body.videoId;
        console.log(videoId)
        videoId = (videoId) ? videoId.trim() : null;
        if (videoId) {
            await likesModel.findOneAndDelete({ videoId: videoId, userId: req.session.userId });
            response.msg = "";
            response.status = "success.";
            res.json(response);
        } else {
            response.msg = "Invalid Parameters.";
            res.json(response);
        }
    } catch (e) {
        console.log('Server error --> fnRemoveLikedVideo --> e1', e1);
        res.json(response);
    }
}

module.exports.fnGetLikedVideo = async (req, res, next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var likedVideo = await likesModel.find({ userId: req.session.userId }).populate("videoId");
        await usersModel.populate(likedVideo, { path: 'videoId.userId', select: 'name _id profilePic', });
        var data = _.map(likedVideo, "videoId");
        
        response.msg = "";
        response.status = "success.";
        response.data = data
        res.json(response);
    } catch (e) {
        console.log('Server error --> fnRemoveLikedVideo --> e1', e1);
        res.json(response);
    }
}