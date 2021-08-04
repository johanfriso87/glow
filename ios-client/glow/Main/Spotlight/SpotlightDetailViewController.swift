//
//  SpotlightDetailViewController.swift
//  glow
//
//  Created by Dreams on 06/07/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit
import AVFoundation
import ExpandingMenu
import MultiProgressView

class SpotlightDetailViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource {
    
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var moreView: UIView!
    @IBOutlet weak var userButton: UIButton!
    @IBOutlet weak var btn: ExpandingMenuButton!
    var timeObserver: Any?
    var video: Videos?
    var videoList: [Videos] = []
    var selectedIndex: Int?
    var currentIndexPath: IndexPath?
    var playerTimeObserver: Any?
    var isLiked: Bool = false
    override func viewDidLoad() {
        super.viewDidLoad()
        self.collectionView.delegate = self
        self.collectionView.dataSource = self
        self.collectionView.isScrollEnabled = false
        self.setUpFlotyButton()
        self.userButton.addTarget(self, action: #selector(self.userButtonTapped(_:)), for: .touchUpInside)
    }

    override func viewWillAppear(_ animated: Bool) {

    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
    }


    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
       
    }
    
    func setUpFlotyButton() {
        let menuButtonSize: CGSize = CGSize(width: 40, height: 40)
        let menuButton = ExpandingMenuButton(frame: CGRect.zero, image: UIImage(named: "ic_menu")!, rotatedImage: UIImage(named: "ic_menu")!)
        menuButton.center = CGPoint(x: self.view.bounds.width - 30, y: 90 - topPadding)
        self.view.addSubview(menuButton)
//        self.moreView.addSubview(menuButton)
        menuButton.expandingDirection = .bottom
        menuButton.expandingAnimations = .all
        let item1 = ExpandingMenuItem(size: menuButtonSize, title: "Share Video", image: UIImage(named: "ic_share")!, highlightedImage: UIImage(named: "ic_share")!, backgroundImage: UIImage(named: ""), backgroundHighlightedImage: UIImage(named: "")) { () -> Void in
            // Do some action
            let storyBoard = UIStoryboard(name: "Main", bundle: nil)
            if let viewController = storyBoard.instantiateViewController(withIdentifier: "VideoSaveViewController") as? VideoSaveViewController {
                if let url = URL(string: (BASEVIDEOURL) + (self.video?.videoUrl ?? "")) {
                    viewController.videoUrl = url
                    let searchData = Search(asin: self.video?.asin ?? "",
                                            price: "",
                                            title:  self.video?.productName ?? "", thumbnail: self.video?.productThumbnailUrl ?? "",
                                            url: self.video?.productUrl ?? "", images: [])
                    viewController.searchProduct = searchData
//                    viewController.searchProduct?.asin =
//                    viewController.searchProduct?.thumbnail =
//                    viewController.searchProduct?.title =
//                    viewController.searchProduct?.url = self.video?.productUrl
                }
                viewController.modalPresentationStyle = .fullScreen
                self.present(viewController, animated: true, completion: nil)
                //                self.navigationController?.pushViewController(viewController, animated: true)
            }
            
        }

    
        
        let item2 = ExpandingMenuItem(size: menuButtonSize, title: "Subscribe to user", image: UIImage(named: "ic_plus_blue")!, highlightedImage: UIImage(named: "ic_plus_blue")!, backgroundImage: UIImage(named: ""), backgroundHighlightedImage: UIImage(named: "")) { () -> Void in
                    // Do some action
                self.subscribeUser()
            
            }
        
        let item3 = ExpandingMenuItem(size: menuButtonSize, title: "Report to Video", image: UIImage(named: "ic_warning")!, highlightedImage: UIImage(named: "ic_warning")!, backgroundImage: UIImage(named: ""), backgroundHighlightedImage: UIImage(named: "")) { () -> Void in
            // Do some action
        }
                
        menuButton.addMenuItems([item1, item2, item3])
    }
    @objc func userButtonTapped(_ sender: UIButton) {
        self.subscribeUser()
    }
    func subscribeUser() {
        if let id = self.video?.userId as? [String:Any] {
            let tempId = id["_id"] as? String ?? ""
            let param = ["targetUserId" : tempId]
            Utility.showProgress()
            LikeRequest.subscribeUser(param: param) { (success, string, error) in
                Utility.dismissProgress()
                if error == nil {
                    
                } else {
                    Utility.alert(message: error?.localizedDescription ?? "Something went wrong")
                }
            }
        }
    }
    
