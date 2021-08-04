var stickersModel = require("./stickers.model");
const constants = require('./../../const');
const multer = require('multer');
const path = require('path');
var fs = require("fs");
var shortid = require('shortid');
var Jimp = require('jimp');
var storageForProfilePic = multer.diskStorage({

    destination: function (req, file, cb) {
        cb(null, path.join(constants.STICKERS_PICTURE_PATH))
    },
    filename: function (req, file, cb) {
        var extension = file.originalname.split('.');
        extension = extension[extension.length - 1];
        var name =file.fieldname+shortid.generate() + '-' + Date.now() + '.' + extension;
        if (req.body.fileName) {
            req.body.fileName.push(name);
        } else {
            req.body.fileName = [];
            req.body.fileName[0] = name;
        }
        cb(null,name);
    }
})
var uploadStickers = multer({ storage: storageForProfilePic }).array('stickers');
module.exports.fnAddStickers = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        uploadStickers(req, res, async (e1) => {
            if(!e1) {
                console.log(req.body.fileName);
                var insertBatch = [];
                for (let index = 0; index < req.body.fileName.length; index++) {
                    const element = req.body.fileName[index];
                    console.log(element);
                    insertBatch.push({"url":"stickers/"+element});
                }
                var data =  await stickersModel.insertMany(insertBatch);
                response.status = "success";
                    response.data = data;
                    response.msg = "";
                    res.json(response);
            } else {
                console.log('Server error --> fnAddStickers --> e1', e1);
                res.json(response);
            }
        })
    } catch(e) {
        console.log('Server error --> fnAddStickers --> e', e);
        res.json(response);
    }
}


module.exports.fnGetStickers = async (req,res,next) => {
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
        var data =  await stickersModel.find({}).sort({"_id":-1}).lean();
        response.status = "success";
        response.data = data;
        response.msg = "";
        res.json(response);
    } catch (e) {
        console.log('Server error --> fnGetStickers --> e', e);
        res.json(response);
    }

}

module.exports.fnCreateStickerForProduct = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var stickerUrl = req.body.productThumbnailUrl;
        stickerUrl = (stickerUrl) ? stickerUrl.trim() : null;
        if(stickerUrl){
            Jimp.read(stickerUrl).then( async (image) => {
                const targetColor = {r: 255, g: 255, b: 255, a: 255};  // Color you want to replace
                const replaceColor = {r: 0, g: 0, b: 0, a: 0};  // Color you want to replace with
                const colorDistance = (c1, c2) => Math.sqrt(Math.pow(c1.r - c2.r, 2) + Math.pow(c1.g - c2.g, 2) + Math.pow(c1.b - c2.b, 2) + Math.pow(c1.a - c2.a, 2));  // Distance between two colors
                const threshold = 16;  // Replace colors under this threshold. The smaller the number, the more specific it is.
                image.scan(0, 0, image.bitmap.width, image.bitmap.height, (x, y, idx) => {
                  const thisColor = {
                    r: image.bitmap.data[idx + 0],
                    g: image.bitmap.data[idx + 1],
                    b: image.bitmap.data[idx + 2],
                    a: image.bitmap.data[idx + 3]
                  };
                  if(colorDistance(targetColor, thisColor) <= threshold) {
                    image.bitmap.data[idx + 0] = replaceColor.r;
                    image.bitmap.data[idx + 1] = replaceColor.g;
                    image.bitmap.data[idx + 2] = replaceColor.b;
                    image.bitmap.data[idx + 3] = replaceColor.a;
                  }
                });
                var fileName = shortid.generate() + '-' + Date.now()+".png";
                image.write(constants.PRODUCT_STICKERS_PICTURE_PATH +'/'+fileName);
                response.status = "success";
                response.data = 'product-stickers/'+fileName;
                response.msg = "";
                res.json(response);
              });
        }else{
            response.msg = "Invalid Parameters.";
            res.json(response);
        }

    } catch (e){
        console.log('Server error --> fnCreateStickerForProduct --> e', e);
        res.json(response);
    }
}