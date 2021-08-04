//
//  VideoSaveViewController.swift
//  glow
//
//  Created by Dreams on 02/07/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit
import AVKit
import Photos
import SCSDKCreativeKit
import FBSDKShareKit
import FBSDKCoreKit
import TwitterKit
import MaterialComponents.MaterialProgressView

class VideoSaveViewController: UIViewController, UIDocumentInteractionControllerDelegate , UIGestureRecognizerDelegate, UIScrollViewDelegate {
    @IBOutlet weak var playerView: PlayerViewClass!{
        didSet {
            // playerView.isHidden = true
        }
    }
    @IBOutlet weak var addedView: UIView!
    @IBOutlet weak var slideshow: ImageSlideshow!
    @IBOutlet weak var shareLogoImageView: UIImageView!
    @IBOutlet weak var shareTitleButton: UIButton!
    @IBOutlet weak var heightConst: NSLayoutConstraint!
    @IBOutlet weak var widthConst: NSLayoutConstraint!
    @IBOutlet weak var scrollViewCenterConstant: NSLayoutConstraint!
    @IBOutlet weak var linkButton: UIButton!
    @IBOutlet weak var saveButton: UIButton!
    @IBOutlet weak var sticker: UIButton!
    @IBOutlet weak var moreButton: UIButton!
    @IBOutlet weak var progressView: UIProgressView!
    @IBOutlet weak var downloadLabel: UILabel!
     var shareprogressCallback:((_ success: Bool,_ progress: Float, _ videoObj: Any?)-> Void)?
    let localSource = [BundleImageSource(imageString: "ic_instagram_story"), BundleImageSource(imageString: "ic_instagram_feed"), BundleImageSource(imageString: "ic_snapchat"), BundleImageSource(imageString: "ic_message"), BundleImageSource(imageString: "ic_line"),
                       BundleImageSource(imageString: "ic_facebook"),
                       BundleImageSource(imageString: "ic_twitter")]
    var currentHeight = 0.0
    var currentWidth = 0.0
    var staticHeight = 550
    var secondHeight = 570
    var tempHeight = 0.0
    var secondTempHeight = 0.0
    var tabs:[UIViewController] = []
    let numberOfTabs = 5
    var currentTab = 0
    var arrayString = ["", "", "", "", ""]
    var isSelected = true
    var videoUrl: URL! = nil
    var videoUploader = VideoUploader()
    var videoData: Data! = nil
    var snapAPI: SCSDKSnapAPI?
    var productID = ""
    var searchProduct: Search?
    var selectedThumb: UIImage? = nil
    var slides: [SlideView] = []
    var isFastForward = false
    var scrollViewFrame = CGRect.zero
    var playerFrmae = CGRect.zero
    
    private let byteFormatter: ByteCountFormatter = {
           let formatter = ByteCountFormatter()
           formatter.allowedUnits = [.useKB, .useMB]
           return formatter
       }()
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //        self.shareTitleButton.backgroundColor = UIColor.blue
        self.shareTitleButton.applyGradient(colours: [UIColor.hexStringToUIColor(hex: "5851DB"), UIColor.hexStringToUIColor(hex: "E1306C"), UIColor.hexStringToUIColor(hex: "FFDC80")],name:"0")
        self.shareTitleButton.AddText(title: "Share On Your Story",name: "0title")
        self.linkButton.layer.cornerRadius = 25
        self.saveButton.layer.cornerRadius = 25
        self.sticker.layer.cornerRadius = 25
        self.moreButton.layer.cornerRadius = 25
        self.progressView.clipsToBounds = true
        self.progressView.transform.scaledBy(x: 0.5, y: 10)
        self.progressView.layer.cornerRadius = 15
        //snapAPI = SCSDKSnapAPI()
        // Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool) {
        if let url = videoUrl {
            self.addPlayer(for: url)
        }
        let height = (self.heightConst.constant * UIScreen.main.bounds.height) / 896
        //        let width = (self.widthConst.constant * UIScreen.main.bounds.width) / 414
        if self.view.frame.height != 896 {
            self.staticHeight = Int(height - 45)
            self.secondHeight = Int(height - 25)
            self.heightConst.constant = height - 70
        }
        self.currentHeight = Double(height)
        
