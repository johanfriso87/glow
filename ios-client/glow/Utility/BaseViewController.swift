//
//  BaseViewController.swift
//  Construction
//
//  Created by Logileap on 11/03/19.
//  Copyright © 2019 KMSOFT. All rights reserved.
//

import UIKit

class BaseViewController: UIViewController {

    var allTextField: [UITextField] = []
    var beginTextField:((_ textField: UITextField) -> Bool)?
    override func viewDidLoad() {
        super.viewDidLoad()
        self.addGestureIfTextfieldExists()
    }
    
    //MARK: - AddGestureIfTextfieldExists
    func addGestureIfTextfieldExists() {
        self.allTextField = getAllTextFields(fromView : self.view)
        if  self.allTextField.count > 0 {
            let tapGesture = UITapGestureRecognizer(target: self, action: #selector(self.tapGestureClickEvent(_:)))
            tapGesture.cancelsTouchesInView = false
            self.view.addGestureRecognizer(tapGesture)
            self.setKeyboardIfTextFieldExists()
        }
    }
    
    
    
    @objc func tapGestureClickEvent(_ sender: UITapGestureRecognizer) {
        self.view.endEditing(true)
    }
    
    func getAllTextFields(fromView view: UIView)-> [UITextField] {
        return view.subviews.compactMap { (view) -> [UITextField]? in
            if view is UITextField {
                return [(view as! UITextField)]
            } else {
                return getAllTextFields(fromView: view)
            }
            }.flatMap({$0})
    }
    
    //MARK: - Set Keyboard Type
    
    func setKeyboardIfTextFieldExists() {
        for i in 0 ..< self.allTextField.count {
            let textfield = self.allTextField[i]
            textfield.delegate = self
            textfield.tag = i
            textfield.returnKeyType = i == self.allTextField.count - 1 ? .done : .next
            
            switch (textfield.textContentType) {
            case UITextContentType.emailAddress:
                textfield.keyboardType = .emailAddress
            case UITextContentType.password:
                textfield.keyboardType = .default
                textfield.isSecureTextEntry = true
            case UITextContentType.telephoneNumber:
                textfield.keyboardType = .numberPad
            default:
                textfield.keyboardType = .default
            }
        }
    }
    
}

extension BaseViewController: UITextFieldDelegate {
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        if let returnValue = self.beginTextField?(textField) {
            return returnValue
        }
        return true
    }
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        self.nextTextField(textField: textField)
        return true
    }
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if textField.textContentType == UITextContentType.telephoneNumber {
            return range.location < 10        
        }
        else {
            return true
        }
    }
    func nextTextField(textField: UITextField) {
        let nextTag = textField.tag + 1
        if let nextResponder = self.view.viewWithTag(nextTag) as? UITextField {
            if nextResponder.isUserInteractionEnabled {
                nextResponder.becomeFirstResponder()
            }
            else {
                self.nextTextField(textField: nextResponder)
            }
        } else {
            _ = UITableView().viewWithTag(nextTag) as? UITextField
            textField.resignFirstResponder()
        }
    }
}
