//
//  LikeResponse.swift
//  glow
//
//  Created by dhruv dhola on 23/08/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import Foundation

class CommentResponse {
    
    
    var status: String?
    var data: [Comment]?
    var msg: String?
    var isLiked: Bool?
    
    init(dictionary: [String: Any]) {
        self.status = dictionary["status"] as? String
        if let obj = dictionary["data"] as? [[String: Any]] {
            self.data = Comment.getArrayList(array: obj)
        }
        self.msg = dictionary["msg"] as? String
        self.isLiked = dictionary["isLiked"] as? Bool
        
    }
    
    class func getInstance(dictionary: [String: Any]) -> CommentResponse? {
        let response = CommentResponse(dictionary: dictionary)
        if response.status == "success" {
            return response
        }
        return nil
    }
    
    
}
