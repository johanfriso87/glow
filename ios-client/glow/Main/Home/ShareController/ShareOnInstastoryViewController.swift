//
//  ShareOnInstastoryViewController.swift
//  glow
//
//  Created by Dreams on 26/07/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit
import AVKit

class ShareOnInstastoryViewController: UIViewController {
    
    @IBOutlet weak var playerView: PlayerViewClass!{
        didSet {
            // playerView.isHidden = true
        }
    }
    var videoUrl: URL! = nil
    override func viewDidLoad() {
        super.viewDidLoad()
        if let url = videoUrl {
            self.addPlayer(for: url)
        }
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        
    }
    
    func addPlayer(for url: URL, timeInterval: TimeInterval = 0.0) {
        if self.playerView.playerLayer.player != nil {
            self.playerView.playerLayer.player = nil
            // self.stopPlayback()
        }
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

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
