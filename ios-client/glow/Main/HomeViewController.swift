//
//  HomeViewController.swift
//  glow
//
//  Created by Dreams on 22/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit
import VideoEditorSDK

class HomeViewController: BaseViewController {
    
    @IBOutlet weak var segmentCollectionView: UICollectionView!
    
    @IBOutlet weak var tableView:UITableView!
    @IBOutlet weak var tableConstraint: NSLayoutConstraint!

    var arrayString = ["Featured", "Articals", "Playlists", "Brands", "Categories"]
    var isSelected = true
    var dashData: Dashboard?
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tableView.delegate = self
        self.tableView.dataSource = self
        self.tableView.tableFooterView = UIView(frame: CGRect.zero)
        //self.getDashBoardData()
        self.getAllStickersUrl()
        //self.tableConstraint.constant = CGFloat(self.tabBarController?.tabBar.frame.size.height ?? 100)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.getDashBoardData()
        
    }
    
    func getDashBoardData() {
        Utility.showProgress()
        DashBoardRequest.getData { (success, response, error) in
            Utility.dismissProgress()
            if error == nil {
                self.dashData = response
                self.tableView.reloadData()
            } else {
               // Utility.alert(message: error?.localizedDescription ?? "")
            }
        }
    }
    
    func getAllStickersUrl() {
        let isAdded = UserDefaults.standard.bool(forKey: "Stickers")
        //if !isAdded {
            var stickerList: [StickersList] = []
            StickerRequest.getStickers(param: [:]) { (success, response, error) in
                if error == nil && response != nil {
                    stickerList = response ?? []
                    self.addAllStickers(stickerList: stickerList)
                } else {
                    
                }
            }
        //}
        
    }
    
    func addAllStickers(stickerList: [StickersList]) {
        var categories = StickerCategory.all

        var stickers: [Sticker] = []
        if !stickerList.isEmpty {
            for (index,i) in stickerList.enumerated() {
                if let url = URL(string: BASE + i.url!) {
                    let tempStick = Sticker(imageURL: url, thumbnailURL: nil, identifier: "\(index)")
                    stickers.append(tempStick)
                }
            }
            if let previewURL = URL(string: BASE + stickerList.first!.url!) {
                categories.append(StickerCategory(title: "Glow", imageURL: previewURL, stickers: stickers))
                UserDefaults.standard.set(true, forKey: "Stickers")
            }

            StickerCategory.all = categories
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
    
    @IBAction func profileButton(_ sender: Any) {
        let storyBoard = UIStoryboard(name: "Profile", bundle: nil)
        if let viewController = storyBoard.instantiateViewController(withIdentifier: "ProfileViewController") as? ProfileViewController {
            
            self.navigationController?.pushViewController(viewController, animated: true)
        }
    }
    
}


extension HomeViewController: UITableViewDelegate, UITableViewDataSource, TableViewInsideCollectionViewDelegate, CreatorsTableViewCellTapped, CreatorsUserTapped, ProductDelegate {
    
    
    func productTapped(index: Int, video: Videos) {
        let storyBoard = UIStoryboard(name: "UserScreen", bundle: nil)
        if let viewController = storyBoard.instantiateViewController(withIdentifier: "ProductDetailViewController") as? ProductDetailViewController {
            viewController.selectedProduct = video
            viewController.modalPresentationStyle = .overFullScreen
            self.present(viewController, animated: true, completion: nil)
        }
    }
    
    func creatorsUserTapped(index: Int, user: User) {
        let storyBoard = UIStoryboard(name: "UserScreen", bundle: nil)
        if let viewController = storyBoard.instantiateViewController(withIdentifier: "UserScreenViewController") as? UserScreenViewController {
            viewController.selectedUser = user
            viewController.modalPresentationStyle = .overFullScreen
            self.present(viewController, animated: true, completion: nil)
        }
    }
    
    func creatorsCellTaped(data: Int, video: Videos,videos: [Videos] ,index: Int) {
                let storyBoard = UIStoryboard(name: "SpotlightDetail", bundle: nil)
        if let viewController = storyBoard.instantiateViewController(withIdentifier: "SpotlightDetailViewController") as? SpotlightDetailViewController {
            viewController.video = video
            viewController.selectedIndex = index
            viewController.videoList = videos
            viewController.modalPresentationStyle = .overFullScreen
            self.present(viewController, animated: true, completion: nil)
        }
    }
    
    func cellTaped(data: Int, videoUrl: Videos, index: Int) {
        let storyBoard = UIStoryboard(name: "SpotlightDetail", bundle: nil)
        if let viewController = storyBoard.instantiateViewController(withIdentifier: "SpotlightDetailViewController") as? SpotlightDetailViewController {
            viewController.video = videoUrl
            viewController.selectedIndex = index
            viewController.videoList = self.dashData?.videos ?? []
            viewController.modalPresentationStyle = .overFullScreen
            self.present(viewController, animated: true, completion: nil)
        }
    }

    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 7 {
            return 3
        }
        return 1
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        if indexPath.section == 0 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "HomeTableViewCell", for: indexPath) as! HomeTableViewCell
            cell.spotlightData = dashData?.videos ?? []
            cell.delegate = self
            cell.productDelegate = self
            cell.spotlightCollectionView.delegate = cell
            cell.spotlightCollectionView.dataSource = cell
            cell.contentView.layer.masksToBounds = true
            cell.contentView.layer.shadowColor = UIColor.black.cgColor
            cell.spotlightCollectionView.reloadData()
            return cell
        } else if  indexPath.section == 1 {
                   let cell = tableView.dequeueReusableCell(withIdentifier: "HomeTableViewCell", for: indexPath) as! HomeTableViewCell
                   cell.spotlightData = dashData?.uniqueVideos ?? []
                   cell.delegate = self
                   cell.productDelegate = self
                   cell.spotlightCollectionView.delegate = cell
                   cell.spotlightCollectionView.dataSource = cell
                   cell.contentView.layer.masksToBounds = true
                   cell.contentView.layer.shadowColor = UIColor.black.cgColor
                   cell.spotlightCollectionView.reloadData()
                   return cell
               }
        else if indexPath.section == 2 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "CreatorsTableViewCell", for: indexPath) as! CreatorsTableViewCell
            cell.spotlightData = ["a","b","c"]
            cell.usersList = self.dashData?.users ?? []
            cell.delegate = self
            cell.creatorDelegate = self
            cell.creatorsCollectionView.reloadData()
            return cell
        
        }
