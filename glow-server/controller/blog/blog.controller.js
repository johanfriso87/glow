
const constants = require('./../../const')
const multer = require('multer');
const path = require('path');
const blogModel = require("./blog.model");
var storageForProfilePic = multer.diskStorage({

    destination: function (req, file, cb) {
        cb(null, path.join(constants.BLOG_PICTURE_PATH))
    },
    filename: function (req, file, cb) {
        var extension = file.originalname.split('.');
        extension = extension[extension.length - 1];
        req.body.fileName =file.fieldname + '-' + Date.now() + '.' + extension;
        cb(null, req.body.fileName)
    }
})
var blogPicture = multer({ storage: storageForProfilePic }).single('picture');

module.exports.fnAddBlog = async (req,res,next) =>{
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        blogPicture(req, res, async (e1) => {
            if(!e1){
                var title = req.body.title;
                var content = req.body.content;
                var fileName = req.body.fileName;
                title = (title) ? title.trim() : null;
                content = (content) ? content.trim() : null;
                fileName = (fileName) ? fileName.trim() : null;
                if(title && content && fileName){
                    var insertData = {
                        title:title,
                        picture:"blog/"+fileName,
                        content:content,
                    }
                    var data = await blogModel(insertData).save();
                    response.status = 'success';
                    response.msg = "Blog inserted";
                    response.data = data;
                    res.json(response);
                } else {
                    response.msg = "Invalid Parameters.";
                    res.json(response);
                }

            } else {
                console.log('Server error --> fnUpdateProfilePic --> e1', e1);
                res.json(response);
            }
          
        })
    }catch(e){
        console.log('Server error --> fnAddBlog --> e', e);
        res.json(response);
    }
}

module.exports.fnGetBlog = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var data = await blogModel.find().sort({"_id":-1}).lean();
        response.msg = "";
        response.status = "success";
        response.data = data;
        res.json(response);
    } catch (e) {
        console.log('Server error --> fnGetBlog --> e', e);
        res.json(response);
    }
}

module.exports.fnUpdateBlog = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        blogPicture(req, res, async (e1) => {
            if(!e1){
                var title = req.body.title;
                var _id = req.body._id;
                var content = req.body.content;
                var fileName = req.body.fileName;
                title = (title) ? title.trim() : null;
                _id = (_id) ? _id.trim() : null;
                content = (content) ? content.trim() : null;
                fileName = (fileName) ? fileName.trim() : null;
                if(title && content && fileName && _id){
                    var insertData = {
                        title:title,
                        picture:picture,
                        content:content,
                    }
                    var data = await blogModel.findByIdAndUpdate(_id,insertData);
                    response.status = 'success';
                    response.msg = "Blog uploaded";
                    response.data = data;
                    res.json(response);
                } else {
                    response.msg = "Invalid Parameters.";
                    res.json(response);
                }

            } else {
                console.log('Server error --> fnUpdateBlog --> e1', e1);
                res.json(response);
            }
          
        })
    }catch(e){
        console.log('Server error --> fnUpdateBlog --> e', e);
        res.json(response);
    }
}


module.exports.fnViewBlog = async (req,res,next) =>{
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try{
        var _id = req.body._id;
        _id = (_id) ? _id.trim() : null;
        if(_id){
            var videoData = await blogModel.findByIdAndUpdate(_id, {$inc: { views: 1} });
            response.msg = "";
            response.status = "success";
            res.json(response);
        }else{
            response.msg = "Invalid Parameters.";
            res.json(response);
        }
    } catch(e) {
        console.log('Server error --> fnViewBlog --> e', e);
        res.json(response);
    }
}

module.exports.fnDeletBlog = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {

    } catch (e){
        console.log('Server error --> fnDeletBlog --> e', e);
        res.json(response);
    }
}