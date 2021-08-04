//
//  HomeTableViewCell.swift
//  glow
//
//  Created by Dreams on 23/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit
import AVKit
import AVFoundation
import SwiftyGif

protocol TableViewInsideCollectionViewDelegate:class {
    func cellTaped(data:Int, videoUrl: Videos, index: Int)
}

protocol ProductDelegate:class {
    func productTapped(index: Int, video: Videos)
}


class HomeTableViewCell: UITableViewCell, UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout, UIScrollViewDelegate {
    
    
    
    @IBOutlet weak var spotlightCollectionView: UICollectionView!
    //  @IBOutlet weak var creatorsCollectionView: UICollectionView!
    @IBOutlet weak var shadowView: UIView!
    var spotlightData: [Videos] = []
    var sec: [Int] = []
    var isRefresh: Bool = true
    weak var delegate:TableViewInsideCollectionViewDelegate?
    weak var productDelegate: ProductDelegate?
    
    var currentVisibleIndex: [IndexPath] = []
    var previousIndexPath: [IndexPath] = []
    override func awakeFromNib() {
        super.awakeFromNib()
        self.setUp()
    }
    
    
    func setUp() {
        spotlightCollectionView.delegate = self
        spotlightCollectionView.dataSource = self
        spotlightCollectionView.register(UINib(nibName: "HomeCollectionViewCell", bundle: nil), forCellWithReuseIdentifier: "HomeCollectionViewCell")
        //        spotlightCollectionView.register(UINib(nibName: "CreatorsCollectionViewCell", bundle: nil), forCellWithReuseIdentifier: "CreatorsCollectionViewCell")
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
        
        //  if let obj = spotlightData[indexPath.item].userId as? [String : Any] {
        cell.titleLabel.text = spotlightData[indexPath.item].productName
        if let userData = spotlightData[indexPath.item].userId as? [String:Any] {
            let userName = userData["name"] as? String ?? ""
            cell.userNameButton.setTitle(userName, for: .normal)
            if let userpic = userData["profilePic"] as? String {
                if let url = URL(string: BASEVIDEOURL + userpic) {
                    cell.requestButton.sd_setImage(with: url, for: .normal) { (image, error, type, url) in
                        if image != nil {
                            cell.requestButton.setImage(image, for: .normal)
                        } else {
                            cell.requestButton.setImage(UIImage(named: "ic_user"), for: .normal)
                        }
                    }
                }
            }
        }
        
        if let stringUrl = spotlightData[indexPath.item].productThumbnailUrl as? String {
            if let url = URL(string: stringUrl) {
                cell.userImageView.sd_setImage(with: url) { (image, error, type, url) in
                    cell.userImageView.contentMode = .scaleAspectFit
                    cell.userImageView.image = image
                }
            }
        }
        //   }
        if let url = self.spotlightData[indexPath.item].gif {
            if let urlString = URL(string: BASE + url) {
                cell.gifImageView.setGifFromURL(urlString)
            }
        }
        
        cell.navButton.tag = indexPath.item
        cell.navButton.addTarget(self, action: #selector(self.navigation(_:)), for: .touchUpInside)
        cell.productImageButton.tag = indexPath.item
        cell.productImageButton.addTarget(self, action: #selector(self.productTapped(_:)), for: .touchUpInside)
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let padding = 50
        let size: CGFloat = collectionView.frame.size.width - CGFloat(padding)
        return CGSize(width: size / 2, height: 351)
    }
    
    @objc func navigation(_ sender: UIButton) {
        self.delegate?.cellTaped(data: sender.tag, videoUrl: self.spotlightData[sender.tag], index: sender.tag)
    }
    
    @objc func productTapped(_ sender : UIButton) {
        self.productDelegate?.productTapped(index: sender.tag, video: self.spotlightData[sender.tag])
    }
    
    //    func scrollViewDidScroll(_ scrollView: UIScrollView) {
    ////        let visibleRect = CGRect(origin: spotlightCollectionView.contentOffset, size: spotlightCollectionView.bounds.size)
    ////        let visiblePoint = CGPoint(x: visibleRect.midX, y: visibleRect.midY)
    ////        let visibleIndexPath = spotlightCollectionView.indexPathForItem(at: visiblePoint)
    ////        print("\(visibleIndexPath)")
    //        let cells =  spotlightCollectionView.visibleCells
    //        for cell in cells {
    ////            if let tempCell = cell as? HomeCollectionViewCell {
    ////                let indexPath = spotlightCollectionView.indexPath(for: cell)
    ////                if let videoUrlString = spotlightData[indexPath!.item].videoUrl {
    ////                    if let videoUrl = URL(string:  BASEVIDEOURL + videoUrlString) {
    ////                        tempCell.addPlayer(for: videoUrl)
    ////                    }
    ////                }
    ////            }
    //
    //
    //            //print("sdd\(indexPath)")
    //        }
    //        spotlightCollectionView.visibleCells.forEach { cell in
    //            // TODO: write logic to start the video after it ends scrolling
    //            if let tempCell = cell as? HomeCollectionViewCell {
    //                let indexPath = spotlightCollectionView.indexPath(for: tempCell)
    //                if let videoUrlString = spotlightData[indexPath!.item].videoUrl {
    //                    if let videoUrl = URL(string:  BASEVIDEOURL + videoUrlString) {
    //                        print("play visible")
    //                        tempCell.addPlayer(for: videoUrl)
    //                    }
    //                }
    //            }
    //
    //
    //    }
    //   // extension YourViewController: UIScrollViewDelegate {
    //        func scrollViewWillBeginDragging(_ scrollView: UIScrollView) {
    //            spotlightCollectionView.visibleCells.forEach { cell in
    //                // TODO: write logic to stop the video before it begins scrolling
    //                if let tempCell = cell as? HomeCollectionViewCell {
    //                    let indexPath = spotlightCollectionView.indexPath(for: tempCell)
    //                    print("stop play vi")
    //                    tempCell.stopPlayback()
    //                }
    //            }
    //        }
    //
    ////    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
    ////        spotlightCollectionView.visibleCells.forEach { cell in
    ////            // TODO: write logic to start the video after it ends scrolling
    ////            if let tempCell = cell as? HomeCollectionViewCell {
    ////                let indexPath = spotlightCollectionView.indexPath(for: tempCell)
    ////                if let videoUrlString = spotlightData[indexPath!.item].videoUrl {
    ////                    if let videoUrl = URL(string:  BASEVIDEOURL + videoUrlString) {
    ////                        print("play visible")
    ////                        tempCell.addPlayer(for: videoUrl)
    ////                    }
    ////                }
    ////            }
    ////        }
    ////    }
    //
    //    }
    
}