    func removeSubscribeUser() {
        let param = ["targetUserId" : video?._id ?? ""]
        Utility.showProgress()
        LikeRequest.removeSubscribeUser(param: param) { (success, string, error) in
            Utility.dismissProgress()
            if error == nil {
                
            } else {
                Utility.alert(message: error?.localizedDescription ?? "Something went wrong")
            }
        }
    }
 
    func addPullUpController(animated: Bool, video: Videos) {
        let pullUpControllers: SlideUpViewController = UIStoryboard(name: "SpotlightDetail",bundle: nil).instantiateViewController(withIdentifier: "SlideUpViewController") as! SlideUpViewController
        pullUpControllers.video = video
        if let user = video.userId as? [String:Any] {
            let name = user["name"] as? String ?? ""
            self.userButton.setTitle(name, for: .normal)
        }
        //pullUpControllers.isLiked = self.isLiked
        let pullUpController = pullUpControllers
        _ = pullUpController.view // call pullUpController.viewDidLoad()
        addPullUpController(pullUpController,
                            initialStickyPointOffset: pullUpControllers.initialPointOffset,
                            animated: false)
    
    }
    

     
     @IBAction func backButton(_ sender: UIButton) {
        NotificationCenter.default.post(name: NSNotification.Name(rawValue: "StopLandscapePlay"), object: nil)
        self.dismiss(animated: false, completion: nil)
     }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}

