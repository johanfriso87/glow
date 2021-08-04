//
//  SearchVideoTableViewCell.swift
//  glow
//
//  Created by Dreams on 29/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class SearchVideoTableViewCell: UITableViewCell {

    @IBOutlet weak var searchTextField: UITextField!
    @IBOutlet weak var searchBar: UISearchBar!
    @IBOutlet weak var scanButton: UIButton!
    @IBOutlet weak var urlButton: UIButton!
    @IBOutlet weak var backButton: UIButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
