//
//  LibraryRequest.swift
//  What a TV
//
//  Created by KMSOFT on 07/11/19.
//  Copyright © 2019 What a TV Inc. All rights reserved.
//

import Foundation

class AuthRequest {
    var email: String?
    var firstname: String?
    var lastname: String?
    var birthdate: String?
    var password: String?
    var password_confirmed: String?
    var token: String?
    
    init(email: String = "",
         firstname: String = "", lastname: String = "", birthdate: String = "", password: String = "", password_confirmed: String = "", token: String = "") {
        self.email = email
        self.firstname = firstname
        self.lastname = lastname
        self.birthdate = birthdate
        self.password = password
        self.password_confirmed = password_confirmed
        self.token = token
    }
    
    func getDictionary() -> [String: Any] {
        var dictionary: [String: Any] = [:]
        dictionary["email"] = self.email
        dictionary["firstname"] = self.firstname
        dictionary["lastname"] = self.lastname
        dictionary["birthdate"] = self.birthdate
        dictionary["password"] = self.password
        dictionary["password_confirmed"] = self.password_confirmed
        dictionary["token"] = self.token
        
        return dictionary
    }
    //MARK: - Login
    static func loginAPI(param: [String: Any], loginIndex: Int = 0,callback: @escaping ((_ status: Bool, _ User: User?, _ error: Error?) -> Void)) {
        Utility.showProgress("")
        var loginApi = ""
        switch loginIndex {
        case 0:
            loginApi = LoginGoogle
            break;
        case 1:
            loginApi = LoginFB
            break;
        case 2:
            loginApi = LoginApple
        default:
            break;
        }
        Utility.apiCallWithRawData(loginApi, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
            if error != nil {
                callback(false,nil,error)
            } else {
                if let responseObj = response as? [String:Any] {
                    if !responseObj.isEmpty {
                        let errorobj = responseObj["errors"] as? [String: Any]
                        if errorobj != nil {
                            let password = errorobj?["password"] as? [String] ?? []
                            let passwordMessage = password[0]
                            let error = NSError(domain:"", code:401, userInfo:[ NSLocalizedDescriptionKey: passwordMessage]) as Error
                            callback(false, nil, error)
                        }
                        else {
                            let loginuser = LoginResponse.getInstance(dictionary: responseObj)
                            if loginuser != nil {
                                callback(true, loginuser!.user, nil)
                            } else {
                                //let errorObj = responseObj["error"] as? [String: Any]
                                let message = responseObj["message"] as? String ?? ""
                                let error = NSError(domain:"", code:401, userInfo:[ NSLocalizedDescriptionKey: message]) as Error
                                callback(false,nil,error)
                            }
                        }
                    }
                    else {
                        let errorObj = responseObj["error"] as? [String: Any]
                        let message = errorObj?["error"] as? String ?? ""
                        let error = NSError(domain:"", code:401, userInfo:[ NSLocalizedDescriptionKey: message]) as Error
                        callback(false,nil,error)
                    }
                } else {
                    let error = NSError(domain:"", code:401, userInfo:[ NSLocalizedDescriptionKey: "Invalid"]) as Error
                    callback(false,nil,error)
                }
            }
        }
    }
    
    static func signUpWithEmail(param: [String: Any], loginIndex: Int = 0,callback: @escaping ((_ status: Bool, _ msg: String?, _ error: Error?) -> Void)) {
        // Utility.showProgress("")
        
        Utility.apiCallWithRawData(SignupWithEmail, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
            if error != nil {
                callback(false,nil,error)
            } else {
                if error != nil {
                    callback(false,nil,error)
                } else {
                    if let responseObj = response as? [String : Any] {
                        let loginuser = LoginResponse.getInstance(dictionary: responseObj)
                        if loginuser != nil {
                            callback(true, loginuser?.msg, nil)
                        } else {
                            callback(false,nil,error)
                        }
                    }
                }
            }
        }
    }
    
    static func signInWithEmail(param: [String: Any], loginIndex: Int = 0,callback: @escaping ((_ status: Bool, _ user: LoginResponse?, _ error: Error?) -> Void)) {
        // Utility.showProgress("")
        
        Utility.apiCallWithRawData(SignInWithEmail, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
            if error != nil {
                callback(false,nil,error)
            } else {
                if let responseObj = response as? [String : Any] {
                    let loginuser = LoginResponse.getInstance(dictionary: responseObj)
                    if loginuser != nil {
                        callback(true, loginuser, nil)
                    } else {
                        callback(false,nil,error)
                    }
                }
            }
        }
    }


static func otpVerify(param: [String: Any], loginIndex: Int = 0,callback: @escaping ((_ status: Bool, _ user: LoginResponse?, _ error: Error?) -> Void)) {
   
    
    Utility.apiCallWithRawData(OtpVerify, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
        if error != nil {
            callback(false,nil,error)
        } else {
            if error != nil {
                callback(false,nil,error)
            } else {
                if let responseObj = response as? [String : Any] {
                    let loginuser = LoginResponse.getInstance(dictionary: responseObj)
                    if loginuser != nil {
                        callback(true, loginuser, nil)
                    } else {
                        callback(false,nil,error)
                    }
                }
            }
        }
    }
}

}
