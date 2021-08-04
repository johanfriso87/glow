//
//  SelectProductWebViewController.swift
//  glow
//
//  Created by Dreams on 26/07/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit
import WebKit

class SelectProductWebViewController: UIViewController , WKNavigationDelegate{

    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var productButton: UIButton!
    @IBOutlet weak var webView: WKWebView!
     var isNavigationUrl = true
    var webUrl = ""
    var asin = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        self.webView.navigationDelegate = self
        self.sendRequest(urlString: webUrl)
        self.productButton.isHidden = true
        //self.webView.uiDelegate = self
        // Do any additional setup after loading the view.
    }
    
    func sendRequest(urlString: String) {
        let myURL = URL(string: urlString)
        let myRequest = URLRequest(url: myURL!)
        webView.load(myRequest)
    }
    
    func webView(_ webView: WKWebView, didCommit navigation: WKNavigation!) {
        self.productButton.isHidden = false
        self.isNavigationUrl = true
    }
    
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        self.productButton.isHidden = false
    }
    
    func webView(_ webView: WKWebView, didStartProvisionalNavigation navigation: WKNavigation!) {
        
    }
    
    
    func webView(_ webView: WKWebView, didReceiveServerRedirectForProvisionalNavigation navigation: WKNavigation!) {
        
    }
   
    func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
           if let urlStr = navigationAction.request.mainDocumentURL?.absoluteString {
               
            if isNavigationUrl {
                print(urlStr)
                self.isNavigationUrl = false
                   self.webUrl = urlStr
            }
            
                //urlStr is what you want, I guess.
             }
             decisionHandler(.allow)
       }


    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */
    func validate(YourEMailAddress: String) -> Bool {
        let REGEX: String
        REGEX = "http://www.amazon.com/([\\w-]+/)?(dp|gp/product)/(\\w+/)?(\\w{10})"
        return NSPredicate(format: "SELF MATCHES %@", REGEX).evaluate(with: YourEMailAddress)
    }
    
    func urlApiCall() {
        print(self.webUrl.split(separator: "/"))
        let splitUrl = self.webUrl.split(separator: "/")
        //let asin =
        for (index,i) in splitUrl.enumerated() {
            if ((i.lowercased() == "dp" || i.lowercased() == "gp" || i.lowercased() == "product" ) && splitUrl[index+1].lowercased() != "product" && splitUrl[index+1].lowercased() != "dp"&&splitUrl[index+1].lowercased() != "gp")  {
                self.asin = String(splitUrl[index+1].split(separator: "?")[0])
                print(asin)
                //break
            }
            
        }
        let plitUrl = self.webUrl.components(separatedBy: "amazon.")
        let country: String? = plitUrl[1].split(separator: "/")[0].uppercased()
        if !self.asin.isEmpty {
            let param = ["asin" : self.asin,
                         "country" : "JP"] as [String : Any]
            Utility.showProgress()
            SearchRequest.searchProductFromUrl(param: param) { (success, search, error) in
                Utility.dismissProgress()
                if error == nil && search != nil {
                    let storyBoard = UIStoryboard(name: "Main", bundle: nil)
                    if let viewController = storyBoard.instantiateViewController(withIdentifier: "SearchResultViewController") as? SearchResultViewController {
                        viewController.searchList = [search!]
                        viewController.modalPresentationStyle = .overFullScreen
                        self.present(viewController, animated: true, completion: nil)
                    }
                    print("\(search)")
                } else {
                    Utility.alert(message: error?.localizedDescription ?? "Not supported country")
                }
            }
        } else {
            Utility.alert(message: "Sorry we can't fetch product from your url please  try again with valid amazon url")
        }
    }
    
    
    @IBAction func dismissButton(_ sender: UIButton) {
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func productSelect(_ sender: UIButton) {
        self.urlApiCall()
    }

}
