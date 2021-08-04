//
//  Buttons.swift
//  glow
//
//  Created by Dreams on 27/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit
import LTHRadioButton

class Buttons: UIView {

    @IBOutlet weak var firstOption: LTHRadioButton!
    @IBOutlet weak var secondOption: LTHRadioButton!
    @IBOutlet weak var thirdOption: LTHRadioButton!
    @IBOutlet weak var fourthOption: LTHRadioButton!
    @IBOutlet weak var fifthOption: LTHRadioButton!
    @IBOutlet weak var sixthOption: LTHRadioButton!
    @IBOutlet weak var sevenOption: LTHRadioButton!
    @IBOutlet weak var eightOption: LTHRadioButton!
    @IBOutlet weak var nineOption: LTHRadioButton!
    @IBOutlet weak var tenOption: LTHRadioButton!
    @IBOutlet weak var elevenOption: LTHRadioButton!
    @IBOutlet weak var twelveOption: LTHRadioButton!
    @IBOutlet weak var thirteenOption: LTHRadioButton!
    @IBOutlet weak var fourteenOption: LTHRadioButton!
    
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */
    
}

public extension UIView
{
    static func loadFromXib<T>(withOwner: Any? = nil, options: [UINib.OptionsKey : Any]? = nil) -> T where T: UIView
    {
        let bundle = Bundle(for: self)
        let nib = UINib(nibName: "\(self)", bundle: bundle)

        guard let view = nib.instantiate(withOwner: withOwner, options: options).first as? T else {
            fatalError("Could not load view from nib file.")
        }
        return view
    }
}
