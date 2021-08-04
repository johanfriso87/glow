//
//  Videos.swift
//  glow
//
//  Created by Dreams on 02/07/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import Foundation

class Videos {
    var _id: String?
    var videoUrl: String?
    var thumbnailUrl: String?
    var productThumbnailUrl: String?
    var asin: String?
    var productName: String?
    var productUrl: String?
    var userId: Any?
    var product: Search?
    var __v: Int?
    var gif: String?
    
    init(dictionary: [String: Any]) {
        self._id = dictionary["_id"] as? String
        self.videoUrl = dictionary["videoUrl"] as? String
        self.thumbnailUrl = dictionary["thumbnailUrl"] as? String
        self.productThumbnailUrl = dictionary["productThumbnailUrl"] as? String
        self.asin = dictionary["asin"] as? String
        self.productName = dictionary["productName"] as? String
        self.productUrl = dictionary["productUrl"] as? String
        self.userId = dictionary["userId"] as? Any
        self.product = Search.getInstance(dictionary: dictionary["productId"] as? [String : Any] ?? [:])
        self.__v = dictionary["__v"] as? Int
        self.gif = dictionary["gif"] as? String
        
    }
    
    init(_id: String?,
         videoUrl: String?,
         __v: Int?,
         product: Search?,
         thumbnailUrl: String?,
         productThumbnailUrl: String?,
         asin: String?,
         productName: String?,
         productUrl: String?,
         userId: Any,
         gif: String){
        
        self._id = _id
        self.videoUrl = videoUrl
        self.thumbnailUrl = thumbnailUrl
        self.productThumbnailUrl = productThumbnailUrl
        self.asin = asin
        self.productName = productName
        self.productUrl = productUrl
        self.__v = __v
        self.product = product
        self.userId = userId
        self.gif = gif
       
    }
    class func getArrayList(array: [[String: Any]]) -> [Videos]? {
        var arrayList: [Videos] = []
        for obj in array {
            let response = Videos(dictionary: obj)
            arrayList.append(response)
        }
        return arrayList
    }
    
    func getDictionary() -> [String:Any] {
        var dictionary: [String:Any] = [:]
        dictionary["_id"] = self._id
        dictionary["videoUrl"] = self.videoUrl
        dictionary["userId"] = self.userId
        dictionary["__v"] = self.__v
        dictionary["productId"] = product?.getDictionary()
        dictionary["thumbnailUrl"] = self.thumbnailUrl
        dictionary["productThumbnailUrl"] = self.productThumbnailUrl
        dictionary["productName"] = self.productName
        dictionary["productUrl"] = self.productUrl
        dictionary["asin"] = self.asin
        dictionary["gif"] = self.gif
        
        return dictionary
    }
    
    class func getInstance(dictionary: [String:Any]) -> Videos?  {
        let response = Videos(dictionary: dictionary)
        if response._id != nil {
            return response
        }
        return nil
    }
    
}

class ContacList {
    var number: String?
    var name: String?

    init(dictionary: [String: Any]) {
        self.number = dictionary["_id"] as? String
        self.name = dictionary["videoUrl"] as? String
    }

    init(number: String?,
         name: String?){
        
        self.number = number
        self.name = name
    }
    
    class func getArrayList(array: [[String: Any]]) -> [ContacList]? {
        var arrayList: [ContacList] = []
        for obj in array {
            let response = ContacList(dictionary: obj)
            arrayList.append(response)
        }
        return arrayList
    }
    
    func getDictionary() -> [String:Any] {
        var dictionary: [String:Any] = [:]
        dictionary["_id"] = self.number
        dictionary["videoUrl"] = self.name
        return dictionary
    }
    
    class func getInstance(dictionary: [String:Any]) -> ContacList?  {
        let response = ContacList(dictionary: dictionary)
        if response.number != nil {
            return response
        }
        return nil
    }
    
}


