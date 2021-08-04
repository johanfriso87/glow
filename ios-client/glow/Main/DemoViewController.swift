//
//  DemoViewController.swift
//  glow
//
//  Created by Dreams on 28/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class DemoViewController: UIViewController, WormTabStripDelegate {
    func TabDidScroll(scrollView: UIScrollView) {
        
    }
    
    func WTSCurrentTab(index: Int) {
        
    }
    
    
    
    @IBOutlet weak var addedView: UIView!
    var tabs:[UIViewController] = []
    let numberOfTabs = 5
    override func viewDidLoad() {
        super.viewDidLoad()
        self.setUpTabs()
        self.setUpViewPager()
        // Do any additional setup after loading the view.
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

extension DemoViewController {
    func setUpTabs(){
       
        let vc = self.storyboard!.instantiateViewController(withIdentifier: "HomeViewController")
        let vc1 = self.storyboard!.instantiateViewController(withIdentifier: "ArticalViewController")
        let vc2 = self.storyboard!.instantiateViewController(withIdentifier: "PlaylistViewController")
        let vc3 = self.storyboard!.instantiateViewController(withIdentifier: "BrandsViewController")
        let vc4 = self.storyboard!.instantiateViewController(withIdentifier: "CategoriesViewController")
        tabs.append(vc)
        tabs.append(vc1)
        tabs.append(vc2)
        tabs.append(vc3)
        tabs.append(vc4)
        
    
    }
    
    func setUpViewPager(){
        let frame =  CGRect(x: 0, y: 0, width: self.view.frame.size.width, height: self.view.frame.size.height - 200)
        let viewPager:WormTabStrip = WormTabStrip(frame: frame)
        self.addedView.addSubview(viewPager)
        viewPager.delegate = self
        viewPager.eyStyle.wormStyel = .BUBBLE
        viewPager.eyStyle.isWormEnable = true
        viewPager.eyStyle.topScrollViewBackgroundColor = .groupTableViewBackground
        viewPager.eyStyle.spacingBetweenTabs = 15
        viewPager.eyStyle.WormColor = .purple
        viewPager.eyStyle.dividerBackgroundColor = .gray
        viewPager.eyStyle.tabItemSelectedColor = .white
        viewPager.eyStyle.tabItemDefaultColor = .darkGray
        viewPager.currentTabIndex = 3
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
        case 2:
            return "Playlists"
        case 3:
            return "Brands"
        case 4:
            return "Categories"
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
