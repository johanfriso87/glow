//
//  AppDelegate.swift
//  supergrate
//
//  Created by Dreams on 20/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit
import FBSDKCoreKit
import FBSDKLoginKit
import Firebase
import GoogleSignIn
import VideoEditorSDK


@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    

    var window: UIWindow?
    
    static var sharedInstance: AppDelegate = {
        let instance = AppDelegate()
        return instance
    }()
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        AppDelegate.sharedInstance = self
        FirebaseApp.configure()
        GIDSignIn.sharedInstance().clientID = FirebaseApp.app()?.options.clientID
        ApplicationDelegate.shared.application(
            application,
            didFinishLaunchingWithOptions: launchOptions
        )
        if let licenseURL = Bundle.main.url(forResource: "license", withExtension: "") {
          VESDK.unlockWithLicense(at: licenseURL)
        }
        if Loggdinuser.value(forKey: USERLOGIN) != nil {
            let loginuser = Loggdinuser.value(forKey: USERLOGIN)as? Bool
            if loginuser == true {
               AppData.sharedInstance.user = UserDefaultHelper.getUser()
               navigateToHomeScreen()
            }
            else {
                navigateToLoginScreen()
            }
        }
        else {
            navigateToLoginScreen()
        }
        // Override point for customization after application launch.
        return true
    }
    
    func navigateToHomeScreen() {
        let storyBoard = UIStoryboard(name: "Main", bundle: nil)
        let vc = storyBoard.instantiateViewController(withIdentifier: "navigation") as? UINavigationController
        AppDelegate.sharedInstance.window?.rootViewController = vc
    }
    
    func navigateToLoginScreen() {
        let storyBoard = UIStoryboard(name: "Auth", bundle: nil)
        if let vc = storyBoard.instantiateViewController(withIdentifier: "AuthNavigation") as? UINavigationController {
        AppDelegate.sharedInstance.window?.rootViewController = vc
        }
    }
    
    
    
    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        print("appurl:-\(url)")
        ApplicationDelegate.shared.application(
            app,
            open: url,
            sourceApplication: options[UIApplication.OpenURLOptionsKey.sourceApplication] as? String,
            annotation: options[UIApplication.OpenURLOptionsKey.annotation]
        )
        
        return ApplicationDelegate.shared.application(app,
                                                      open: url,
                                                      sourceApplication: options[.sourceApplication] as? String,
                                                      annotation: options[.annotation])
        
        return GIDSignIn.sharedInstance().handle(url)
        
    }

}

