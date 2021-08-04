//
//  ArticalTableViewCell.swift
//  glow
//
//  Created by Dreams on 24/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class ArticalTableViewCell: UITableViewCell {
    
//    @IBOutlet weak var articalImageView: UIImageView!
//    @IBOutlet weak var titleLabel : UILabel!
//    @IBOutlet weak var descLabel: UILabel!
    @IBOutlet weak var blogCollectionView: UICollectionView!
    var blogs: [Blogs] = []
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        self.blogCollectionView.delegate = self
        self.blogCollectionView.dataSource = self
         blogCollectionView.register(UINib(nibName: "ArticalCollectionViewCell", bundle: nil), forCellWithReuseIdentifier: "ArticalCollectionViewCell")
        
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}

extension ArticalTableViewCell: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.blogs.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
         let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "ArticalCollectionViewCell", for: indexPath) as! ArticalCollectionViewCell
        if let strUrl = self.blogs[indexPath.item].picture {
            if let url = URL(string: BASE + strUrl) {
                cell.thumbnailImage.sd_setImage(with: url) { (image, error, type, url) in
                    cell.thumbnailImage.image = image
                }
            }
        }
        cell.titleLabel.text = self.blogs[indexPath.item].title
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
            let size: CGFloat = collectionView.frame.size.width / 2
    
            return CGSize(width: 256, height: 156)
        }
}

