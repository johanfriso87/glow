//
//  SearchResultViewController.swift
//  glow
//
//  Created by Dreams on 29/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//
extension UISearchBar {
    func getTextField() -> UITextField? { return value(forKey: "searchField") as? UITextField }
    func setTextField(color: UIColor, cornerRadius: CGFloat) {
        guard let textField = getTextField() else { return }
        switch searchBarStyle {
        case .minimal:
            textField.layer.backgroundColor = color.cgColor
            textField.layer.cornerRadius = cornerRadius
        case .prominent, .default: textField.backgroundColor = color
        @unknown default: break
        }
    }
}

import UIKit
import VideoEditorSDK

class SearchResultViewController: UIViewController, UISearchBarDelegate {

    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var searchBar: UISearchBar!
    var searchList: [Search] = []
    override func viewDidLoad() {
        super.viewDidLoad()
       
        searchBar.placeholder = "search"
        searchBar.setTextField(color: UIColor.white, cornerRadius: 6)
        self.collectionView.delegate = self
        self.collectionView.dataSource = self
        self.collectionView.register(UINib(nibName: "MakeReviewCollectionViewCell", bundle: nil), forCellWithReuseIdentifier: "MakeReviewCollectionViewCell")
        self.collectionView.contentInset = UIEdgeInsets(top: 0, left: 10, bottom: 0, right: 10)
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
         self.searchBar.delegate = self
    }
    
    func getSearchData(_ searchBar: UISearchBar) {
        let param = ["keyword": searchBar.text!.lowercased(),
                     "page": "1",
                     "country" : "jp"] as [String : Any]
        Utility.showProgress()
        SearchRequest.searchProduct(param: param) { (success, response, error) in
            Utility.dismissProgress()
            if error == nil {
                if let data = response?.data {
                    self.searchList = data
                    self.collectionView.reloadData()
                } else {
                    Utility.alert(message: response?.msg ?? "")
                }
                
            } else {
                Utility.alert(message: error?.localizedDescription ?? "")
            }
        }
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        self.getSearchData(searchBar)
    }
    //

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
}

extension SearchResultViewController : UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return searchList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "MakeReviewCollectionViewCell", for: indexPath) as! MakeReviewCollectionViewCell
        cell.titleLabel.isHidden = true
        cell.descLabel.text = searchList[indexPath.item].title
        if let ulrString =  self.searchList[indexPath.item].thumbnail {
            if let url = URL(string: ulrString) {
                cell.productIMageView.sd_setImage(with: url) { (image, error, imageType, url) in
                    cell.productIMageView.image = image
                }
            }
            
        } else if let ulrString =  self.searchList[indexPath.item].images?.first {
            if let url = URL(string: ulrString) {
                cell.productIMageView.sd_setImage(with: url) { (image, error, imageType, url) in
                    cell.productIMageView.image = image
                }
            }
            
        }
        
        
        cell.makeReviewButton.tag = indexPath.row
        cell.makeReviewButton.addTarget(self, action: #selector(navigateToRecording(_:)), for: .touchUpInside)
        cell.reviewButton.tag = indexPath.row
        cell.reviewButton.addTarget(self, action: #selector(self.navigateToProduct(_:)), for: .touchUpInside)
        cell.saveStickerButton.tag = indexPath.row
        cell.saveStickerButton.addTarget(self, action: #selector(self.saveStickerButtonTapped(_:)), for: .touchUpInside)
        
        return cell
    }
    
    func createSticker(str: String) {
        let param = ["productThumbnailUrl": str]
        StickerRequest.getProductStickers(param: param) { (success, response, error) in
            if error != nil {
                Utility.alert(message: error?.localizedDescription ?? "")
            } else {
                if let data = response?["data"] as? String {
                    self.saveSticker(string: BASE + data)
                }
                //self.saveSticker(url: <#T##String#>)
            }
        }
    }
    
    @objc func saveStickerButtonTapped(_ sender: UIButton) {
//        self.saveSticker(url: self.video.)
        self.createSticker(str: self.searchList[sender.tag].thumbnail ?? "")
    }
    
    
    
    func saveSticker(string: String) {
        Utility.showProgress()
        let imageView = UIImageView()
        if let url = URL(string: string) {
            imageView.getImageFromURL(url: url, indexPath: nil) { (image, index) in
                self.saveImage(image: image)
            }
        }
    }
    func saveImage(image: UIImage?) {
        
        guard let selectedImage = image else {
            return
        }
        
        UIImageWriteToSavedPhotosAlbum(selectedImage, self, #selector(image(_:didFinishSavingWithError:contextInfo:)), nil)
    }
        
        //MARK: - Save Image callback
        
    @objc func image(_ image: UIImage, didFinishSavingWithError error: Error?, contextInfo: UnsafeRawPointer) {
        Utility.dismissProgress()
        if let error = error {
            
            print(error.localizedDescription)
            
        } else {
            Utility.alert(message: "Sticker save successfully!")
            print("Success")
        }
    }
    
    @objc func navigateToProduct(_ sender: UIButton) {
        let storyBoard = UIStoryboard(name: "UserScreen", bundle: nil)
        if let viewController = storyBoard.instantiateViewController(withIdentifier: "ProductDetailViewController") as? ProductDetailViewController {
            var tempImage = ""
            if (self.searchList[sender.tag].thumbnail?.isEmpty ?? false) {
                tempImage = self.searchList[sender.tag].images?.first ?? ""
            } else {
                tempImage = self.searchList[sender.tag].thumbnail ?? ""
            }
            let video = Videos(_id: nil, videoUrl: nil, __v: nil, product: nil, thumbnailUrl: nil, productThumbnailUrl: tempImage, asin: self.searchList[sender.tag].asin, productName: self.searchList[sender.tag].title, productUrl: self.searchList[sender.tag].url, userId: 0, gif: "")
            viewController.selectedProduct = video
            viewController.modalPresentationStyle = .overFullScreen
            self.present(viewController, animated: true, completion: nil)
        }
    }
    
    @objc func navigateToRecording(_ sender: UIButton) {
        let storyBoard = UIStoryboard(name: "Main", bundle: nil)
        if let viewController = storyBoard.instantiateViewController(withIdentifier: "VideoRecordingViewController") as? VideoRecordingViewController {
            viewController.searchData = self.searchList[sender.tag]
            viewController.modalPresentationStyle = .overFullScreen
            self.present(viewController, animated: true, completion: nil)
        }
    }
    
    
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let padding: CGFloat = 40
        let collectionViewSize = collectionView.frame.size.width - padding

        return CGSize(width: collectionViewSize/2, height: 300)
    }
    
    
}