extension SpotlightDetailViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.videoList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "SpotlightDetailCollectionViewCell", for: indexPath) as! SpotlightDetailCollectionViewCell
        cell.playbackSlider.setThumbImage(UIImage(), for: .normal)
        let swipeToLeft = UISwipeGestureRecognizer(target: self, action: #selector(changePageOnSwipe(_:)))
        let swipeToRight = UISwipeGestureRecognizer(target: self, action: #selector(changePageOnSwipe(_:)))
        swipeToLeft.direction = .right
        swipeToRight.direction = .left
        cell.contentView.addGestureRecognizer(swipeToLeft) // Gesture are added to the top view that should handle them
        cell.contentView.addGestureRecognizer(swipeToRight)
        
        if self.selectedIndex != nil {
            self.addPullUpController(animated: true, video: self.videoList[selectedIndex!])
            
            if let urlStr = self.videoList[selectedIndex!].videoUrl {
                if let url = URL(string: BASE + urlStr) {
                    print("vurl\(url)")
                    cell.addPlayer(for: url)
                    self.collectionView(self.collectionView, didSelectItemAt: indexPath)
//                    cell.player.url = url
//                    cell.player.playFromBeginning()
                    self.selectedIndex = nil
                }
            }
        }
        
        return cell
    }
    
    func updateSlider(elapsedTime: CMTime, cell: SpotlightDetailCollectionViewCell) {
        if let playerDuration = cell.playerView.player?.currentItem?.duration{
            if CMTIME_IS_INVALID(playerDuration) {
                cell.playbackSlider.minimumValue = 0.0
                       return
                   }
                   let duration = Float(CMTimeGetSeconds(playerDuration))
                   if duration.isFinite && duration > 0 {
                    cell.playbackSlider.minimumValue = 0.0
                    cell.playbackSlider.maximumValue = duration
                       let time = Float(CMTimeGetSeconds(elapsedTime))
                    cell.playbackSlider.setValue(time, animated: true)
                   }

        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        self.currentIndexPath = indexPath
    }
    
    func playVideoAfterDaraging(newIndexPath: IndexPath) {
        if let cell = self.collectionView.cellForItem(at: newIndexPath) as? SpotlightDetailCollectionViewCell {
            
            if newIndexPath == self.collectionView.indexPath(for: cell) {
                
                self.addPullUpController(animated: true, video: self.videoList[newIndexPath.item])
                self.video = self.videoList[newIndexPath.item]
                if let urlStr = self.videoList[newIndexPath.item].videoUrl {
                    if let url = URL(string: BASE + urlStr) {
                        print("vurl\(url)")
                        cell.addPlayer(for: url)
//                        cell.player.stop()
//                        cell.player.url = url
//                        cell.player.playFromBeginning()
                    }
                }
            }
        }
        
    }
   @objc func changePageOnSwipe(_ gesture: UISwipeGestureRecognizer) {
        guard let indexPath = self.currentIndexPath else {
            // When the page loads and there is no current selected VC, the swipe will not work
            // unless you set an initial value for self.currentIndexPath
            return
        }

        var newIndex = indexPath    // if self.collectionview.indexPathsForSelectedItems is not empty, you can also use it instead of having a self.currentIndexPath property

        // move in the opposite direction for the movement to be intuitive
        // So swiping " <-- " should show index on the right (row + 1)
        if gesture.direction == .left {
            //self.selectedIndex! += 1
            newIndex = IndexPath(row: newIndex.row+1, section: newIndex.section)
        } else {
            //.selectedIndex! -= 1
            newIndex = IndexPath(row: newIndex.row-1, section: self.currentIndexPath!.section)
        }

        if canPresentPageAt(indexPath: newIndex) {
            // Programmatically select the item and make the collectionView scroll to the correct number
            self.collectionView.selectItem(at: newIndex, animated: true, scrollPosition: UICollectionView.ScrollPosition.centeredHorizontally)
            if let cell = self.collectionView.cellForItem(at: currentIndexPath!) as? SpotlightDetailCollectionViewCell {
//                cell.player.stop()
                cell.stopPlayback()
            }
            if let cell = self.collectionView.cellForItem(at: newIndex) as? SpotlightDetailCollectionViewCell {
                let index = newIndex.item
                let newIndexPath = IndexPath(item: index, section: 0)
                self.playVideoAfterDaraging(newIndexPath: newIndexPath)
                // The delegate method is not automatically called when you select programmatically
                self.collectionView(self.collectionView, didSelectItemAt: newIndex)
            }
            
        } else {
            // Do something when the landing page is invalid (like if the swipe would got to page at index -1 ...)
            // You could choose to direct the user to the opposite side of the collection view (like the VC at index self.items.count-1
            print("You are tying to navigate to an invalid page")
        }
    }
    func canPresentPageAt(indexPath: IndexPath) -> Bool {
        // Do necessary checks to ensure that the user can indeed go to the desired page
        // Like: check if you have a non-nil ViewController at the given index. (If you haven't implemented index 3,4,5,... it should return false)
        //
        // This method can be called either from a swipe
        // or another way when you need it
        if indexPath.row < 0 || indexPath.row >= self.videoList.count {
            print("You are trying to go to a non existing page")
            return false
        } else {
            print("If you haven't implemented a VC for page 4 it will crash here")
            return true;
        }
    }
    
    @objc func backButtons(_ sender: UIButton) {
        NotificationCenter.default.post(name: NSNotification.Name(rawValue: "StopLandscapePlay"), object: nil)
        self.dismiss(animated: false, completion: nil)
    }
    
    @objc func moreButton(_ sender: UIButton) {
        
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return UIScreen.main.bounds.size
    }
}

//extension SpotlightDetailViewController: PlayerDelegate {
//
//    func playerReady(_ player: Player) {
//        print("\(#function) ready")
//    }
//
//    func playerPlaybackStateDidChange(_ player: Player) {
//        print("\(#function) \(player.playbackState.description)")
//    }
//
//    func playerBufferingStateDidChange(_ player: Player) {
//    }
//
//    func playerBufferTimeDidChange(_ bufferTime: Double) {
//    }
//
//    func player(_ player: Player, didFailWithError error: Error?) {
//        print("\(#function) error.description")
//    }
//
//}

// MARK: - PlayerPlaybackDelegate

//extension SpotlightDetailViewController: PlayerPlaybackDelegate {
//
//    func playerCurrentTimeDidChange(_ player: Player) {
//        self.updateSlider(elapsedTime: player.currentTime)
//    }
//
//    func playerPlaybackWillStartFromBeginning(_ player: Player) {
//    }
//
//    func playerPlaybackDidEnd(_ player: Player) {
//    }
//
//    func playerPlaybackWillLoop(_ player: Player) {
//    }
//
//    func playerPlaybackDidLoop(_ player: Player) {
//    }
//}

open class CustomSlider : UISlider {
    @IBInspectable open var trackWidth:CGFloat = 2 {
        didSet {setNeedsDisplay()}
    }

    override open func trackRect(forBounds bounds: CGRect) -> CGRect {
        let defaultBounds = super.trackRect(forBounds: bounds)
        return CGRect(
            x: defaultBounds.origin.x,
            y: defaultBounds.origin.y + defaultBounds.size.height/2 - trackWidth/2,
            width: defaultBounds.size.width,
            height: trackWidth
        )
    }
}

