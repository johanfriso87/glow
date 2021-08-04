var userModel = require("./../../models/users.model");
var helper = require("./../../helper/helper")
var crypto = require("crypto-js");

var TOKEN_VALIDITY = 24 * 60 * 60 * 1000;
var AES_KEY = '6fnhkgo71s0caeqma6ojjftu4n1m1d85';
var BASE_URL = '';
var async = require('async');
module.exports.fnLoginWithFacebook = async (req, res, next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var facebookId = req.body.facebookId;
        var name = req.body.name;
        facebookId = (facebookId) ? facebookId.trim() : null;
        if (facebookId) {
            var userData = await userModel.findOne({ facebookId: facebookId }).lean();
            console.log(userData);
            if (userData && userData.facebookId) {
                delete userData.password;
                var token = userData._id.toString()
                token = crypto.AES.encrypt(token, AES_KEY).toString();
                userData.token = token;
                response.status = 'success';
                response.data = userData;
                response.msg = '';
                res.json(response);
            } else {
                var insertUserData = {
                    facebookId: facebookId,
                    name: name
                }
                userModel(insertUserData).save({ lean: true }, function (e2, savedUserData) {
                    if (!e2) {
                        if (savedUserData && savedUserData._id) {
                            savedUserData = savedUserData.toObject()
                            var token = savedUserData._id.toString()
                            token = crypto.AES.encrypt(token, AES_KEY).toString();
                            delete savedUserData.password;
                            savedUserData["token"] = token;
                            response.status = 'success';
                            response.data = savedUserData;
                            response.msg = '';
                            res.json(response);
                        } else {
                            console.log('Server error --> fnLoginWithFacebook --> e2', 'error while save data');
                            res.json(response);
                        }
                    } else {
                        console.log('Server error --> fnLoginWithFacebook --> e2', e2);
                        res.json(response);
                    }
                })
            }

        } else {
            response.msg = "Invalid Parameters.";
            res.json(response);
        }
    } catch (e) {
        console.log('Server error --> fnLoginWithFacebook --> e', e);
        res.json(response);
    }
}

module.exports.fnLoginWithApple = async (req, res, next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var appleId = req.body.appleId;
        var name = req.body.name;
        appleId = (appleId) ? appleId.trim() : null;
        if (appleId) {
            var userData = await userModel.findOne({ appleId: appleId }).lean();
            console.log(userData);
            if (userData && userData.appleId) {
                delete userData.password;
                var token = userData._id.toString()
                token = crypto.AES.encrypt(token, AES_KEY).toString();
                userData.token = token;
                response.status = 'success';
                response.data = userData;
                response.msg = '';
                res.json(response);
            } else {
                var insertUserData = {
                    appleId: appleId,
                    name: name
                }
                userModel(insertUserData).save({ lean: true }, function (e2, savedUserData) {
                    if (!e2) {
                        if (savedUserData && savedUserData._id) {
                            savedUserData = savedUserData.toObject()
                            var token = savedUserData._id.toString()
                            token = crypto.AES.encrypt(token, AES_KEY).toString();
                            delete savedUserData.password;
                            savedUserData["token"] = token;
                            response.status = 'success';
                            response.data = savedUserData;
                            response.msg = '';
                            res.json(response);
                        } else {
                            console.log('Server error --> fnLoginWithApple --> e2', 'error while save data');
                            res.json(response);
                        }
                    } else {
                        console.log('Server error --> fnLoginWithApple --> e2', e2);
                        res.json(response);
                    }
                })
            }

        } else {
            response.msg = "Invalid Parameters.";
            res.json(response);
        }
    } catch (e) {
        console.log('Server error --> fnLoginWithApple --> e', e);
        res.json(response);
    }

}

module.exports.fnLoginWithEmail = async (req, res, next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var email = req.body.email;
        var name = req.body.name;
        var password = req.body.password;
        email = (email) ? email.trim() : null;
        name = (name) ? name.trim() : null;
        password = (password && password.length > 5) ? password.trim() : null;
        if (email && password) {
            if(!name){
                    name = email.split("@")[0]
            }
            var userData = await userModel.findOne({ email: email }).lean();
            if (userData && userData.email) {
                response.status = 'error';
                response.msg = "You already associate with us. Please try with login!"
                res.json(response);
            } else {
                var otp = helper.createRandomString(6)
                var insertUserData = {
                    email: email,
                    name: name,
                    otp: otp,
                    password:password,
                    isVerified:false,
                }
                userModel(insertUserData).save({ lean: true }, async (e2, savedUserData) => {
                    if (!e2) {
                        if (savedUserData && savedUserData._id) {
                            // savedUserData = savedUserData.toObject()
                            // var token = savedUserData._id.toString()
                            // token = crypto.AES.encrypt(token, AES_KEY).toString();
                            // delete savedUserData.password;
                            // savedUserData["token"] = token;
                            // response.status = 'success';
                            // response.data = savedUserData;
                            // response.msg = '';
                            // res.json(response);
                            var maileData = {
                                email: savedUserData.email,
                                subject: "Email verification",
                                text: `会員登録ありがとうございます。
                                本人確認のため、こちらのURLをクリックしてください。
                                
                                Thank you for registering as a member.
                                Please click this URL to verify your identity.http://13.233.98.117:3333/auth/verify_otp?otp=`+otp+`&email=`+email ,
                                name:name
                            }
                            await helper.sendMail(maileData);
                            response.status = 'success';
                            response.msg = "otp sent to your email!";
                            res.json(response);
                        } else {
                            console.log('Server error --> fnLoginWithEmail --> e2', 'error while save data');
                            res.json(response);
                        }
                    } else {
                        console.log('Server error --> fnLoginWithEmail --> e2', e2);
                        res.json(response);
                    }
                })
            }

        } else {
            response.msg = "Invalid Parameters.";
            res.json(response);
        }
    } catch (e) {
        console.log('Server error --> fnLoginWithEmail --> e', e);
        res.json(response);
    }
}

