//
//  TabBarViewController.swift
//  glow
//
//  Created by Dreams on 24/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit


class TabBarViewController: UITabBarController {

    
    let menuButton = UIButton(frame: CGRect.zero)
    static func getInstance() -> TabBarViewController {
        let viewController = Constants.Storyboard.MAIN.instantiateViewController(withIdentifier: "TabBarViewController") as! TabBarViewController
        return viewController
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.setupMiddleButton()
        // Do any additional setup after loading the view.
    }
    func setupMiddleButton() {
        menuButton.frame = CGRect(x: 0, y: 0, width: 70, height: 70)
        var menuButtonFrame = menuButton.frame
        menuButtonFrame.origin.y = self.view.bounds.height - menuButtonFrame.height - self.view.safeAreaInsets.bottom
        menuButtonFrame.origin.x = self.view.bounds.width/2 - menuButtonFrame.size.width/2
        menuButton.frame = menuButtonFrame
        menuButton.backgroundColor = UIColor.clear
        menuButton.setImage(UIImage(named: "ic_plus"), for: .normal)
        self.view.addSubview(menuButton)
        self.view.layoutIfNeeded()
        menuButton.addTarget(self, action: #selector(self.searchVideoButton(_:)), for: .touchUpInside)
    }
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        menuButton.frame.origin.y = self.view.bounds.height - menuButton.frame.height - self.view.safeAreaInsets.bottom
    }
    
    //override func viewWillAppear(_ animated: Bool) {
      //  self.navigationController?.navigationBar.isHidden = true
   // }
    
    @objc func searchVideoButton(_ sender: UIButton) {
        let storyBoard = UIStoryboard(name: "Main", bundle: nil)
        if let viewController = storyBoard.instantiateViewController(withIdentifier: "VideoSearchViewController") as? VideoSearchViewController {
//            viewController.modalPresentationStyle = .popover
            let nav = UINavigationController(rootViewController: viewController)
            nav.isNavigationBarHidden = true
            self.present(nav, animated: true, completion: nil)
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
