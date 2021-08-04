//
//  SpotlightDetailCollectionViewCell.swift
//  glow
//
//  Created by dhruv dhola on 23/08/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit
import AVKit

class SpotlightDetailCollectionViewCell: UICollectionViewCell {
//    @IBOutlet weak var playerView: PlayerView!
    @IBOutlet weak var playerView: PlayerViewClass!
   // var player = Player()
    @IBOutlet weak var playbackSlider: UISlider!
   // @IBOutlet weak var backButton: UIButton!
   // @IBOutlet weak var moreButton: UIButton!
    var nextVideoPlayer: AVPlayer?
    var playerTimeObserver: Any?
    var timeObserver: Any?
    func addSecondPlayerOnQueue(for url: URL) {
        let avAsset = AVAsset(url: url)
        let playerItem = AVPlayerItem(asset: avAsset)
        self.nextVideoPlayer = AVPlayer(playerItem: playerItem)
        self.nextVideoPlayer?.play()
        self.nextVideoPlayer?.pause()
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name(rawValue: "StopLandscapePlay"), object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.stopPlay), name: NSNotification.Name(rawValue: "StopLandscapePlay"), object: nil)
        self.playbackSlider.setThumbImage(UIImage(), for: .normal)

    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    func addPlayer(for url: URL, timeInterval: TimeInterval = 0.0) {

        //   soundButton.isHidden = false
        //   videopassButton.isHidden = false
        if self.playerView.playerLayer.player != nil {
           self.playerView.playerLayer.player = nil
           // self.stopPlayback()
        }
        let avPlayer = self.nextVideoPlayer ?? AVPlayer(url: url)
        self.playerView.playerLayer.player = avPlayer
        self.playerView.playerLayer.videoGravity = .resizeAspectFill
        self.playerView.playerLayer.player?.addObserver(self, forKeyPath: #keyPath(AVPlayerItem.status), options: [.old, .new], context: nil)
        
        self.timeObserver = self.playerView.player?.addPeriodicTimeObserver(forInterval: CMTime(value: 1, timescale: 2),
                                                                            queue: DispatchQueue.main, using: { [weak self] (progressTime) in
                                                                                
                                                                                self?.updateSlider(elapsedTime: progressTime)
                                                                                
        })
        self.playerView.playerLayer.masksToBounds = true
        self.startPlayback()
    }

    @objc func stopPlay() {
        self.stopPlayback()
    }

    func stopPlayback(){
        print("Stop Playback")
        self.playerView.playerLayer.player?.removeObserver(self, forKeyPath:  #keyPath(AVPlayerItem.status))
        self.playerView.player?.removeTimeObserver(self.timeObserver)
        self.playerView.playerLayer.player?.pause()
        self.playerView.playerLayer.player = nil
    }
    func startPlayback() {
        self.playerView.playerLayer.player?.play()

    }
    
    func addObserver() {
        self.playerTimeObserver = self.playerView.player?.addPeriodicTimeObserver(forInterval: CMTimeMake(value: 1, timescale: 100), queue: DispatchQueue.main, using: { [weak self] timeInterval in
            guard let strongSelf = self else {
                return
            }

        })
    }
    
    func updateSlider(elapsedTime: CMTime) {
        if let playerDuration = self.playerView.player?.currentItem?.duration{
            if CMTIME_IS_INVALID(playerDuration) {
                      playbackSlider.minimumValue = 0.0
                       return
                   }
                   let duration = Float(CMTimeGetSeconds(playerDuration))
                   if duration.isFinite && duration > 0 {
                       playbackSlider.minimumValue = 0.0
                       playbackSlider.maximumValue = duration
                       let time = Float(CMTimeGetSeconds(elapsedTime))
                       playbackSlider.setValue(time, animated: true)
                   }

        }
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
//extension SpotlightDetailCollectionViewCell: PlayerDelegate {
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
//
// //MARK: - PlayerPlaybackDelegate
//
//extension SpotlightDetailCollectionViewCell: PlayerPlaybackDelegate {
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