//        else if indexPath.section == 1 {
//            let cell = tableView.dequeueReusableCell(withIdentifier: "HomeVideoSectionTableViewCell", for: indexPath) as! HomeVideoSectionTableViewCell
//            cell.spotlightData = dashData?.videos ?? []
//            cell.delegate = self
//            cell.spotlightCollectionView.reloadData()
//            return cell
//        }
//        else if indexPath.section == 2 {
//            let cell = tableView.dequeueReusableCell(withIdentifier: "HomeVideoSectionTableViewCell", for: indexPath) as! HomeVideoSectionTableViewCell
//            cell.spotlightData = dashData?.videos ?? []
//            cell.delegate = self
//            cell.spotlightCollectionView.reloadData()
//            return cell
//        }
        
//        else if indexPath.section == 4 {
//            let cell = tableView.dequeueReusableCell(withIdentifier: "ArticalTableViewCell", for: indexPath) as! ArticalTableViewCell
//            cell.blogs = self.dashData?.blogs ?? []
//            cell.blogCollectionView.reloadData()
//            //cell.spotlightData = ["dhruv","parth","sanjay"]
//            return cell
//        }
        
//        if indexPath.section == 0 {
//            let cell = tableView.dequeueReusableCell(withIdentifier: "HomeTableViewCell", for: indexPath) as! HomeTableViewCell
//            cell.spotlightData = dashData?.videos ?? []
//            cell.delegate = self
//            cell.spotlightCollectionView.reloadData()
//            return cell
//        } else if indexPath.section == 1 {
//            let cell = tableView.dequeueReusableCell(withIdentifier: "HomeVideoSectionTableViewCell", for: indexPath) as! HomeVideoSectionTableViewCell
//            cell.spotlightData = dashData?.videos ?? []
//            cell.delegate = self
//            cell.spotlightCollectionView.reloadData()
//            return cell
//        } else if indexPath.section == 2 {
//            let cell = tableView.dequeueReusableCell(withIdentifier: "CreatorsTableViewCell", for: indexPath) as! CreatorsTableViewCell
//            cell.spotlightData = ["a","b","c"]
//            cell.usersList = self.dashData?.users ?? []
//            cell.creatorsCollectionView.reloadData()
//            return cell
//        } else if indexPath.section == 3 {
//            let cell = tableView.dequeueReusableCell(withIdentifier: "PlaylistTableViewCell", for: indexPath) as! PlaylistTableViewCell
//            cell.spotlightData = ["a","b","c"]
//            return cell
//        } else if indexPath.section == 4 {
//            let cell = tableView.dequeueReusableCell(withIdentifier: "AccessoriesTableViewCell", for: indexPath) as! AccessoriesTableViewCell
//            cell.spotlightData = dashData?.videos ?? []
//            cell.accessoriesCollectionView.reloadData()
//            return cell
//        } else if indexPath.section == 5 {
//            let cell = tableView.dequeueReusableCell(withIdentifier: "ArticalTableViewCell", for: indexPath) as! ArticalTableViewCell
//            //cell.spotlightData = ["dhruv","parth","sanjay"]
//            return cell
//        }
//        else if indexPath.section == 7 {
//            let cell = tableView.dequeueReusableCell(withIdentifier: "ProductTableViewCell", for: indexPath) as! ProductTableViewCell
//            cell.layer.cornerRadius = 10
//
//            //cell.spotlightData = ["dhruv","parth","sanjay"]
//            return cell
//        }
        return UITableViewCell()
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
//         if indexPath.section == 0 || indexPath.section == 1 || indexPath.section == 2 {
        if indexPath.section == 0 {
            return 370
        } else if indexPath.section == 1 {
            return 370
        } else if indexPath.section == 2 {
            return 350//200
        } else {
            return 0
        }
