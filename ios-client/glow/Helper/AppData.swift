//
//  AppData.swift
//  What a TV
//
//  Created by Logileap on 23/10/19.
//  Copyright © 2019 What a TV Inc. All rights reserved.
//

import Foundation

class AppData {
    //var sceneDelegate: SceneDelegate!
    var appDelegate: AppDelegate!
    var user: User!
    
    static let sharedInstance: AppData = {
        let instance = AppData()
        return instance
    }()
}
