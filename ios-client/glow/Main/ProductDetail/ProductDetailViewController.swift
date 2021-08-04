//
//  ProductDetailViewController.swift
//  glow
//
//  Created by dhruv dhola on 02/11/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class ProductDetailViewController: UIViewController {
    
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var amazonButton: UIButton!
    @IBOutlet weak var reviewButton: UIButton!
    @IBOutlet weak var shareButton: UIButton!
    @IBOutlet weak var productNameLabel: UILabel!
    @IBOutlet weak var productImageView: UIImageView!
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var noDataLabel: UILabel!
    var selectedProduct: Videos!
    var selectedVideoList: [Videos] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.setUpUi()
    }
    
    func setUpUi() {
        self.productImageView.contentMode = .scaleAspectFit
        self.collectionView.delegate = self
        self.collectionView.dataSource = self
        self.collectionView.showsHorizontalScrollIndicator = false
        self.collectionView.showsVerticalScrollIndicator = false
        self.collectionView.register(UINib(nibName: "HomeCollectionViewCell", bundle: nil), forCellWithReuseIdentifier: "HomeCollectionViewCell")
        if let urlString = self.selectedProduct.productThumbnailUrl {
            if let url = URL(string: urlString){
                Utility.showProgress()
                self.productImageView?.sd_setImage(with: url) { (downloadimage, error, type, url) in
                    Utility.dismissProgress()
                    print(error?.localizedDescription ?? "")
                    self.productImageView.image = downloadimage
                }
            }
        }
        self.productNameLabel.text = self.selectedProduct.productName
        self.getReviewProduct()
    }
    
    func getReviewProduct() {
        let param = ["q": "AOC",
                     "page" : 0,
                     "dataPerPage" : 100,
                     "asin": self.selectedProduct.asin ?? ""] as [String : Any]
        UserRequest.getReviewProduct(param: param, filePathKey: []) { (success, videos, error) in
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
    }
    
    @IBAction func amazonButton(_ sender: UIButton) {
        if let urlString = self.selectedProduct.productUrl {
            if let url = URL(string: urlString) {
                UIApplication.shared.open(url, options: [:]) { (success) in
                    print("open url")
                }
            }
        }
    }
    
    @IBAction func reviewButton(_ sender: UIButton) {
        let storyBoard = UIStoryboard(name: "Main", bundle: nil)
        if let viewController = storyBoard.instantiateViewController(withIdentifier: "VideoRecordingViewController") as? VideoRecordingViewController {
            viewController.searchData?.asin = self.selectedProduct.asin
            viewController.searchData?.title = self.selectedProduct.productName
            viewController.searchData?.url = self.selectedProduct.productUrl
            viewController.searchData?.thumbnail = self.selectedProduct.productThumbnailUrl
            viewController.modalPresentationStyle = .overFullScreen
            self.present(viewController, animated: true, completion: nil)
        }
    }
    
    @IBAction func shareButton(_ sender: UIButton) {
        let storyBoard = UIStoryboard(name: "Main", bundle: nil)
        if let viewController = storyBoard.instantiateViewController(withIdentifier: "VideoSaveViewController") as? VideoSaveViewController {
            if let url = URL(string: (BASEVIDEOURL) + (self.selectedProduct.videoUrl ?? "")) {
                viewController.videoUrl = url
                viewController.searchProduct?.asin = self.selectedProduct.asin
                viewController.searchProduct?.thumbnail = self.selectedProduct.thumbnailUrl
                viewController.searchProduct?.title = self.selectedProduct.productName
                viewController.searchProduct?.url = self.selectedProduct.productUrl
            }
            viewController.modalPresentationStyle = .fullScreen
            self.present(viewController, animated: true, completion: nil)
        }
        
    }
}
extension ProductDetailViewController: UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {
    
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
        //        cell.descLabel.text = self.selectedVideoList[indexPath.item]
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let padding = 50.0
        let size: CGFloat = collectionView.frame.size.width - CGFloat(padding)
        return CGSize(width: size/2, height: 351)
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
    }
}
