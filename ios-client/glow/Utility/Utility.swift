//
//  Utility.swift
//  What a TV
//
//  Created by Logileap on 22/10/19.
//  Copyright © 2019 What a TV Inc. All rights reserved.
//

import Foundation
import UIKit
import Reachability
import SVProgressHUD
import Photos

class Utility {
    class func getDateFromTimeStamp(timeStamp : Double) -> String {
        let date = Date(timeIntervalSince1970: timeStamp)
        let dayTimePeriodFormatter = DateFormatter()
        dayTimePeriodFormatter.dateFormat = "MM-dd-yy HH:mm"
        let dateString = dayTimePeriodFormatter.string(from: date as Date)
        return dateString
    }
    
    class func getDateFromTimeStampForReport(timeStamp : Double) -> String {
        let date = Date(timeIntervalSince1970: timeStamp)
        let dayTimePeriodFormatter = DateFormatter()
        dayTimePeriodFormatter.dateFormat = "HH:mm:ss"
        let dateString = dayTimePeriodFormatter.string(from: date as Date)
        return dateString
    }
    class func getDateTimestamp(timeStamp : Double) -> String {
        let date = Date(timeIntervalSince1970: timeStamp)
        let dayTimePeriodFormatter = DateFormatter()
        dayTimePeriodFormatter.dateFormat = "MM/dd/yyyy"
        let dateString = dayTimePeriodFormatter.string(from: date as Date)
        return dateString
    }
    class func getDate(format: String, timeStamp : Double) -> String {
        let date = Date(timeIntervalSince1970: timeStamp)
        let dayTimePeriodFormatter = DateFormatter()
        dayTimePeriodFormatter.dateFormat = format
        let dateString = dayTimePeriodFormatter.string(from: date as Date)
        return dateString
    }
    
    class func fontToFitHeight(font: UIFont, fontSize: CGFloat) -> UIFont {
           let defaultScreenWidth: CGFloat = 375
           let fontSize: CGFloat = fontSize
           let fontSizeAverage = UIScreen.main.bounds.width * fontSize / defaultScreenWidth
           return font.withSize(fontSizeAverage)
       }
    
    class func fontNameChange(font: UIFont) -> UIFont {
       
        
         return font
    }
       
       class func getHeightOfView(height: CGFloat) -> CGFloat {
           let defaultScreenWidth: CGFloat = 375
           let width = UIScreen.main.bounds.width * height / defaultScreenWidth
           return width
       }
       
       class func getResize(size: CGFloat) -> CGFloat {
           let defaultScreenWidth: CGFloat = 375
           let fontSizeAverage = UIScreen.main.bounds.width * size / defaultScreenWidth
           return fontSizeAverage
       }
    
    class func getDirectoryPath() -> String {
        