//        if indexPath.section == 0 || indexPath.section == 1 || indexPath.section == 4 {
//            return 351
//        } else if indexPath.section == 2 {
//            return 160
//        } else if indexPath.section == 3 {
//            return 1000
//        } else if indexPath.section == 5 {
//            return 240
//        } else if indexPath.section == 7 {
//            return 100
//        } else {
//            return 0
//        }
        //        return 351
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 5
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        if section == 0 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 30, width: tableView.frame.size.width, height: 50))
            label.font = UIFont.systemFont(ofSize: 25)
            label.text = "あなたのためのビデオ"
            label.textColor = UIColor.black
            view.backgroundColor = .groupTableViewBackground
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        } else if section == 1 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 30, width: tableView.frame.size.width, height: 50))
            label.font = UIFont.systemFont(ofSize: 25)
            label.text = "あなたのためのビデオ"
            label.textColor = UIColor.black
            view.backgroundColor = .groupTableViewBackground
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        } else if section == 2 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 30, width: tableView.frame.size.width, height: 50))
            label.text = "注目のクリエイター"
            label.font = UIFont.systemFont(ofSize: 25)
            label.textColor = UIColor.black
            view.backgroundColor = .groupTableViewBackground
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        }
        /*
        else if section == 2 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 30, width: tableView.frame.size.width, height: 50))
            label.font = UIFont.systemFont(ofSize: 25)
            label.text = "Videos For you"
            label.textColor = UIColor.black
            view.backgroundColor = .groupTableViewBackground
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        }else if section == 3 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 30, width: tableView.frame.size.width, height: 50))
            label.font = UIFont.systemFont(ofSize: 25)
            label.text = "Featured Creators"
            label.textColor = UIColor.black
            view.backgroundColor = .groupTableViewBackground
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        }
       */
//        else if section == 4 {
//            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
//            let label = UILabel(frame: CGRect(x: 20, y: 30, width: tableView.frame.size.width, height: 50))
//            label.font = UIFont.systemFont(ofSize: 25)
//            label.text = "Featured Articals"
//            label.textColor = UIColor.black
//            view.backgroundColor = .groupTableViewBackground
//            view.addSubview(label)
//            self.view.addSubview(view)
//            return view
//        }
//          else  if section == 5 {
//            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
//            let label = UILabel(frame: CGRect(x: 20, y: 20, width: tableView.frame.size.width, height: 50))
//            label.text = "TEST TEXT"
//            label.textColor = UIColor.black
//            view.backgroundColor = .groupTableViewBackground
//            view.addSubview(label)
//            self.view.addSubview(view)
//            return view
//        } else if section == 6 {
//            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
//            let label = UILabel(frame: CGRect(x: 20, y: 20, width: tableView.frame.size.width, height: 50))
//            label.text = ""
//            label.textColor = UIColor.black
//            view.backgroundColor = .groupTableViewBackground
//            //view.backgroundColor = UIColor.white
//            view.addSubview(label)
//           // view.backgroundColor = UIColor.white
//            self.view.addSubview(view)
//            return view
//        }
//        else if section == 7 {
//            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width - 40, height: 18))
//            let label = UILabel(frame: CGRect(x: 20, y: 20, width: tableView.frame.size.width - 40, height: 50))
//            label.text = "Top Products"
//            label.textColor = UIColor.black
//            label.center = CGPoint(x: view.center.x, y: view.center.y + 20)
//            view.layer.cornerRadius = 10
//            view.backgroundColor = UIColor.white
//            view.backgroundColor = .groupTableViewBackground
//            view.addSubview(label)
//           // view.backgroundColor = UIColor.white
//            self.view.addSubview(view)
//            return view
//        }
        else {
            return nil
        }
     
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("dhruv")
    }
      
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if section == 7 || section == 6 {
            return 40
        }
        return 80
    }
}
//extension UIViewController {
//    func hideKeyboardWhenTappedAround(view: UIView) {
//        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(UIViewController.dismissKeyboard))
//        tap.cancelsTouchesInView = false
//        view.addGestureRecognizer(tap)
//    }
//
//    @objc func dismissKeyboard() {
//        view.endEditing(true)
//    }
//}
//
//
//extension UISearchBar {
//
//    func hideKeyboardWhenTappedAround()  {
//        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
//        tap.cancelsTouchesInView = true
//        self.addGestureRecognizer(tap)
//    }
//
//    @objc func dismissKeyboard() {
//        self.endEditing(true)
//    }
//}
