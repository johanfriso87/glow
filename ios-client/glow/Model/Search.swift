//
//  Search.swift
//  glow
//
//  Created by Dreams on 26/07/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import Foundation
class Search {
    
    var asin: String?
    var price: String?
    var title: String?
    var thumbnail: String?
    var url: String?
    var images: [String]?
   // var sticker: String?

    init(dictionary: [String: Any]) {
        
        self.asin = dictionary["asin"] as? String
        self.price = dictionary["price"] as? String
        self.title = dictionary["title"] as? String
        self.thumbnail = dictionary["thumbnail"] as? String
        self.url = dictionary["url"] as? String
        let tempObj = dictionary["images"] as? [String] ?? []
        self.images = []
        for i in tempObj {
            self.images?.append(i)
        }
        
        
    }
    
    init(asin: String,
         price: String,
         title: String,
         thumbnail: String,
         url: String,
         images: [String])
                  {
        self.asin = asin
        self.price = price
        self.title = title
        self.thumbnail = thumbnail
        self.url = url
        self.images = images
       
    }
    class func getArrayList(array: [[String: Any]]) -> [Search]? {
        var arrayList: [Search] = []
        for obj in array {
            let response = Search(dictionary: obj)
            arrayList.append(response)
        }
        return arrayList
    }
    
    func getDictionary() -> [String:Any] {
        var dictionary: [String:Any] = [:]
        dictionary["asin"] = self.asin
        dictionary["price"] = self.price
        dictionary["title"] = self.title
        dictionary["thumbnail"] = self.thumbnail
        dictionary["url"] = self.url
        dictionary["images"] = self.images
        return dictionary
    }
    
    class func getInstance(dictionary: [String:Any]) -> Search?  {
        let response = Search(dictionary: dictionary)
//        if response. != nil {
            return response
//        }
       // return nil
    }
    
}

class StickersList {
    
    var id: String?
    var url: String?
   
   // var sticker: String?

    init(dictionary: [String: Any]) {
        self.id = dictionary["_id"] as? String
        self.url = dictionary["url"] as? String
    }
    
    init(id: String,
         url: String)
                  {
        self.id = id
        self.url = url
       
    }
    class func getArrayList(array: [[String: Any]]) -> [StickersList]? {
        var arrayList: [StickersList] = []
        for obj in array {
            let response = StickersList(dictionary: obj)
            arrayList.append(response)
        }
        return arrayList
    }
    
    func getDictionary() -> [String:Any] {
        var dictionary: [String:Any] = [:]
        dictionary["_id"] = self.id
        dictionary["url"] = self.url
        return dictionary
    }
    
    class func getInstance(dictionary: [String:Any]) -> StickersList?  {
        let response = StickersList(dictionary: dictionary)
//        if response. != nil {
            return response
//        }
       // return nil
    }
    
}
