//
//  AccessoriesTableViewCell.swift
//  glow
//
//  Created by Dreams on 23/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class AccessoriesTableViewCell: UITableViewCell, UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
   
    @IBOutlet weak var accessoriesCollectionView: UICollectionView!
    var spotlightData: [Videos] = []
   
    override func awakeFromNib() {
        super.awakeFromNib()
       
        accessoriesCollectionView.delegate = self
        accessoriesCollectionView.dataSource = self
        
        accessoriesCollectionView.register(UINib(nibName: "HomeCollectionViewCell", bundle: nil), forCellWithReuseIdentifier: "HomeCollectionViewCell")
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return spotlightData.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "HomeCollectionViewCell", for: indexPath) as! HomeCollectionViewCell
        if let obj = spotlightData[indexPath.item].userId as? [String : Any] {
            cell.titleLabel.text = obj["name"] as? String
        }
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
       let padding = 50
       let size: CGFloat = collectionView.frame.size.width - CGFloat(padding)
       return CGSize(width: size / 2, height: 351)
    }
    
}
