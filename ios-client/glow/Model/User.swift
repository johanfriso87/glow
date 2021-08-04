//
//  Model.swift
//  What a TV
//
//  Created by Logileap on 22/10/19.
//  Copyright © 2019 What a TV Inc. All rights reserved.
//

import Foundation

class User {
    var _id: String?
    var name: String?
    var profilePic: String?
    var userName: String?
    var email: String?
    var about: String?
    var website: String?
    var skinCareInterests: [String]?
    var hairCareInterests: [String]?
    var appleId: String?
    var googleId: String?
    var facebookId: String?
    var phone: Bool?
    var isVerified: String?
    var status : Int?
    var __v: Int?
    var token: String?
    var userType: String?
    var birthDate: String?
    var eyeColor: String?
    var hairColor: String?
    var hairTexture: String?
    var hairType: String?
    var skinTone: String?
    var skinType: String?
    var videos: [Videos]?
    
    init(dictionary: [String: Any]) {
        self._id = dictionary["_id"] as? String
        self.profilePic = dictionary["profilePic"] as? String
        self.email = dictionary["email"] as? String
        self.name = dictionary["name"] as? String
        self.userName = dictionary["userName"] as? String
         self.about = dictionary["about"] as? String
        self.website = dictionary["website"] as? String
        self.skinCareInterests = dictionary["skinCareInterests"] as? [String]
        self.hairCareInterests = dictionary["hairCareInterests"] as? [String]
        self.appleId = dictionary["appleId"] as? String ?? ""
        self.googleId = dictionary["googleId"] as? String
        self.facebookId = dictionary["facebookId"] as? String
        self.phone = dictionary["phone"] as? Bool
        self.isVerified = dictionary["isVerified"] as? String
        self.status = dictionary["status"] as? Int
        self.__v = dictionary["__v"] as? Int
        self.token = dictionary["token"] as? String
        self.userType = dictionary["userType"] as? String
        self.birthDate = dictionary["birthDate"] as? String
        self.eyeColor = dictionary["eyeColor"] as? String
        self.hairColor = dictionary["hairColor"] as? String
        self.hairTexture = dictionary["hairTexture"] as? String
        self.hairType = dictionary["hairType"] as? String
        self.skinTone = dictionary["skinTone"] as? String
        self.skinType =  dictionary["skinType"] as? String
        if let videosObj = dictionary["videos"] as? [[String:Any]] {
            self.videos = Videos.getArrayList(array: videosObj)
        }
    }
    
    init(_id: String?,
         name: String?,
         profilePic: String?,
         userName: String?,
         email: String?,
         about: String?,
         website: String?,
         skinCareInterests: [String]?,
         hairCareInterests: [String]?,
         appleId: String?,
         googleId: String?,
         facebookId: String?,
         phone: Bool?,
         isVerified: String?,
         status : Int?,
         __v: Int?,
         token: String?,
         userType: String?,
         birthDate: String?,
         eyeColor: String?,
         hairColor: String?,
         hairTexture: String?,
         hairType: String?,
         skinTone: String?,
         skinType: String?,
         videos: [Videos]?){
        
        self._id = _id
        self.name = name
        self.profilePic = profilePic
        self.email = email
        self.userName = userName
        self.about = about
        self.website = website
        self.skinCareInterests = skinCareInterests
        self.hairCareInterests = hairCareInterests
        self.appleId = appleId
        self.googleId = googleId
        self.facebookId = facebookId
        self.phone = phone
        self.isVerified = isVerified
        self.status = status
        self.__v = __v
        self.token = token
        self.userType = userType
        self.birthDate = birthDate
        self.eyeColor = eyeColor
        self.hairType = hairType
        self.hairColor = hairColor
        self.hairTexture = hairTexture
        self.skinType = skinType
        self.skinTone = skinTone
        self.videos = videos
    }
    class func getArrayList(array: [[String: Any]]) -> [User]? {
        var arrayList: [User] = []
        for obj in array {
            let response = User(dictionary: obj)
            arrayList.append(response)
        }
        return arrayList
    }
    
    func getDictionary() -> [String:Any] {
        var dictionary: [String:Any] = [:]
        dictionary["_id"] = self._id
        dictionary["email"] = self.email
        dictionary["profilePic"] = self.profilePic
        dictionary["name"] = self.name
        dictionary["userName"] = self.userName
         dictionary["about"] = self.about
        dictionary["website"] = self.website
        dictionary["skinCareInterests"] = self.skinCareInterests
        dictionary["hairCareInterests"] = self.hairCareInterests
        dictionary["googleId"] = self.googleId
        dictionary["appleId"] = self.appleId
        dictionary["facebookId"] = self.facebookId
        dictionary["isVerified"] = self.isVerified
        dictionary["token"] = self.token
        dictionary["__v"] = self.__v
        dictionary["status"] = self.status
        dictionary["userType"] = self.userType
        dictionary["phone"] = self.phone
        dictionary["birthDate"] = self.birthDate
        dictionary["eyeColor"] = self.eyeColor
        dictionary["hairColor"] = self.hairColor
        dictionary["hairType"] = self.hairType
        dictionary["hairTexture"] = self.hairTexture
        dictionary["skinTone"] = self.skinTone
        dictionary["skinType"] = self.skinType
        
        return dictionary
    }
    
    class func getInstance(dictionary: [String:Any]) -> User?  {
        let response = User(dictionary: dictionary)
        if response._id != nil {
            return response
        }
        return nil
    }
    
}
