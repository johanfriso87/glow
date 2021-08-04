//
//  ProductCollectionViewCell.swift
//  glow
//
//  Created by dhruv dhola on 01/11/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class ProductCollectionViewCell: UICollectionViewCell {
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var userImageView: UIImageView!
    @IBOutlet weak var makeReviewButton: UIButton!
    @IBOutlet weak var seeReviewButton: UIButton!
    @IBOutlet weak var saveStickerButton: UIButton!
    @IBOutlet weak var shadowView: UIView!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        self.contentView.layer.cornerRadius = 2.0
        self.contentView.layer.borderWidth = 1.0
        self.contentView.layer.borderColor = UIColor.clear.cgColor
        self.contentView.layer.masksToBounds = true
        self.layer.shadowColor = UIColor.gray.cgColor
        self.layer.shadowOffset = CGSize(width: 0, height: 2.0)
        self.layer.shadowRadius = 2.0
        self.layer.shadowOpacity = 0.5
        self.layer.masksToBounds = false
        self.makeReviewButton.layer.cornerRadius = 20.0
    }
    
}
