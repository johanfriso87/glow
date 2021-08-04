//
//  VideoPlayingViewController.swift
//  glow
//
//  Created by Dreams on 02/07/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit
import AVKit

class VideoPlayingViewController: UIViewController {
    @IBOutlet weak var playerView: PlayerViewClass!{
        didSet {
            // playerView.isHidden = true
        }
    }
    //        @IBOutlet weak var viewSound: UIView! {
    //               didSet {
    //                   viewSound.layer.cornerRadius = 8
    //                   viewSound.clipsToBounds = true
    //               }
    //           }
    @IBOutlet weak var cancelButton: UIButton!
    var videoUrl = ""
    override func viewDidLoad() {
        super.viewDidLoad()
    
        // Do any additional setup after loading the view.
    }
     
    override func viewWillAppear(_ animated: Bool) {
        if let url = URL(string: BASE + videoUrl) {
            self.addPlayer(for: url)
        }
    }
    
    func addPlayer(for url: URL, timeInterval: TimeInterval = 0.0) {
        
        //   soundButton.isHidden = false
        //   videopassButton.isHidden = false
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
    
    @IBAction func backButton(_ sender: UIButton) {
      //  self.stopPlay()
        self.stopPlayback()
        self.navigationController?.popViewController(animated: true)
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
