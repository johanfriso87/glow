//
//  ShelfRequest.swift
//  glow
//
//  Created by dhruv dhola on 01/11/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import Foundation
class ShelfRequest {
    static func getShelfData(callback: @escaping ((_ status: Bool, _ shelf: [MyShelf]?, _ error: Error?) -> Void)) {
        Utility.apiCall(GetMyshelf, param: nil, method: .POST) { (success, response, error) in
            if error != nil {
                callback(success,nil,error)
            } else {
                if let responseObj = response as? [String : Any] {
                    let obj = ShelfResponse.init(dictionary: responseObj)
                    let dash = obj.data
                    callback(success, dash, error)
                }
            }
        }
    }
    
    static func addToShelf(param: [String: Any], callback: @escaping ((_ status: Bool, _ videos: ShelfResponse?, _ error: Error?) -> Void)) {
        Utility.showProgress("")
        Utility.apiCallWithRawData(AddToMyshelf, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
            if error != nil {
                callback(false,nil,error)
            } else {
                if let responseObj = response as? [String:Any] {
                    let shelfResponse = ShelfResponse.init(dictionary: responseObj)
                    if shelfResponse.status != nil {
                        callback(true, shelfResponse, nil)
                    } else {
                        let message = responseObj["msg"] as? String ?? ""
                        let error = NSError(domain:"", code:401, userInfo:[ NSLocalizedDescriptionKey: message]) as Error
                        callback(false,nil,error)
                    }
                    
                } else {
                    let error = NSError(domain:"", code:401, userInfo:[ NSLocalizedDescriptionKey: "Invalid"]) as Error
                    callback(false,nil,error)
                }
            }
        }
    }
}
