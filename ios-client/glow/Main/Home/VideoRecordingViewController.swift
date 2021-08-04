//
//  VideoRecordingViewController.swift
//  glow
//
//  Created by Dreams on 29/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit
import Photos
import VideoEditorSDK
import AVKit
import AVFoundation

class VideoRecordingViewController: UIViewController, CustomSegmentedControlDelegate {
    
    @IBOutlet weak var previewImageView: UIImageView!
    @IBOutlet weak var recordButton: UIButton!
    @IBOutlet weak var recordCancelButton: UIButton!
    @IBOutlet weak var uploadVideoButton: UIButton!
    @IBOutlet weak var instructionView: UIView!
    @IBOutlet weak var timerLabel: UILabel!
    @IBOutlet weak var recordTimerLabel: UILabel!
    @IBOutlet weak var controlView: UIView!
    @IBOutlet weak var previewButton: UIButton!
    @IBOutlet weak var countDownSlider: UISlider!
    @IBOutlet weak var progressBar: AWStepBar!
    
    @IBOutlet weak var interfaceSegmented: CustomSegmentedControl!{
        didSet{
            interfaceSegmented.setButtonTitles(buttonTitles: ["Normal","Hands-Free","Countdown", "Fast-Forward"])
            interfaceSegmented.selectorViewColor = .purple
            interfaceSegmented.selectorTextColor = .purple
            
        }
    }
    @IBOutlet weak var playerView: PlayerViewClass!{
        didSet {
            // playerView.isHidden = true
        }
    }
    var totalDuration = 0
    var cameraConfig: CameraConfiguration!
    var totalHour = Int()
    var totalMinut = Int()
    var totalSecond = Int()
    var maxTime = 0
    var timer:Timer?
    var video_key: String = ""
    var videoData: Data?
    var videoUrls: URL! = nil
    var lastVideoPosition = 0
    var videoUploader = VideoUploader()
    var arrayAsset : [AVAsset] = []
    var searchData: Search?
    var videoRecordingStarted = false
    var reccordTimer: Timer?
    let recordStratInTime = 3
    var recordCurrentTime = 0
    var isFastForward = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tabBarController?.tabBar.isHidden = true
        self.recordTimerLabel.isHidden = true
        self.interfaceSegmented.delegate = self
        self.setupUI(index: 0)
        self.addPreviewGesture()
        self.progressBar.numberOfSteps = 60
        self.progressBar.set(step: 0.0, animated: false)
        self.progressBar.stepBarForegroundColor = UIColor(red: 137/255, green: 0/255, blue: 255/255, alpha: 1)
        self.progressBar.stepBarBackgroundColor = UIColor(red: 216/255, green: 216/255, blue: 216/255, alpha: 1)
        self.progressBar.dotColor = UIColor.clear
        self.progressBar.dotSelectedColor = .clear
        
    }
    
    override var prefersStatusBarHidden: Bool {
        return true
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.tabBarController?.tabBar.isHidden = true
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        self.setUpCamera()
//        self.countDownSlider.setThumbImage(UIImage(), for: .normal)
//        countDownSlider.minimumValue = 0.0
//        countDownSlider.maximumValue = 60
        
    }
    
    func updateSlider(currentDuration: Int) {
        let duration = currentDuration
        print("\(duration)")
        if currentDuration != 60 {
            UIView.animate(withDuration: 1) {
//                self.countDownSlider.setValue(Float(currentDuration), animated: true)
            }
        }
    }
    //Mark:- Setup Video Recording Button
    
    func setupUI(index: Int) {
        switch index {
        case 0:
             self.isFastForward = false
            self.recordButton.removeTarget(self, action: nil, for: .allEvents)
            self.recordButton.setImage(UIImage(named: "circle"), for: .normal)
            self.longPress()
            break;
        case 1:
             self.isFastForward = false
            self.recordButton.removeTarget(self, action: nil, for: .allEvents)
            //self.recordButton.removeGestureRecognizer(longPressGesture)
            self.recordButton.setImage(UIImage(named: "stop_button"), for: .normal)
            self.recordButton.addTarget(self, action: #selector(handsFreeButton(_:)), for: .touchUpInside)
            break;
        case 2:
             self.isFastForward = false
            self.recordButton.removeTarget(self, action: nil, for: .allEvents)
            //self.recordButton.removeGestureRecognizer(longPressGesture)
            self.recordButton.setImage(UIImage(named: "start_countdown"), for: .normal)
            self.recordButton.addTarget(self, action: #selector(self.timerButton(_:)), for: .touchUpInside)
            break;
        case 3:
            self.isFastForward = true
            self.recordButton.removeTarget(self, action: nil, for: .allEvents)
            //self.recordButton.removeGestureRecognizer(longPressGesture)
            self.recordButton.setImage(UIImage(named: "start_fast_forward"), for: .normal)
            self.recordButton.addTarget(self, action: #selector(self.fastforwardbutton(_:)), for: .touchUpInside)
            break;
        default:
            self.recordButton.removeTarget(self, action: nil, for: .allEvents)
            self.longPress()
            self.recordButton.setImage(UIImage(named: "circle"), for: .normal)
            
            break;
        }
    }
    
    //Mark:- Handel video recording controls
    
    func change(to index: Int) {
        self.setupUI(index: index)
    }
    
    //Mark:- Add Gesture for longpress video recording button
       
       func longPress() {
           let longPressGesture = UILongPressGestureRecognizer.init(target: self, action: #selector(handleLongPress(gestureRecognizer:)))
           self.recordButton.addGestureRecognizer(longPressGesture);
       }
       
       @objc func handleLongPress(gestureRecognizer: UILongPressGestureRecognizer) {
           
           if gestureRecognizer.state == UIGestureRecognizer.State.began {
               debugPrint("long press started")
               self.recordButton.setImage(UIImage(named: "circle1"), for: .normal)
               self.uploadVideoButton.isUserInteractionEnabled = false
               self.previewButton.isUserInteractionEnabled = false
               self.startTimer()
               self.cameraConfig.recordVideo { (url, error) in
                   guard let url = url else {
                       print(error ?? "Video recording error")
                       return
                   }
                   print("URL:", url)
                   self.videoData = try? Data(contentsOf: url)
                   self.videoUrls = url
                   let asset = AVAsset(url: url)
                   self.arrayAsset.append(asset)
               }
           }
           else if gestureRecognizer.state == UIGestureRecognizer.State.ended {
               debugPrint("longpress ended")
               self.recordButton.setImage(UIImage(named: "circle"), for: .normal)
               self.stopTimer()
               self.maxTime = totalSecond
               self.uploadVideoButton.isUserInteractionEnabled = true
               self.previewButton.isUserInteractionEnabled = true
               self.cameraConfig.stopRecording { (error) in
                   Utility.alert(message: error?.localizedDescription ?? "")
               }
               
           }
       }
    
    @objc func handsFreeButton (_ sender: UIButton) {
        if videoRecordingStarted {
            videoRecordingStarted = false
            self.stopTimer()
            self.uploadVideoButton.isUserInteractionEnabled = true
            self.previewButton.isUserInteractionEnabled = true
            self.cameraConfig.stopRecording { (error) in
                print(error ?? "Video recording error")
            }
        } else if !videoRecordingStarted {
            videoRecordingStarted = true
            self.uploadVideoButton.isUserInteractionEnabled = false
            self.previewButton.isUserInteractionEnabled = false
            self.startTimer()
            self.cameraConfig.recordVideo { (url, error) in
                guard let url = url else {
                    print(error ?? "Video recording error")
                    return
                }
                //  self.videoUrls = url
                self.videoData = try? Data(contentsOf: url)
                self.videoUrls = url
                let asset = AVAsset(url: url)
                self.arrayAsset.append(asset)
            }
        }
    }
    
    @objc func fastforwardbutton (_ sender: UIButton) {
        if videoRecordingStarted {
            videoRecordingStarted = false
            self.stopTimer()
            self.uploadVideoButton.isUserInteractionEnabled = true
            self.previewButton.isUserInteractionEnabled = true
            self.cameraConfig.stopRecording { (error) in
                print(error ?? "Video recording error")
            }
        } else if !videoRecordingStarted {
            videoRecordingStarted = true
            self.uploadVideoButton.isUserInteractionEnabled = false
            self.previewButton.isUserInteractionEnabled = false
            self.startTimer()
            self.cameraConfig.recordVideo { (url, error) in
                guard let url = url else {
                    print(error ?? "Video recording error")
                    return
                }

                self.videoCompressInFastForward(inputURL: url) { (url) in
                    let asset = AVAsset(url: url)
                    self.arrayAsset.append(asset)
                    self.videoData = try? Data(contentsOf: url)
                    self.videoUrls = url
                }
            }
        }
    }
    
     //Mark:- Time Duration calculate for timer recording
    @objc func timerButton (_ sender: UIButton) {
        self.totalSecond = 0
        self.maxTime = 60
        self.startRecordTimer()
        
    }
   
    func startRecordTimer(){
        self.recordButton.isUserInteractionEnabled = false
        self.previewButton.isUserInteractionEnabled = false
        self.reccordTimer = Timer.scheduledTimer(timeInterval: 1, target: self, selector: #selector(countdownRecord), userInfo: nil, repeats: true)
    }
    
    func stopRecordTimer() {
        self.recordButton.isUserInteractionEnabled = true
        self.previewButton.isUserInteractionEnabled = true
        self.reccordTimer?.invalidate()
        self.reccordTimer = nil
        self.recordTimerLabel.text = ""
        
        if videoRecordingStarted {
            videoRecordingStarted = false
            self.stopTimer()
            
            self.cameraConfig.stopRecording { (error) in
                print(error ?? "Video recording error")
            }
        } else if !videoRecordingStarted {
            videoRecordingStarted = true
            self.recordTimerLabel.isHidden = true
            self.startTimer()
            self.cameraConfig.recordVideo { (url, error) in
                guard let url = url else {
                    print(error ?? "Video recording error")
                    return
                }
                self.videoData = try? Data(contentsOf: url)
                self.videoUrls = url
                let asset = AVAsset(url: url)
                self.arrayAsset.append(asset)
            }
        }
    }
    
    @objc func countdownRecord() {
        self.recordCurrentTime += 1
        
        self.recordTimerLabel.isHidden = false
        self.recordTimerLabel.fadeTransition(0.3)
        self.recordTimerLabel.text = ("\(self.recordCurrentTime)")
        if self.recordCurrentTime == 4 {
            self.stopRecordTimer()
        }
    }
       
    //Mark:- Time Duration calculate for recording
    func startTimer(){
        self.timer = Timer.scheduledTimer(timeInterval: 1, target: self, selector: #selector(countdown), userInfo: nil, repeats: true)
    }
    
    func stopTimer() {
        self.timer?.invalidate()
        self.timer = nil
    }
  
    @objc func countdown() {
        var minutes: Int
        var seconds: Int
        timerLabel.isHidden = false
        maxTime += 1
        self.progressBar.set(step: CGFloat(maxTime), animated: true)
        totalSecond = maxTime
        if maxTime == Int(60) {
            self.progressBar.set(step: 60.0, animated: true)
            self.stopTimer()
            self.cameraConfig.stopRecording { (error) in
                print(error ?? "Video recording error")
            }
        }
        minutes = (maxTime % 3600) / 60
        seconds = (maxTime % 3600) % 60
        self.timerLabel.text = String(format: "%02d:%02d", minutes, seconds)
    }
    
    
    
    
    //Mark:- Add Gesture For Preview of video recording
    
    func addPreviewGesture() {
        let tap = UITapGestureRecognizer(target: self, action: #selector(self.handleTapGesture(_:)))
        playerView.addGestureRecognizer(tap)
    }
    
    @objc func handleTapGesture(_ sender: UIGestureRecognizer) {
        self.stopPlay()
        self.playerView.isHidden = true
        self.controlView.isHidden = false
        self.interfaceSegmented.isHidden = false
        self.timerLabel.isHidden = false
    }

    //Mark:- IBActions for video recording controller
    
    @IBAction func cancelRecordButton(_ sender: UIButton) {
        self.stopTimer()
        self.dismiss(animated: true, completion: nil)
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func uploadVideoButton(_ sender: UIButton) {
        
        if !self.arrayAsset.isEmpty {
            if (maxTime) > 10 {
                self.addProductSticker()
                if arrayAsset.count == 1 {
                    
                        self.gotoEdit(videoUrl: self.videoUrls)
                    
                } else {
                    self.merge(arrayVideos: self.arrayAsset) { (session) in
                        
                            self.videoData = try? Data.init(contentsOf: session.outputURL!)
                            self.gotoEdit(videoUrl: session.outputURL!)
                        
                    }
                }
                
            } else {
                Utility.alert(message: "Your post is too short. Please make sure that is atleast 10 seconds.")
            }
        }
    }
    
    @IBAction func flashButton(_ sender: UIButton) {
        if self.cameraConfig.flashMode == .off {
            self.cameraConfig.flashMode = .on
        } else {
            self.cameraConfig.flashMode = .off
        }
        
    }
    
    @IBAction func previewButton(_ sender: UIButton) {
        if self.arrayAsset.count == 1 {
            self.playerView.isHidden = false
            self.timerLabel.isHidden = true
            self.controlView.isHidden = true
            self.interfaceSegmented.isHidden = true
//            self.totalDuration = Int(session.asset.duration.seconds)
            if let url = self.videoUrls {
                self.addPlayer(for: url)
            }
        } else {
            self.merge(arrayVideos: self.arrayAsset) { (session) in
                self.playerView.isHidden = false
                self.timerLabel.isHidden = true
                self.controlView.isHidden = true
                self.interfaceSegmented.isHidden = true
                self.totalDuration = Int(session.asset.duration.seconds)
                if let url = session.outputURL {
                    self.addPlayer(for: url)
                }
            }
        }
    }
    
    @IBAction func rotateButton(_ sender: UIButton) {
        if cameraConfig != nil {
            do {
                try cameraConfig.switchCameras()
            } catch {
                print(error.localizedDescription)
            }
        }
    }
}
extension VideoRecordingViewController: AVCaptureFileOutputRecordingDelegate {
    func fileOutput(_ output: AVCaptureFileOutput, didFinishRecordingTo outputFileURL: URL, from connections: [AVCaptureConnection], error: Error?) {
        
    }
    
    func fileOutput(_ output: AVCaptureFileOutput, didStartRecordingTo fileURL: URL, from connections: [AVCaptureConnection]) {
        
    }
    
    
    func checkPermission(completion: @escaping ()->Void) {
        let photoAuthorizationStatus = PHPhotoLibrary.authorizationStatus()
        switch photoAuthorizationStatus {
        case .authorized:
            print("Access is granted by user")
            completion()
        case .notDetermined:
            PHPhotoLibrary.requestAuthorization({
                (newStatus) in
                print("status is \(newStatus)")
                if newStatus ==  PHAuthorizationStatus.authorized {
                    /* do stuff here */
                    completion()
                    print("success")
                }
            })
            print("It is not determined until now")
        case .restricted:
            // same same
            print("User do not have access to photo album.")
        case .denied:
            // same same
            print("User has denied the permission.")
        }
    }
    
    fileprivate func registerNotification() {
        let notificationCenter = NotificationCenter.default
        notificationCenter.addObserver(self, selector: #selector(appMovedToBackground), name: NSNotification.Name(rawValue: "App is going background"), object: nil)
        
        notificationCenter.addObserver(self, selector: #selector(appCameToForeground), name: UIApplication.willEnterForegroundNotification, object: nil)
    }
    
    @objc func appMovedToBackground() {
        //        if videoRecordingStarted {
        //            videoRecordingStarted = false
        //            self.cameraConfig.stopRecording { (error) in
        //                print(error ?? "Video recording error")
        //            }
        //        }
    }
    
    @objc func appCameToForeground() {
        print("app enters foreground")
    }
    
    //Mark:- Setup camera configurations
    
    func setUpCamera() {
        self.cameraConfig = CameraConfiguration()
        cameraConfig.setup { (error) in
            if error != nil {
                print(error!.localizedDescription)
            }
            try? self.cameraConfig.displayPreview(self.previewImageView)
        }
        registerNotification()
    }
    
    //Mark:- Merge Multiple Videos
    
    func merge(arrayVideos:[AVAsset], completion:@escaping (_ exporter: AVAssetExportSession) -> ()) -> Void {
        
        let mainComposition = AVMutableComposition()
        let compositionVideoTrack = mainComposition.addMutableTrack(withMediaType: .video, preferredTrackID: kCMPersistentTrackID_Invalid)
        compositionVideoTrack?.preferredTransform = CGAffineTransform(rotationAngle: .pi / 2)
        
        let soundtrackTrack = mainComposition.addMutableTrack(withMediaType: .audio, preferredTrackID: kCMPersistentTrackID_Invalid)
        
        var insertTime = CMTime.zero
        
        for videoAsset in arrayVideos {
            try! compositionVideoTrack?.insertTimeRange(CMTimeRangeMake(start: CMTime.zero, duration: videoAsset.duration), of: videoAsset.tracks(withMediaType: .video)[0], at: insertTime)
            try! soundtrackTrack?.insertTimeRange(CMTimeRangeMake(start: CMTime.zero, duration: videoAsset.duration), of: videoAsset.tracks(withMediaType: .audio)[0], at: insertTime)
            
            insertTime = CMTimeAdd(insertTime, videoAsset.duration)
        }
        
        let outputFileURL = URL(fileURLWithPath: NSTemporaryDirectory() + "merge.mp4")
        
        let fileManager = FileManager()
        try? fileManager.removeItem(at: outputFileURL)
        
        let exporter = AVAssetExportSession(asset: mainComposition, presetName: AVAssetExportPresetMediumQuality)
        
        exporter?.outputURL = outputFileURL
        exporter?.outputFileType = AVFileType.mp4
        exporter?.shouldOptimizeForNetworkUse = true
        
        exporter?.exportAsynchronously {
            DispatchQueue.main.async {
                completion(exporter!)
            }
        }
    }
    
    func videoCompressInFastForward( inputURL : URL, completion:@escaping (_ url: URL) -> ()) -> Void {
        let videoAsset = AVAsset(url: inputURL) as AVAsset
            let clipVideoTrack = videoAsset.tracks(withMediaType: AVMediaType.video).first! as AVAssetTrack

            let composition = AVMutableComposition()
        
        let videoCompositions = composition.addMutableTrack(withMediaType: AVMediaType.video, preferredTrackID: CMPersistentTrackID())
        videoCompositions?.preferredTransform = CGAffineTransform(rotationAngle: .pi / 2)
            do {
                try videoCompositions?.insertTimeRange(CMTimeRangeMake(start: CMTime.zero, duration: videoAsset.duration), of: (videoAsset.tracks(withMediaType: AVMediaType.video).first)!, at: CMTime.zero)
            } catch {
                print("handle insert error")
                return
            }
        let videoDuration = videoAsset.duration
        let finalTimeScale:Int64 = (Int64(float_t(videoDuration.value) / 2.0))
        print("player rate is",2.0)
    
        videoCompositions?.scaleTimeRange(CMTimeRangeMake(start: CMTime.zero, duration: videoDuration), toDuration: CMTimeMake(value: finalTimeScale, timescale: videoDuration.timescale))
        let fileManager = FileManager.default
            let documentDirectory = fileManager.urls(for: .documentDirectory, in: .userDomainMask)[0]
            var outputURL = documentDirectory.appendingPathComponent("output")
            do {

                try fileManager.createDirectory(at: outputURL, withIntermediateDirectories: true, attributes: nil)
                outputURL = outputURL.appendingPathComponent("\(String(describing: inputURL.lastPathComponent)).mp4")
            }catch let error {
                print(error)
            }
            try? fileManager.removeItem(at: outputURL)

             guard let exporter = AVAssetExportSession(asset: composition, presetName: AVAssetExportPresetHighestQuality) else { return}
        exporter.outputURL = outputURL
            exporter.outputFileType = .mp4
            exporter.exportAsynchronously {
                switch exporter.status {
                case .completed:
                    completion(outputURL)
                    print("exported at \(outputURL)")
//                    UISaveVideoAtPathToSavedPhotosAlbum(outputURL.path,nil, nil, nil)
                case .failed:
                    print("failed \(exporter.error.debugDescription)")
                case .cancelled:
                    print("cancelled \(exporter.error.debugDescription)")
                default: break
                }
            }
    }
    
    @IBAction func cancelButton(_ sender: Any) {
        self.stopTimer()
        //videoRecordingStarted = false
        self.dismiss(animated: true, completion: nil)
        self.tabBarController?.selectedIndex = 0
    }
    
    //Mark:- Navigate to video editor sdk
    
    func gotoEdit(videoEdit: Data? = nil, videoUrl: URL) {
        let video = Video(url: videoUrl)
        let vcEdit = VideoEditViewController(videoAsset: video)
        vcEdit.delegate = self
        vcEdit.modalPresentationStyle = .overCurrentContext
        present(vcEdit, animated: true, completion: nil)
        
    }
    
    //Mark:- Add Product sticker in videoedtior sdk
    
    func addProductSticker() {
        var sticker: Sticker?
        var categories = StickerCategory.all
        if let strUrl = self.searchData?.thumbnail {
            if let url = URL(string: strUrl) {
                sticker = Sticker(imageURL: url, thumbnailURL: url, identifier: "product")
            }
        } else if let strURl = self.searchData?.images?.first {
            if let url = URL(string: strURl) {
                sticker = Sticker(imageURL: url, thumbnailURL: url, identifier: "product")
            }
        }
        let stickers = [sticker!]
        if let previewURL = Bundle.main.url(forResource: "face_decor", withExtension: "jpg") {
            categories.append(StickerCategory(title: "Oldschool", imageURL: previewURL, stickers: stickers))
        }
        let url = self.searchData?.thumbnail ?? ""
        categories.append(StickerCategory(title: "Product", imageURL: URL(string: url)!, stickers: stickers))
        StickerCategory.all = categories
    }
    
}

//Mark:- Video Editor SDK Delegate Method

extension VideoRecordingViewController: VideoEditViewControllerDelegate {
    func videoEditViewController(_ videoEditViewController: VideoEditViewController, didFinishWithVideoAt url: URL?) {
        totalSecond = 0
        maxTime = 60
        self.dismiss(animated: true, completion: nil)
        let storyBoard = UIStoryboard(name: "Main", bundle: nil)
        if let vc = storyBoard.instantiateViewController(withIdentifier: "SelectThumbnailViewController") as? SelectThumbnailViewController {
            vc.modalPresentationStyle = .overFullScreen
            vc.searchProduct = self.searchData
            if url != nil {
                vc.url = url
            } else {
                vc.url = self.videoUrls
            }
            vc.url = url
            self.present(vc, animated: true, completion: nil)
        }
    }
    
    func videoEditViewControllerDidFailToGenerateVideo(_ videoEditViewController: VideoEditViewController) {
        
    }
    
    func videoEditViewControllerDidCancel(_ videoEditViewController: VideoEditViewController) {
        self.dismiss(animated: true, completion: nil)
    }
    
    
}
extension UIView {
    func fadeTransition(_ duration:CFTimeInterval) {
        let animation = CATransition()
        animation.timingFunction = CAMediaTimingFunction(name:
            CAMediaTimingFunctionName.easeInEaseOut)
        animation.type = CATransitionType.fade
        animation.duration = duration
        layer.add(animation, forKey: CATransitionType.fade.rawValue)
    }
}

//Mark:- Preview Video Playing

extension VideoRecordingViewController {
    
    func addPlayer(for url: URL, timeInterval: TimeInterval = 0.0) {
        if self.playerView.playerLayer.player != nil {
            self.playerView.playerLayer.player = nil
            // self.stopPlayback()
        }
        self.playerView.playerLayer.player?.isMuted = false
        self.playerView.playerLayer.player = AVPlayer(url: url)
        self.playerView.playerLayer.videoGravity = .resizeAspectFill
        let cmTime = CMTime(seconds: timeInterval, preferredTimescale: 1)
        self.playerView.playerLayer.player?.seek(to: cmTime)
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
        //            self.staticVideoPlayer.playerLayer.player = nil
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
