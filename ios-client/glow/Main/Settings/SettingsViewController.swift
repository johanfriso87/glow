//
//  SettingsViewController.swift
//  glow
//
//  Created by Dreams on 25/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class SettingsViewController: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    let profileSection = ["Edit Profile", "My Wish List", "Like Videos", "Share Profile", "My Orders"]
    let connectSection = ["Contact Supergreat", "Rate & Review Glow", "Glow Tips", "Shop Glow Swag"]
    let paymentSection = ["In-App Purchase"]
    let inviteSection = ["Enter Invite Code", "Invite Friends, Get Coins"]
    let accountSection = ["Notification Settings", "Terms of Service", "Logout"]
    let cellReuseIdentifier = "cell"
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tableView.register(UITableViewCell.self, forCellReuseIdentifier: cellReuseIdentifier)
        self.tableView.delegate = self
        self.tableView.dataSource = self
        self.tableView.tableFooterView = UIView(frame: CGRect.zero)
        
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
    
    @IBAction func backButton(_ sender: Any) {
        self.navigationController?.popViewController(animated: true)
        self.dismiss(animated: true, completion: nil)
    }

}

extension SettingsViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case 0:
            return self.profileSection.count
        case 1:
            return self.connectSection.count
        case 2:
            return self.paymentSection.count
        case 3:
            return self.inviteSection.count
        case 4:
            return self.accountSection.count
        default:
            return 0
        }
    }
    
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
        if indexPath.section == 0 {
            cell.textLabel?.text = self.profileSection[indexPath.row]
        } else if indexPath.section == 1 {
            cell.textLabel?.text = self.connectSection[indexPath.row]
        } else if indexPath.section == 2 {
            cell.textLabel?.text = self.paymentSection[indexPath.row]
        } else if indexPath.section == 3 {
            cell.textLabel?.text = self.inviteSection[indexPath.row]
        } else if indexPath.section == 4 {
            cell.textLabel?.text = self.accountSection[indexPath.row]
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        switch indexPath.section {
        case 0:
            if indexPath.row == 0 {
                let storyBoard = UIStoryboard(name: "Profile", bundle: nil)
                if let viewController = storyBoard.instantiateViewController(withIdentifier: "ProfileEditViewController") as? ProfileEditViewController {
                    self.navigationController?.pushViewController(viewController, animated: true)
                }
            } else if indexPath.row == 2 {
               let storyBoard = UIStoryboard(name: "Profile", bundle: nil)
                if let viewController = storyBoard.instantiateViewController(withIdentifier: "LikeVideosViewController") as? LikeVideosViewController {
                    self.navigationController?.pushViewController(viewController, animated: true)
                }
            } else if indexPath.row == 3 {
                let activityViewController = UIActivityViewController(activityItems: ["imageToShare"], applicationActivities: nil)
                activityViewController.popoverPresentationController?.sourceView = self.view // so that 
                self.present(activityViewController, animated: true, completion: nil)
            }
            
            break;
        case 1:
            if indexPath.row == 2 {
                let storyBoard = UIStoryboard(name: "Profile", bundle: nil)
                let vc = storyBoard.instantiateViewController(withIdentifier: "TipsViewController") as! TipsViewController
                vc.customBlurEffectStyle = .dark
                vc.customAnimationDuration = TimeInterval(0.5)
                vc.customInitialScaleAmmount = CGFloat(Double(0.3))
                MIBlurPopup.show(vc, on: self)
            }
            break;
        case 3:
            if indexPath.row == 1 {
                let storyBoard = UIStoryboard(name: "Profile", bundle: nil)
                let vc = storyBoard.instantiateViewController(withIdentifier: "InviteViewController") as! InviteViewController
                self.navigationController?.pushViewController(vc, animated: true)
            }
            break;
        case 4:
            if indexPath.row == 2 {
                Loggdinuser.set(false, forKey: USERLOGIN)
                Loggdinuser.removeObject(forKey: USERIMAGE)
                UserDefaults.standard.removeObject(forKey: "Stickers")
                UserDefaultHelper.removeUser()
                AppDelegate.sharedInstance.navigateToLoginScreen()
            }
            break;
        default:
            break;
        }
    }
    
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 5
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        //self.tableView.tableHeaderView = section.he
        if section == 0 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 20, width: tableView.frame.size.width, height: 50))
            label.text = "MY PROFILE"
            label.font = label.font.withSize(14.0)
            label.textColor = UIColor.lightGray
            view.backgroundColor = UIColor.groupTableViewBackground
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        } else if section == 1 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 20, width: tableView.frame.size.width, height: 50))
            label.text = "CONNECT WITH US"
            label.font = label.font.withSize(14.0)
            label.textColor = UIColor.lightGray
            view.backgroundColor = UIColor.groupTableViewBackground
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        }else if section == 2 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 20, width: tableView.frame.size.width, height: 50))
            label.text = "PAYMENT & SHIPPING"
            label.font = label.font.withSize(14.0)
            label.textColor = UIColor.lightGray
            view.backgroundColor = UIColor.groupTableViewBackground
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        }else if section == 3 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 20, width: tableView.frame.size.width, height: 50))
            label.text = "INVITES"
            label.font = label.font.withSize(14.0)
            label.textColor = UIColor.lightGray
            view.backgroundColor = UIColor.groupTableViewBackground
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        }else if section == 4 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 20, width: tableView.frame.size.width, height: 50))
            label.text = "ACCOUNT"
            label.font = label.font.withSize(14.0)
            label.textColor = UIColor.lightGray
            view.backgroundColor = UIColor.groupTableViewBackground
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        } else {
            return nil
        }
    }
       
       func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
            return 60
       }
    
}