        let paths = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)
        let documentsDirectory = paths[0]
        return documentsDirectory + "/"
        
    }

    
     class func saveThumbnailLocally(id: String, image: UIImage) {
         let fileManager = FileManager.default
         do {
             let documentDirectory = try fileManager.url(for: .documentDirectory, in: .userDomainMask, appropriateFor:nil, create:false)
             let fileURL = documentDirectory.appendingPathComponent(id)
             if let imageData = image.jpegData(compressionQuality: 0.5) {
                if Utility.self.getThumbnailFromLocally(id: id) != nil {
                     try fileManager.removeItem(at: fileURL)
                 }
                 try imageData.write(to: fileURL)
                 //return true
             }
         } catch {
             print(error)
         }
     }
    
    
     
  class func getThumbnailFromLocally(id: String) -> UIImage?{
         // var paths = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0] as String
         let getImagePath =  (self.getDirectoryPath() as NSString).appendingPathComponent(id)
         if let image = UIImage(contentsOfFile: getImagePath) {
             return image
         }
         return nil
     }
     
     func getDirectoryPath() -> String {
         let paths = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)
         let documentsDirectory = paths[0]
         return documentsDirectory
     }
    
    //MARK: - SVProgressHUD
    class func showProgress(_ message: String = "") {
           SVProgressHUD.setDefaultMaskType(SVProgressHUDMaskType.black)
           if(message == "") {
               SVProgressHUD.show()
           }
           else {
               SVProgressHUD.show(withStatus: message)
           }
       }
       class func dismissProgress() {
           SVProgressHUD.dismiss()
       }
    
    //MARK:- email validation
    class func stringMatchedREGEX(_ input: String, RegexStr: String) -> Bool {
        let myTest = NSPredicate(format: "SELF MATCHES %@", RegexStr)
        if myTest.evaluate(with: input) {
            return true
        }
        else {
            return false
        }
    }
    
    class func alert(message: String, title: String? = nil, button1: String, button2: String? = nil, button3: String? = nil, action:@escaping (Int)->())
    {
        DispatchQueue.main.async {
            let alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertController.Style.alert)
            let action1 = UIAlertAction(title: button1, style: .default) { _ in
                action(0)
            }
            alert.addAction(action1)
            if button2 != nil {
                let action2 = UIAlertAction(title: button2, style: .default) { _ in
                    action(1)
                }
                alert.addAction(action2)
            }
            
            if button3 != nil {
                let action3 = UIAlertAction(title: button3, style: .default) { _ in
                    action(2)
                }
                alert.addAction(action3)
            }
            alert.show()
        }
    }
    class func alert(message: String, title: String? = APPNAME) {
           DispatchQueue.main.async {
               let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
            let action = UIAlertAction(title: "OK", style: .cancel, handler: nil)
               alert.addAction(action)
               alert.show()
           }
       }
    
    class func checkInternet() -> Bool {
        let networkReachability: Reachability = try! Reachability()

        if (networkReachability.connection != .unavailable) {
            return true
        } else {
            return false
        }
        
    }
    
   class func convertToDictionary(text: String) -> [String: Any]? {
        if let data = text.data(using: .utf8) {
            do {
                return try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any] ?? [:]
            } catch {
                print(error.localizedDescription)
            }
        }
        return nil
    }
    
    class func generateBoundaryString() -> String {
        return "Boundary-\(UUID().uuidString)"
    }
    
    //MARK: - rendomstring
    class func randomString(length: Int = 10) -> String {
        let letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return String((0..<length).map{ _ in letters.randomElement()! })
    }
    
    class func apiCallWithRawData(_ urlString: String, data: [String: Any]?, filePathKey: String?, imageDataKey: [Data], method: HTTPMethod,callback: ((_ success: Bool, _ data: Any?, _ error: Error?) -> Void)?) {
        if Utility.checkInternet() {
            var url: URL! = URL(string: urlString)!
            
            var query = ""
            if(data != nil) {
                let parameters = data!.keys
                var i = 0
                for parameter in parameters {
                    query += "\(parameter)=\(data![parameter]!)&"
                    i += 1
                }
                if(i > 0) {
                    _ = query.removeLast()
                }
            }
            url = URL(string: urlString)
            print("url:-\(url)")
            
            if url == nil {
                return
            }
            var request = URLRequest(url: url!, cachePolicy: .reloadIgnoringCacheData
                , timeoutInterval: 0)
            request.httpMethod = method.rawValue
            
            // let boundary = AppData.sharedInstance.utility.generateBoundaryString()
            request.httpMethod = method.rawValue
            request.setValue("application/json; charset=UTF-8", forHTTPHeaderField: "Content-Type")
            request.addValue(TimeZone.current.identifier , forHTTPHeaderField: "timeZone")
            // let body = NSMutableData();
            
            let jsonData = try? JSONSerialization.data(withJSONObject: data!)
            request.httpBody = jsonData
            if let acessToken = Loggdinuser.value(forKey: ACCESSTOKEN)as? String {
                let Bearer = "Bearer " + acessToken
                request.setValue(Bearer, forHTTPHeaderField: "Authorization")
            }
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            config.timeoutIntervalForResource = 60
            let session = URLSession(configuration: config)
            let task = session.dataTask(with: request, completionHandler: { (data: Data?, response: URLResponse?, error: Error?) in
                DispatchQueue.main.async(execute: {
                    if(error == nil){
                        if let d = try? JSONSerialization.jsonObject(with: data!, options: []) {
                            if let responseObj = d as? [String:Any] {
                                callback?(true, responseObj, nil)
                            }else{
                                callback?(true, d, nil)
                            }
                            
                        }
                        else {
                            let message = data != nil ? String(data: data!, encoding: .utf8) ?? "" :  ""
                            let error = NSError(domain: message, code: 0, userInfo: nil)
                            callback?(false, nil, error)
                        }
                    }
                    else {
                        callback?(false, nil, error)
                    }
                })
            })
            task.resume()
        } else {
            //                Utility.dismissProgress()
            //                Utility.alert(message: "Check your network connection.")
            print("Check your network connection.")
            //            Utility.showAlert("Error", "Check your network connection.", viewController: self)
        }
    }
    
    // MARK : Search Scan API Call
    class func searchScanFirstApiCall(_ urlString: String, param: [String: Any]?, method: HTTPMethod, callback: ((_ success: Bool, _ data: Any?, _ error: Error?) -> Void)?) {
        let data: [String: Any] = param ?? [:]
        
        if Utility.checkInternet() {
            var url: URL! = URL(string: urlString)!
            var query = ""
            if(data != nil) {
                let parameters = data.keys
                var i = 0
                for parameter in parameters {
                    query += "\(parameter)=\(data[parameter]!)&"
                    i += 1
                }
                if(i > 0) {
                    _ = query.removeLast()
                }
            }
            if(method == .GET) {
                url = URL(string: urlString + query.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!)!
            }
            var request = URLRequest(url: url!)
            request.httpMethod = method.rawValue
            if(method == .PUT || method == .POST) {
                request.httpBody =  query.data(using: .utf8) ?? Data()
            }
            request.addValue("amazon-price1.p.rapidapi.com", forHTTPHeaderField: "x-rapidapi-host")
            request.addValue("e2d8634ee1msh7520587499e31d3p1767c1jsnea1a994c8cb3", forHTTPHeaderField: "x-rapidapi-key")
            request.addValue("true", forHTTPHeaderField: "useQueryString")
            
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            config.timeoutIntervalForResource = 60
            
            let session = URLSession(configuration: config)
            print("URL: ", url!)
            print("Data: ", data)
            let task = session.dataTask(with: request, completionHandler: { (data: Data?, response: URLResponse?, error: Error?) in
                print("End: \(Date()), \(url.absoluteString)")
                DispatchQueue.main.async(execute: {
                    if(error == nil){
                        if let d = try? JSONSerialization.jsonObject(with: data!, options: []) {
                            callback?(true, d, nil)
                        }
                        else {
                            let message = data != nil ? String(data: data!, encoding: .utf8) ?? "" : ""
                            let error = NSError(domain: message, code: 0, userInfo: nil)
                            callback?(false, nil, error)
                        }
                    }
                    else {
                        callback?(false, nil, error)
                    }
                })
            })
            task.resume()
        } else {
            Utility.dismissProgress()

        }
    }
    
    
    
    
    
    class func searchScanApiCall(_ urlString: String, param: [String: Any]?, method: HTTPMethod, callback: ((_ success: Bool, _ data: Any?, _ error: Error?) -> Void)?) {
        let data: [String: Any] = param ?? [:]
        
        if Utility.checkInternet() {
            var url: URL! = URL(string: urlString)!
            var query = ""
            if(data != nil) {
                let parameters = data.keys
                var i = 0
                for parameter in parameters {
                    query += "\(parameter)=\(data[parameter]!)&"
                    i += 1
                }
                if(i > 0) {
                    _ = query.removeLast()
                }
            }
            if(method == .GET) {
                url = URL(string: urlString + query.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!)!
            }
            var request = URLRequest(url: url!)
            request.httpMethod = method.rawValue
            if(method == .PUT || method == .POST) {
                request.httpBody =  query.data(using: .utf8) ?? Data()
            }
            request.addValue("product-data1.p.rapidapi.com", forHTTPHeaderField: "x-rapidapi-host")
            
            request.addValue("e2d8634ee1msh7520587499e31d3p1767c1jsnea1a994c8cb3", forHTTPHeaderField: "x-rapidapi-key")
            request.addValue("true", forHTTPHeaderField: "useQueryString")
            
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            config.timeoutIntervalForResource = 60
            
            let session = URLSession(configuration: config)
            print("URL: ", url!)
            print("Data: ", data)
            let task = session.dataTask(with: request, completionHandler: { (data: Data?, response: URLResponse?, error: Error?) in
                print("End: \(Date()), \(url.absoluteString)")
                DispatchQueue.main.async(execute: {
                    if(error == nil){
                        if let d = try? JSONSerialization.jsonObject(with: data!, options: []) {
                            callback?(true, d, nil)
                        }
                        else {
                            let message = data != nil ? String(data: data!, encoding: .utf8) ?? "" : ""
                            let error = NSError(domain: message, code: 0, userInfo: nil)
                            callback?(false, nil, error)
                        }
                    }
                    else {
                        callback?(false, nil, error)
                    }
                })
            })
            task.resume()
        } else {
            Utility.dismissProgress()

        }
    }
    
    // MARK : Search API Call
    class func searchApiCall(_ urlString: String, param: [String: Any]?, method: HTTPMethod, callback: ((_ success: Bool, _ data: Any?, _ error: Error?) -> Void)?) {
        let data: [String: Any] = param ?? [:]
        
        if Utility.checkInternet() {
            var url: URL! = URL(string: urlString)!
            var query = ""
            if(data != nil) {
                let parameters = data.keys
                var i = 0
                for parameter in parameters {
                    query += "\(parameter)=\(data[parameter]!)&"
                    i += 1
                }
                if(i > 0) {
                    _ = query.removeLast()
                }
            }
            if(method == .GET) {
                url = URL(string: urlString + query.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!)!
            }
            var request = URLRequest(url: url!)
            request.httpMethod = method.rawValue
            if(method == .PUT || method == .POST) {
                request.httpBody =  query.data(using: .utf8) ?? Data()
            }
            request.addValue("amazon-product-reviews-keywords.p.rapidapi.com", forHTTPHeaderField: "x-rapidapi-host")
            request.addValue("e2d8634ee1msh7520587499e31d3p1767c1jsnea1a994c8cb3", forHTTPHeaderField: "x-rapidapi-key")
            request.addValue("true", forHTTPHeaderField: "useQueryString")
            
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            config.timeoutIntervalForResource = 60
            
            let session = URLSession(configuration: config)
            print("URL: ", url!)
            print("Data: ", data)
            let task = session.dataTask(with: request, completionHandler: { (data: Data?, response: URLResponse?, error: Error?) in
                print("End: \(Date()), \(url.absoluteString)")
                DispatchQueue.main.async(execute: {
                    if(error == nil){
                        if let d = try? JSONSerialization.jsonObject(with: data!, options: []) {
                            callback?(true, d, nil)
                        }
                        else {
                            let message = data != nil ? String(data: data!, encoding: .utf8) ?? "" : ""
                            let error = NSError(domain: message, code: 0, userInfo: nil)
                            callback?(false, nil, error)
                        }
                    }
                    else {
                        callback?(false, nil, error)
                    }
                })
            })
            task.resume()
        } else {
            Utility.dismissProgress()

        }
    }
    
    
    class func apiCall(_ urlString: String, param: [String: Any]?, method: HTTPMethod, callback: ((_ success: Bool, _ data: Any?, _ error: Error?) -> Void)?) {
        let data: [String: Any] = param ?? [:]
        
        if Utility.checkInternet() {
            var url: URL! = URL(string: urlString)!
            var query = ""
            if(data != nil) {
                let parameters = data.keys
                var i = 0
                for parameter in parameters {
                    query += "\(parameter)=\(data[parameter]!)&"
                    i += 1
                }
                if(i > 0) {
                    _ = query.removeLast()
                }
            }
            if(method == .GET) {
                url = URL(string: urlString + query.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!)!
            }
            var request = URLRequest(url: url!)
            request.httpMethod = method.rawValue
            if(method == .PUT || method == .POST) {
                request.httpBody =  query.data(using: .utf8) ?? Data()
            }
            
            if let acessToken = Loggdinuser.value(forKey: ACCESSTOKEN)as? String {
                let Bearer = "Bearer " + acessToken
                request.setValue(Bearer, forHTTPHeaderField: "Authorization")
            }
            
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            config.timeoutIntervalForResource = 60
            
            let session = URLSession(configuration: config)
            print("URL: ", url!)
            print("Data: ", data)
            let task = session.dataTask(with: request, completionHandler: { (data: Data?, response: URLResponse?, error: Error?) in
                print("End: \(Date()), \(url.absoluteString)")
                DispatchQueue.main.async(execute: {
                    if(error == nil){
                        if let d = try? JSONSerialization.jsonObject(with: data!, options: []) {
                            callback?(true, d, nil)
                        }
                        else {
                            let message = data != nil ? String(data: data!, encoding: .utf8) ?? "" : ""
                            let error = NSError(domain: message, code: 0, userInfo: nil)
                            callback?(false, nil, error)
                        }
                    }
                    else {
                        callback?(false, nil, error)
                    }
                })
            })
            task.resume()
        } else {
            Utility.dismissProgress()
            Utility.alert(message: "Internet connection not avaliable")
        }
    }
    
   // MARK: file upload
     class func apiCall(_ urlString: String, data: [String: Any]?, filePathKey: [String]?, imageDataKey: [Data]? = nil, method: HTTPMethod, isNeedToSave: Bool = false, isOfflineCall: Bool = false ,callback: ((_ success: Bool, _ data: Any?, _ error: Error?) -> Void)?) {
         //if Utility.checkInternet() {
             var url: URL! = URL(string: urlString)!

             var query = ""
             if(data != nil) {
                 let parameters = data!.keys
                 var i = 0
                 for parameter in parameters {
                     query += "\(parameter)=\(data![parameter]!)&"
                     i += 1
                 }
                 if(i > 0) {
                     _ = query.removeLast()
                 }
             }
            if method == .GET {
                url = URL(string: urlString + query.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!)!
            }
            else {
                url = URL(string: urlString)!
            }

             if url == nil {
                 return
             }
             var request = URLRequest(url: url!, cachePolicy: .reloadIgnoringCacheData
                 , timeoutInterval: 0)
             request.httpMethod = method.rawValue

            let boundary = generateBoundaryString()
            request.httpMethod = method.rawValue
            request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
            
          
            if let acessToken = Loggdinuser.value(forKey: ACCESSTOKEN)as? String {
                let Bearer = "Bearer " + acessToken
                request.setValue(Bearer, forHTTPHeaderField: "Authorization")
            }
            print("URL: ", url!)
        print("Data: " , data as Any)
            request.httpBody =  Utility.createImageBodyWithParameters(parameters: data, filePathKey: filePathKey?.first, imageDataKey: imageDataKey?.first as NSData?, boundary: boundary) as Data
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            config.timeoutIntervalForResource = 60
            let session = URLSession(configuration: config)
            let task = session.dataTask(with: request, completionHandler: { (data: Data?, response: URLResponse?, error: Error?) in
                DispatchQueue.main.async(execute: {
                    if(error == nil){
                        if let d = try? JSONSerialization.jsonObject(with: data!, options: []) {
                            callback?(true, d, nil)
                        }
                        else {
                            let message = data != nil ? String(data: data!, encoding: .utf8) ?? "" :  ""
                            let error = NSError(domain: message, code: 0, userInfo: nil)
                            callback?(false, nil, error)
                        }
                    }
                    else {
                        callback?(false, nil, error)
                    }
                })
            })
            task.resume()
//         } else {
//            Utility.dismissProgress()
//            Utility.alert(message: "Check your network connection")
//            print("Check your network connection.")
//            //          Utility.showAlert("Error", "Check your network connection.", viewController: self)
//        }
    }
    class func createThumbnailOfVideoFromFileURL(_ strVideoURL: String) -> UIImage?{
        let asset = AVAsset(url: URL(string: strVideoURL)!)
        let assetImgGenerate = AVAssetImageGenerator(asset: asset)
        assetImgGenerate.appliesPreferredTrackTransform = true
        let time = CMTimeMakeWithSeconds(Float64(1), preferredTimescale: 100)
        do {
            let img = try assetImgGenerate.copyCGImage(at: time, actualTime: nil)
            let thumbnail = UIImage(cgImage: img)
            return thumbnail
        } catch {
            /* error handling here */
        }
        return nil
    }
    class func createBodyWithParameters(parameters: [String: Any]?, filePathKey: String?, videoDataKey: Data?,imagePathKey: String?, imageDataKey: Data?,boundary: String) -> NSData {
        let body = NSMutableData();
         
        if parameters != nil {
          for (key, value) in parameters! {
            body.appendString("--\(boundary)\r\n")
            body.appendString("Content-Disposition: form-data; name=\"\(key)\"\r\n\r\n")
            body.appendString("\(value)\r\n")
          }
        }
         
        let filename = Utility.randomString()
        let mimetype = "mp4"
        if videoDataKey != nil {
        body.appendString("--\(boundary)\r\n")
        body.appendString("Content-Disposition: form-data; name=\"\(filePathKey!)\"; filename=\"\(filename).mp4\"\r\n")
        body.appendString("Content-Type: \(mimetype)\r\n\r\n")
        
        body.append(videoDataKey!)
        body.appendString("\r\n")
         
        }
        //let imageFileName = Utility.randomString()
        let mimeImagetype = "jpg"
        if imageDataKey != nil {
        body.appendString("--\(boundary)\r\n")
        body.appendString("Content-Disposition: form-data; name=\"\(imagePathKey!)\"; filename=\"\(filename).jpg\"\r\n")
        body.appendString("Content-Type: \(mimeImagetype)\r\n\r\n")
        
        body.append(videoDataKey!)
        body.appendString("\r\n")
         
        }
         
        body.appendString("--\(boundary)--\r\n")
         
        return body
      }
      
    
    class func createImageBodyWithParameters(parameters: [String: Any]?, filePathKey: String?, imageDataKey: NSData? = nil, boundary: String) -> NSData {
           let body = NSMutableData();
            
           if parameters != nil {
             for (key, value) in parameters! {
               body.appendString("--\(boundary)\r\n")
               body.appendString("Content-Disposition: form-data; name=\"\(key)\"\r\n\r\n")
               body.appendString("\(value)\r\n")
             }
           }
        if imageDataKey != nil {
            let filename = Utility.randomString()
            let mimetype = "image/jpg"
            
            
            body.appendString("--\(boundary)\r\n")
            body.appendString("Content-Disposition: form-data; name=\"\(filePathKey!)\"; filename=\"\(filename).jpg\"\r\n")
            body.appendString("Content-Type: \(mimetype)\r\n\r\n")
            body.append(imageDataKey! as Data)
            body.appendString("\r\n")
            
            body.appendString("--\(boundary)--\r\n")
        }
           return body
         }
}

