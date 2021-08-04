//
//  TipsViewController.swift
//  glow
//
//  Created by Dreams on 28/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class TipsViewController: UIViewController, UIScrollViewDelegate {

    // MARK: - IBOutlets
    
    @IBOutlet weak var dismissButton: UIButton! {
        didSet {
            dismissButton.layer.cornerRadius = dismissButton.frame.height/2
        }
    }
    
    @IBOutlet weak var slideScrollView: UIScrollView!
    @IBOutlet weak var pageControl: UIPageControl!
    @IBOutlet weak var popupContentContainerView: UIView!
    var slides:[SlideView] = []
    var customBlurEffectStyle: UIBlurEffect.Style?
    var customInitialScaleAmmount: CGFloat!
    var customAnimationDuration: TimeInterval!
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        customBlurEffectStyle == .dark ? .lightContent : .default
    }
    
    // MARK: - Lifecycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        slideScrollView.delegate = self
               slides = createSlides()
        
               setupSlideScrollView(slides: slides)
               
               pageControl.numberOfPages = slides.count
               pageControl.currentPage = 0
               popupContentContainerView.bringSubviewToFront(pageControl)
        modalPresentationCapturesStatusBarAppearance = true
    }
        func setupSlideScrollView(slides : [SlideView]) {
            slideScrollView.frame = CGRect(x: 0, y: 0, width: popupContentContainerView.frame.width, height: popupContentContainerView.frame.height)
            slideScrollView.contentSize = CGSize(width: popupContentContainerView.frame.width * CGFloat(slides.count), height: popupContentContainerView.frame.height)
            slideScrollView.isPagingEnabled = true
            
            for i in 0 ..< slides.count {
                slides[i].frame = CGRect(x: popupContentContainerView.frame.width * CGFloat(i), y: 0, width: popupContentContainerView.frame.width, height: popupContentContainerView.frame.height)
                slideScrollView.addSubview(slides[i])
            }
        }
        
        func createSlides() -> [SlideView] {

            let slide1:SlideView = Bundle.main.loadNibNamed("SlideView", owner: self, options: nil)?.first as! SlideView
            slide1.slideImageView.image = UIImage(named: "2")
    //        slide1.slideImageView.contentMode = .scaleAspectFill
            slide1.slideLabel.text = "A real-life bear"
            
            let slide2:SlideView = Bundle.main.loadNibNamed("SlideView", owner: self, options: nil)?.first as! SlideView
            slide2.slideImageView.image = UIImage(named: "1")
            //slide1.slideImageView.contentMode = .scaleAspectFill
            slide2.slideLabel.text = "A real-life bear"
            
            let slide3:SlideView = Bundle.main.loadNibNamed("SlideView", owner: self, options: nil)?.first as! SlideView
            slide3.slideImageView.image = UIImage(named: "2")
           // slide1.slideImageView.contentMode = .scaleAspectFill
            slide3.slideLabel.text = "A real-life bear"
           
            let slide4:SlideView = Bundle.main.loadNibNamed("SlideView", owner: self, options: nil)?.first as! SlideView
            slide4.slideImageView.image = UIImage(named: "3")
           // slide1.slideImageView.contentMode = .scaleAspectFill
            slide4.slideLabel.text = "A real-life bear"
           
            return [slide1, slide2, slide3, slide4]
        }

        
         func scrollViewDidScroll(_ scrollView: UIScrollView) {
            if scrollView.contentOffset.y > 0 || scrollView.contentOffset.y < 0 {
               scrollView.contentOffset.y = 0
            }
                let pageIndex = round(scrollView.contentOffset.x/view.frame.width)
                pageControl.currentPage = Int(pageIndex)
                
                let maximumHorizontalOffset: CGFloat = scrollView.contentSize.width - scrollView.frame.width
                let currentHorizontalOffset: CGFloat = scrollView.contentOffset.x
                
                // vertical
                let maximumVerticalOffset: CGFloat = scrollView.contentSize.height - scrollView.frame.height
                let currentVerticalOffset: CGFloat = scrollView.contentOffset.y
                
                let percentageHorizontalOffset: CGFloat = currentHorizontalOffset / maximumHorizontalOffset
                let percentageVerticalOffset: CGFloat = currentVerticalOffset / maximumVerticalOffset
                
            
                let percentOffset: CGPoint = CGPoint(x: percentageHorizontalOffset, y: percentageVerticalOffset)
                
                if(percentOffset.x > 0 && percentOffset.x <= 0.25) {

                    slides[1].slideImageView.transform = CGAffineTransform(scaleX: percentOffset.x/0.25, y: percentOffset.x/0.25)
                    
                } else if(percentOffset.x > 0.25 && percentOffset.x <= 0.50) {
                    slides[2].slideImageView.transform = CGAffineTransform(scaleX: percentOffset.x/0.50, y: percentOffset.x/0.50)
                    
                } else if(percentOffset.x > 0.50 && percentOffset.x <= 0.75) {
                    slides[3].slideImageView.transform = CGAffineTransform(scaleX: percentOffset.x/0.75, y: percentOffset.x/0.75)
                    
                }

            }
    // MARK: - IBActions
    
    @IBAction func dismissButtonTapped(_ sender: Any) {
        dismiss(animated: true)
    }

}

// MARK: - MIBlurPopupDelegate

extension TipsViewController: MIBlurPopupDelegate {
    
    var popupView: UIView {
        popupContentContainerView ?? UIView()
    }
    
    var blurEffectStyle: UIBlurEffect.Style? {
        customBlurEffectStyle
    }
    
    var initialScaleAmmount: CGFloat {
        customInitialScaleAmmount
    }
    
    var animationDuration: TimeInterval {
        customAnimationDuration
    }
    
}
