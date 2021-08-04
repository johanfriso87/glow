//
//  SubscribeUserTableViewCell.swift
//  glow
//
//  Created by dhruv dhola on 01/11/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class SubscribeUserTableViewCell: UITableViewCell {

    @IBOutlet weak var userImageView: UIImageView!
    @IBOutlet weak var userLabel: UILabel!
    @IBOutlet weak var shadowView: UIView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        self.shadowView.dropShadow()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
