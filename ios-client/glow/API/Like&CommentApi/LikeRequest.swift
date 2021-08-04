//
//  LikeRequest.swift
//  glow
//
//  Created by dhruv dhola on 23/08/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import Foundation

class LikeRequest {
    static func saveLike(param: [String: Any], callback: @escaping ((_ status: Bool, _ videos: VideoResponse?, _ error: Error?) -> Void)) {
        Utility.showProgress("")
        Utility.apiCallWithRawData(LikeVideo, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
            if error != nil {
                callback(false,nil,error)
            } else {
                if let responseObj = response as? [String:Any] {
                    let videoResponse = VideoResponse.init(dictionary: responseObj)
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

    static func getLikeVideos(param: [String: Any], callback: @escaping ((_ status: Bool, _ videos: VideoResponse?, _ error: Error?) -> Void)) {
        Utility.showProgress("")
        Utility.apiCall(GetLikeVideo, data: param, filePathKey: [], method: .POST) { (success, response, error) in
            if error != nil {
                callback(false,nil,error)
            } else {
                if let responseObj = response as? [String:Any] {
                    let videoResponse = VideoResponse.init(dictionary: responseObj)
                    if videoResponse.videos != nil {
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
    


    static func removeLike(param: [String: Any], callback: @escaping ((_ status: Bool, _ videos: VideoResponse?, _ error: Error?) -> Void)) {
        
        Utility.apiCallWithRawData(RemoveLike, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
            if error != nil {
                callback(false,nil,error)
            } else {
                if let responseObj = response as? [String:Any] {
                    let videoResponse = VideoResponse.init(dictionary: responseObj)
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
    
    static func subscribeUser(param: [String: Any], callback: @escaping ((_ status: Bool, _ message: String?, _ error: Error?) -> Void)) {
        
        Utility.apiCallWithRawData(SubscribeUser, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
            if error != nil {
                callback(false,nil,error)
            } else {
                if let responseObj = response as? [String:Any] {
                    let message = responseObj["msg"] as? String
                    callback(true, message, nil)
                    
                } else {
                    let error = NSError(domain:"", code:401, userInfo:[ NSLocalizedDescriptionKey: "Invalid"]) as Error
                    callback(false,nil,error)
                }
            }
        }
    }
    
    
    
    static func removeSubscribeUser(param: [String: Any], callback: @escaping ((_ status: Bool, _ message: String?, _ error: Error?) -> Void)) {
        
        Utility.apiCallWithRawData(RemoveSubscribe, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
            
            if error != nil {
                callback(false,nil,error)
            } else {
                if let responseObj = response as? [String:Any] {
                    let message = responseObj["msg"] as? String
                    callback(true, message, nil)
                    
                } else {
                    let error = NSError(domain:"", code:401, userInfo:[ NSLocalizedDescriptionKey: "Invalid"]) as Error
                    callback(false,nil,error)
                }
            }
        }
    }
    
    static func videoView(param: [String: Any], callback: @escaping ((_ status: Bool, _ videos: VideoResponse?, _ error: Error?) -> Void)) {
        
        Utility.apiCallWithRawData(VideoView, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
            if error != nil {
                callback(false,nil,error)
            } else {
                if let responseObj = response as? [String:Any] {
                    let videoResponse = VideoResponse.init(dictionary: responseObj)
                    if videoResponse.isLiked != nil {
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


class CommentRequest {
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

    static func getVideosComment(param: [String: Any], callback: @escaping ((_ status: Bool, _ videos: CommentResponse?, _ error: Error?) -> Void)) {
        Utility.showProgress("")
        Utility.apiCallWithRawData(getCommentOnVideo, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
            if error != nil {
                callback(false,nil,error)
            } else {
                if let responseObj = response as? [String:Any] {
                    let videoResponse = CommentResponse.init(dictionary: responseObj)
                    if videoResponse.status == "success" {
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

    static func removeComment(param: [String: Any], callback: @escaping ((_ status: Bool, _ videos: CommentResponse?, _ error: Error?) -> Void)) {
        
        Utility.apiCallWithRawData(RemoveComment, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
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
    
    static func CommentReplay(param: [String: Any], callback: @escaping ((_ status: Bool, _ videos: CommentResponse?, _ error: Error?) -> Void)) {
        
        Utility.apiCallWithRawData(replyOnComment, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
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
    
    static func removeCommentReplay(param: [String: Any], callback: @escaping ((_ status: Bool, _ videos: CommentResponse?, _ error: Error?) -> Void)) {
        
        Utility.apiCallWithRawData(removeReplyOnComment, data: param, filePathKey: nil, imageDataKey: [], method: .POST) { (success, response, error) in
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
