//
//  HairCareTableViewCell.swift
//  glow
//
//  Created by Dreams on 27/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit
import LTHRadioButton

class HairCareTableViewCell: UITableViewCell {
    
    @IBOutlet weak var baseView: UIView!
    @IBOutlet weak var antiAging: LTHRadioButton!
    @IBOutlet weak var curlEnhancing: LTHRadioButton!
    @IBOutlet weak var heatProtaction: LTHRadioButton!
    @IBOutlet weak var oiliness: LTHRadioButton!
    @IBOutlet weak var straightening: LTHRadioButton!
    @IBOutlet weak var damaged: LTHRadioButton!
    @IBOutlet weak var hold: LTHRadioButton!
    @IBOutlet weak var texture: LTHRadioButton!
    @IBOutlet weak var shine: LTHRadioButton!
    @IBOutlet weak var colorProtaction: LTHRadioButton!

    @IBOutlet weak var antiAgingLabel: UILabel!
    @IBOutlet weak var curlEnhancingLabel: UILabel!
    @IBOutlet weak var heatProtactionLabel: UILabel!
    @IBOutlet weak var oilinessLabel: UILabel!
    @IBOutlet weak var straighteningLabel: UILabel!
    @IBOutlet weak var damagedLabel: UILabel!
    @IBOutlet weak var holdLabel: UILabel!
    @IBOutlet weak var textureLabel: UILabel!
    @IBOutlet weak var shineLabel: UILabel!
    @IBOutlet weak var colorProtactionLabel: UILabel!
    //let view = HaircareButtonView.loadFromXib() as! HaircareButtonView
    var hairCareList: [String] = []
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        antiAging.selectedColor = .black
        curlEnhancing.selectedColor = .black
        heatProtaction.selectedColor = .black
        oiliness.selectedColor = .black
        straightening.selectedColor = .black
        damaged.selectedColor = .black
        hold.selectedColor = .black
        texture.selectedColor = .black
        shine.selectedColor = .black
        colorProtaction.selectedColor = .black
        
        antiAging.onSelect {
            self.hairCareList.append("anti-aging")
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        antiAging.onDeselect {
            self.hairCareList.removeAll { $0 == "anti-aging" }
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        
        curlEnhancing.onSelect {
            self.hairCareList.append("curl-enhancing")
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        curlEnhancing.onDeselect {
            self.hairCareList.removeAll { $0 == "curl-enhancing" }
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        heatProtaction.onSelect {
            self.hairCareList.append("heat protaction")
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        heatProtaction.onDeselect {
            self.hairCareList.removeAll { $0 == "heat protaction" }
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        
        oiliness.onSelect {
            self.hairCareList.append("oiliness")
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        oiliness.onDeselect {
            self.hairCareList.removeAll { $0 == "oiliness" }
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        straightening.onSelect {
            self.hairCareList.append("straightening")
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        straightening.onDeselect {
            self.hairCareList.removeAll { $0 == "straightening" }
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        
        damaged.onSelect {
            self.hairCareList.append("damaged")
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        damaged.onDeselect {
            self.hairCareList.removeAll { $0 == "damaged" }
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        hold.onSelect {
            self.hairCareList.append("hold")
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        hold.onDeselect {
            self.hairCareList.removeAll { $0 == "hold" }
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        
        texture.onSelect {
            self.hairCareList.append("texture")
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        texture.onDeselect {
            self.hairCareList.removeAll { $0 == "texture" }
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        shine.onSelect {
            self.hairCareList.append("shine")
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        shine.onDeselect {
            self.hairCareList.removeAll { $0 == "shine" }
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        
        colorProtaction.onSelect {
            self.hairCareList.append("color protaction")
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        colorProtaction.onDeselect {
            self.hairCareList.removeAll { $0 == "color protaction" }
            NotificationCenter.default.post(name: Notification.Name("HairCare"), object: self.hairCareList as AnyObject)
        }
        
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
}
