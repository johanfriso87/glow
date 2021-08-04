var crypto = require("crypto-js");
var AES_KEY = '6fnhkgo71s0caeqma6ojjftu4n1m1d85';
var nodemailer = require('nodemailer');
var _ = require('lodash')
const SMTPConnection = require("nodemailer/lib/smtp-connection");
module.exports.fnGetToken=(userId)=>{
    return crypto.AES.encrypt(userId, AES_KEY).toString();
}


module.exports.sendMail = async(data) => {
    var MAIL_SENDFROM_NAME = 'Glow';
    var MAIL_SENDFROM_MAIL = 'info@glowteams.com';

    var transporter = nodemailer.createTransport({
        host: 'smtpout.secureserver.net', 
        port: 465, 
        secure: true,
        auth: {
            user: 'admin@glowteams.com', 
            pass: '@ShopD2CVeri' 
        }
    });
  
    var mail = {
        from: '"' + MAIL_SENDFROM_NAME + '" <' + MAIL_SENDFROM_MAIL + '>',				
        to: data.email,
        subject: data.subject,
        generateTextFromHTML: true,
        text:data.text,

    }

    
    transporter.sendMail(mail, function (error, response1) {
        console.log(error)
        console.log(response1)
        if (error) {
            console.log("error while mail sending", error);
            response = {status: 'error', message: 'Error while mail sending.'};
            // res.send(response);                   		                       
        } else {
            response = {status: 'success', message: 'We sent you instruction for password reset on your registred email address. Please check that.'};
            // res.send(response);
        }
    });
            
  }

  module.exports.createRandomString = (length) => {
        var chars = "abcdefghijklmnopqrstufwxyzABCDEFGHIJKLMNOPQRSTUFWXYZ1234567890"
        var pwd = _.sampleSize(chars, length || 12)  // lodash v4: use _.sampleSize
        return pwd.join("")
    }