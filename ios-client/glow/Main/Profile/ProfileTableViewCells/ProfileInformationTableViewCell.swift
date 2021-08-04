//
//  ProfileInformationTableViewCell.swift
//  glow
//
//  Created by Dreams on 26/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit
import SkyFloatingLabelTextField

protocol profileInformationDelegate: class {
    func information(name: String, username: String, email: String, website: String, birthdate: String, about: String)
}

class ProfileInformationTableViewCell: UITableViewCell {

    @IBOutlet weak var userNameTextField: SkyFloatingLabelTextField!
    @IBOutlet weak var nameTextField: SkyFloatingLabelTextField!
    @IBOutlet weak var emailTextField: SkyFloatingLabelTextField!
    @IBOutlet weak var websiteTextField: SkyFloatingLabelTextField!
    @IBOutlet weak var aboutTextField: SkyFloatingLabelTextField!
    @IBOutlet weak var birthdateTextField: SkyFloatingLabelTextField!
    let delegate: profileInformationDelegate? = nil
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code

    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    

}
