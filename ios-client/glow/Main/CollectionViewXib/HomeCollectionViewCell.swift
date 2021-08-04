//
//  HomeCollectionViewCell.swift
//  glow
//
//  Created by Dreams on 23/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit
import AVKit

class HomeCollectionViewCell: UICollectionViewCell {
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var descLabel: UILabel!
    @IBOutlet weak var userImageView: UIImageView!
    @IBOutlet weak var gifImageView: UIImageView!
    @IBOutlet weak var playerView: PlayerViewClass!
    @IBOutlet weak var requestButton: UIButton!
    @IBOutlet weak var userNameButton: UIButton!
    @IBOutlet weak var requestStatusImageView: UIImageView!
    @IBOutlet weak var userRequestLabelView: UIView!
    @IBOutlet weak var navButton: UIButton!
    @IBOutlet weak var shadowView: UIView!
    @IBOutlet weak var productImageButton: UIButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        userRequestLabelView.layer.opacity = 0.3
        userNameButton.setTitleColor(.black, for: .normal)
        self.contentView.layer.cornerRadius = 2.0
        self.contentView.layer.borderWidth = 1.0
        self.contentView.layer.borderColor = UIColor.clear.cgColor
        self.contentView.layer.masksToBounds = true
        self.layer.shadowColor = UIColor.gray.cgColor
        self.layer.shadowOffset = CGSize(width: 0, height: 2.0)
        self.layer.shadowRadius = 2.0
        self.layer.shadowOpacity = 0.5
        self.layer.masksToBounds = false
    }
    
    
    
    func addPlayer(for url: URL, timeInterval: TimeInterval = 0.0) {
        
        //   soundButton.isHidden = false
        //   videopassButton.isHidden = false
        if self.playerView.playerLayer.player != nil {
            self.playerView.playerLayer.player = nil
             self.stopPlayback()
        }
        self.playerView.playerLayer.player = AVPlayer(url: url)
        self.playerView.playerLayer.videoGravity = .resizeAspectFill
        let cmTime = CMTime(seconds: timeInterval, preferredTimescale: 1)
        self.playerView.playerLayer.player?.seek(to: cmTime)
        self.playerView.playerLayer.player?.addObserver(self, forKeyPath: #keyPath(AVPlayerItem.status), options: [.old, .new], context: nil)
        self.playerView.playerLayer.masksToBounds = true
        self.playerView.playerLayer.player?.isMuted = true
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
    
//    @IBAction func backButton(_ sender: UIButton) {
//      //  self.stopPlay()
//        self.stopPlayback()
//        self.navigationController?.popViewController(animated: true)
//    }
    
    override func prepareForReuse() {
        self.titleLabel.text = ""
        self.playerView.playerLayer.player = nil
    }
    
    
}

    
