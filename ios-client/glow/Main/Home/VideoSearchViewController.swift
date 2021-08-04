//
//  VideoSearchViewController.swift
//  glow
//
//  Created by Dreams on 29/06/20.
//  Copyright © 2020 Dreams. All rights reserved.
//

import UIKit

class VideoSearchViewController: BaseViewController {

    @IBOutlet weak var tableView: UITableView!
    var searchList: [Search] = []
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.tableView.delegate = self
        self.tableView.dataSource = self
        self.tableView.tableFooterView = UIView(frame: CGRect.zero)
        tableView.register(UINib(nibName: "SearchVideoTableViewCell", bundle: nil), forCellReuseIdentifier: "SearchVideoTableViewCell")
        self.keybordDismiss()
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
    
    func searchProductData(searchBarText: String) {
        let param = ["keyword" : searchBarText,
                     "page": "1",
                     "country" : "JP"] as [String : Any]
               Utility.showProgress()
        SearchRequest.searchProduct(param: param) { (success, response, error) in
            Utility.dismissProgress()
            if error == nil {
                if let data = response?.data {
                    let storyBoard = UIStoryboard(name: "Main", bundle: nil)
                    if let viewController = storyBoard.instantiateViewController(withIdentifier: "SearchResultViewController") as? SearchResultViewController {
                        viewController.searchList = data
                        self.navigationController?.pushViewController(viewController, animated: true)
                    }
                } else {
                    Utility.alert(message: response?.msg ?? "")
                }
            } else {
                Utility.alert(message: error?.localizedDescription ?? "")
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

extension VideoSearchViewController: UITableViewDelegate, UITableViewDataSource, UISearchBarDelegate {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section {
        case 0:
            let cell = tableView.dequeueReusableCell(withIdentifier: "SearchVideoTableViewCell", for: indexPath) as! SearchVideoTableViewCell
//            cell.searchTextField.tag = indexPath.row
//            cell.searchTextField.placeholder = "Search a product..."
//            cell.searchTextField.returnKeyType = .search
//            cell.searchTextField.delegate = self
            cell.searchBar.delegate = self
            cell.backButton.addTarget(self, action: #selector(self.backButton(_:)), for: .touchUpInside)
            cell.searchBar.placeholder = "search product"
            cell.searchBar.setTextField(color: UIColor.white, cornerRadius: 10)
            cell.urlButton.tag = indexPath.row
            cell.urlButton.addTarget(self, action: #selector(urlSearchButton(_:)), for: .touchUpInside)
            cell.scanButton.tag = indexPath.row
            cell.scanButton.addTarget(self, action: #selector(barcodeScanButton(_:)), for: .touchUpInside)
            return cell
        case 1:
            let cell = tableView.dequeueReusableCell(withIdentifier: "AddTopicTableViewCell", for: indexPath) as! AddTopicTableViewCell
            return cell
        default:
            return UITableViewCell()
        }
    }
    
    @objc func urlSearchButton(_ sender: UIButton) {
        let storyBoard = UIStoryboard(name: "Search", bundle: nil)
        if let vc = storyBoard.instantiateViewController(withIdentifier: "SearchUrlViewController") as? SearchUrlViewController {
            let nav = UINavigationController(rootViewController: vc)
            nav.isNavigationBarHidden = true
            self.present(nav, animated: true, completion: nil)
        }
    }
    
    @objc func barcodeScanButton(_ sender: UIButton) {
        let storyBoard = UIStoryboard(name: "Search", bundle: nil)
        if let vc = storyBoard.instantiateViewController(withIdentifier: "QRScannerController") as? QRScannerController {
            let nav = UINavigationController(rootViewController: vc)
            nav.isNavigationBarHidden = true
            self.present(nav, animated: true, completion: nil)
        }
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        self.searchProductData(searchBarText: searchBar.text ?? "")
    }
    
    @objc func backButton(_ sender: UIButton) {
        self.dismiss(animated: true, completion: nil)
       // self.navigationController?.popViewController(animated: true)
    }
    
    @objc func search(_ sender: UITextField) {
        
        let storyBoard = UIStoryboard(name: "Main", bundle: nil)
        if let viewController = storyBoard.instantiateViewController(withIdentifier: "SearchResultViewController") as? SearchResultViewController {
            self.modalPresentationStyle = .popover
            self.navigationController?.pushViewController(viewController, animated: true)
        }
    }
    override func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        let index = IndexPath(row: textField.tag, section: 0)
        if let cell = tableView.cellForRow(at: index) as? SearchVideoTableViewCell {
            if textField == cell.searchTextField {
               //any task to perform
               textField.resignFirstResponder() //if you want to dismiss your keyboard
            }
        }
        return true
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        switch indexPath.section {
        case 0:
            return 312
        case 1:
            return 300
        default:
            return 0
        }
    }
    
   
    
}
