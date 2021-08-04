//
//  SearchUrlViewController.swift
//  glow
//
//  Created by Dreams on 26/07/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class SearchUrlViewController: BaseViewController {

    @IBOutlet weak var nextTextField: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.addButtonInTextField()
        
        // Do any additional setup after loading the view.
    }
    
    func addButtonInTextField() {
        let button = UIButton(type: .custom)
        button.setImage(UIImage(named: "correct"), for: .normal)
        button.setTitleColor(.black, for: .normal)
        button.imageEdgeInsets = UIEdgeInsets(top: 0, left: -16, bottom: 0, right: 0)
        let text = self.nextTextField.text
        button.frame = CGRect(x: CGFloat(nextTextField.frame.size.width - 25), y: CGFloat(5), width: CGFloat(25), height: CGFloat(25))
        button.addTarget(self, action: #selector(self.refresh(_:)), for: .touchUpInside)
        nextTextField.rightView = button
        nextTextField.rightViewMode = .always
    }
    @objc func refresh(_ sender: Any) {
        let storyBoard = UIStoryboard(name: "Search", bundle: nil)
       if let vc = storyBoard.instantiateViewController(withIdentifier: "SelectProductWebViewController") as? SelectProductWebViewController {
            vc.webUrl = nextTextField.text!
            self.present(vc, animated: true, completion: nil)
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
    @objc func navigateToProduct(_ sender: UIButton) {
        
    }
    
    
    
    
    @IBAction func dismissButton(_ sender: UIButton) {
        self.dismiss(animated: true, completion: nil)
    }

}
