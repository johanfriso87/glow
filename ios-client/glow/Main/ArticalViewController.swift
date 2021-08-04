//
//  ArticalViewController.swift
//  glow
//
//  Created by Dreams on 25/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class ArticalViewController: BaseViewController {

    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tableView.delegate = self
        self.tableView.dataSource = self
        self.tableView.tableFooterView = UIView(frame: CGRect.zero)
        //self.keybordDismiss()
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

}

extension ArticalViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "FeaturedArticalTableViewCell", for: indexPath) as! FeaturedArticalTableViewCell
        //cell.blogs = ["dhruv","parth","sanjay"]
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 365
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 8
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        if section == 0 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 20, width: tableView.frame.size.width, height: 50))
            label.text = "TEST TEXT"
            label.textColor = UIColor.black
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        } else if section == 1 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 20, width: tableView.frame.size.width, height: 50))
            label.text = "TEST TEXT"
            label.textColor = UIColor.black
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        }else if section == 2 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 20, width: tableView.frame.size.width, height: 50))
            label.text = "TEST TEXT"
            label.textColor = UIColor.black
            view.backgroundColor = .groupTableViewBackground
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        }else if section == 3 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 20, width: tableView.frame.size.width, height: 50))
            label.text = "TEST TEXT"
            label.textColor = UIColor.black
            view.backgroundColor = .groupTableViewBackground
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        } else if section == 4 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 20, width: tableView.frame.size.width, height: 50))
            label.text = "TEST TEXT"
            label.textColor = UIColor.black
            view.backgroundColor = .groupTableViewBackground
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        } else if section == 5 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 20, width: tableView.frame.size.width, height: 50))
            label.text = "TEST TEXT"
            label.textColor = UIColor.black
            view.backgroundColor = .groupTableViewBackground
            view.addSubview(label)
            self.view.addSubview(view)
            return view
        } else if section == 6 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 20, width: tableView.frame.size.width, height: 50))
            label.text = "Top Products"
            label.textColor = UIColor.black
            view.backgroundColor = .groupTableViewBackground
            //view.backgroundColor = UIColor.white
            view.addSubview(label)
           // view.backgroundColor = UIColor.white
            self.view.addSubview(view)
            return view
        }
        else if section == 7 {
            let view = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width - 40, height: 18))
            let label = UILabel(frame: CGRect(x: 20, y: 20, width: tableView.frame.size.width - 40, height: 50))
            label.text = "Top Products"
            label.textColor = UIColor.black
            view.backgroundColor = .groupTableViewBackground
            label.center = CGPoint(x: view.center.x, y: view.center.y + 20)
            view.layer.cornerRadius = 10
            
            view.addSubview(label)
           // view.backgroundColor = UIColor.white
            self.view.addSubview(view)
            return view
        }
        else {
            return nil
        }
     
    }
      
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 80
    }
    
}
