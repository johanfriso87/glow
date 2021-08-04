//
//  MyLabel.swift
//  RateTheRafes
//
//  Created by Logileap on 6/14/19.
//  Copyright © 2019 KMSOFT. All rights reserved.
//

import Foundation
import UIKit

@IBDesignable class RefsLabel: UILabel {
    
    @IBInspectable var fontSize:CGFloat = 12 {
        didSet {
            font = fontToFitHeight()
        }
    }
    override func layoutSubviews() {
        super.layoutSubviews()
        font = fontToFitHeight()
    }
    
    private func fontToFitHeight() -> UIFont {
        return Utility.fontToFitHeight(font: font, fontSize: self.fontSize)
    }
}
class ScaledHeightImageView: UIImageView {
    
    override var intrinsicContentSize: CGSize {
        
        if let myImage = self.image {
            let myImageWidth = myImage.size.width
            let myImageHeight = myImage.size.height
            let myViewWidth = self.frame.size.width
            
            let ratio = myViewWidth/myImageWidth
            let scaledHeight = myImageHeight * ratio
            
            return CGSize(width: myViewWidth, height: scaledHeight)
        }
        return CGSize(width: -1.0, height: -1.0)
    }
}


@IBDesignable class RefsButtonFrame: UIButton {
    @IBInspectable var height:CGFloat = 12 {
        didSet {
            height =  self.setHeight()
        }
    }
    override func layoutSubviews() {
        super.layoutSubviews()
        height =  self.setHeight()
        self.titleLabel?.frame.size.height = height
    }
    private func setHeight() -> CGFloat {
        return self.height / UIScreen.main.bounds.width//self.heightAnchor.constraint(equalTo: self.widthAnchor, multiplier: self.height / UIScreen.main.bounds.width).constant

    }
}

@IBDesignable class RefsButtonFixFrame: UIButton {
//    @IBOutlet weak var refBtnWidth: NSLayoutConstraint!
    @IBInspectable var height:CGFloat = 12
//        {
//        didSet {
//            height =  self.setHeight()
//        }
//    }
    override func layoutSubviews() {
        super.layoutSubviews()
        let f = self.frame
        self.frame =  CGRect(x: f.origin.x, y: f.origin.y, width: self.setHeight(), height: self.setHeight())
//        refBtnWidth.constant = height
    }
    private func setHeight() -> CGFloat {
        
        let defaultScreenWidth: CGFloat = 414
        let width = UIScreen.main.bounds.width * height / defaultScreenWidth
        return width
//        return self.height / UIScreen.main.bounds.width
        
    }
}

@IBDesignable class RefsView: UIView {
    @IBInspectable var height:CGFloat = 12 {
        didSet {
            height =  80//self.setHeight()
        }
    }
    override func layoutSubviews() {
        super.layoutSubviews()
        height =  80//self.setHeight()
    }
    private func setHeight() -> CGFloat {
        return self.height / UIScreen.main.bounds.width//self.heightAnchor.constraint(equalTo: self.widthAnchor, multiplier: self.height / UIScreen.main.bounds.width).constant
        
    }
}
@IBDesignable class RefsConstraint: NSLayoutConstraint {
    @IBInspectable var height: CGFloat = 0.0 {
        didSet {
            changeContraint()
        }
    }
    func changeContraint() {
         self.constant = Utility.getHeightOfView(height: self.height)
    }
}

@IBDesignable class RefsConstraintHeightWidth: NSLayoutConstraint {
    @IBInspectable var height: CGFloat = 0.0 {
        didSet {
            changeContraint(value: self.height)
        }
    }
    
    @IBInspectable var width: CGFloat = 0.0 {
        didSet {
            changeContraint(value: self.width)
        }
    }
    
    func changeContraint(value: CGFloat) {
        self.constant = Utility.getHeightOfView(height: value)
    }
}


@IBDesignable class RefsButton: UIButton {
    
    @IBInspectable var fontSize:CGFloat = 12 {
        didSet {
            self.titleLabel?.font = fontToFitHeight()
        }
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        self.titleLabel?.font = fontToFitHeight()
    }
    
    private func fontToFitHeight() -> UIFont {
        return Utility.fontToFitHeight(font: self.titleLabel!.font, fontSize: self.fontSize)
    }
}

@IBDesignable class RefsTextField: UITextField {
    
    @IBInspectable var fontSize:CGFloat = 12 {
        didSet {
            font = fontToFitHeight()
        }
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        font = fontToFitHeight()
    }
    
    private func fontToFitHeight() -> UIFont {
        return Utility.fontToFitHeight(font: font!, fontSize: self.fontSize)
    }
}

@IBDesignable class RefsTextView: UITextView {
    
    @IBInspectable var fontSize:CGFloat = 12 {
        didSet {
            font = fontToFitHeight()
        }
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        font = fontToFitHeight()
    }
    
    private func fontToFitHeight() -> UIFont {
        return Utility.fontToFitHeight(font: font!, fontSize: self.fontSize)
    }
}
