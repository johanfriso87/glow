//
//  WtachViewController.swift
//  glow
//
//  Created by Dreams on 24/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class WatchViewController: UIViewController, UISearchBarDelegate {
    
    @IBOutlet weak var addedView: UIView!
    @IBOutlet weak var searchBar: UISearchBar!
    var tabs:[UIViewController] = []
    let numberOfTabs = 1
    //var arrayString = ["Featured", "Articals", "Playlists", "Brands", "Categories"]
    var isSelected = true
    var index = 0
    var dashBoard: Dashboard?
    static func getInstance() -> WatchViewController {
        let viewController = Constants.Storyboard.MAIN.instantiateViewController(withIdentifier: "WatchViewController") as! WatchViewController
        return viewController
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        self.searchBar.delegate = self
        self.setUpTabs()
        self.setUpViewPager()
        hideKeyboardWhenTappedAround()
        //self.getDashBoardData()
        //self.searchBar.hideKeyboardWhenTappedAround()
    }
//    override func viewDidAppear(_ animated: Bool) {
//        self.setUpViewPager()
//    }
//    override func viewWillDisappear(_ animated: Bool) {
//        self.setUpViewPager()
//    }
    
//    override func viewWillAppear(_ animated: Bool) {
//       // self.searchBar.delegate = self
//
//        self.setUpViewPager()
////        hideKeyboardWhenTappedAround()
//    }
    
    func getDashBoardData() {
        Utility.showProgress()
        DashBoardRequest.getData { (success, response, error) in
            Utility.dismissProgress()
            if error == nil {
                self.dashBoard = response
            } else {
                Utility.alert(message: error?.localizedDescription ?? "")
            }
        }
    }
    
    func hideKeyboardWhenTappedAround()  {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
        tap.cancelsTouchesInView = true
        self.view.addGestureRecognizer(tap)
    }
    
    @objc func dismissKeyboard() {
        self.searchBar.endEditing(true)
    }
    
    
    @IBAction func profileButton(_ sender: Any) {
        let storyBoard = UIStoryboard(name: "Profile", bundle: nil)
        if let viewController = storyBoard.instantiateViewController(withIdentifier: "ProfileViewController") as? ProfileViewController {
            
            self.navigationController?.pushViewController(viewController, animated: true)
        }
    }
}

extension WatchViewController: WormTabStripDelegate {
    func TabDidScroll(scrollView: UIScrollView) {
        
    }
    
    func WTSCurrentTab(index: Int) {
        
    }
    
    func setUpTabs(){
       
        let vc = self.storyboard!.instantiateViewController(withIdentifier: "HomeViewController")
        let vc1 = self.storyboard!.instantiateViewController(withIdentifier: "ArticalViewController")
       // let vc2 = self.storyboard!.instantiateViewController(withIdentifier: "PlaylistViewController")
        //let vc3 = self.storyboard!.instantiateViewController(withIdentifier: "BrandsViewController")
       // let vc4 = self.storyboard!.instantiateViewController(withIdentifier: "CategoriesViewController")
        tabs.append(vc)
        tabs.append(vc1)
       // tabs.append(vc2)
      //  tabs.append(vc3)
      //  tabs.append(vc4)
      
    
    }
    
    func setUpViewPager(){
        let frame =  CGRect(x: 0, y: 0, width: self.view.frame.size.width, height: self.view.frame.size.height)
        print("ff:- \(frame)")
        let viewPager:WormTabStrip = WormTabStrip(frame: frame)
        self.addedView.addSubview(viewPager)
        viewPager.delegate = self
        viewPager.eyStyle.wormStyel = .BUBBLE
        viewPager.eyStyle.isWormEnable = true
        viewPager.eyStyle.topScrollViewBackgroundColor = .groupTableViewBackground
        viewPager.eyStyle.spacingBetweenTabs = 1
        viewPager.eyStyle.WormColor = .purple
        viewPager.eyStyle.dividerBackgroundColor = .gray
        viewPager.eyStyle.tabItemSelectedColor = .white
        viewPager.eyStyle.tabItemDefaultColor = .darkGray
        viewPager.currentTabIndex = 0
        viewPager.buildUI()
    }
        func WTSNumberOfTabs() -> Int {
        return numberOfTabs
    }
    
    func WTSTitleForTab(index: Int) -> String {
        switch index {
        case 0:
            return "Featured"
        case 1:
            return "Articals"
//        case 2:
//            return "Playlists"
//        case 3:
//            return "Brands"
//        case 4:
//            return "Categories"
        default:
            return ""
        }
    }
    
    func WTSViewOfTab(index: Int) -> UIView {
        let view = tabs[index]
        return view.view
    }
    
    func WTSReachedLeftEdge(panParam: UIPanGestureRecognizer) {
        
    }
    
    func WTSReachedRightEdge(panParam: UIPanGestureRecognizer) {
        
    }
}