private var kAlertControllerWindow = "kAlertControllerWindow"
extension UIAlertController {
    
    var alertWindow: UIWindow? {
        get {
            return objc_getAssociatedObject(self, &kAlertControllerWindow) as? UIWindow
        }
        set {
            objc_setAssociatedObject(self, &kAlertControllerWindow, newValue, .OBJC_ASSOCIATION_RETAIN)
        }
    }
    
    func show() {
        show(animated: true)
    }
    
    func show(animated: Bool) {
        alertWindow = UIWindow(frame: UIScreen.main.bounds)
        alertWindow?.rootViewController = UIViewController()
        alertWindow?.windowLevel = UIWindow.Level.alert + 1
        alertWindow?.makeKeyAndVisible()
        alertWindow?.rootViewController?.present(self, animated: animated, completion: nil)
    }
    
    open override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        alertWindow?.isHidden = true
        alertWindow = nil
    }
}
class VideoUploader: NSObject, URLSessionDelegate, URLSessionTaskDelegate, URLSessionDataDelegate {
    var progressCallback:((_ success: Bool,_ progress: Float, _ videoObj: Any?)-> Void)?
    var shareprogressCallback:((_ success: Bool,_ progress: Float, _ videoObj: Any?)-> Void)?
    override init() {
        
    }

