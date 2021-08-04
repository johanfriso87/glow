//
//  SkinCareTableViewCell.swift
//  glow
//
//  Created by Dreams on 27/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit
import LTHRadioButton

class SkinCareTableViewCell: UITableViewCell {

    @IBOutlet weak var baseView: UIView!
    @IBOutlet weak var acneButton: LTHRadioButton!
    @IBOutlet weak var blackHeadButton: LTHRadioButton!
    @IBOutlet weak var cuticlesButton: LTHRadioButton!
    @IBOutlet weak var dullnessButton: LTHRadioButton!
    @IBOutlet weak var puffinesButton: LTHRadioButton!
    @IBOutlet weak var sensitivityButton: LTHRadioButton!
    @IBOutlet weak var sunDamageButton: LTHRadioButton!
    @IBOutlet weak var agingButton: LTHRadioButton!
    @IBOutlet weak var celluliteButton: LTHRadioButton!
    @IBOutlet weak var darkCirclesButton: LTHRadioButton!
    @IBOutlet weak var poresButton: LTHRadioButton!
    @IBOutlet weak var rednessButton: LTHRadioButton!
    @IBOutlet weak var strechMarksButton: LTHRadioButton!
    @IBOutlet weak var unevenTonesButton: LTHRadioButton!
    @IBOutlet weak var acneButtonLabel: UILabel!
    @IBOutlet weak var blackHeadButtonLabel: UILabel!
    @IBOutlet weak var cuticlesButtonLabel: UILabel!
    @IBOutlet weak var dullnessButtonLabel: UILabel!
    @IBOutlet weak var puffinesButtonLabel: UILabel!
    @IBOutlet weak var sensitivityButtonLabel: UILabel!
    @IBOutlet weak var sunDamagButtonLabel: UILabel!
    @IBOutlet weak var agingButtonLabel: UILabel!
    @IBOutlet weak var celluliteButtonLabel: UILabel!
    @IBOutlet weak var darkCirclesButtonLabel: UILabel!
    @IBOutlet weak var poresButtonLabel: UILabel!
    @IBOutlet weak var rednessButtonLabel: UILabel!
    @IBOutlet weak var strechMarksButtonLabel: UILabel!
    @IBOutlet weak var unevenTonesButtonLabel: UILabel!
    
    var skinCareList: [String] = []

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        acneButton.selectedColor = .black
        blackHeadButton.selectedColor = .black
        cuticlesButton.selectedColor = .black
        dullnessButton.selectedColor = .black
        puffinesButton.selectedColor = .black
        sensitivityButton.selectedColor = .black
        sunDamageButton.selectedColor = .black
        agingButton.selectedColor = .black
        celluliteButton.selectedColor = .black
        darkCirclesButton.selectedColor = .black
        poresButton.selectedColor = .black
        rednessButton.selectedColor = .black
        strechMarksButton.selectedColor = .black
        unevenTonesButton.selectedColor = .black
        
        acneButton.onSelect {
            self.skinCareList.append("acne")
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        acneButton.onDeselect {
            self.skinCareList.removeAll { $0 == "acne" }
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        
        blackHeadButton.onSelect {
            self.skinCareList.append("blackheads")
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        blackHeadButton.onDeselect {
            self.skinCareList.removeAll { $0 == "blackheads" }
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        cuticlesButton.onSelect {
            self.skinCareList.append("cuticles")
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        cuticlesButton.onDeselect {
            self.skinCareList.removeAll { $0 == "cuticles" }
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        
        dullnessButton.onSelect {
            self.skinCareList.append("dullness")
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        dullnessButton.onDeselect {
             self.skinCareList.removeAll { $0 == "dullness" }
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        puffinesButton.onSelect {
            self.skinCareList.append("puffiness")
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        puffinesButton.onDeselect {
            self.skinCareList.removeAll { $0 == "pufiness" }
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        
        sensitivityButton.onSelect {
            self.skinCareList.append("sensitivity")
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        sensitivityButton.onDeselect {
             self.skinCareList.removeAll { $0 == "sensitivity" }
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        sunDamageButton.onSelect {
            self.skinCareList.append("sun damage")
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        sunDamageButton.onDeselect {
             self.skinCareList.removeAll { $0 == "sun damage" }
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        
        agingButton.onSelect {
            self.skinCareList.append("aging")
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        agingButton.onDeselect {
             self.skinCareList.removeAll { $0 == "aging" }
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        celluliteButton.onSelect {
            self.skinCareList.append("cellulite")
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        celluliteButton.onDeselect {
             self.skinCareList.removeAll { $0 == "cellulite" }
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        
        darkCirclesButton.onSelect {
            self.skinCareList.append("dark circles")
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        darkCirclesButton.onDeselect {
             self.skinCareList.removeAll { $0 == "dark circles" }
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        
        rednessButton.onSelect {
            self.skinCareList.append("redness")
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        rednessButton.onDeselect {
            self.skinCareList.removeAll { $0 == "redness" }
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        
        poresButton.onSelect {
            self.skinCareList.append("pores")
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        poresButton.onDeselect {
            self.skinCareList.removeAll { $0 == "pores" }
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        strechMarksButton.onSelect {
            self.skinCareList.append("strech marks")
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        strechMarksButton.onDeselect {
            self.skinCareList.removeAll { $0 == "strech marks" }
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        
        unevenTonesButton.onSelect {
            self.skinCareList.append("uneven tones")
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
        unevenTonesButton.onDeselect {
            self.skinCareList.removeAll { $0 == "uneven tones" }
            NotificationCenter.default.post(name: Notification.Name("SkinCare"), object: self.skinCareList as AnyObject)
        }
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
    }

    deinit {
        NotificationCenter.default.removeObserver(self)
    }

}
