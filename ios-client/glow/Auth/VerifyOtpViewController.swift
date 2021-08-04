//
//  VerifyOtpViewController.swift
//  glow
//
//  Created by Dreams on 15/08/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class VerifyOtpViewController: BaseViewController {

    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var enterOtpTextField: UITextField!
    @IBOutlet weak var signUpButton: UIButton!
    var email = ""
    static func getInstance(email: String) -> VerifyOtpViewController {
        let viewController = Constants.Storyboard.LOGIN.instantiateViewController(withIdentifier: "VerifyOtpViewController") as! VerifyOtpViewController
        viewController.email = email
        return viewController
    }
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    func verifyOtpApiCall() {
        
            if self.enterOtpTextField.text?.isEmpty ?? true {
                Utility.alert(message: "Please enter otp.")
            } else  {
                let param = ["email" : email,
                             "otp" : self.enterOtpTextField.text ?? ""] as [String : Any]
                Utility.showProgress()
                AuthRequest.otpVerify(param: param) { (succeass, response, error) in
                    Utility.dismissProgress()
                    if error == nil && response?.user != nil {
                        let user = response!.user
                        UserDefaultHelper.saveUser(user: user!)
                        AppData.sharedInstance.user = user
                        Loggdinuser.set(true, forKey: USERLOGIN)
                        Loggdinuser.set(user?.profilePic, forKey: USERIMAGE)
                        Loggdinuser.set(user?.token ?? "", forKey: ACCESSTOKEN)
                        Utility.alert(message: response?.msg ?? "", button1: "OK") { (index) in
                            self.navigateToHomeScreen()
                        }
                    } else {
                        Utility.alert(message: error?.localizedDescription ?? response?.msg ?? "")
                    }
                }
            }
        
    }
    
    func navigateToHomeScreen() {
        let storyBoard = UIStoryboard(name: "Main", bundle: nil)
        let vc = storyBoard.instantiateViewController(withIdentifier: "navigation") as? UINavigationController
        AppDelegate.sharedInstance.window?.rootViewController = vc
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */
    
    @IBAction func signUpButton(_ sender: UIButton) {
        self.verifyOtpApiCall()
    }
    
    @IBAction func backButton(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }

}
