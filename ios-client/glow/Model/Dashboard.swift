//
//  Dashbord.swift
//  glow
//
//  Created by Dreams on 10/07/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import Foundation

class Dashboard {
    var uniqueVideos:[Videos]?
    var videos: [Videos]?
    var users: [User]?
    var blogs: [Blogs]?

    init(dictionary: [String: Any]) {
        if let uniqueVideosObj = dictionary["uniqueVideos"] as? [[String:Any]] {
            self.uniqueVideos = Videos.getArrayList(array: uniqueVideosObj)
        }
        if let videosObj = dictionary["videos"] as? [[String:Any]] {
            self.videos = Videos.getArrayList(array: videosObj)
        }
        if let usersObj = dictionary["users"] as? [[String:Any]] {
            self.users = User.getArrayList(array: usersObj)
        }
        if let blogObj = dictionary["blogs"] as? [[String:Any]] {
            self.blogs = Blogs.getArrayList(array: blogObj)
        }
        
    }
    
    init(videos: [Videos],
         users: [User],
         blogs:[Blogs]){
        self.videos = videos
        self.users = users
        self.blogs = blogs
    }
    class func getArrayList(array: [[String: Any]]) -> [Dashboard]? {
        var arrayList: [Dashboard] = []
        for obj in array {
            let response = Dashboard(dictionary: obj)
            arrayList.append(response)
        }
        return arrayList
    }
    
    func getDictionary() -> [String:Any] {
        var dictionary: [String:Any] = [:]
        dictionary["videos"] = videos
        dictionary["users"] = users
        dictionary["blogs"] = blogs
        return dictionary
    }
    
    class func getInstance(dictionary: [String:Any]) -> Dashboard?  {
        let response = Dashboard(dictionary: dictionary)
//        if response. != nil {
            return response
//        }
       // return nil
    }
    
}
