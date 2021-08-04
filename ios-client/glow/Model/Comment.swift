//
//  Comment.swift
//  glow
//
//  Created by dhruv dhola on 03/10/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import Foundation

class Comment {
    var replay: [CommentReplay]?
    var comment: String?
    var _id: String?
    var userId: UserId?
    var videoId: String?
    var __v: Int?
    
    init(dictionary: [String: Any]) {
        self._id = dictionary["_id"] as? String
        self.__v = dictionary["__v"] as? Int
        if let reply = dictionary["replay"] as? [[String : Any]] {
            self.replay = CommentReplay.getArrayList(array: reply)
        }
        if let userId = dictionary["userId"] as? [String : Any] {
            self.userId = UserId.getInstance(dictionary: userId)
        }
        self.videoId = dictionary["videoId"] as? String
        self.comment = dictionary["comment"] as? String
        
        
    }
    
    init(_id: String?,
         __v: Int?,
         replay: [CommentReplay]?,
         userId: UserId?,
         comment: String?,
         videoId: String){
        
        self._id = _id
        self.__v = __v
        self.replay = replay
        self.userId = userId
        self.comment = comment
        self.videoId = videoId
    }
    class func getArrayList(array: [[String: Any]]) -> [Comment]? {
        var arrayList: [Comment] = []
        for obj in array {
            let response = Comment(dictionary: obj)
            arrayList.append(response)
        }
        return arrayList
    }
    
    func getDictionary() -> [String:Any] {
        var dictionary: [String:Any] = [:]
        dictionary["_id"] = self._id
        dictionary["userId"] = self.userId?.getDictionary()
        dictionary["videoId"] = self.videoId
        dictionary["comment"] = self.comment
        dictionary["__v"] = self.__v
        
        return dictionary
    }
    
    class func getInstance(dictionary: [String:Any]) -> Comment?  {
        let response = Comment(dictionary: dictionary)
        if response._id != nil {
            return response
        }
        return nil
    }
    
}

class CommentReplay {
    var comment: String?
    var _id: String?
    var userId: UserId?
    
    init(dictionary: [String: Any]) {
        self._id = dictionary["_id"] as? String
        if let userId = dictionary["userId"] as? [String:Any] {
            self.userId = UserId.getInstance(dictionary: userId)
        }
        self.comment = dictionary["comment"] as? String
        
    }
    
    init(_id: String?,
         userId: UserId?,
        comment: String?){
        self._id = _id
        self.userId = userId
        self.comment = comment
    }
    class func getArrayList(array: [[String: Any]]) -> [CommentReplay]? {
        var arrayList: [CommentReplay] = []
        for obj in array {
            let response = CommentReplay(dictionary: obj)
            arrayList.append(response)
        }
        return arrayList
    }
    
    func getDictionary() -> [String:Any] {
        var dictionary: [String:Any] = [:]
        dictionary["_id"] = self._id
        dictionary["userId"] = self.userId?.getDictionary()
        dictionary["comment"] = self.comment
        
        return dictionary
    }
    
    class func getInstance(dictionary: [String:Any]) -> CommentReplay?  {
        let response = CommentReplay(dictionary: dictionary)
        if response._id != nil {
            return response
        }
        return nil
    }
    
}

class UserId {
    var _id: String?
    var name: String?
    
    init(dictionary: [String: Any]) {
        self._id = dictionary["_id"] as? String
        self.name = dictionary["name"] as? String
        
    }
    
    init(_id: String?,
         name: String?){
        self._id = _id
        self.name = name
       
    }
    class func getArrayList(array: [[String: Any]]) -> [UserId]? {
        var arrayList: [UserId] = []
        for obj in array {
            let response = UserId(dictionary: obj)
            arrayList.append(response)
        }
        return arrayList
    }
    
    func getDictionary() -> [String:Any] {
        var dictionary: [String:Any] = [:]
        dictionary["_id"] = self._id
        dictionary["name"] = self.name
        
        return dictionary
    }
    
    class func getInstance(dictionary: [String:Any]) -> UserId?  {
        let response = UserId(dictionary: dictionary)
        if response._id != nil {
            return response
        }
        return nil
    }
    
}
