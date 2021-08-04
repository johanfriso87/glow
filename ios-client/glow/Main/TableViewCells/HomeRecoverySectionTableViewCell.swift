//
//  HomeRecoverySectionTableViewCell.swift
//  glow
//
//  Created by Dreams on 12/07/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class HomeRecoverySectionTableViewCell: UITableViewCell, UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    
    @IBOutlet weak var spotlightCollectionView: UICollectionView!

    var spotlightData: [Videos] = []
    var sec: [Int] = []
    var isRefresh: Bool = true
    weak var delegate:TableViewInsideCollectionViewDelegate?
    override func awakeFromNib() {
        super.awakeFromNib()
        spotlightCollectionView.delegate = self
        spotlightCollectionView.dataSource = self
        spotlightCollectionView.register(UINib(nibName: "HomeCollectionViewCell", bundle: nil), forCellWithReuseIdentifier: "HomeCollectionViewCell")
//        spotlightCollectionView.register(UINib(nibName: "CreatorsC
        
        spotlightCollectionView.contentInset = UIEdgeInsets(top: 0, left: 20, bottom: 0, right: 20)
        
    }
    

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return spotlightData.count
    }
    var count = 0
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "HomeCollectionViewCell", for: indexPath) as! HomeCollectionViewCell
       
//           if let obj = spotlightData[indexPath.item].userId as? [String : Any] {
//               cell.titleLabel.text = obj["name"] as? String
//            if let stringUrl = obj["profilePic"] as? String {
//                if let url = URL(string: BASEVIDEOURL + stringUrl) {
//                    cell.userImageView.sd_setImage(with: url) { (image, error, type, url) in
//                        cell.userImageView.image = image
//                    }
//                }
//            }
//           }
        
//           if let objuserpic = spotlightData[indexPath.item].userId as? [String : Any] {
//            cell.userNameButton.setTitle(objuserpic["name"] as? String, for: .normal)
//            if let stringUrl = objuserpic["profilePic"] as? String {
//                if let url = URL(string: BASEVIDEOURL + stringUrl) {
//                    cell.requestButton.sd_setImage(with: url, for: UIControl.State.normal) { (img, error, type, url) in
//                        cell.requestButton.setImage(img, for: .normal)
//                    }
//                }
//            }
//           }
        
//           if let videoUrlString = spotlightData[indexPath.item].videoUrl {
//               if let videoUrl = URL(string:  BASEVIDEOURL + videoUrlString) {
//                   cell.addPlayer(for: videoUrl)
//               }
//           }
        return cell
    }
    
//    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
//        self.delegate?.cellTaped(data: indexPath)
//    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let padding = 50
        let size: CGFloat = collectionView.frame.size.width - CGFloat(padding)
        return CGSize(width: size / 2, height: 351)
    }
}
