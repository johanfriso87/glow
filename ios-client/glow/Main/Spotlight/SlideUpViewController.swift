//
//  SlideUpViewController.swift
//  glow
//
//  Created by Dreams on 06/07/20.
//  Copyright © 2020 Dreams. All rights reserved.
//
extension UIViewController {
    
    var hasSafeArea: Bool {
        guard
            #available(iOS 11.0, tvOS 11.0, *)
            else {
                return false
        }
        return UIApplication.shared.delegate?.window??.safeAreaInsets.top ?? 0 > 20
    }
    
}
import UIKit

enum InitialState {
    case expanded
}

class SlideUpViewController: PullUpController {
    
    
    @IBOutlet private weak var visualEffectView: UIView!
    @IBOutlet private weak var searchSeparatorView: UIView! {
        didSet {
            searchSeparatorView.layer.cornerRadius = searchSeparatorView.frame.height/2
        }
    }
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var commentTextField: UITextField!
    @IBOutlet weak var productLabel: UILabel!
    @IBOutlet weak var productImage: UIImageView!
    @IBOutlet weak var likeButton: UIButton!
    @IBOutlet weak var buyButton: UIButton!
    @IBOutlet weak var shelfButton: UIButton!
    @IBOutlet weak var wishlistButton: UIButton!
    @IBOutlet weak var stackMenu: UIStackView!
    var isLiked: Bool = false
    var video: Videos?
    var initialState: InitialState = .expanded
    public var portraitSize: CGSize = .zero
    public var landscapeFrame: CGRect = .zero
    private var safeAreaAdditionalOffset: CGFloat {
        hasSafeArea ? 20 : 0
    }
    
    var initialPointOffset: CGFloat {
        switch initialState {
        case .expanded:
            return 225
        }
    }
    var comment: [Comment] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        portraitSize = CGSize(width: min(UIScreen.main.bounds.width, UIScreen.main.bounds.height),
                              height: visualEffectView.frame.maxY - 200)
        landscapeFrame = CGRect(x: 5, y: 50, width: 280, height: 250)
        let button = UIButton()
        button.frame = CGRect(x: 0, y: 0, width: 25, height: 25)
        button.setTitle("Send  ", for: .normal)
        button.setTitleColor(.black, for: .normal)
        button.clipsToBounds = true
        button.addTarget(self, action: #selector(self.refresh(_:)), for: .touchUpInside)
        commentTextField.rightView = button
        commentTextField.rightViewMode = .always
        self.setUpUI()
        // Do any additional setup after loading the view.
    }
    
    func setUpUI() {
        self.tableView.delegate = self
        self.tableView.dataSource = self
        self.tableView.tableFooterView = UIView(frame: CGRect.zero)
        self.buyButton.layer.cornerRadius = 10
        self.buyButton.layer.borderWidth = 0.5
        self.buyButton.layer.borderColor = UIColor.black.cgColor
        self.shelfButton.layer.cornerRadius = 10
        self.shelfButton.layer.borderWidth = 0.5
        self.shelfButton.layer.borderColor = UIColor.black.cgColor
        self.wishlistButton.layer.cornerRadius = 10
        self.wishlistButton.layer.borderWidth = 0.5
        self.wishlistButton.layer.borderColor = UIColor.black.cgColor
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.viewVideo()
        self.stackMenu.isHidden = true
        self.productLabel.text = self.video?.productName
        if let urlStr = self.video?.productThumbnailUrl {
            if let url = URL(string: urlStr) {
                self.productImage.sd_setImage(with: url) { (image, error, type, url) in
                    self.productImage.image = image
                }
            }
        }
        self.getVideoComments()
    }
    
    @objc func refresh(_ sender: Any) {
        if !(commentTextField.text?.isEmpty ?? false) {
            self.commentOnVideo()
        }
    }
    func viewVideo() {
        let param = ["_id" : self.video?._id ?? ""]
        Utility.showProgress()
        LikeRequest.videoView(param: param) { (success, response, error) in
            Utility.dismissProgress()
            if error == nil {
                self.isLiked = response?.isLiked ?? false
                print("videoLiked\(self.isLiked)")
                if self.isLiked {
                    self.likeButton.setImage(UIImage(named: "like"), for: .normal)
                    
                } else {
                    self.likeButton.setImage(UIImage(named: "heart"), for: .normal)
                    
                }
            } else {
                print("video view error")
            }
        }
    }
    
    func likeVideo() {
        let param = ["videoId": self.video?._id ?? ""]
        Utility.showProgress()
        LikeRequest.saveLike(param: param) { (success, response, error) in
            Utility.dismissProgress()
            if error == nil {
                self.isLiked = true
                self.likeButton.setImage(UIImage(named: "like"), for: .normal)
                
            } else {
                Utility.alert(message: error?.localizedDescription ?? "Something went wrong")
            }
        }
    }
    
