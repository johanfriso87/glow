//
//  CreatorsTableViewCell.swift
//  glow
//
//  Created by Dreams on 23/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

protocol CreatorsTableViewCellTapped:class {
    func creatorsCellTaped(data:Int, video: Videos,videos: [Videos] ,index: Int)
}

protocol CreatorsUserTapped:class {
    func creatorsUserTapped(index: Int, user: User)
}
class CreatorsTableViewCell: UITableViewCell, UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
   
    @IBOutlet weak var creatorsCollectionView: UICollectionView!
    var spotlightData: [String] = []
    var usersList: [User] = []
    weak var delegate:CreatorsTableViewCellTapped?
    weak var creatorDelegate: CreatorsUserTapped?
    
    override func awakeFromNib() {
        super.awakeFromNib()
       
        creatorsCollectionView.delegate = self
        creatorsCollectionView.dataSource = self
        
        creatorsCollectionView.register(UINib(nibName: "CreatorsCollectionViewCell", bundle: nil), forCellWithReuseIdentifier: "CreatorsCollectionViewCell")
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return usersList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "CreatorsCollectionViewCell", for: indexPath) as! CreatorsCollectionViewCell
        cell.userNameLabel.text = self.usersList[indexPath.item].name
        cell.userImageView.layer.cornerRadius = 35
        cell.requestButton.layer.cornerRadius = 10
        cell.gif1View.layer.cornerRadius = 10
        cell.gif2View.layer.cornerRadius = 10
        cell.gif3View.layer.cornerRadius = 10
        if let stringUrl = self.usersList[indexPath.item].profilePic {
            if let url = URL(string: BASEVIDEOURL + stringUrl) {
                cell.userImageView.sd_setImage(with: url) { (image, error, type, url) in
                    cell.userImageView.image = image
                }
            }
        }
        let count = self.usersList[indexPath.item].videos?.count
        if count == 1 {
            if let url = self.usersList[indexPath.item].videos?.first?.gif {
                if let urlString = URL(string: BASE + url) {
                    cell.gif1View.setGifFromURL(urlString)
                }
            }
        } else if count == 2 {
            if let url = self.usersList[indexPath.item].videos?.first?.gif {
                if let urlString = URL(string: BASE + url) {
                    cell.gif1View.setGifFromURL(urlString)
                }
            }
            if let url2 = self.usersList[indexPath.item].videos?[1].gif {
                if let urlString = URL(string: BASE + url2) {
                    cell.gif2View.setGifFromURL(urlString)
                }
            }
        } else if count == 3 {
            if let url = self.usersList[indexPath.item].videos?.first?.gif {
                if let urlString = URL(string: BASE + url) {
                    cell.gif1View.setGifFromURL(urlString)
                }
            }
            if let url2 = self.usersList[indexPath.item].videos?[1].gif {
                if let urlString = URL(string: BASE + url2) {
                    cell.gif2View.setGifFromURL(urlString)
                }
            }
           if let url3 = self.usersList[indexPath.item].videos?[2].gif {
                if let urlString = URL(string: BASE + url3) {
                    cell.gif3View.setGifFromURL(urlString)
                }
            }
        }
        cell.requestButton.tag = indexPath.item
        cell.requestButton.addTarget(self, action: #selector(self.subscribeUser(_:)), for: .touchUpInside)
        cell.gif1Button.tag = indexPath.item
        cell.gif1Button.addTarget(self, action: #selector(self.gif1ButtonTapped(_:)), for: .touchUpInside)
        cell.gif2Button.tag = indexPath.item
        cell.gif2Button.addTarget(self, action: #selector(self.gif2ButtonTapped(_:)), for: .touchUpInside)
        cell.gif3Button.tag = indexPath.item
        cell.gif3Button.addTarget(self, action: #selector(self.gif3ButtonTapped(_:)), for: .touchUpInside)
        cell.userImageButton.tag = indexPath.item
        cell.userImageButton.addTarget(self, action: #selector(self.userImageTapped(_:)), for: .touchUpInside)
            return cell
    }
    
    @objc func userImageTapped(_ sender : UIButton) {
        self.creatorDelegate?.creatorsUserTapped(index: sender.tag, user: self.usersList[sender.tag])
    }
    
    @objc func gif1ButtonTapped(_ sender: UIButton) {
        if let video = self.usersList[sender.tag].videos?[0] {
            self.delegate?.creatorsCellTaped(data: sender.tag, video: video, videos: self.usersList[sender.tag].videos ?? [], index: 0)
        }
    }
    @objc func gif2ButtonTapped(_ sender: UIButton) {
        if let video = self.usersList[sender.tag].videos?[1] {
            self.delegate?.creatorsCellTaped(data: sender.tag, video: video, videos: self.usersList[sender.tag].videos ?? [], index: 1)
        }
    }
    
    @objc func gif3ButtonTapped(_ sender: UIButton) {
        if let video = self.usersList[sender.tag].videos?[2] {
            self.delegate?.creatorsCellTaped(data: sender.tag, video: video, videos: self.usersList[sender.tag].videos ?? [], index: 2)
        }
    }
    
    @objc func subscribeUser(_ sender: UIButton) {
        self.subscribeUser(index: sender.tag)
    }
    
    func subscribeUser(index: Int) {
        let indexPath = IndexPath(row: index, section: 0)
//        if let cell = self.creatorsCollectionView.cellForItem(at: indexPath) as?
         let id = self.usersList[indexPath.item]._id!
//            let tempId = id["_id"] as? String ?? ""
            let param = ["targetUserId" : id]
            print(param)
            Utility.showProgress()
        LikeRequest.subscribeUser(param: param as [String : Any]) { (success, string, error) in
                Utility.dismissProgress()
                if error == nil {
                    Utility.alert(message: "User subscribed!")
                } else {
                    Utility.alert(message: error?.localizedDescription ?? "Something went wrong")
                }
            }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        //let size: CGFloat = collectionView.frame.size.width / 2
        return CGSize(width: 185, height: 350)
    }
    
}
