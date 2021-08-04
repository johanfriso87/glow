//
//  SelectThumbnailViewController.swift
//  glow
//
//  Created by Dreams on 02/08/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit
import AVFoundation
import Photos

class SelectThumbnailViewController: UIViewController, ThumbSelectorViewDelegate {

    var asset: AVAsset?
    var url: URL!
    var videoData: Data? = nil
    var searchProduct: Search?
    var selectedThumb: UIImage? = nil
    @IBOutlet weak var videoCropView: VideoCropView!
    @IBOutlet weak var selectThumbView: ThumbSelectorView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        videoCropView.setAspectRatio(CGSize(width: 3, height: 2), animated: false)
         UISaveVideoAtPathToSavedPhotosAlbum(url.path, self, #selector(self.video(_:didFinishSavingWithError:contextInfo:)), nil)
        
    }
    
    @objc func video(_ video: String, didFinishSavingWithError error: NSError?, contextInfo: UnsafeRawPointer) {
        if let error = error {
            print("Could not save!! \(error.localizedDescription)")
        } else {
            print("Saved")
            self.videoSelectedFromGellary(videoUrl: URL(fileURLWithPath: video))
        }
        print(video)
    }
    
    func videoSelectedFromGellary (videoUrl: URL) {
        let filevideo = videoUrl.absoluteString
        do {
            self.videoData = try? Data.init(contentsOf: videoUrl)
            let asset1 = AVURLAsset(url:URL(fileURLWithPath: filevideo), options: nil)
            let imgGenerator = AVAssetImageGenerator(asset: asset1)
            let cgImage = (try? imgGenerator.copyCGImage(at: CMTimeMake(value: 0, timescale: 1), actualTime: nil))!
            let uiImage = UIImage(cgImage: cgImage)
            let imageView = UIImageView(image: uiImage)
            
            let image = imageView.image//Utility.createThumbnailOfVideoFromFileURL(filevideo)
            let formatter = ByteCountFormatter()
            formatter.allowedUnits = [.useBytes]
            formatter.countStyle = .file
            var displaySize = formatter.string(fromByteCount: Int64(self.videoData!.count))
            
            let asset = AVAsset(url: videoUrl)
            let duration = asset.duration
            let durationTime = CMTimeGetSeconds(duration)
            let cs = CharacterSet.init(charactersIn: ", bytes")
            displaySize = displaySize.trimmingCharacters(in: cs)
            displaySize = displaySize.replacingOccurrences(of: ",", with: "", options: .literal, range: nil)
            selectThumbView.asset = asset1
            selectThumbView.delegate = self
            videoCropView.asset = asset1
            
            
        }
        
        
    }

//    func deleteVideos() {
//           let library = PHPhotoLibrary.shared()
//           library.performChanges({
//               let fetchOptions = PHFetchOptions()
//               let allPhotos = PHAsset.fetchAssets(with: .video, options: fetchOptions)
//            PHAssetChangeRequest.deleteAssets(allPhotos.lastObject as! NSFastEnumeration)
//           }) { (success, error) in
//               // Handle success & errors
//           }
//    }
    
    
    @IBAction func doneButton(_ sender: UIButton) {
        //self.deleteVideos()
        let storyBoard = UIStoryboard(name: "Main", bundle: nil)
        let viewController = storyBoard.instantiateViewController(withIdentifier: "VideoSaveViewController") as! VideoSaveViewController
        viewController.videoData = self.videoData
        viewController.videoUrl = self.url
        viewController.selectedThumb = self.selectedThumb
        viewController.searchProduct = self.searchProduct
        viewController.modalPresentationStyle = .overFullScreen
        self.present(viewController, animated: true, completion: nil)
    }
    
    @IBAction func cancelButton(_ sender: UIButton) {
        self.dismiss(animated: true, completion: nil)
    }

//       override func loadAsset(_ asset: AVAsset) {
//           selectThumbView.asset = asset
//           selectThumbView.delegate = self
//           videoCropView.asset = asset
//       }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
extension SelectThumbnailViewController {

    func didChangeThumbPosition(_ imageTime: CMTime) {
        videoCropView.player?.seek(to: imageTime, toleranceBefore: CMTime.zero, toleranceAfter: CMTime.zero)
        
    }
    
    func currentImage(_ image: UIImage) {
        self.selectedThumb = image
    }
}

//class AssetSelectionViewController: UIViewController {
//
//    var fetchResult: PHFetchResult<PHAsset>?
//
//    override func viewDidLoad() {
//        super.viewDidLoad()
//        loadLibrary()
//
//    }
//
//    func loadLibrary() {
//        PHPhotoLibrary.requestAuthorization { (status) in
//            if status == .authorized {
//                self.fetchResult = PHAsset.fetchAssets(with: .video, options: nil)
//            }
//        }
//    }
//
//    func loadAssetRandomly() {
//        guard let fetchResult = fetchResult, fetchResult.count > 0 else {
//            print("Error loading assets.")
//            return
//        }
//
////        let randomAssetIndex = Int(arc4random_uniform(UInt32(fetchResult.count - 1)))
//        let asset = fetchResult.lastObject!
//        PHCachingImageManager().requestAVAsset(forVideo: asset, options: nil) { (avAsset, _, _) in
//            DispatchQueue.main.async {
//                if let avAsset = avAsset {
//                    self.loadAsset(avAsset)
//                }
//            }
//        }
//    }
//
//    func loadAsset(_ asset: AVAsset) {
//        // override in subclass
//    }
//}
