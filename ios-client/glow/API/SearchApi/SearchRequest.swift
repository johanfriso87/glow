//
//  SearchRequest.swift
//  glow
//
//  Created by Dreams on 26/07/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import Foundation
class SearchRequest {
    static func getData(param: [String:Any],
                        callback: @escaping ((_ status: Bool, _ dashBoard: [Search]?, _ error: Error?) -> Void)) {
        Utility.apiCallWithRawData(GetSearchProduct, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
            if error != nil {
                callback(success,nil,error)
            } else {
                if let responseObj = response as? [String : Any] {
                    let dashobj = SearchResponse.init(dictionary: responseObj)
                    let dash = dashobj.data
                    callback(success, dash, error)
                }
            }
        }
    }
    
    static func searchProduct(param: [String:Any],
                        callback: @escaping ((_ status: Bool, _ dashBoard: SearchResponse?, _ error: Error?) -> Void)) {
        Utility.searchApiCall(GetSearchProduct, param: param, method: .GET) { (success, response, error) in
            if error != nil {
                callback(success,nil,error)
            } else {
                if let responseObj = response as? [String : Any] {
                   let dashobj = SearchResponse.init(dictionary: responseObj)
                        let dash = dashobj
                        callback(success, dash, error)
                }
            }
        }
    }
    
    static func upcToAsin(param: [String:Any],
                        callback: @escaping ((_ status: Bool, _ dashBoard: SearchResponse?, _ error: Error?) -> Void)) {
        Utility.searchApiCall(GetSearchProduct, param: param, method: .GET) { (success, response, error) in
            if error != nil {
                callback(success,nil,error)
            } else {
                if let responseObj = response as? [String : Any] {
                   let dashobj = SearchResponse.init(dictionary: responseObj)
                        let dash = dashobj
                        callback(success, dash, error)
                }
            }
        }
    }
    
    
    static func searchProductFromUrl(param: [String:Any],
                        callback: @escaping ((_ status: Bool, _ dashBoard: Search?, _ error: Error?) -> Void)) {
        Utility.searchApiCall(GetSearchProductFromUrl, param: param, method: .GET) { (success, response, error) in
            if error != nil {
                callback(success,nil,error)
            } else {
                if let responseObj = response as? [String : Any] {
                    let dashobj = SearchResponse.init(dictionary: responseObj)
                    let dash = dashobj.search
                    callback(success, dash, error)
                }
            }
        }
    }
    
    static func searchProductFromBarcode(param: String,
                        callback: @escaping ((_ status: Bool, _ dashBoard: Search?, _ error: Error?) -> Void)) {
        Utility.searchScanApiCall(ScanProduct + param, param: [:], method: .GET) { (success, response, error) in
            if error != nil {
                callback(success,nil,error)
            } else {
                if let responseObj = response as? [String : Any] {
                    let dashobj = SearchResponse.init(dictionary: responseObj)
                    let dash = dashobj.search
                    callback(success, dash, error)
                }
            }
        }
    }
    
    static func searchProductForAsin(param: String,
                        callback: @escaping ((_ status: Bool, _ dashBoard: Any?, _ error: Error?) -> Void)) {
        Utility.searchScanFirstApiCall(GetAsin + param, param: [:], method: .GET) { (success, response, error) in
            if error != nil {
                callback(success,nil,error)
            } else {
                callback(success, response, error)
            }
        }
    }
}


class StickerRequest {
    static func getStickers(param: [String:Any],
                        callback: @escaping ((_ status: Bool, _ dashBoard: [StickersList]?, _ error: Error?) -> Void)) {
        Utility.apiCallWithRawData(GetStickers, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
            if error != nil {
                callback(success,nil,error)
            } else {
                if let responseObj = response as? [String : Any] {
                    let dashobj = StickerResponse.init(dictionary: responseObj)
                    let dash = dashobj.data
                    callback(success, dash, error)
                }
            }
        }
    }
   
    static func getProductStickers(param: [String:Any],
                                   callback: @escaping ((_ status: Bool, _ response: [String: Any]?, _ error: Error?) -> Void)) {
        Utility.apiCallWithRawData(ProductSticker, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
            if error != nil {
                callback(success,nil,error)
            } else {
                if let responseObj = response as? [String : Any] {
                   
                    callback(success, responseObj, error)
                }
            }
        }
    }
    
}
