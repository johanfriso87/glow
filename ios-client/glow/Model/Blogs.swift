//
//  Blogs.swift
//  glow
//
//  Created by Dreams on 15/08/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import Foundation
class Blogs {
    var title: String?
    var picture: String?
    var content: String?
    var views: Int?
    var _id: String?
    
    
    init(dictionary: [String: Any]) {
        self._id = dictionary["id"] as? String
        self.title = dictionary["title"] as? String
        self.content = dictionary["content"] as? String
        self.views = dictionary["views"] as? Int
        self.picture = dictionary["picture"] as? String
        
    }
    
    init(_id: String?,
         title: String?,
         content: String?,
         views: Int?,
         picture: String?){
        
        self._id = _id
        self.title = title
        self.content = content
        self.views = views
        self.picture = picture
    }
    class func getArrayList(array: [[String: Any]]) -> [Blogs]? {
        var arrayList: [Blogs] = []
        for obj in array {
            let response = Blogs(dictionary: obj)
            arrayList.append(response)
        }
        return arrayList
    }
    
    func getDictionary() -> [String:Any] {
        var dictionary: [String:Any] = [:]
        dictionary["_id"] = self._id
        dictionary["title"] = self.title
        dictionary["picture"] = self.picture
        dictionary["views"] = self.views
        dictionary["content"] = self.content
        return dictionary
    }
    
    class func getInstance(dictionary: [String:Any]) -> Blogs?  {
        let response = Blogs(dictionary: dictionary)
        if response._id != nil {
            return response
        }
        return nil
    }
    
}