        self.tempHeight = currentHeight - Double(staticHeight)
        self.secondTempHeight = currentHeight - Double(secondHeight)
        self.setUpImageView()
        if self.view.frame.width != 414 {
            let width = (self.widthConst.constant * self.view.frame.width) / 414
            self.currentWidth = Double(width)
            self.widthConst.constant = width - 70
        }
        self.progressView.isHidden = true
        self.downloadLabel.isHidden = true
    }
    
    func setUpImageView() {
        slideshow.contentScaleMode = UIViewContentMode.scaleAspectFit
        slideshow.delegate = self
        slideshow.setImageInputs(localSource)
        
    }
 
    func shareVideoOnInstagram() {
         
        self.downloadVideo { (success, response, error) in
            
        }
        
        self.shareprogressCallback = {
            (success, progress, obj) in
            
            if progress == 1 {
                if let storiesUrl = URL(string: "instagram-stories://share") {
                    if UIApplication.shared.canOpenURL(storiesUrl) {
                        guard let imageData = try? Data(contentsOf: self.videoUrl!) else { return }
                        let pasteboardItems: [String: Any] = [
                            "com.instagram.sharedSticker.backgroundVideo": imageData,
                            "com.instagram.sharedSticker.backgroundTopColor": "#636e72",
                            "com.instagram.sharedSticker.backgroundBottomColor": "#b2bec3"
                        ]
                        let pasteboardOptions = [
                            UIPasteboard.OptionsKey.expirationDate: Date().addingTimeInterval(300)
                        ]
                        UIPasteboard.general.setItems([pasteboardItems], options: pasteboardOptions)
                        UIApplication.shared.open(storiesUrl, options: [:], completionHandler: nil)
                    } else {
                        print("User doesn't have instagram on their device.")
                    }
                }
            }
        }
        
        
       
    }
   
    func downloadVideo(callback: ((_ success: Bool, _ data: Any?, _ error: Error?) -> Void)?) {
        if let url = self.videoUrl {

            let config = URLSessionConfiguration.default
            let session = URLSession(configuration: config, delegate: self, delegateQueue: nil)

            // Don't specify a completion handler here or the delegate won't be called
            session.downloadTask(with: url).resume()
        }

    }
    
    func downloadImage(image: String,callback: ((_ success: Bool, _ data: Any?, _ error: Error?) -> Void)?) {
        if let url = self.videoUrl {

            let config = URLSessionConfiguration.default
            let session = URLSession(configuration: config, delegate: self, delegateQueue: nil)

            // Don't specify a completion handler here or the delegate won't be called
            session.downloadTask(with: url).resume()
        }

    }

    
    func shareVideoOnInstagramFeed(){
        self.downloadVideo { (success, response, error) in
                    
        }
        let videoImageUrl = self.videoUrl
        //Utility.showProgress()
        DispatchQueue.global(qos: .background).async {
            if let url = self.videoUrl,
                let urlData = NSData(contentsOf: url) {
                let documentsPath = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0];
                let filePath="\(documentsPath)/glow.mp4"
                
                DispatchQueue.main.async {
                    urlData.write(toFile: filePath, atomically: true)
                    PHPhotoLibrary.shared().performChanges({
                        PHAssetChangeRequest.creationRequestForAssetFromVideo(atFileURL: URL(fileURLWithPath: filePath))
                    }) { completed, error in
                        
                        if completed {
                            DispatchQueue.main.async {
                                let videoFilePath = self.videoUrl.absoluteString
                                let instagramURL = NSURL(string: "instagram://app")
                                if (UIApplication.shared.canOpenURL(instagramURL! as URL)) {
                                    
                                    let url = URL(string: ("instagram://library?AssetPath="+filePath))
                                    
                                    if UIApplication.shared.canOpenURL(url!) {
                                        //Utility.dismissProgress()
                                        UIApplication.shared.open(url!, options: [:], completionHandler:nil)
                                    }
                                    
                                } else {
                                    // Utility.dismissProgress()
                                    Utility.alert(message: "Instagram isn't installed")
                                    print(" Instagram isn't installed")
                                }
                            }
                            if (error != nil) {
                                //Utility.dismissProgress()
                            }
                            
                        }
                        
                    }
                }
            }
        }
    }
    
    func shareOnMessage() {
        self.downloadVideo { (success, response, error) in
                    
        }
        self.shareprogressCallback = {
            (success, progress, obj) in
            if progress == 1 {
                let sms: String = "sms: &body=Please check out my review "+self.videoUrl.absoluteString
                let strURL: String = sms.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!
                UIApplication.shared.open(URL.init(string: strURL)!, options: [:], completionHandler: nil)

            }
        }
//        self.startAndShowProgressView(flot: CGFloat(1))
    }
    
    func uploadVideoOnFacebook() {
        
        self.downloadVideo { (success, response, error) in
            
        }
        
        self.shareprogressCallback = {
            (success, progress, obj) in
            if progress == 1 {
                let content = ShareLinkContent()
                content.contentURL = self.videoUrl
                let dialog = ShareDialog()
                dialog.fromViewController = self
                dialog.shareContent = content
                dialog.mode = .shareSheet
                dialog.show()
            }
        }
                
        
        
//        let fetchOptions = PHFetchOptions()
//        fetchOptions.sortDescriptors = [NSSortDescriptor(key: "creationDate", ascending: false)]
//        let fetchResult = PHAsset.fetchAssets(with: .video, options: fetchOptions)
//        if let lastAsset = fetchResult.firstObject {
//            let localID = lastAsset.localIdentifier
//            let assetID = localID.replacingOccurrences(of: "/.*", with: "", options: NSString.CompareOptions.regularExpression, range: nil)
//            let ext = "mp4"
//            let assetURLStr = "assets-library://asset/asset.\(ext)?id=\(assetID)&ext=\(ext)"
//            let video = ShareVideo(videoURL: URL(string: self.videoUrl.absoluteString)!)
//            let content = ShareVideoContent()
//            content.video = video
//            let dialog = FBSDKShareKit.ShareDialog(fromViewController: self, content: content, delegate: self)
//            dialog.mode = .automatic
//            do {
//                try dialog.show()
//            } catch {
//                print(error)
//            }
//        }
        
        
    }
    
    func dialog(withContent content: SharingContent) -> ShareDialog {
        return ShareDialog(
            fromViewController: self,
            content: content,
            delegate: self
        )
    }
    
    func shareOnSnapChat(completionHandler: (Bool, Error?) ->()) {
        let videoUrl = self.videoUrl /* prepare a local video URL */
        let video = SCSDKSnapVideo(videoUrl: videoUrl!)
        let videoContent = SCSDKVideoSnapContent(snapVideo: video)
        view.isUserInteractionEnabled = false
        let api = SCSDKSnapAPI(content: videoContent)
        api.startSnapping { error in
            
            if let error = error {
                print(error.localizedDescription)
            } else {
                // success
                
            }
        }
    }
    
    func shareOnTwitter() {

        self.downloadVideo { (success, response, error) in
            
        }
        
        self.shareprogressCallback = {
            (success, progress, obj) in
            
            if progress == 1 {
                let tweetText = "Check out my Glow product review  "
                let tweetUrl = self.videoUrl
                
                let shareString = "https://twitter.com/intent/tweet?text=\(tweetText)&url=\(tweetUrl)"
                
                // encode a space to %20 for example
                let escapedShareString = shareString.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed)!
                
                // cast to an url
                let url = URL(string: escapedShareString)
                
                // open in safari
                if(UIApplication.shared.canOpenURL(url!)){
                    UIApplication.shared.open(url!)
                }else {
                    let alertView = UIAlertView();
                    alertView.addButton(withTitle: "Ok");
                    alertView.title = "Twitter sharing";
                    alertView.message = "Twitter is not installed on your iphone.";
                    alertView.show();
                }
            }
        }
       
    }
    
    
    func shareOnLine() {
        //let path = NSBundle.mainBundle().pathForResource(videoName, ofType:"m4v")!
        let fileUrl = self.videoUrl
        
        let docController = UIDocumentInteractionController(url: fileUrl!)
        docController.uti = "jp.naver.line.ios"
        docController.presentOpenInMenu(from: CGRect.zero, in: self.view, animated: true)
    }
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destination.
     // Pass the selected object to the new view controller.
     }
     */
    func videoUpload() {
        self.videoUploader.progressCallback = {(success, percentage,  obj) in
            print("\(percentage)")
            DispatchQueue.main.async {
                self.videoUploader.progressCallback = {
                    (success, progress, obj) in
                    DispatchQueue.main.async {
                        self.progressView.isHidden = false
                        self.progressView.setProgress(progress, animated: true)
                        if progress == 1 {
                            self.progressView.isHidden = true
                            self.progressView.setProgress(progress, animated: true)
                        }
                    }
                    
                }
            }
        }
        let param = [
            "productName": self.searchProduct?.title,
            "asin" : self.searchProduct?.asin,
            "productUrl": self.searchProduct?.url,
            "productThumbnailUrl": self.searchProduct?.thumbnail 
        ]
        //Utility.showProgress()
        self.videoUploader.apiCall(UploadVideo,
                                   data: param as [String : Any],
                                   filePathKey: ["video"],
                                   videoDataKey: videoData,
                                   method: .POST,
                                   imagePathKey: "thumbnail",
                                   imageDataKey: self.selectedThumb?.jpegData(compressionQuality: 1.0)!,
                                   videoListObj: nil) { (success, response, error) in
                                    Utility.dismissProgress()
                                    if error == nil {
                                        self.view.window!.rootViewController?.dismiss(animated: false, completion: nil)
                                        self.dismiss(animated: true, completion: nil)
                                        //                self.dismiss(animated: true, completion: nil)
                                        //                self.navigationController?.popToRootViewController(animated: true)
                                    } else {
                                        Utility.alert(message: error?.localizedDescription ?? "", button1: "Ok") { (index) in
                                            self.view.window!.rootViewController?.dismiss(animated: false, completion: nil)
                                            self.dismiss(animated: true, completion: nil)
                                            //                  self.dismiss(animated: true, completion: nil)
                                            //                  self.navigationController?.popViewController(animated: true)
                                        }
                                    }
        }
    }
    
    
    func saveSticker() {
        
//        Utility.showProgress()
        self.downloadImage(image: self.searchProduct?.thumbnail ?? "") { (success, response, error) in
            
        }
        
        self.shareprogressCallback = {
            (success, progress, obj) in
            if progress == 1 {
                let imageView = UIImageView()
                if let sticker = self.searchProduct?.thumbnail {
                    if let url = URL(string: sticker) {
                        imageView.getImageFromURL(url: url, indexPath: nil) { (image, index) in
                            self.saveImage(image: image)
                        }
                    }
                }
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
    
    @IBAction func shareButton(_ sedner: UIButton) {
        
        switch self.currentTab {
        case 0:
            self.shareVideoOnInstagram()
            break;
        case 1:
            self.shareVideoOnInstagramFeed()
            break;
        case 2:
            self.shareOnSnapChat { (success, error) in
                
            }
            break
        case 3:
            self.shareOnMessage()
            break
        case 4:
            //line
            self.shareOnLine()
            break
        case 5:
            self.uploadVideoOnFacebook()
            break;
        case 6:
            shareOnTwitter()
            break;
        case 7:
            break
            
        default:
            break
        }
    }
    
    func videoSaveInAlbum() {

        self.downloadVideo { (success, response, error) in
            
        }
        
        self.shareprogressCallback = {
            (success, progress, obj) in
            
        }
        
        let videoImageUrl = self.videoUrl
        DispatchQueue.global(qos: .background).async {
            if let url = self.videoUrl,
                let urlData = NSData(contentsOf: url) {
                let documentsPath = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0];
                let filePath="\(documentsPath)/glow.mp4"
                DispatchQueue.main.async {
                    urlData.write(toFile: filePath, atomically: true)
                    PHPhotoLibrary.shared().performChanges({
                        PHAssetChangeRequest.creationRequestForAssetFromVideo(atFileURL: URL(fileURLWithPath: filePath))
                    }) { completed, error in
                        if completed {
                            print("Video is saved!")
                            Utility.alert(message: "Your video saved successfully!")
                        }
                    }
                }
            }
        }
    }
    
    func moreOption() {
        // Setting description
        let firstActivityItem = self.videoUrl.absoluteString
        
        // Setting url
        // let secondActivityItem : NSURL = NSURL(string: "http://your-url.com/")!
        
        // If you want to use an image
        //let image : UIImage = UIImage(named: "your-image-name")!
        let activityViewController : UIActivityViewController = UIActivityViewController(
            activityItems: [firstActivityItem], applicationActivities: nil)
        
        // This lines is for the popover you need to show in iPad
        activityViewController.popoverPresentationController?.sourceView = UIView(frame: .zero)
        
        // This line remove the arrow of the popover to show in iPad
        activityViewController.popoverPresentationController?.permittedArrowDirections = UIPopoverArrowDirection.down
        activityViewController.popoverPresentationController?.sourceRect = CGRect(x: 150, y: 150, width: 0, height: 0)
        
        // Pre-configuring activity items
        //        activityViewController.activityItemsConfiguration = [
        //        UIActivity.ActivityType.message
        //        ] as? UIActivityItemsConfigurationReading
        
        // Anything you want to exclude
        activityViewController.excludedActivityTypes = [
            UIActivity.ActivityType.postToWeibo,
            UIActivity.ActivityType.print,
            UIActivity.ActivityType.assignToContact,
            UIActivity.ActivityType.saveToCameraRoll,
            UIActivity.ActivityType.addToReadingList,
            UIActivity.ActivityType.postToFlickr,
            UIActivity.ActivityType.postToVimeo,
            UIActivity.ActivityType.postToTencentWeibo,
            UIActivity.ActivityType.postToFacebook
        ]
        
        //        activityViewController.isModalInPresentation = true
        
        self.present(activityViewController, animated: true, completion: nil)
    }
    
    
    @IBAction func doneButton(_ sender: UIButton) {
        self.videoUpload()
    }
    
    @IBAction func saveButton(_ sender: UIButton) {
        self.videoSaveInAlbum()
    }
    
    @IBAction func linkButton(_ sender: UIButton) {
        print(videoUrl)
        if let str = videoUrl {
            //            if let url = URL(string: str) {
            UIApplication.shared.open(str, options: [:]) { (success) in
                print(success)
            }
            //            }
        }
    }
    
    @IBAction func stickerButton(_ sender: UIButton) {
        self.saveSticker()
    }
    
    @IBAction func moreButton(_ sender: UIButton) {
        self.moreOption()
    }
    
    
    @IBAction func backButton(_ sender: UIButton) {
        self.navigationController?.popToRootViewController(animated: true)
    }
}
extension VideoSaveViewController: SharingDelegate {
    
    func sharer(_ sharer: Sharing, didCompleteWithResults results: [String : Any]) {
        print(results)
    }
    
    func sharer(_ sharer: Sharing, didFailWithError error: Error) {
        // presentAlert(for: error)
    }
    
    func sharerDidCancel(_ sharer: Sharing) {
        // presentAlert(title: "Cancelled", message: "Sharing cancelled")
    }
    
    
}


extension VideoSaveViewController {
    func addPlayer(for url: URL, timeInterval: TimeInterval = 0.0) {
        if self.playerView.playerLayer.player != nil {
            self.playerView.playerLayer.player = nil
            // self.stopPlayback()
        }
        self.playerView.playerLayer.player = AVPlayer(url: url)
        self.playerView.playerLayer.videoGravity = .resizeAspectFill
        let cmTime = CMTime(seconds: timeInterval, preferredTimescale: 1)
        self.playerView.playerLayer.player?.seek(to: cmTime)
        //        self.playerView.playerLayer.player
        if self.isFastForward {
            self.playerView.playerLayer.player?.rate = 2.0
            self.playerView.playerLayer.player?.volume = 0.4
        } else {
            self.playerView.playerLayer.player?.rate = 2.0
        }
        
        self.playerView.playerLayer.player?.addObserver(self, forKeyPath: #keyPath(AVPlayerItem.status), options: [.old, .new], context: nil)
        self.playerView.playerLayer.masksToBounds = true
        self.startPlayback()
    }
    
    @objc func stopPlay() {
        self.stopPlayback()
    }
    
    func stopPlayback(){
        self.playerView.playerLayer.player?.removeObserver(self, forKeyPath:  #keyPath(AVPlayerItem.status))
        self.playerView.playerLayer.player?.pause()
        self.playerView.playerLayer.player = nil
    }
    func startPlayback() {
        self.playerView.playerLayer.player?.play()
        
    }
    
    func statickPlayerStop() {
    }
    
    override func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey : Any]?, context: UnsafeMutableRawPointer?) {
        if object as AnyObject? === self.playerView.playerLayer.player {
            if keyPath == "status" {
                if self.playerView.playerLayer.player!.status == .readyToPlay {
                    self.playerView.playerLayer.player!.play()
                    print("PlayLS")
                }
            }
        }
    }
    
}

extension VideoSaveViewController: ImageSlideshowDelegate {
    
    func didScroll(scrollView: UIScrollView) {
        self.scrollViewCenterConstant.constant = -20.0
        if scrollView.contentOffset.y > 0 || scrollView.contentOffset.y < 0 {
            scrollView.contentOffset.y = 0
        }
        
        let offSet = scrollView.contentOffset.x / self.view.frame.width
        // print("offSet\(offSet)")
        if offSet <= 2 {
            print("1")
            
            let newOffset = offSet * CGFloat(self.tempHeight)
            let hg = currentHeight - Double(newOffset)
            self.heightConst.constant = CGFloat(hg)
            print("hg\(hg)")
            //            self.widthConst.constant = CGFloat(260)
            //            self.scrollViewCenterConstant.constant = -40
        } else if offSet <= 3 {
            print("\(currentHeight)")
            if self.view.frame.height == 896 {
                let tempOffSet = 3 - offSet
                let newOffset = tempOffSet * CGFloat(self.secondTempHeight)
                let hg = (currentHeight - 25) - Double(newOffset)
                print("hg\(hg)")
                self.heightConst.constant = CGFloat(hg)
                self.scrollViewCenterConstant.constant = -25
            } else {
                let tempOffSet = 3 - offSet
                let newOffset = tempOffSet * CGFloat(self.tempHeight)
                let hg = (currentHeight - 60) - Double(newOffset)
                print("hg\(hg)")
                self.heightConst.constant = CGFloat(hg)
                self.scrollViewCenterConstant.constant = -30
                print("hg\(hg)")
            }
            
        } else if offSet <= 4 {
            let tempOffSet = 4 - offSet
            let newOffset = offSet * CGFloat(self.secondTempHeight)
            let hg = currentHeight - Double(newOffset)
            self.heightConst.constant = CGFloat(hg)
            print("hg\(hg)")
            //            self.widthConst.constant = CGFloat(260)
            //            self.scrollViewCenterConstant.constant = -20
        } else if offSet <= 5 {
            let tempOffSet = 5 - offSet
            let newOffset = offSet * CGFloat(self.tempHeight)
            let hg = currentHeight - Double(newOffset)
            self.heightConst.constant = CGFloat(hg)
            print("hg\(hg)")
            //            self.widthConst.constant = CGFloat(260)
            //            self.scrollViewCenterConstant.constant = -20
        } else if offSet <= 6 {
            if self.view.frame.height == 896 {
                let tempOffSet = 6 - offSet
                let newOffset = offSet * CGFloat(self.tempHeight)
                let hg = currentHeight - Double(newOffset)
                self.heightConst.constant = CGFloat(hg)
                print("hg\(hg)")
            } else {
                let tempOffSet = 6 - offSet
                let newOffset = offSet * CGFloat(self.secondTempHeight)
                let hg = currentHeight - Double(newOffset)
                self.heightConst.constant = CGFloat(hg)
                print("hg\(hg)")
            }
            
        } else if offSet <= 7 {
            if self.view.frame.height == 896 {
                let tempOffSet = 7 - offSet
                let newOffset = offSet * CGFloat(self.tempHeight)
                let hg = currentHeight - Double(newOffset)
                self.heightConst.constant = CGFloat(hg)
                print("hg\(hg)")
            } else {
                let tempOffSet = 7 - offSet
                let newOffset = offSet * CGFloat(self.secondTempHeight)
                let hg = currentHeight - Double(newOffset)
                self.heightConst.constant = CGFloat(hg)
                print("hg\(hg)")
            }
        }
    }
    
    
    
    func imageSlideshow(_ imageSlideshow: ImageSlideshow, didChangeCurrentPageTo page: Int) {
        self.currentTab = page
        print("current page:", page)
        switch self.currentTab {
        case 0:
            
            
            
            self.shareTitleButton.applyGradient(colours: [UIColor.hexStringToUIColor(hex: "5851DB"), UIColor.hexStringToUIColor(hex: "E1306C"), UIColor.hexStringToUIColor(hex: "FFDC80")],name:"0")
            
            self.shareTitleButton.AddText(title: "Share On Your Story",name: "0title")
            
            
            
            break;
        case 1:
            
            
            
            self.shareTitleButton.applyGradient(colours: [UIColor.hexStringToUIColor(hex: "5851DB"), UIColor.hexStringToUIColor(hex: "E1306C"), UIColor.hexStringToUIColor(hex: "FFDC80")],name:"1")
            self.shareTitleButton.AddText(title: "Share On Instagram",name: "1title")
            
            break;
        case 2:
            self.shareTitleButton.applyGradient(colours: [UIColor.hexStringToUIColor(hex: "EDEB34"),UIColor.hexStringToUIColor(hex: "EDEB34")],name:"2")
            self.shareTitleButton.AddText(title: "Share On Snapchat",name: "2title")
            break
        case 3:
            
            self.shareTitleButton.applyGradient(colours: [UIColor.hexStringToUIColor(hex: "50EE6C"),UIColor.hexStringToUIColor(hex: "50EE6C")],name:"3")
            self.shareTitleButton.AddText(title: "Share On Message",name: "3title")
            break
        case 4:
            //line
            self.shareTitleButton.applyGradient(colours: [UIColor.hexStringToUIColor(hex: "00B900"),UIColor.hexStringToUIColor(hex: "00B900")],name:"4")
            self.shareTitleButton.AddText(title: "Share On Line",name: "4title")
            break
        case 5:
            self.shareTitleButton.applyGradient(colours: [UIColor.hexStringToUIColor(hex: "4267B2"),UIColor.hexStringToUIColor(hex: "4267B2")],name:"5")
            self.shareTitleButton.AddText(title: "Share On Facebook",name: "5title")
            break;
        case 6:
            
            self.shareTitleButton.applyGradient(colours: [UIColor.hexStringToUIColor(hex: "00ACEE"),UIColor.hexStringToUIColor(hex: "00ACEE")],name:"6")
            self.shareTitleButton.AddText(title: "Share On Twitter",name: "6title")
            break;
        case 7:
            self.shareTitleButton.applyGradient(colours: [UIColor.hexStringToUIColor(hex: "00ACEE"),UIColor.hexStringToUIColor(hex: "00ACEE")],name:"7")
            self.shareTitleButton.AddText(title: "Share Video",name: "7title")
            break;
            
        default:
            break
        }
    }
}

extension CGFloat {
    var integerValue: Int {
        return Int(self)
    }
    var resizeFrame: CGFloat {
        return (UIScreen.main.bounds.width * self) / 375.0
    }
}

extension UIImageView {
    func getImageFromURL(url: URL,
                         indexPath: IndexPath?,
                         callback: ((_ image: UIImage?,_ indexPath: IndexPath?)-> Void)?)  {
        self.image = UIImage(named: " ")
        self.sd_setImage(with: url, completed:  { (image, error, cache, url) in
            DispatchQueue.main.async {
                self.contentMode = image == nil ? .center : .scaleAspectFill
                callback?(image == nil ? UIImage(named: "") : image, indexPath)
            }
        })
    }
    
}


extension UIView {
    func applyGradient(colours: [UIColor],name:String) -> Void {
        self.applyGradient(colours: colours, locations: nil,name: name)
    }
    
    
    
    func applyGradient(colours: [UIColor], locations: [NSNumber]?,name:String) -> Void {
        
        let gradient: CAGradientLayer = CAGradientLayer()
        gradient.frame = self.bounds
        gradient.colors = colours.map { $0.cgColor }
        gradient.locations = locations
        gradient.cornerRadius = 25
        gradient.name = name;
        self.removeLayer(layerName: "0");
        self.removeLayer(layerName: "1");
        self.removeLayer(layerName: "2");
        self.removeLayer(layerName: "3");
        self.removeLayer(layerName: "4");
        self.removeLayer(layerName: "5");
        self.removeLayer(layerName: "6");
        self.removeLayer(layerName: "7");
        self.removeLayer(layerName: "8");
        gradient.startPoint = CGPoint(x: 0.0, y: 0.0)
        gradient.endPoint = CGPoint(x: 1.0, y: 0.0)
        self.layer.insertSublayer(gradient,at: 0)
        
        
    }
    func removeLayer(layerName: String) {
        for item in self.layer.sublayers ?? [] where item.name == layerName {
            print("layerName")
            print(layerName)
            print("layerName")
            item.removeFromSuperlayer()
        }
    }
    func AddText(title:String,name:String){
        let gradient: CATextLayer = CATextLayer()
        gradient.frame = self.bounds
        gradient.string=title;
        gradient.fontSize = 18.0
        gradient.frame = CGRect(x:0.0,y: (self.bounds.height - 18.0)/2,width:self.bounds.width,height: self.bounds.height)
        gradient.alignmentMode = CATextLayerAlignmentMode.center
        gradient.name = name
        self.removeLayer(layerName: "0title");
        self.removeLayer(layerName: "1title");
        self.removeLayer(layerName: "2title");
        self.removeLayer(layerName: "3title");
        self.removeLayer(layerName: "4title");
        self.removeLayer(layerName: "5title");
        self.removeLayer(layerName: "6title");
        self.removeLayer(layerName: "7title");
        self.removeLayer(layerName: "8title");
        self.layer.insertSublayer(gradient,at: UInt32(self.layer.sublayers?.count ?? 0))
    }
}

extension UIButton {
    func applyGradient(colors: [CGColor]) {
        self.backgroundColor = nil
        self.layoutIfNeeded()
        let gradientLayer = CAGradientLayer()
        gradientLayer.colors = colors
        gradientLayer.startPoint = CGPoint(x: 0, y: 0)
        gradientLayer.endPoint = CGPoint(x: 1, y: 0)
        gradientLayer.frame = self.bounds
        gradientLayer.cornerRadius = self.frame.height/2
        
        gradientLayer.shadowColor = UIColor.darkGray.cgColor
        gradientLayer.shadowOffset = CGSize(width: 2.5, height: 2.5)
        gradientLayer.shadowRadius = 5.0
        gradientLayer.shadowOpacity = 0.3
        gradientLayer.masksToBounds = false
        
        self.layer.insertSublayer(gradientLayer, at: 0)
        self.contentVerticalAlignment = .center
        self.setTitleColor(UIColor.white, for: .normal)
        self.titleLabel?.font = UIFont.boldSystemFont(ofSize: 17.0)
        self.titleLabel?.textColor = UIColor.white
    }
}

extension VideoSaveViewController: URLSessionDelegate, URLSessionDownloadDelegate {
    
    func urlSession(_ session: URLSession, downloadTask: URLSessionDownloadTask, didWriteData bytesWritten: Int64, totalBytesWritten: Int64, totalBytesExpectedToWrite: Int64) {
        let written = byteFormatter.string(fromByteCount: totalBytesWritten)
        let expected = byteFormatter.string(fromByteCount: totalBytesExpectedToWrite)
        print("Downloaded \(written) / \(expected)")
        
        DispatchQueue.main.async {
            self.progressView.isHidden = false
            self.downloadLabel.isHidden = false
            self.progressView.layer.cornerRadius = self.progressView.frame.height / 2
            let percentage = Float(totalBytesWritten) / Float(totalBytesExpectedToWrite)
            print("progresssss\(Float(totalBytesWritten) / Float(totalBytesExpectedToWrite))")
            self.progressView.progress = percentage
            self.shareprogressCallback?(true,percentage, nil)
        }
    }
    
    func urlSession(_ session: URLSession, downloadTask: URLSessionDownloadTask, didFinishDownloadingTo location: URL) {
        // The location is only temporary. You need to read it or copy it to your container before
        // exiting this function. UIImage(contentsOfFile: ) seems to load the image lazily. NSData
        // does it right away.
        DispatchQueue.main.async {
            self.downloadLabel.isHidden = true
            self.progressView.isHidden = true
        }
        
    }
    
}

