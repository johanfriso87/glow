//
//  SlideTableViewCell.swift
//  glow
//
//  Created by Dreams on 06/07/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class SlideTableViewCell: UITableViewCell {

    @IBOutlet weak var userName: UILabel!
    @IBOutlet weak var userWidthConstraint: NSLayoutConstraint!
    @IBOutlet weak var commentLabel: UILabel!
    @IBOutlet weak var replyBUtton: UIButton!
    @IBOutlet weak var userImageView: UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
