//
//  CategoriesViewController.swift
//  glow
//
//  Created by Dreams on 25/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class CategoriesViewController: BaseViewController {
    
    @IBOutlet weak var tableView: UITableView!
    let arrayString = ["dhruv","parth","sanjay"]
    var dashData: Dashboard?
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tableView.delegate = self
        self.tableView.dataSource = self
        self.tableView.tableFooterView = UIView(frame: CGRect.zero)
        self.getDashBoardData()
        // Do any additional setup after loading the view.
    }
    
    func getDashBoardData() {
        Utility.showProgress()
        DashBoardRequest.getData { (success, response, error) in
            Utility.dismissProgress()
            if error == nil {
                self.dashData = response
                self.tableView.reloadData()
            } else {
               // Utility.alert(message: error?.localizedDescription ?? "")
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
    
}

extension CategoriesViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.section == 0 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "HomeTableViewCell", for: indexPath) as! HomeTableViewCell
            cell.spotlightData = dashData?.uniqueVideos ?? []
            cell.spotlightCollectionView.reloadData()
        //    cell.spotlightData = ["dhruv","parth","sanjay"]
            return cell
        } else if indexPath.section == 1 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "HomeTableViewCell", for: indexPath) as! HomeTableViewCell
                cell.spotlightData = dashData?.videos ?? []
                cell.spotlightCollectionView.reloadData()
            //    cell.spotlightData = ["dhruv","parth","sanjay"]
                return cell
//            let cell = tableView.dequeueReusableCell(withIdentifier: "PlaylistTableViewCell", for: indexPath) as! PlaylistTableViewCell
//            cell.spotlightData = ["jay","nilay","abbhi"]
//            return cell
        } else if indexPath.section == 2 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "HomeTableViewCell", for: indexPath) as! HomeTableViewCell
            cell.spotlightData = dashData?.videos ?? []
            cell.spotlightCollectionView.reloadData()
         //   cell.spotlightData = ["dhruv","parth","sanjay"]
            return cell
        } else if indexPath.section == 3 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "PlaylistTableViewCell", for: indexPath) as! PlaylistTableViewCell
            cell.spotlightData = ["jay","nilay","abbhi"]
            return cell
        }
        return UITableViewCell()
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 4
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        if section == 0 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 20, width: tableView.frame.size.width, height: 50))
            label.text = "TEST TEXT"
            label.textColor = UIColor.black
            view.backgroundColor = .groupTableViewBackground
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        } else if section == 2 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 20, width: tableView.frame.size.width, height: 50))
            label.text = "TEST TEXT"
            label.textColor = UIColor.black
            view.backgroundColor = .groupTableViewBackground
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        } else {
            return nil
        }
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if section == 0 || section == 2{
            return 80
        } else {
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.section == 0 || indexPath.section == 2 {
            return 351
        } else if indexPath.section == 1 || indexPath.section == 3 {
            return 475
        } else {
            return 0
        }
    }
    
}