module.exports.fnVerifyEmail = async (req, res, next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var email = req.query.email;
        var otp = req.query.otp;
        email = (email) ? email.trim() : null;
        otp = (otp && otp.toString().trim().length == 6) ? otp.trim() : null;
        if (email && otp) {
            var userData = await userModel.findOne({ email: email, otp: otp }).lean();
            console.log(userData);
            if (userData && userData._id) {
                delete userData.password;
                var token = userData._id.toString()
                await userModel.findByIdAndUpdate(userData._id, { otp: "",isVerified:true });
                token = crypto.AES.encrypt(token, AES_KEY).toString();
                userData.token = token;
                response.status = 'success';
                response.data = userData;
                response.msg = '';
                res.send(`<html>
                <head></head>
                <body>
                    <script>
                        setTimeout(function() {
                        window.location = "glow://data"; }, 4000
                                        );
                        </script>
                        <h1>
                        あなたの電子メールが正常に確認された。Your email is verified try with login.
                        </h1>
                </body>
                </html>`);
            } else {
                response.msg = "Incorrect Email or Password.";
                res.json(response);
            }
        } else {
            response.msg = "Invalid Parameters.";
            res.json(response);
        }
    } catch (e) {
        response.msg = "Invalid Parameters.";
        res.json(response);
    }
}

module.exports.fnLoginWithGoogle = async (req, res, next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var googleId = req.body.googleId;
        var name = req.body.name;
        googleId = (googleId) ? googleId.trim() : null;
        if (googleId) {
            var userData = await userModel.findOne({ googleId: googleId }).lean();
            console.log(userData);
            if (userData && userData.googleId) {
                delete userData.password;
                var token = userData._id.toString()
                token = crypto.AES.encrypt(token, AES_KEY).toString();
                userData.token = token;
                response.status = 'success';
                response.data = userData;
                response.msg = '';
                res.json(response);
            } else {
                var insertUserData = {
                    googleId: googleId,
                    name: name
                }
                userModel(insertUserData).save({ lean: true }, function (e2, savedUserData) {
                    if (!e2) {
                        if (savedUserData && savedUserData._id) {
                            savedUserData = savedUserData.toObject()
                            var token = savedUserData._id.toString()
                            token = crypto.AES.encrypt(token, AES_KEY).toString();
                            delete savedUserData.password;
                            savedUserData["token"] = token;
                            response.status = 'success';
                            response.data = savedUserData;
                            response.msg = '';
                            res.json(response);
                        } else {
                            console.log('Server error --> fnLoginWithGoogle --> e2', 'error while save data');
                            res.json(response);
                        }
                    } else {
                        console.log('Server error --> fnLoginWithGoogle --> e2', e2);
                        res.json(response);
                    }
                })
            }

        } else {
            response.msg = "Invalid Parameters.";
            res.json(response);
        }
    } catch (e) {
        console.log('Server error --> fnLoginWithGoogle --> e', e);
        res.json(response);
    }

}

module.exports.fnEmailPassowrdLogin = async (req, res, next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var email = req.body.email;
        var password = req.body.password;
        email = (email) ? email.trim() : null;
        password = (password) ? password.trim() : null;
        if (email && password) {
            var userData = await userModel.findOne({ email: email }).lean();
            if (userData && userData._id) {
                if (userData.password == password) {
                    if(userData.isVerified){
                        delete userData.password;
                        delete userData.otp;
                        var token = userData._id.toString()
                        token = crypto.AES.encrypt(token, AES_KEY).toString();
                        userData.token = token;
                        response.status = 'success';
                        response.data = userData;
                        response.msg = '';
                        res.json(response);
                    }else{
                        response.msg = "Please verify your email address first."
                        res.json(response);
                    }
                    
                } else {
                    response.msg = "Wrong password."
                    res.json(response);
                }
            } else {
                response.msg = "Its seems like you are not associate with us. please try with signUp."
                res.json(response);
            }
        } else {
            response.msg = "Invalid Parameters.";
            res.json(response);
        }

    } catch (e) {
        console.log('Server error --> fnEmailPAssowrdLogin --> e', e);
        res.json(response);
    }
}

module.exports.fnForgotPassword = async (req, res, next) => {
    var response = {
        status: "error",
        msg: "Something happened wrong try again after sometimes.",
        data: {}
    }
    try {
        var email = req.body.email;
        email = (email) ? email.trim() : null;
        if (email) {
            var userData = await userModel.findOne({ email: email }).lean();
            if (userData && userData._id) {
                var password = userData.password;
                if (!userData.password || userData.password.length < 5) {
                    password = helper.createRandomString(6);
                    await userModel.findByIdAndUpdate(userData._id.toString(), { password: password });
                }

                var maileData = {
                    email: userData.email,
                    subject: "password for glow app.",
                    text: "your password for glow app is " + password
                }
                await helper.sendMail(maileData);
                response.status = "success";
                response.msg = "Password is send to your email. try with login now!"
                res.json(response);
            } else {
                response.msg = "Invalid email please try with register.";
                res.json(response);
            }
        } else {
            response.msg = "Invalid Parameters.";
            res.json(response);
        }

    } catch (e) {
        console.log('Server error --> fnForgotPassword --> e', e);
        res.json(response);
    }
}

module.exports.fnLoginWithPhoneNumber = async (req, res, next) => {}