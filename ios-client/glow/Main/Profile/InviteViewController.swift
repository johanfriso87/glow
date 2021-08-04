//
//  InviteViewController.swift
//  glow
//
//  Created by Dreams on 28/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit
import ContactsUI
import MessageUI

class InviteViewController: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    var contactsList: [ContacList] = []
    override func viewDidLoad() {
        super.viewDidLoad()

        self.tableView.delegate = self
        self.tableView.dataSource = self
        self.tableView.tableFooterView = UIView(frame: CGRect.zero)
        self.tableView.contentInset = UIEdgeInsets(top: -50, left: 0, bottom: 0, right: 0)
    
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.contactsList = self.phoneNumberWithContryCode()
        self.tableView.reloadData()
    }
    
    
    func phoneNumberWithContryCode() -> [ContacList] {

        let contacts = PhoneContacts.getContacts() // here calling the getContacts methods
        var arrPhoneNumbers: [ContacList] = []
        for contact in contacts {
           
            for ContctNumVar: CNLabeledValue in contact.phoneNumbers {
                if let fulMobNumVar  = ContctNumVar.value as? CNPhoneNumber {
                    //let countryCode = fulMobNumVar.value(forKey: "countryCode") get country code
                       if let MccNamVar = fulMobNumVar.value(forKey: "digits") as? String {
                        let tempObj = ContacList(number: MccNamVar, name: contact.givenName)
                        arrPhoneNumbers.append(tempObj)
                    }
                }
            }
        }
        return arrPhoneNumbers // here array has all contact numbers.
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
    }

}

extension InviteViewController: UITableViewDelegate, UITableViewDataSource, MFMessageComposeViewControllerDelegate {
    
    
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 3
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case 0:
            return 1
        case 1:
            return 1
        case 2:
            return self.contactsList.count
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section {
        case 0:
            let cell = tableView.dequeueReusableCell(withIdentifier: "SearchTableViewCell", for: indexPath) as! SearchTableViewCell
            return cell
        case 1:
            let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath)
            cell.textLabel?.textAlignment = .center
            cell.textLabel?.textColor = .purple
            cell.textLabel?.numberOfLines = 0
            cell.textLabel?.text = "Get 1 Supercoin for each friend that joins Supergreat!"
            return cell
        case 2:
            let cell = tableView.dequeueReusableCell(withIdentifier: "InviteTableViewCell", for: indexPath) as! InviteTableViewCell
            cell.friendNameLabel.text = self.contactsList[indexPath.row].name ?? self.contactsList[indexPath.row].number
            cell.inviteButton.tag = indexPath.row
            cell.inviteButton.addTarget(self, action: #selector(self.sendMessage(_:)), for: .touchUpInside)
            return cell
        default:
            return UITableViewCell()
        }
    }
    
    @objc func sendMessage(_ sender: UIButton) {
        if (MFMessageComposeViewController.canSendText()) {
            let controller = MFMessageComposeViewController()
            controller.body = "Come join me on supergreate, community of beauty"
            controller.recipients = [self.contactsList[sender.tag].number!]
            controller.messageComposeDelegate = self
            self.present(controller, animated: true, completion: nil)
        }
    }
    
    func messageComposeViewController(_ controller: MFMessageComposeViewController, didFinishWith result: MessageComposeResult) {
         dismiss(animated: true, completion: nil)
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        switch indexPath.section {
        case 0:
            return 100
        case 1:
            return 100
        case 2:
            return 70
        default:
            return 0
        }
    }
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if section == 1 || section == 2 {
            return 50
        }
        return 0
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        if section == 1 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 0, width: tableView.frame.size.width, height: 50))
            label.text = "Friends You Invited"
            label.textColor = UIColor.black
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        } else if section == 2 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 0, width: tableView.frame.size.width, height: 50))
            label.text = "Invite Friends"
            label.textColor = UIColor.black
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        }
        return nil
    }
    
}
