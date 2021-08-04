//
//  MyShelf.swift
//  glow
//
//  Created by dhruv dhola on 01/11/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import Foundation

class MyShelf {
    var asin: String?
    var title: String?
    var url: String?
    var images: [String]?
    var brand: String?
    var userId: String?
    var _id: String?
    
    
    init(dictionary: [String: Any]) {
        self.asin = dictionary["asin"] as? String
        self._id = dictionary["_id"] as? String
        self.title = dictionary["title"] as? String
        self.url = dictionary["url"] as? String
        self.images = []
        if let imgArry = dictionary["images"] as? [String] {
            for i in imgArry {
                  self.images?.append(i)
            }
        }
        self.brand = dictionary["brand"] as? String
        self.userId = dictionary["userId"] as? String
    }
    
    init(_id: String?,
         title: String?,
         url: String?,
         images: [String]?,
         brand: String?,
         userId: String?,
         asin: String?){
        
        self._id = _id
        self.title = title
        self.url = url
        self.images = images
        self.brand = brand
        self.userId = userId
        self.asin = asin
    }
    class func getArrayList(array: [[String: Any]]) -> [MyShelf]? {
        var arrayList: [MyShelf] = []
        for obj in array {
            let response = MyShelf(dictionary: obj)
            arrayList.append(response)
        }
        return arrayList
    }
    
    func getDictionary() -> [String:Any] {
        var dictionary: [String:Any] = [:]
        dictionary["_id"] = self._id
        dictionary["title"] = self.title
        dictionary["url"] = self.url
        dictionary["images"] = self.images
        dictionary["brand"] = self.brand
        dictionary["userId"] = self.userId
        dictionary["asin"] = self.asin
        return dictionary
    }
    
    class func getInstance(dictionary: [String:Any]) -> MyShelf?  {
        let response = MyShelf(dictionary: dictionary)
        if response._id != nil {
            return response
        }
        return nil
    }
    
}
