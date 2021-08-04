//
//  SignupViewController.swift
//  glow
//
//  Created by Dreams on 20/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit
func FONT_REGULAR(_ size:CGFloat) -> UIFont {
    return UIFont.systemFont(ofSize: size)
}
func FONT_MEDIUM(_ size:CGFloat) -> UIFont {
    return UIFont.boldSystemFont(ofSize: size)
}
func FONT_BOLD(_ size:CGFloat) -> UIFont {
    return UIFont.boldSystemFont(ofSize: size)
}
func FONT_THIN(_ size:CGFloat) -> UIFont {
    return UIFont.boldSystemFont(ofSize: size)
}

class SignupViewController: UIViewController, UIScrollViewDelegate {

    @IBOutlet weak var slideScrollView: UIScrollView!
    @IBOutlet weak var pageControl: UIPageControl!
    @IBOutlet weak var titleLabel: UILabel!
    var slides:[SlideView] = []
    static func getInstance() -> SignupViewController {
        let viewController = Constants.Storyboard.LOGIN.instantiateViewController(withIdentifier: "SignupViewController") as! SignupViewController
        return viewController
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        self.pageControl.isHidden = true
//        slideScrollView.delegate = self
//        slides = createSlides()
//        setupSlideScrollView(slides: slides)
//
//        pageControl.numberOfPages = slides.count
//        pageControl.currentPage = 0
//        view.bringSubviewToFront(pageControl)
        let color = UIColor(displayP3Red: 175/255, green: 82/255, blue: 222/255, alpha: 1.0)
        self.titleLabel.attributedText = getAttibutedString(attrs1Color: color, attrs2Color: .red, attrs3Color: .yellow, attrs4Color: .green, string1: "G", string2: "L", string3: "O", string4: "W", font1: FONT_BOLD(70), font2: FONT_BOLD(60), font3: FONT_BOLD(50), font4: FONT_BOLD(50))
        // Do any additional setup after loading the view.
    }
    
    func getAttibutedString(attrs1Color: UIColor,
                            attrs2Color: UIColor,
                            attrs3Color: UIColor,
                            attrs4Color: UIColor,
                            string1: String,
                            string2: String,
                            string3: String,
                            string4: String,
                            font1: UIFont,
                            font2: UIFont,
                            font3: UIFont,
                            font4: UIFont) -> NSAttributedString {
                
        let combination = NSMutableAttributedString()
        let attrs1 = [NSAttributedString.Key.font: font1, NSAttributedString.Key.foregroundColor :  attrs1Color]
        let attrs2 = [NSAttributedString.Key.font: font2, NSAttributedString.Key.foregroundColor : attrs2Color]
        let attrs3 = [NSAttributedString.Key.font: font3, NSAttributedString.Key.foregroundColor :  attrs3Color]
        let attrs4 = [NSAttributedString.Key.font: font4, NSAttributedString.Key.foregroundColor : attrs4Color]
        let attributedString1 = NSMutableAttributedString(string:string1, attributes:attrs1)
        let attributedString2 = NSMutableAttributedString(string: string2, attributes:attrs2)
        let attributedString3 = NSMutableAttributedString(string:string3, attributes:attrs3)
        let attributedString4 = NSMutableAttributedString(string: string4, attributes:attrs4)
        combination.append(attributedString1)
        combination.append(attributedString2)
        combination.append(attributedString3)
        combination.append(attributedString4)
        return combination
    }
    func setupSlideScrollView(slides : [SlideView]) {
        slideScrollView.frame = CGRect(x: 0, y: 0, width: view.frame.width, height: view.frame.height)
        slideScrollView.contentSize = CGSize(width: view.frame.width * CGFloat(slides.count), height: view.frame.height)
        slideScrollView.isPagingEnabled = true
        
        for i in 0 ..< slides.count {
            slides[i].frame = CGRect(x: view.frame.width * CGFloat(i), y: 0, width: view.frame.width, height: view.frame.height)
            slideScrollView.addSubview(slides[i])
        }
    }
    
    func createSlides() -> [SlideView] {

        let slide1:SlideView = Bundle.main.loadNibNamed("SlideView", owner: self, options: nil)?.first as! SlideView
        slide1.slideImageView.image = UIImage(named: "ic_icon")
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
   
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */
    
//    @IBAction func signupButton(_ sender: UIButton) {
//        let viewController = AuthViewController.getInstance(isSignUp: true)
//        self.navigationController?.pushViewController(viewController, animated: true)
//        
//    }
//    
//    @IBAction func loginButton(_ sender: UIButton) {
//        let viewController = AuthViewController.getInstance(isSignUp: false)
//        self.navigationController?.pushViewController(viewController, animated: true)
//    }

}
