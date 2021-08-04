//
//  SkinTypeTableViewCell.swift
//  glow
//
//  Created by Dreams on 26/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit



class SkinTypeTableViewCell: UITableViewCell {
    
    

    @IBOutlet weak var collectionView: UICollectionView!
    let type = ["Normal","Dry", "Oily", "Combo"]
    var selectedType = ""
    var hexColorString = ""
    override func awakeFromNib() {
        super.awakeFromNib()
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.register(UINib(nibName: "CircularButtonCollectionView", bundle: nil), forCellWithReuseIdentifier: "CircularButtonCollectionView")
        NotificationCenter.default.addObserver(self, selector: #selector(self.didChangeSkin(_:)), name: Notification.Name("SkinTone"), object: nil)
        
        // Initialization code
    }

    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    @objc func didChangeSkin(_ notification: NSNotification) {
        if let obj = notification.object as? String {
            self.hexColorString = obj
            self.collectionView.reloadData()
        }
    }

}

extension SkinTypeTableViewCell:  UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return type.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "CircularButtonCollectionView", for: indexPath) as! CircularButtonCollectionView
        cell.selectedView.isHidden = false
        cell.selectedLabel.text = type[indexPath.item]
        cell.selectedColorView.backgroundColor = UIColor.hexStringToUIColor(hex: hexColorString)
        cell.selectedView.backgroundColor = UIColor.darkGray
        if cell.selectedLabel.text == self.selectedType {
            cell.selectedView.backgroundColor = .black
        } else {
            cell.selectedView.backgroundColor = .white
        }
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        //let size: CGFloat = collectionView.frame.size.width / 2
        
        return CGSize(width: 100, height: 100)
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        self.selectedType = self.type[indexPath.item]
        for (index,_) in self.type.enumerated() {
            let indexPaths = IndexPath(item: index, section: 0)
            if let cell = collectionView.cellForItem(at: indexPaths) as? CircularButtonCollectionView {
                cell.selectedView.backgroundColor = .white
            }
        }
        if let cell = collectionView.cellForItem(at: indexPath) as? CircularButtonCollectionView {
            cell.selectedView.backgroundColor = .black
        }
        NotificationCenter.default.post(name: NSNotification.Name(rawValue: "SkinType"), object: self.selectedType)
    }
}
extension UIColor {
   static func hexStringToUIColor (hex:String) -> UIColor {
        var cString:String = hex.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()

        if (cString.hasPrefix("#")) {
            cString.remove(at: cString.startIndex)
        }

        if ((cString.count) != 6) {
            return UIColor.gray
        }

        var rgbValue:UInt64 = 0
        Scanner(string: cString).scanHexInt64(&rgbValue)

        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
}
