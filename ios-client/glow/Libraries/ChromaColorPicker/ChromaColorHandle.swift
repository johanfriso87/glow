//
//  ChromaColorHandle.swift
//  ChromaColorPicker
//
//  Created by Jon Cardasis on 4/11/19.
//  Copyright © 2019 Jonathan Cardasis. All rights reserved.
//

import UIKit
//internal extension UIView {
//    
//    /// Forces the view to layout synchronously and immediately
//    func layoutNow() {
//        setNeedsLayout()
//        layoutIfNeeded()
//    }
//}

public class ChromaColorHandle: UIView, ChromaControlStylable {
    public var borderWidth: CGFloat = 3.0 {
        didSet { layoutNow() }
    }
    
    public var borderColor: UIColor = .white {
        didSet { layoutNow() }
    }
    
    public var showsShadow: Bool = true {
        didSet { layoutNow() }
    }
    func updateShadowIfNeeded() {
        
    }
    
    
       open var color = UIColor.black {
        didSet{
            circleLayer.fillColor = color.cgColor
        }
    }
    override open var frame: CGRect{
        didSet { self.layoutCircleLayer() }
    }
    open var circleLayer = CAShapeLayer()
    
    open var shadowOffset: CGSize?{
        set{
            if let offset = newValue {
                circleLayer.shadowColor = UIColor.black.cgColor
                circleLayer.shadowRadius = 3
                circleLayer.shadowOpacity = 0.3
                circleLayer.shadowOffset = offset
            }
        }
        get{
            return circleLayer.shadowOffset
        }
    }
    
    override public init(frame: CGRect) {
        super.init(frame:frame)
        self.backgroundColor = UIColor.clear
        
        /* Add Shape Layer */
        //circleLayer.shouldRasterize = true
        self.layoutCircleLayer()
        circleLayer.strokeColor = UIColor.white.cgColor
        circleLayer.fillColor = color.cgColor
        
        self.layer.addSublayer(circleLayer)
    }
    
    open func layoutCircleLayer(){
        circleLayer.path = UIBezierPath(ovalIn: self.bounds).cgPath
        circleLayer.strokeColor = UIColor.white.cgColor
        circleLayer.lineWidth = frame.width/8.75 //4
    }
    
    required public init(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

}