    func removeLikeVideo() {
        let param = ["videoId": self.video?._id ?? ""]
        Utility.showProgress()
        LikeRequest.removeLike(param: param) { (success, response, error) in
            Utility.dismissProgress()
            if error == nil {
                self.isLiked = false
                self.likeButton.setImage(UIImage(named: "heart"), for: .normal)
                
            } else {
                Utility.alert(message: error?.localizedDescription ?? "Something went wrong")
            }
        }
    }
    
    func commentOnVideo() {
        let param = ["comment" : self.commentTextField.text ?? "",
                     "videoId" : self.video?._id ?? "" ]
        Utility.showProgress()
        CommentRequest.saveComment(param: param) { (success, response, error) in
            Utility.dismissProgress()
            if error == nil {
                self.getVideoComments()
            } else {
                Utility.alert(message: error?.localizedDescription ?? "")
            }
        }
    }
    
    
    func getVideoComments() {
        let param = ["videoId" : self.video?._id ?? ""]
        Utility.showProgress()
        CommentRequest.getVideosComment(param: param) { (success, response, error) in
            Utility.dismissProgress()
            if error == nil {
                self.comment = response?.data ?? []
                self.tableView.reloadData()
            } else {
                Utility.alert(message: error?.localizedDescription ?? "")
            }
        }
    }
    
    func addToShelf() {
        let param = ["asin" : self.video?.asin ?? "",
                     "title" : self.video?.productName ?? "",
                     "url" : self.video?.productUrl ?? "",
                     "images": self.video?.productThumbnailUrl ?? "",
                     "brand": ""]
        ShelfRequest.addToShelf(param: param) { (success, response, error) in
            Utility.dismissProgress()
            if error != nil {
                Utility.alert(message: error?.localizedDescription ?? "Something went wrong!")
            } else {
                print(response?.status)
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
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        view.layer.cornerRadius = 12
    }
    
    
    @IBAction func buyButton(_ sender: UIButton) {
        if let url = URL(string: self.video?.productUrl ?? "") {
            UIApplication.shared.open(url, options: [:]) { (success) in
                
            }
        }
    }
    
    @IBAction func shelfButton(_ sender: UIButton) {
        self.addToShelf()
    }
    
    @IBAction func wishListButton(_ sender: UIButton) {
        
    }
    
    @IBAction func likeVideButtonTapped(_ sender: Any) {
        if self.isLiked {
            self.removeLikeVideo()
        } else {
            self.likeVideo()
        }
        
    }
    
    @IBAction func saveCommentButtonTapped(_ sender: Any) {
        
    }
    
    
    override func pullUpControllerWillMove(to stickyPoint: CGFloat) {
        //        print("will move to \(stickyPoint)")
    }
    
    override func pullUpControllerDidMove(to stickyPoint: CGFloat) {
        //        print("did move to \(stickyPoint)")
    }
    
    override func pullUpControllerDidDrag(to point: CGFloat) {
        //        print("did drag to \(point)")
        if point > 230 {
            self.stackMenu.isHidden = false
        } else {
            self.stackMenu.isHidden = true
        }
    }
    
    override var pullUpControllerPreferredSize: CGSize {
        return portraitSize
    }
    
    override var pullUpControllerPreferredLandscapeFrame: CGRect {
        return landscapeFrame
    }
    
    override var pullUpControllerBounceOffset: CGFloat {
        return 20
    }
    
    override func pullUpControllerAnimate(action: PullUpController.Action,
                                          withDuration duration: TimeInterval,
                                          animations: @escaping () -> Void,
                                          completion: ((Bool) -> Void)?) {
        switch action {
        case .move:
            UIView.animate(withDuration: 0.5,
                           delay: 0,
                           usingSpringWithDamping: 0.7,
                           initialSpringVelocity: 0,
                           options: .curveEaseInOut,
                           animations: animations,
                           completion: completion)
        default:
            UIView.animate(withDuration: 0.5,
                           animations: animations,
                           completion: completion)
        }
    }
    
}

extension SlideUpViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.comment.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "SlideTableViewCell", for: indexPath) as! SlideTableViewCell
        cell.userName.text = self.comment[indexPath.row].userId?.name
        if let urlStr = self.video?.productThumbnailUrl {
            if let url = URL(string: BASE + urlStr){
                cell.userImageView.sd_setImage(with: url) { (image, error, type, url) in
                    cell.userImageView.image = image
                }
            }
        }
        
        let font = UIFont.boldSystemFont(ofSize: 15)
        let fontAttributes = [NSAttributedString.Key.font: font]
        let userName = self.comment[indexPath.row].userId?.name ?? ""
        let calWidth = (userName as NSString).size(withAttributes: fontAttributes)
        cell.userWidthConstraint.constant = calWidth.width + 5
        cell.commentLabel.text = self.comment[indexPath.row].comment
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 100.0
    }
    
    
}
