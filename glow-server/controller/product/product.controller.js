const productModel = require("./product.model");
const mySelfProductModel = require("./myself.product.model");
var videoModel = require('./../video/video.model');
const axios = require('axios');
const cheerio = require('cheerio');
var fs = require("fs");
const constants = require('./../../const')
const path = require('path');
var shortid = require('shortid');
var Jimp = require('jimp');
const superagent = require('superagent');
module.exports.fnSearchProduct = async (req, res, next) => {
    var response = {
        status: "error",
        msg: "Something happpned wrong try again after sometimes.",
        data: {}
    }
    try {
        var q = req.body.q;
        var page = req.body.page;
        var dataPerPage = req.body.dataPerPage;
        dataPerPage = dataPerPage ? dataPerPage : 20;
        page = page ? page : 0;
        q = (q) ? q.trim() : null;
        console.log(q)
        if (q) {
            var data = await productModel.find({ name: { $regex: new RegExp(q,"i") } }).limit(dataPerPage).skip(dataPerPage * page).lean();
            response.status = "success";
            response.data = data;
            response.msg = "";
            res.json(response);
        } else {
        console.log(q)

            res.json(response);
        }

    } catch (e) {
        console.log('Server error --> fnSearchProduct --> e', e);
        res.json(response);
    }
}

module.exports.fnGetProductFromUrl = async (req, res, next) => {
    var response = {
        status: "error",
        msg: "Something happpned wrong try again after sometimes.",
        data: {}
    }
    try {
        var url = req.body.url;
        url = (url && typeof url === 'string') ? url.trim() : null;
        if (url) {
           
                var productData = await fnfetchDataFromUrl(url);
             
                if(productData.name && productData.images && productData.images[1]){
                    var inputProductData = {
                        name: productData.name,
                        image: productData.images,
                        brand: "",
                        shopUrl: url,
                    }
                    // console.log(constants.PRODUCT_PICTURE_PATH+productData.img);
                    // product = await productModel(inputProductData).save();
                    response.status = "success";
                    response.data = inputProductData;
                    response.msg = "";
                    res.json(response);
                }else{
            res.json(response);
                }
                
        } else {
            response.msg = "Invalid Parameters.";
            res.json(response);
        }
    } catch (e) {
        console.log('Server error --> fnGetProductFromUrl --> e', e);
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
            response.status = "success";
            response.msg = "";
            response.data = videoData;
            res.json(response)
        }else{
            response.msg = "Invalid Parameters.";
            res.json(response);
        }
    } catch(e) {
        console.log('Server error --> fnGetUserVideo --> e', e);
        res.json(response);
    }
}


module.exports.fnAddToMySelf = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var asin = req.body.asin;
        var title = req.body.title;
        var url = req.body.url;
        var images = req.body.images;
        var brand = req.body.brand;
        asin = (asin) ? asin.trim() : null;
        title = (title) ? title.trim() : null;
        url = (url) ? url.trim() : null;
        images = (images) ? images.trim() : null;
        if(asin && title && url && images){
            var savedData = await mySelfProductModel.findOne({userId:req.session.userId.toString(),asin:asin});
            if(savedData){
                response.status="success"
                response.msg="Product Saved!"
                res.json(response);
            }else{
                var img = [];
                img.push(images);
                var inputData = {
                    asin:asin,
                    title:title,
                    url:url,
                    images:img,
                    brand:brand,
                    userId:req.session.userId
                }
                mySelfProductModel(inputData).save();
                response.status="success"
                response.msg="Product Saved!"
                res.json(response);
            }


    
        }else{
            response.msg = "Invalid Parameters.";
            res.json(response);
        }
    }
    catch(e){
        console.log('Server error --> fnAddToMySelf --> e', e);
        res.json(response);
    }
}

module.exports.fnAddToMyProduct = async (req,res,next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var asin = req.body.asin;
        var title = req.body.title;
        var url = req.body.url;
        var images = req.body.images;
        var brand = req.body.brand;
        asin = (asin) ? asin.trim() : null;
        title = (title) ? title.trim() : null;
        url = (url) ? url.trim() : null;
        images = (images) ? images.trim() : null;
        if(asin && title && url && images){
            var savedData = await mySelfProductModel.findOne({userId:req.session.userId.toString(),asin:asin});
            if(savedData){
                response.status="success"
                response.msg="Product Saved!"
                res.json(response);
            }else{
                var img = [];
                img.push(images);
                var inputData = {
                    asin:asin,
                    title:title,
                    url:url,
                    images:img,
                    brand:brand,
                    userId:req.session.userId,
                    isMyself:true
                }
                mySelfProductModel(inputData).save();
                response.status="success"
                response.msg="Product Saved!"
                res.json(response);
            }


    
        }else{
            response.msg = "Invalid Parameters.";
            res.json(response);
        }
    }
    catch(e){
        console.log('Server error --> fnAddToMySelf --> e', e);
        res.json(response);
    }
}

module.exports.fnGetMyProduct = async (req,res,next) =>{
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var savedData = await mySelfProductModel.find({userId:req.session.userId.toString(),isMyself: true });
        
            response.status="success"
            response.msg="Product Saved!"
            response.data = savedData
            res.json(response);
        
    } catch(e) {
        console.log('Server error --> fnGetMySelfProduct --> e', e);
        res.json(response);
    }
}

module.exports.fnGetMySelfProduct = async (req,res,next) =>{
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var savedData = await mySelfProductModel.find({userId:req.session.userId.toString(),isMyself:{ $ne: true }});
        
            response.status="success"
            response.msg="Product Saved!"
            response.data = savedData
            res.json(response);
        
    } catch(e) {
        console.log('Server error --> fnGetMySelfProduct --> e', e);
        res.json(response);
    }
}

async function fnfetchDataFromUrl(url) {
    var $ = await fetchHTML(url);
    console.log($.html());
    var title = $('meta[property="og:title"]').attr('content')
    var img = $('meta[property="og:image"]').attr('content')
    var description = $('meta[property="og:description"]').attr('content')
    console.log(title);
    console.log(title);
    console.log(img);
    console.log(description);
    // let name = $('#productTitle').text().replace(/\n/g, '');
    // var productDetail = {}
    // $("#productDetails_techSpec_section_1 tr").each(function () {
    //     productDetail[$(this).find("th").text().trim().toLowerCase()] = $(this).find("td").text().trim();
    // });
    // var img = "";
    // $('#main-image-container img').each(function () {
    //     img = $(this).attr("src").replace(/\n/g, '')
    // })
    // img = img.replace(/^data:image\/jpeg;base64,/, "");
    // img = img.replace(/^data:image\/png;base64,/, "");
    // var imagePath = path.join(constants.PRODUCT_PICTURE_PATH);
    // var fileName = shortid.generate() + '-' + Date.now()+".png";
    // imagePath = imagePath + fileName;
    // fs.writeFile(imagePath, img, 'base64', function(err) {
    //     console.log(err);
    //   });
    img = img != null ? img :"";
    var images = [img];
    
    var product = {
        name: title,
        productDetail: description,
        images: images
    }
    console.log(product)
    return product;
}


async function fetchHTML(url) {
    // axios.get(url).then(
    //     (response) => {
    //       console.log(response);
    //     }
    //   )
    //   .catch(
    //     (error) => {
    //       console.log('error');
    //       console.log(error);
    //     }
    //   );
    const { data } = await axios.get(url)
    return cheerio.load(data)
}