var crypto = require("crypto-js");
var AES_KEY = '6fnhkgo71s0caeqma6ojjftu4n1m1d85';
var userModel = require("./../models/users.model");
module.exports.fnAuthMiddleware= async (req,res,next)=>{
    var response = {
        status: 'error',
        msg: "Unauthorised request."
    };
    var url = req.originalUrl;
    if (req.headers && req.headers.authorization) {
        var parts = req.headers.authorization.split(' ');
        if (parts && parts.length >= 2) {
            if (parts[0] === 'Bearer') {
                var str = parts[1];
                var data = crypto.AES.decrypt(str, AES_KEY).toString(crypto.enc.Utf8);
                if (data) {

                    var userData = await userModel.findById(data).lean();
                    if ( userData && userData._id ) {
                        req.session.userId = userData._id.toString();
                        req.session.companyId = userData.companyId;
                        req.session.name = userData.name;
                        req.session.firstName = userData.firstName;
                        req.session.lastName = userData.lastName;
                        req.session.companyEmail = userData.companyEmail;
                        req.session.personalEmail = userData.personalEmail;
                        req.session.phone = userData.phone;
                        req.session.userName = userData.userName;
                        req.session.usertype = userData.usertype;
                        req.session.status = userData.status;
                        req.session.password = userData.password;
                        req.session.employeeId = userData && userData.employeeId ? userData.employeeId.toString() : "";
                        req.session.requestUser = userData.requestUser;
                        console.log("i am here");
                        console.log(req.session.userId);
                        next();
                    } else {
                        res.json(response);
                    }
                }
                else {
                    console.log(1)
                    console.log('Server error --> fnAuthorise --> data missmatch --> 1 --> ');
                    return res.json(response);
                }
            }
            else {
                console.log(11)
                console.log('Server error --> fnAuthorise ---> Bearer missmatch --> 11 --> ');
                return res.json(response);
            }
        }
        else {
            console.log(111)
            console.log('Server error --> fnAuthorise --> parts and parts length missmatch --> 111 --> ');
            return res.json(response);
        }
    }
    else {
        console.log(1111)
        console.log('Server error --> fnAuthorise --> authorization does not exist on header --> 1111 --> ');
        return res.json(response);
    }
}