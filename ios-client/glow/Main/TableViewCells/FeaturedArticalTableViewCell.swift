//
//  FeaturedArticalTableViewCell.swift
//  glow
//
//  Created by Dreams on 25/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class FeaturedArticalTableViewCell: UITableViewCell  {
    
    @IBOutlet weak var articalImageView: UIImageView!
    @IBOutlet weak var titleLabel : UILabel!
    @IBOutlet weak var descLabel: UILabel!
   //@IBOutlet weak var articalCollectionView: UICollectionView!
    var spotlightData: [String] = []
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        //articalCollectionView.delegate = self
       // articalCollectionView.dataSource = self
       // articalCollectionView.register(UINib(nibName: "ArticalCollectionViewCell", bundle: nil), forCellWithReuseIdentifier: "ArticalCollectionViewCell")
        
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

//        func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
//        return spotlightData.count
//    }
//
//    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
//            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "ArticalCollectionViewCell", for: indexPath) as! ArticalCollectionViewCell
//            cell.titleLabel.text = spotlightData[indexPath.item]
//            return cell
//
//
//    }
//
//    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
//       // let size: CGFloat = collectionView.frame.size.width / 2
//
//        return CGSize(width: 312, height: 102)
//    }
    
//    var isHeightCalculated: Bool = false
//
//     func preferredLayoutAttributesFitting(_ layoutAttributes: UICollectionViewLayoutAttributes) -> UICollectionViewLayoutAttributes {
//        //Exhibit A - We need to cache our calculation to prevent a crash.
//        if !isHeightCalculated {
//            setNeedsLayout()
//            layoutIfNeeded()
//            let size = contentView.systemLayoutSizeFitting(layoutAttributes.size)
//            var newFrame = layoutAttributes.frame
//            newFrame.size.width = CGFloat(ceilf(Float(size.width)))
//            layoutAttributes.frame = newFrame
//            isHeightCalculated = true
//        }
//        return layoutAttributes
//    }
}