        func apiCall(_ urlString: String,
                 data: [String: Any]?,
                 filePathKey: [String]?,
                 videoDataKey: Data?,
                 method: HTTPMethod,
                 imagePathKey: String?,
                 imageDataKey: Data?,
                 isNeedToSave: Bool = false,
                 isOfflineCall: Bool = false ,
                 videoListObj: Any?,
                 callback: ((_ success: Bool, _ data: Any?, _ error: Error?) -> Void)?) {
//         if Utility.checkInternet() {
            
            //AppData.sharedInstance.videoList = videoListObj
            
             var url: URL! = URL(string: urlString)!

             var query = ""
             if(data != nil) {
                 let parameters = data!.keys
                 var i = 0
                 for parameter in parameters {
                     query += "\(parameter)=\(data![parameter]!)&"
                     i += 1
                 }
                 if(i > 0) {
                     _ = query.removeLast()
                 }
             }
             url = URL(string: urlString)!
//             let queryObj = query.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!

             if url == nil {
                 return
             }
             var request = URLRequest(url: url!, cachePolicy: .reloadIgnoringCacheData
                 , timeoutInterval: 0)
             request.httpMethod = method.rawValue

            let boundary = Utility.generateBoundaryString()
            request.httpMethod = method.rawValue
            request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")

            if let acessToken = Loggdinuser.value(forKey: ACCESSTOKEN)as? String {
                let Bearer = "Bearer " + acessToken
                request.setValue(Bearer, forHTTPHeaderField: "Authorization")
            }
          
        request.httpBody = Utility.createBodyWithParameters(parameters: data, filePathKey: filePathKey?.first, videoDataKey: videoDataKey ?? nil,imagePathKey: imagePathKey ,imageDataKey: imageDataKey,boundary: boundary) as Data
            print("U URL: ", url!)
            print("U Data: ", data!)
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 180
            config.timeoutIntervalForResource = 180
            let session = URLSession(configuration: config, delegate: self, delegateQueue: nil)
            
            let task1 = session.uploadTask(with: request, from: request.httpBody) { (data, reponse, error) in
                DispatchQueue.main.async(execute: {
                    if(error == nil){
                        if let d = try? JSONSerialization.jsonObject(with: data!, options: []) {
                            callback?(true, d, nil)
                        }
                        else {
                            let message = data != nil ? String(data: data!, encoding: .utf8) ?? "" :  ""
                            let error = NSError(domain: message, code: 0, userInfo: nil)
                            callback?(false, nil, error)
                        }
                    }
                    else {
                        callback?(false, nil, error)
                    }
                })
            }
            task1.resume()
//         } else {
//            Utility.dismissProgress()
//            Utility.alert(message: "Check your network connection")
//            print("Check your network connection.")
//            //          Utility.showAlert("Error", "Check your network connection.", viewController: self)
//        }
    }
    
    
    func urlSession(_ session: URLSession, task: URLSessionTask, didSendBodyData bytesSent: Int64, totalBytesSent: Int64, totalBytesExpectedToSend: Int64) {
        let uploadProgress:Float = Float(totalBytesSent) / Float(totalBytesExpectedToSend)
//        let progressPercent = Int(uploadProgress*100)
//        print("Progress: \(progressPercent)")
        self.shareprogressCallback?(true,uploadProgress,nil)
        self.progressCallback?(true,uploadProgress,  nil)
    }
    
    
    
    
    func urlSession(_ session: URLSession, task: URLSessionTask, didCompleteWithError error: Error?) {
        print("didCompleteWithError")
//          print("Progress: 0")
        self.progressCallback?(false,0, nil)
    }

    func urlSession(_ session: URLSession, dataTask: URLSessionDataTask, didReceive data: Data) {
    }
       
}
