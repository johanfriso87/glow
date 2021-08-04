//
//  LoginResponse.swift
//  What a TV
//
//  Created by KMSOFT on 08/11/19.
//  Copyright © 2019 What a TV Inc. All rights reserved.
//

import Foundation
class LoginResponse {
    
    var status: String?
    var users: [User]?
    var user: User?
    var access_token: String?
    var msg: String?
    
    init(dictionary: [String: Any]) {
        self.status = dictionary["status"] as? String
        if let obj = dictionary["data"] as? [[String: Any]] {
            self.users = User.getArrayList(array: obj)
        }
        if let obj = dictionary["data"] as? [String: Any] {
            if !obj.isEmpty {
                self.user = User(dictionary: obj)
            }
        }
//        self.msg = dictionary["msg"] as? String
        self.msg = dictionary["msg"] as? String
       // self.access_token = dictionary["access_token"] as? String
        
    }
    
    class func getInstance(dictionary: [String: Any]) -> LoginResponse? {
        let response = LoginResponse(dictionary: dictionary)
        if response.status == "success" {
            let token = response.access_token
            Loggdinuser.set(token, forKey: ACCESSTOKEN)
            return response
        } else if response.status == "error" {
            return response
        }
        return nil
    }
    
    
}


class ProfileResponse {
    
    var status: String?
    var profileImage: String?
    var msg: String?
    
    init(dictionary: [String: Any]) {
        self.status = dictionary["status"] as? String
        
        if let obj = dictionary["data"] as? [String: Any] {
            self.profileImage = obj["profilePic"] as? String
        }
       // self.access_token = dictionary["access_token"] as? String
        
    }
    
    class func getInstance(dictionary: [String: Any]) -> ProfileResponse? {
        let response = ProfileResponse(dictionary: dictionary)
        if response.status == "success" {
            return response
        }
        return nil
    }
    
    
}

class VideoResponse {
    
    var status: String?
    var videos: [Videos]?
    //var user: User?
    var msg: String?
    var isLiked: Bool?
    
    init(dictionary: [String: Any]) {
        self.status = dictionary["status"] as? String
        if let obj = dictionary["data"] as? [[String: Any]] {
            self.videos = Videos.getArrayList(array: obj)
        }
//        if let obj = dictionary["data"] as? [String: Any] {
//            self.user = User(dictionary: obj)
//        }
        self.msg = dictionary["msg"] as? String
        self.isLiked = dictionary["isLiked"] as? Bool
        
    }
    
    class func getInstance(dictionary: [String: Any]) -> VideoResponse? {
        let response = VideoResponse(dictionary: dictionary)
        if response.status == "success" {
            return response
        }
        return nil
    }
    
    
}
