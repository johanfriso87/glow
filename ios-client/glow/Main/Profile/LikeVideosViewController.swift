//
//  LikeVideosViewController.swift
//  glow
//
//  Created by Dreams on 27/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class LikeVideosViewController: UIViewController {

    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var noDataLabel: UILabel!
    var likedVideos: [Videos] = []
    var likeData = ["part","dhruv","nilay","part","dhruv","nilay"]
    override func viewDidLoad() {
        super.viewDidLoad()
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.register(UINib(nibName: "HomeCollectionViewCell", bundle: nil), forCellWithReuseIdentifier: "HomeCollectionViewCell")
        collectionView.contentInset = UIEdgeInsets(top: 0, left: 20, bottom: 0, right: 20)
        self.getLikeVideos()
        // Do any additional setup after loading the view.
    }
    

    func getLikeVideos() {
        Utility.showProgress()
        LikeRequest.getLikeVideos(param: [:]) { (success, response, error) in
            Utility.dismissProgress()
            if error != nil {
                Utility.alert(message: error.debugDescription)
            } else {
                self.likedVideos = response?.videos ?? []
                if !self.likedVideos.isEmpty {
                    self.collectionView.reloadData()
                } else {
                    self.collectionView.isHidden = true
                    self.noDataLabel.isHidden = false
                    self.noDataLabel.text = "No liked videos yet!"
                }
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
        self.navigationController?.popViewController(animated: true)
    }

}

extension LikeVideosViewController: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
            return likeData.count
        }
        
        func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "HomeCollectionViewCell", for: indexPath) as! HomeCollectionViewCell
            cell.titleLabel.text = self.likedVideos[indexPath.item].productName
            let urlString = BASE + self.likedVideos[indexPath.item].videoUrl!
            if !urlString.isEmpty {
                if let image = Utility.createThumbnailOfVideoFromFileURL(urlString) {
                    cell.gifImageView.image = image
                }
            }
            cell.userNameButton.isHidden = true
//            cell.userNameButton.setTitle(likedVideos[indexPath.item], for: .normal)
//            cell.userNameButton.setTitleColor(.white, for: .normal)
            return cell
        }
        
        func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
            let padding: CGFloat =  50
            let collectionViewSize = collectionView.frame.size.width - padding

            return CGSize(width: collectionViewSize/2, height: 351)
        }
}
