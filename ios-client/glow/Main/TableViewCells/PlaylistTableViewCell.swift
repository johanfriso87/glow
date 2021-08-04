//
//  PlaylistTableViewCell.swift
//  glow
//
//  Created by Dreams on 23/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class PlaylistTableViewCell: UITableViewCell, UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
   
    @IBOutlet weak var playListCollectionView: UICollectionView!
    var spotlightData: [String] = []
    var isFrom: String = ""
    override func awakeFromNib() {
        super.awakeFromNib()
       
        playListCollectionView.delegate = self
        playListCollectionView.dataSource = self
        
        playListCollectionView.register(UINib(nibName: "PlaylistCollectionViewCell", bundle: nil), forCellWithReuseIdentifier: "PlaylistCollectionViewCell")
        
        
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
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "PlaylistCollectionViewCell", for: indexPath) as! PlaylistCollectionViewCell
            cell.playlistName.text = spotlightData[indexPath.item]
        if isFrom == "Brands" {
            cell.playlistImageView.isHidden = true
            cell.playlistCoverImageView.isHidden = true
            cell.playlistName.textColor = .black
        }
            return cell
       
            
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let padding: CGFloat =  40
        let collectionViewSize = collectionView.frame.size.width - padding

        return CGSize(width: collectionViewSize/2, height: collectionViewSize/2)
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
    }
    
}
