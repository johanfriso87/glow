//
//  ProfileEditViewController.swift
//  glow
//
//  Created by Dreams on 26/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class ProfileEditViewController: BaseViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {

    @IBOutlet weak var tableView: UITableView!
    var imagePicker = UIImagePickerController()
    var skinToneHexColorString = ""
    var user: User?
    var userName = ""
    var name = ""
    var about = ""
    var birthdate = ""
    var website = ""
    var email = ""
    var skinColor = ""
    var skinType = ""
    var haircolor = ""
    var hairTexture = ""
    var hairType = ""
    var eyeColor = ""
    var skinCare:[String] = []
    var hairCare: [String] = []
    var imageURL: URL! = nil
    var profileImage: UIImage?
    var profileImageUpdate: Bool = false
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.tableView.delegate = self
        self.tableView.dataSource = self
        self.tableView.tableFooterView = UIView(frame: CGRect.zero)
        self.getUserProfile()
        self.keybordDismiss()
        NotificationCenter.default.addObserver(self, selector: #selector(self.didChangeSkin(_:)), name: Notification.Name("SkinTone"), object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.didChangeSkinType(_:)), name: Notification.Name("SkinType"), object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.didChangeHairColor(_:)), name: Notification.Name("HairColor"), object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.didChangeHairTexture(_:)), name: Notification.Name("HairTexture"), object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.didChangeHairType(_:)), name: Notification.Name("HairType"), object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.didChangeEyeColor(_:)), name: Notification.Name("EyeColor"), object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.didChangeSkinCare(_:)), name: Notification.Name("SkinCare"), object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.didChangeHairCare(_:)), name: Notification.Name("HairCare"), object: nil)
   
        // Do any additional setup after loading the view.
    }
    
    
    func keybordDismiss() {
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(self.hideKeyboard))
        tapGesture.cancelsTouchesInView = true
        tableView.addGestureRecognizer(tapGesture)
    }
    
   @objc func hideKeyboard() {
        tableView.endEditing(true)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        NotificationCenter.default.removeObserver(self)
    }

    func getUserProfile() {
        Utility.showProgress()
        UserRequest.getUser { (success, user, error) in
            Utility.dismissProgress()
            if error == nil {
                self.user = user
                UserDefaultHelper.saveUser(user: self.user!)
                self.tableView.reloadData()
            } else {
                Utility.alert(message: error?.localizedDescription ?? "")
            }
        }
    }
    
    func profileImageUpload(image: UIImage) {
        if profileImageUpdate {
            Utility.showProgress()
            let data = image.jpegData(compressionQuality: 1.0)
            UserRequest.userProfileUpdate(param: [:], filePathKey: ["profile"], imageDataKey: [data!]) { (success, response, error) in
                Utility.dismissProgress()
                self.profileImageUpdate = false
                if error == nil {
                    if let profileImage = response?.profileImage {
                        Loggdinuser.set(profileImage, forKey: USERIMAGE)
                    }
                } else {
                    
                    Utility.alert(message: error.debugDescription)
                }
            }
        }
    }
    
    func updateUserProfile() {
        let currentname = name.isEmpty ? user?.name ?? "" : name
        let currentUsername = userName.isEmpty ? user?.userName ?? "" : userName
        let currentAbout = about.isEmpty ? user?.about ?? "" : about
        let currentEmail = email.isEmpty ? user?.email ?? "" : email
        _ = birthdate.isEmpty ? user?.birthDate ?? "" : birthdate
        let currentWebsite = website.isEmpty ? user?.website ?? "" : website
        let currentEyeColor = eyeColor.isEmpty ? user?.eyeColor ?? "" : eyeColor
        let currentHairColor = haircolor.isEmpty ? user?.hairColor ?? "" : haircolor
        let currentHairTexture = hairTexture.isEmpty ? user?.hairTexture ?? "" : hairTexture
        let currentHairType = hairType.isEmpty ? user?.hairType ?? "" : hairType
        let currentSkinTone = skinToneHexColorString.isEmpty ? user?.skinTone ?? "" : skinToneHexColorString
        let currentSkinType = skinType.isEmpty ? user?.skinType ?? "" : skinType
        let currentSkinCare = skinCare.isEmpty ? user?.skinCareInterests ?? [] : skinCare
        let currentHairCare = hairCare.isEmpty ? user?.hairCareInterests ?? [] : hairCare
        
        let param = ["name" : currentname,
                     "userName" : currentUsername,
                     "about": currentAbout,
                     "email": currentEmail,
                     //"birthdate": currentBirthdate,
                     "website": currentWebsite,
                     "eyeColor" : currentEyeColor,
                     "hairColor" : currentHairColor,
                     "hairTexture" : currentHairTexture,
                     "hairType" : currentHairType,
                     "skinTone" : currentSkinTone,
                     "skinType" : currentSkinType,
                     "skinCareInterests" : currentSkinCare,
                     "hairCareInterests" : currentHairCare] as [String : Any]
        UserRequest.userUpdateApi(param: param, filePathKey: []) { (success, user, error) in
            Utility.dismissProgress()
            if error == nil {
                self.user = user?.user
                Utility.alert(message: "Save Successfully", button1: "Ok") { (index) in
                  //  self.navigationController?.popViewController(animated: true)
                    for i in self.navigationController!.viewControllers {
                        if let vc = i as? ProfileViewController {
                            self.navigationController?.popToViewController(vc, animated: true)
                        }
                    }
                }
               
                
            } else {
                Utility.alert(message: error?.localizedDescription ?? "")
            }
        }
    }
    
     @objc func didChangeSkin(_ notification: NSNotification) {
           if let obj = notification.object as? String {
               self.skinToneHexColorString = obj
           }
       }
    
    @objc func didChangeSkinType(_ notification: NSNotification) {
        if let obj = notification.object as? String {
            self.skinType = obj
        }
    }
    @objc func didChangeHairColor(_ notification: NSNotification) {
           if let obj = notification.object as? String {
               self.haircolor = obj
           }
       }
    
    @objc func didChangeHairType(_ notification: NSNotification) {
        if let obj = notification.object as? String {
            self.hairType = obj
        }
    }
    @objc func didChangeHairTexture(_ notification: NSNotification) {
           if let obj = notification.object as? String {
               self.hairTexture = obj
           }
       }
    
    @objc func didChangeEyeColor(_ notification: NSNotification) {
        if let obj = notification.object as? String {
            self.eyeColor = obj
        }
    }
    
    @objc func didChangeSkinCare(_ notification: NSNotification) {
        if let obj = notification.object as? [String] {
            self.skinCare = obj
        }
    }
    
    @objc func didChangeHairCare(_ notification: NSNotification) {
        if let obj = notification.object as? [String] {
            self.hairCare = obj
        }
    }
    
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        let chosenImageURL = info[UIImagePickerController.InfoKey.imageURL] as? URL
        self.imageURL = chosenImageURL
        if let chosenImage = info[UIImagePickerController.InfoKey.originalImage] as? UIImage {
            self.profileImageUpdate = true
            self.profileImage = chosenImage
            self.profileImageUpload(image: chosenImage)
            self.tableView.reloadData()
            dismiss(animated: true, completion: nil)
        }
        
    }
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController){
        dismiss(animated: true, completion: nil)
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */
        func showActionSheet() {
            let actionSheet = UIAlertController(title: nil, message: nil, preferredStyle: UIAlertController.Style.actionSheet)
            actionSheet.addAction(UIAlertAction(title: "Gallery", style: UIAlertAction.Style.default, handler: { (alert:UIAlertAction!) -> Void in
                self.photoLibrary()
            }))
            
//            actionSheet.addAction(UIAlertAction(title: "Camera", style: UIAlertAction.Style.default, handler: { (alert:UIAlertAction!) -> Void in
//                self.selectcamera()
//            }))
    
            actionSheet.addAction(UIAlertAction(title: "Cancel", style: UIAlertAction.Style.cancel, handler: nil))
    
            self.present(actionSheet, animated: true, completion: nil)
    }
    
    func photoLibrary() {
        if UIImagePickerController.isSourceTypeAvailable(.savedPhotosAlbum){
            imagePicker.delegate = self
            imagePicker.sourceType = .photoLibrary
            imagePicker.allowsEditing = false
            imagePicker.navigationBar.topItem?.rightBarButtonItem?.title = "Cancel"
            imagePicker.navigationBar.topItem?.rightBarButtonItem?.isEnabled = true
            imagePicker.navigationBar.barStyle = UIBarStyle.blackOpaque
            imagePicker.navigationBar.topItem?.rightBarButtonItem?.tintColor = UIColor.black
            
            present(imagePicker, animated: true, completion: nil)
        }
    }
    
    func selectcamera() {
        if UIImagePickerController.isSourceTypeAvailable(.savedPhotosAlbum){
            imagePicker.delegate = self
            imagePicker.sourceType = .camera
            imagePicker.allowsEditing = true
            imagePicker.navigationBar.topItem?.rightBarButtonItem?.title = "Cancel"
            imagePicker.navigationBar.topItem?.rightBarButtonItem?.isEnabled = true
            imagePicker.navigationBar.barStyle = UIBarStyle.blackOpaque
            imagePicker.navigationBar.topItem?.rightBarButtonItem?.tintColor = UIColor.black
            
            present(imagePicker, animated: true, completion: nil)
        }
    }
    
//    @IBAction func chooseImageButton(_ sender: UIButton) {
//        self.showActionSheet()
//    }
    
    
    @IBAction func backButton(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func saveButton(_ sender: UIButton) {
        self.updateUserProfile()
    }

}

extension ProfileEditViewController: UITableViewDelegate, UITableViewDataSource {
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 10
    }
    
    @objc func chooseImage(_ sender: UIButton) {
        self.showActionSheet()
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section {
        case 0:
            let cell = tableView.dequeueReusableCell(withIdentifier: "ProfileImageTableViewCell", for: indexPath) as! ProfileImageTableViewCell
            
            if profileImageUpdate {
                if profileImage != nil {
                    cell.profileImageView.image = profileImage
                }
            } else {
                if let urlString = user?.profilePic {
                    if let url = URL(string: BASE + urlString) {
                        Utility.showProgress()
                        cell.profileImageView.sd_setImage(with: url) { (image, error, type, url) in
                            Utility.dismissProgress()
                            cell.profileImageView.image = image
                        }
                    }
                }
            }
            
            cell.profileImageButton.tag = indexPath.row
            cell.profileImageButton.addTarget(self, action: #selector(self.chooseImage(_:)), for: .touchUpInside)
            return cell
        case 1:
            let cell = tableView.dequeueReusableCell(withIdentifier: "ProfileInformationTableViewCell", for: indexPath) as! ProfileInformationTableViewCell
            cell.nameTextField.text = self.user?.name
            cell.userNameTextField.text = self.user?.userName
            cell.aboutTextField.text = self.user?.about
            cell.emailTextField.text = self.user?.email
            cell.birthdateTextField.text = self.user?.birthDate
            cell.websiteTextField.text = self.user?.website
            cell.nameTextField.delegate = self
            cell.userNameTextField.delegate = self
            cell.aboutTextField.delegate = self
            cell.emailTextField.delegate = self
            cell.birthdateTextField.delegate = self
            cell.nameTextField.tag = indexPath.row
            cell.nameTextField.addTarget(self, action: #selector(self.nameTextField(_:)), for: .editingChanged)
            cell.userNameTextField.tag = indexPath.row
            cell.userNameTextField.addTarget(self, action: #selector(self.userNameTextField(_:)), for: .editingChanged)
            cell.emailTextField.tag = indexPath.row
            cell.emailTextField.addTarget(self, action: #selector(self.emailTextField(_:)), for: .editingChanged)
            cell.birthdateTextField.tag = indexPath.row
            cell.birthdateTextField.addTarget(self, action: #selector(self.birthdateTextField(_:)), for: .editingChanged)
            cell.websiteTextField.tag = indexPath.row
            cell.websiteTextField.addTarget(self, action: #selector(self.websiteTextField(_:)), for: .editingChanged)
            cell.aboutTextField.tag = indexPath.row
            cell.aboutTextField.addTarget(self, action: #selector(self.aboutTextField(_:)), for: .editingChanged)
            cell.birthdateTextField.isHidden = true
//            let datePickerView = UIDatePicker()
//               datePickerView.datePickerMode = .date
//            cell.birthdateTextField.inputView = datePickerView
//               datePickerView.addTarget(self, action: #selector(handleDatePicker(sender:)), for: .valueChanged)
//            let toolBar = UIToolbar()
//            toolBar.barStyle = UIBarStyle.default
//            toolBar.isTranslucent = true
//            let space = UIBarButtonItem(barButtonSystemItem: UIBarButtonItem.SystemItem.flexibleSpace, target: nil, action: nil)
//            let doneButton = UIBarButtonItem(title: "Done", style: UIBarButtonItem.Style.done, target: self, action: #selector(self.closedatePickerButton(_:)))
//
//               // if you remove the space element, the "done" button will be left aligned
//               // you can add more items if you want
//               toolBar.setItems([space, doneButton], animated: false)
//            toolBar.isUserInteractionEnabled = true
//               toolBar.sizeToFit()
//
//            cell.birthdateTextField.inputAccessoryView = toolBar
            return cell
        case 2:
            let cell = tableView.dequeueReusableCell(withIdentifier: "SkinToneTableViewCell", for: indexPath) as! SkinToneTableViewCell
            cell.skinTone = user?.skinTone ?? ""
//            cell.brightnessSlider.handle.handleColor = UIColor.hexStringToUIColor(hex: self.user?.skinTone ?? "")
            //cell.delegate = self
            return cell
        case 3:
        let cell = tableView.dequeueReusableCell(withIdentifier: "SkinTypeTableViewCell", for: indexPath) as! SkinTypeTableViewCell
        cell.selectedType = self.user?.skinType ?? ""
        cell.collectionView.reloadData()
        return cell
        case 4:
            let cell = tableView.dequeueReusableCell(withIdentifier: "HairColorTableViewCell", for: indexPath) as! HairColorTableViewCell
            cell.selectedHairColor = self.user?.hairColor ?? ""
            cell.collectionView.reloadData()
            return cell
        case 5:
        let cell = tableView.dequeueReusableCell(withIdentifier: "HairTextureTableViewCell", for: indexPath) as! HairTextureTableViewCell
        cell.selectedHairTexture = self.user?.hairTexture ?? ""
        cell.collectionView.reloadData()
        return cell
        case 6:
            let cell = tableView.dequeueReusableCell(withIdentifier: "HairTypeTableViewCell", for: indexPath) as! HairTypeTableViewCell
            cell.selectedHairType = self.user?.hairType ?? ""
            cell.collectionView.reloadData()
            return cell
        case 7:
            let cell = tableView.dequeueReusableCell(withIdentifier: "EyeColorTableViewCell", for: indexPath) as! EyeColorTableViewCell
            cell.eyeColorString = self.user?.eyeColor ?? ""
            cell.collectionView.reloadData()
            return cell
        case 8:
            let cell = tableView.dequeueReusableCell(withIdentifier: "SkinCareTableViewCell", for: indexPath) as! SkinCareTableViewCell
            cell.skinCareList = self.user?.skinCareInterests ?? []
            
            
            if !(user?.skinCareInterests?.isEmpty ?? true) {
        
                if ((self.user!.skinCareInterests!.filter({$0.lowercased() == "acne".lowercased()}).first) != nil) {
                    cell.acneButton.select()
                }
                if ((self.user!.skinCareInterests!.filter({$0.lowercased() == "blackheads".lowercased()}).first) != nil) {
                    cell.blackHeadButton.select()
                }
                if ((self.user!.skinCareInterests!.filter({$0.lowercased() == "cuticles".lowercased()}).first) != nil) {
                    cell.cuticlesButton.select()
                }
                if((self.user!.skinCareInterests!.filter({$0.lowercased() == "dullness".lowercased()}).first) != nil) {
                    cell.dullnessButton.select()
                }
                if ((self.user!.skinCareInterests!.filter({$0.lowercased() == "puffiness".lowercased()}).first) != nil) {
                    cell.puffinesButton.select()
                }
                if ((self.user!.skinCareInterests!.filter({$0.lowercased() == "sensitivity".lowercased()}).first) != nil) {
                    cell.sensitivityButton.select()
                }
                if ((self.user!.skinCareInterests!.filter({$0.lowercased() == "sun damage".lowercased()}).first) != nil) {
                    cell.sunDamageButton.select()
                }
                if ((self.user!.skinCareInterests!.filter({$0.lowercased() == "aging".lowercased()}).first) != nil) {
                    cell.agingButton.select()
                }
                if ((self.user!.skinCareInterests!.filter({$0.lowercased() == "cellulite".lowercased()}).first) != nil) {
                    cell.celluliteButton.select()
                }
                if ((self.user!.skinCareInterests!.filter({$0.lowercased() == "dark circles".lowercased()}).first) != nil) {
                    cell.darkCirclesButton.select()
                }
                if ((self.user!.skinCareInterests!.filter({$0.lowercased() == "pores".lowercased()}).first) != nil) {
                    cell.rednessButton.select()
                }
                if ((self.user!.skinCareInterests!.filter({$0.lowercased() == "redness".lowercased()}).first) != nil) {
                    cell.poresButton.select()
                }
                if ((self.user!.skinCareInterests!.filter({$0.lowercased() == "strech marks".lowercased()}).first) != nil) {
                    cell.strechMarksButton.select()
                }
                if ((self.user!.skinCareInterests!.filter({$0.lowercased() == "uneven Tones".lowercased()}).first) != nil) {
                    cell.unevenTonesButton.select()
                }
            }
            
           

            return cell
        case 9:
            let cell = tableView.dequeueReusableCell(withIdentifier: "HairCareTableViewCell", for: indexPath) as! HairCareTableViewCell
            cell.hairCareList = self.user?.hairCareInterests ?? []
            if !(self.user?.hairCareInterests?.isEmpty ?? true) {
                if ((self.user!.hairCareInterests!.filter({$0.lowercased() == "anti-aging".lowercased()}).first) != nil) {
                    cell.antiAging.select()
                }
                
                if ((self.user!.hairCareInterests!.filter({$0.lowercased() == "curl-enhancing".lowercased()}).first) != nil) {
                    cell.curlEnhancing.select()
                }
                
                if ((self.user!.hairCareInterests!.filter({$0.lowercased() == "heat protaction".lowercased()}).first) != nil){
                    cell.heatProtaction.select()
                }
                if ((self.user!.hairCareInterests!.filter({$0.lowercased() == "color protaction".lowercased()}).first) != nil) {
                    cell.colorProtaction.select()
                }
                if ((self.user!.hairCareInterests!.filter({$0.lowercased() == "oiliness".lowercased()}).first) != nil) {
                    cell.oiliness.select()
                }
                if ((self.user!.hairCareInterests!.filter({$0.lowercased() == "straightening".lowercased()}).first) != nil) {
                    cell.straightening.select()
                }
                if ((self.user!.hairCareInterests!.filter({$0.lowercased() == "damaged".lowercased()}).first) != nil) {
                    cell.damaged.select()
                }
                if ((self.user!.hairCareInterests!.filter({$0.lowercased() == "hold".lowercased()}).first) != nil) {
                    cell.hold.select()
                }
                if ((self.user!.hairCareInterests!.filter({$0.lowercased() == "texture".lowercased()}).first) != nil) {
                    cell.texture.select()
                }
                if ((self.user!.hairCareInterests!.filter({$0.lowercased() == "shine".lowercased()}).first) != nil) {
                    cell.shine.select()
                }
            }
           
            return cell
        default:
            return UITableViewCell()
        }
    }
    
    @objc func closedatePickerButton(_ sender: UIButton) {
        self.dismiss(animated: true, completion: nil)
    }

    @objc func handleDatePicker(sender: UIDatePicker) {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "dd MMM yyyy"
        let indexPath = IndexPath(row: sender.tag, section: 1)
        if let cell = tableView.cellForRow(at: indexPath) as? ProfileInformationTableViewCell {
            cell.birthdateTextField.text = dateFormatter.string(from: sender.date)
        }
    }
    
    @objc func nameTextField(_ sender: UITextField) {
        if let cell = tableView.cellForRow(at: IndexPath(row: sender.tag, section: 1)) as? ProfileInformationTableViewCell{
            self.name = cell.nameTextField.text ?? ""
        }
    }
    @objc func userNameTextField(_ sender: UITextField) {
        if let cell = tableView.cellForRow(at: IndexPath(row: sender.tag, section: 1)) as? ProfileInformationTableViewCell{
            self.userName = cell.userNameTextField.text ?? ""
        }
    }
    @objc func emailTextField(_ sender: UITextField) {
        if let cell = tableView.cellForRow(at: IndexPath(row: sender.tag, section: 1)) as? ProfileInformationTableViewCell{
            self.email = cell.emailTextField.text ?? ""
        }
    }
    @objc func aboutTextField(_ sender: UITextField) {
        if let cell = tableView.cellForRow(at: IndexPath(row: sender.tag, section: 1)) as? ProfileInformationTableViewCell{
            self.about = cell.aboutTextField.text ?? ""
        }
    }
    @objc func birthdateTextField(_ sender: UITextField) {
        if let cell = tableView.cellForRow(at: IndexPath(row: sender.tag, section: 1)) as? ProfileInformationTableViewCell{
            self.birthdate = cell.birthdateTextField.text ?? ""
        }
    }
    @objc func websiteTextField(_ sender: UITextField) {
        if let cell = tableView.cellForRow(at: IndexPath(row: sender.tag, section: 1)) as? ProfileInformationTableViewCell{
            self.website = cell.websiteTextField.text ?? ""
        }
    }
    
   
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        switch indexPath.section {
        case 0:
            return 120
        case 1:
            return UITableView.automaticDimension
        case 2:
            return 125
        case 3:
            return 175
        case 4:
            return 350
        case 5:
            return 250
        case 6:
            return 250
        case 7:
            return 250
        case 8:
            return 350
        case 9:
            return 250
        default:
            return 0
        }
    }
//    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
//        if section == 8 {
//            let baseView = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
//            let view = UIView(frame: CGRect(x: 0, y: 0, width: baseView.frame.size.width - 50, height: 18))
//            let label = UILabel(frame: CGRect(x: 20, y: 20, width: tableView.frame.size.width - 50, height: 50))
//            tableView.tableHeaderView?.backgroundColor = .clear
//            tableView.tableHeaderView?.tintColor = .clear
//            label.text = "TEST TEXT"
//            label.textColor = UIColor.black
//            label.backgroundColor = UIColor.black
//            baseView.backgroundColor = .black
//            view.backgroundColor = UIColor.white
//            view.addSubview(label)
//
//            baseView.addSubview(view)
//            self.view.addSubview(baseView)
//            return view
//        } else {
//            return nil
//        }
//    }
//
//    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
//        if section == 8 {
//            return 50
//        }
//        return 0
//    }
}
