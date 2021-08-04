//
//  UserRequest.swift
//  What a TV
//
//  Created by Logileap on 21/12/19.
//  Copyright © 2019 What a TV Inc. All rights reserved.
//

import Foundation
class UserRequest {
    
    static func getUser(callback: @escaping ((_ status: Bool, _ createAccount: User?, _ error: Error?) -> Void)) {
        Utility.apiCall(GetUser, param: nil, method: .POST) { (success, response, error) in
            if error != nil {
                callback(success,nil,error)
            } else {
                if let responseObj = response as? [String : Any] {
                    let userObj = LoginResponse.init(dictionary: responseObj)
                    let user = userObj.user
                    callback(success, user, error)
                }
            }
        }
    }
    

    static func getSubscriptions(param: [String: Any], callback: @escaping ((_ status: Bool, _ videos: LoginResponse?, _ error: Error?) -> Void)) {
        Utility.showProgress("")
        Utility.apiCall(GetSubscribeUser, param: nil, method: .POST) { (success, response, error) in
            if error != nil {
                callback(success,nil,error)
            } else {
                if let responseObj = response as? [String : Any] {
                    let obj = LoginResponse.init(dictionary: responseObj)
                    callback(success, obj, error)
                }
            }
        }
    }
    //MARK: - UserLogued
    //    static func userLoguedAPI() {
    //        //Utility.showProgress("")
    //        Utility.apiCall(UpdateUser, param: nil, method: .POST) { (success, response, error) in
    //            //Utility.dismissProgress()
    //            if error != nil {
    //                Utility.alert(message: error!.localizedDescription)
    //            } else {
    //
    //            }
    //        }
    //    }
    
    //MARK: - User Update
    static func userUpdateApi(param: [String: Any],filePathKey: [String],imageDataKey: [Data]? = nil, callback: @escaping ((_ status: Bool, _ user: LoginResponse?, _ error: Error?) -> Void)) {
        Utility.showProgress("")
        Utility.apiCallWithRawData(UpdateUser, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
            if error != nil {
                callback(false,nil,error)
            } else {
                if let responseObj = response as? [String:Any] {
                    let loginResponse = LoginResponse.init(dictionary: responseObj)
                    if loginResponse.user != nil {
                        callback(true, loginResponse, nil)
                    } else {
                        let message = responseObj["msg"] as? String ?? ""
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
    
    static func userProfileUpdate(param: [String: Any],filePathKey: [String],imageDataKey: [Data]? = nil, callback: @escaping ((_ status: Bool, _ user: ProfileResponse?, _ error: Error?) -> Void)) {
        Utility.showProgress("")
        Utility.apiCall(AddProfilePicture, data: param, filePathKey: filePathKey, imageDataKey: imageDataKey, method: .POST) { (success, response, error) in
            if error != nil {
                callback(false,nil,error)
            } else {
                if let responseObj = response as? [String:Any] {
                    let loginResponse = ProfileResponse.init(dictionary: responseObj)
                    if loginResponse.profileImage != nil {
                        callback(true, loginResponse, nil)
                    } else {
                        let message = responseObj["msg"] as? String ?? ""
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
    
    static func userVideos(param: [String: Any],filePathKey: [String],imageDataKey: [Data]? = nil, callback: @escaping ((_ status: Bool, _ videos: VideoResponse?, _ error: Error?) -> Void)) {
        Utility.showProgress("")
        Utility.apiCall(UserVideos, data: param, filePathKey: filePathKey, imageDataKey: imageDataKey, method: .POST) { (success, response, error) in
            if error != nil {
                callback(false,nil,error)
            } else {
                if let responseObj = response as? [String:Any] {
                    let videoResponse = VideoResponse.init(dictionary: responseObj)
                    if videoResponse.videos != nil {
                        callback(true, videoResponse, nil)
                    } else {
                        let message = responseObj["msg"] as? String ?? ""
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
    
    static func targetUserVideos(param: [String: Any],filePathKey: [String],imageDataKey: [Data]? = nil, callback: @escaping ((_ status: Bool, _ videos: VideoResponse?, _ error: Error?) -> Void)) {
        Utility.showProgress("")
        Utility.apiCallWithRawData(TargetUserVideo, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
            if error != nil {
                callback(success,nil,error)
            } else {
                if let responseObj = response as? [String : Any] {
                    let obj = VideoResponse.init(dictionary: responseObj)
                    callback(success, obj, error)
                }
            }
        }
    }
    
    static func getReviewProduct(param: [String: Any],filePathKey: [String],imageDataKey: [Data]? = nil, callback: @escaping ((_ status: Bool, _ videos: VideoResponse?, _ error: Error?) -> Void)) {
        Utility.showProgress("")
        Utility.apiCallWithRawData(GetReviewProduct, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
            if error != nil {
                callback(success,nil,error)
            } else {
                if let responseObj = response as? [String : Any] {
                    let obj = VideoResponse.init(dictionary: responseObj)
                    callback(success, obj, error)
                }
            }
        }
    }
}
