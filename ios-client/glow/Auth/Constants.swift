//
//  Constants.swift
//  glow
//
//  Created by Dreams on 22/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import Foundation
import UIKit

var skinToneColor: String = ""
let sharedWindow = UIApplication.shared.keyWindow
let topPadding = sharedWindow?.safeAreaInsets.top ?? 0
let USER = "USER"
let APPNAME = "glow"
class Constants {
    class Storyboard {
        public static let LOGIN                 =   UIStoryboard(name: "Auth", bundle: nil)
        public static let MAIN                  =   UIStoryboard(name: "Main", bundle: nil)
        public static let PROFILE               =   UIStoryboard(name: "Profile", bundle: nil)
        public static let OUTFIT                =   UIStoryboard(name: "Outfit", bundle: nil)
        public static let FRIEND                =   UIStoryboard(name: "Friend", bundle: nil)
        public static let REWARD                =   UIStoryboard(name: "Reward", bundle: nil)
        public static let REWARDOUTFIT          =   UIStoryboard(name: "RewardOutfit", bundle: nil)
    }
}
let Loggdinuser = UserDefaults.standard

let ACCESSTOKEN       =           "ACCESSTOKEN"
let USERLOGIN         =           "USERLOGIN"
let USERSTORE         =           "USERSTORE"
let USERIMAGE         =           "USERIMAGE"
let USERNAME          =           "USERNAME"
let USEREMAIL         =           "USEREMAIL"

let BASEVIDEOURL      =           "http://13.233.98.117:3333/"
let BASE              =           "http://13.233.98.117:3333/"
let LoginGoogle       =           BASE + "auth/login_with_google"
let LoginFB           =           BASE + "auth/login_with_facebook"
let LoginApple        =           BASE + "auth/login_with_apple"
let GetUser           =           BASE + "profile/get"
let UpdateUser        =           BASE + "profile/update"
let AddProfilePicture =           BASE + "profile/add-profile-picture"
let UploadVideo       =           BASE + "video/upload"
let UserVideos        =           BASE + "video/get_user_video"
let GetDashBoardData  =           BASE + "dashboard/get"
let GetSearchProduct  =          "https://amazon-product-reviews-keywords.p.rapidapi.com/product/search?"
let GetSearchProductFromUrl = "https://amazon-product-reviews-keywords.p.rapidapi.com/product/details?"
let GetStickers = BASE + "stickers/get"
let SignupWithEmail = BASE + "auth/login_with_email"
let SignInWithEmail = BASE + "auth/login_with_email_password"
let OtpVerify = BASE + "auth/verify_otp"
let LikeVideo = BASE + "like/add"
let VideoView = BASE + "video/view"
let GetLikeVideo = BASE + "like/get_liked_video"
let RemoveLike = BASE + "like/remove"
let getCommentOnVideo = BASE + "comment/get"
let commentOnVideo = BASE + "comment/add"
let RemoveComment = BASE + "comment/remove"
let replyOnComment = BASE + "comment/add_replay"
let removeReplyOnComment = BASE + "comment/remove_replay"
let GetSubscription = BASE + "relation/get-favorite-video"
let GetSubscribeUser = BASE + "relation/get-favorite-users"
let SubscribeUser = BASE + "relation/add_to_favorite"
let RemoveSubscribe = BASE + "remove_from_favorite"
let AddToMyshelf = BASE + "product/add_to_myself"
let GetMyshelf = BASE + "product/get_myself_product"
let TargetUserVideo = BASE + "video/get-target-user-video"
let GetReviewProduct = BASE + "product/get_reviews"
let ScanProduct = "https://product-data1.p.rapidapi.com/lookup?upc="
let GetAsin = "https://amazon-price1.p.rapidapi.com/upcToAsin?marketplace=ES&upc="
let ProductSticker = BASE + "stickers/product_stickers_add"

