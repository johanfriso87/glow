//
//  UserScreenViewController.swift
//  glow
//
//  Created by dhruv dhola on 02/11/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class UserScreenViewController: UIViewController {

    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var userImageView: UIImageView!
    @IBOutlet weak var usersubscribeButton: UIButton!
    @IBOutlet weak var userNameLabel: UILabel!
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var noDataLabel: UILabel!
    var selectedVideoList : [Videos] = []
    var selectedUser: User!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.setUpUI()
    }
    
    func setUpUI() {
        self.collectionView.delegate = self
        self.collectionView.dataSource = self
        self.collectionView.showsHorizontalScrollIndicator = false
        self.collectionView.showsVerticalScrollIndicator = false
        self.collectionView.register(UINib(nibName: "HomeCollectionViewCell", bundle: nil), forCellWithReuseIdentifier: "HomeCollectionViewCell")
        self.getUserVideo()
        self.userNameLabel.text = self.selectedUser.name
        if let stringUrl = self.selectedUser.profilePic {
            if let url = URL(string: stringUrl) {
                self.userImageView.sd_setImage(with: url) { (image, error, type, url) in
                    self.userImageView.contentMode = .scaleAspectFit
                    self.userImageView.image = image
                }
            }
        }
    }
    
    func subscribeUser() {
//            let tempId = id["_id"] as? String ?? ""
        let param = ["targetUserId" : self.selectedUser._id]
            Utility.showProgress()
        LikeRequest.subscribeUser(param: param as [String : Any]) { (success, string, error) in
                Utility.dismissProgress()
                if error == nil {
                    
                } else {
                    Utility.alert(message: error?.localizedDescription ?? "Something went wrong")
                }
            }
    }

    func getUserVideo() {
        Utility.showProgress()
        let param = ["userId": self.selectedUser._id]
        UserRequest.targetUserVideos(param: param as [String : Any], filePathKey: [""]) { (success, videos, error) in
            if error == nil {
                Utility.dismissProgress()
                if videos?.videos != nil {
                    print("success")
                    self.selectedVideoList = videos?.videos ?? []
                    self.collectionView.reloadData()
                } else {
                    self.collectionView.isHidden = true
                    self.noDataLabel.isHidden = false
                    self.noDataLabel.text = "No user reviews yet!"
                }
                
            } else {
                Utility.alert(message: error?.localizedDescription ?? "")
            }
        }
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */
    
    @IBAction func backButton(_ sender: UIButton) {
        self.dismiss(animated: true, completion: nil)
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func subscribeButton(_ sender: UIButton) {
        self.subscribeUser()
    }
    

}

extension UserScreenViewController: UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.selectedVideoList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {

            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "HomeCollectionViewCell", for: indexPath) as! HomeCollectionViewCell
            cell.titleLabel.text = self.selectedVideoList[indexPath.item].productName
            let urlString = BASE + self.selectedVideoList[indexPath.item].videoUrl!
            if !urlString.isEmpty {
                if let image = Utility.createThumbnailOfVideoFromFileURL(urlString) {
                    cell.gifImageView.image = image
                }
            }
        if let stringUrl = self.selectedVideoList[indexPath.item].productThumbnailUrl {
            if let url = URL(string: stringUrl) {
                cell.userImageView.sd_setImage(with: url) { (image, error, type, url) in
                    cell.userImageView.contentMode = .scaleAspectFit
                    cell.userImageView.image = image
                }
            }
        }
        cell.descLabel.isHidden = true
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let padding = 30.0
        let size: CGFloat = collectionView.frame.size.width - CGFloat(padding)
        return CGSize(width: size/2, height: 351)
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
            let storyBoard = UIStoryboard(name: "Profile", bundle: nil)
            if let viewController = storyBoard.instantiateViewController(withIdentifier: "VideoPlayingViewController") as? VideoPlayingViewController {
                viewController.videoUrl = self.selectedVideoList[indexPath.item].videoUrl ?? ""
                self.navigationController?.pushViewController(viewController, animated: true)
            }
    }
}
