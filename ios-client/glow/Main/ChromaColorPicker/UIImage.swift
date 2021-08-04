//
//  UIImage.swift
//  AnyConverter
//
//  Created by Logileap on 26/03/19.
//  Copyright © 2019 Bhautik. All rights reserved.
//

import Foundation
import UIKit

extension UIImage {
    @objc func copy(alpha: CGFloat) -> UIImage? {
        UIGraphicsBeginImageContextWithOptions(size, false, scale)
        draw(at: CGPoint.zero, blendMode: .normal, alpha: alpha)
        let newImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return newImage
    }
}
