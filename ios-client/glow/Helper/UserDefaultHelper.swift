//
//  UserDefaultHelper.swift
//  What a TV
//
//  Created by KMSOFT on 09/11/19.
//  Copyright © 2019 What a TV Inc. All rights reserved.
//

import Foundation

class UserDefaultHelper {
    fileprivate static let userDefault = UserDefaults.standard
    static func saveUser(user: User) {
        let dictionary = user.getDictionary()
        userDefault.set(dictionary, forKey: USER)
        userDefault.synchronize()
    }
    
    static func getUser() -> User? {
        if let userDictionary: [String: Any] = userDefault.value(forKey: USER) as? [String: Any] {
            return User.getInstance(dictionary: userDictionary)
        }
        return nil
    }
    
    
    static func removeUser() {
        userDefault.removeObject(forKey: USER)
        AppData.sharedInstance.user = nil
    }
    
}

