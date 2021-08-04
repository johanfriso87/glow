//
//  HairTextureTableViewCell.swift
//  glow
//
//  Created by Dreams on 26/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class HairTextureTableViewCell: UITableViewCell {

    @IBOutlet weak var collectionView: UICollectionView!
    let type = ["Wavy","Curly", "Coily", "Straight"]
    var selectedHairTexture = ""
    var hexColorString = ""
    
    override func awakeFromNib() {
        super.awakeFromNib()
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.register(UINib(nibName: "CircularButtonCollectionView", bundle: nil), forCellWithReuseIdentifier: "CircularButtonCollectionView")
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
 
}

extension HairTextureTableViewCell:  UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return type.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "CircularButtonCollectionView", for: indexPath) as! CircularButtonCollectionView
        cell.selectedLabel.text = type[indexPath.item]
        cell.selectedColorView.backgroundColor = UIColor.hexStringToUIColor(hex: hexColorString)
        cell.selectedView.backgroundColor = UIColor.darkGray
        if cell.selectedLabel.text?.lowercased() == selectedHairTexture.lowercased() {
            cell.selectedView.backgroundColor = .black
        } else {
            cell.selectedView.backgroundColor = .white
        }
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        self.selectedHairTexture = self.type[indexPath.item]
        for (index,_) in self.type.enumerated() {
            let indexPaths = IndexPath(item: index, section: 0)
            if let cell = collectionView.cellForItem(at: indexPaths) as? CircularButtonCollectionView {
                cell.selectedView.backgroundColor = .white
            }
        }
        if let cell = collectionView.cellForItem(at: indexPath) as? CircularButtonCollectionView {
            
            cell.selectedView.backgroundColor = .black
        }
        NotificationCenter.default.post(name: NSNotification.Name(rawValue: "HairTexture"), object: self.selectedHairTexture)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let size: CGFloat = collectionView.frame.size.width / 4
        
        return CGSize(width: size, height: 100)
    }
}
