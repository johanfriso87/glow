//
//  DashBoard Response.swift
//  glow
//
//  Created by Dreams on 10/07/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import Foundation
class DashBoardResponse {
    
    var status: String?
    var data: Dashboard?
    var msg: String?
    
    init(dictionary: [String: Any]) {
        self.status = dictionary["status"] as? String
//        if let obj = dictionary["data"] as? [[String: Any]] {
//            //self.data = Dashboard.getArrayList(array: obj)
//        }
        if let obj = dictionary["data"] as? [String: Any] {
            self.data = Dashboard(dictionary: obj)
        }
        self.msg = dictionary["msg"] as? String
        
    }
    
//    class func getInstance(dictionary: [String: Any]) -> LoginResponse? {
//        let response = LoginResponse(dictionary: dictionary)
//        if response.status == "success" {
//            let token = response.access_token
//            Loggdinuser.set(token, forKey: ACCESSTOKEN)
//            return response
//        }
//        return nil
//    }
    
    
}
