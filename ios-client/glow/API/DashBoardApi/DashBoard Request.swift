//
//  DashBoard Request.swift
//  glow
//
//  Created by Dreams on 10/07/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import Foundation

class DashBoardRequest {
    static func getData(callback: @escaping ((_ status: Bool, _ dashBoard: Dashboard?, _ error: Error?) -> Void)) {
        Utility.apiCall(GetDashBoardData, param: nil, method: .POST) { (success, response, error) in
            if error != nil {
                callback(success,nil,error)
            } else {
                if let responseObj = response as? [String : Any] {
                    let dashobj = DashBoardResponse.init(dictionary: responseObj)
                    let dash = dashobj.data
                    callback(success, dash, error)
                }
            }
        }
    }
    
    static func saveComment(param: [String: Any], callback: @escaping ((_ status: Bool, _ videos: CommentResponse?, _ error: Error?) -> Void)) {
        Utility.showProgress("")
        Utility.apiCallWithRawData(commentOnVideo, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
            if error != nil {
                callback(false,nil,error)
            } else {
                if let responseObj = response as? [String:Any] {
                    let videoResponse = CommentResponse.init(dictionary: responseObj)
                    if videoResponse.status != nil {
                        callback(true, videoResponse, nil)
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
