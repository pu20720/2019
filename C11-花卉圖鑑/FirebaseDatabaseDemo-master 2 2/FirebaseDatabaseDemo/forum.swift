//
//  forum.swift
//  FirebaseDatabaseDemo
//
//  Created by 植物昆蟲圖鑑 on 2019/5/20.
//  Copyright © 2019 Frank.Chen. All rights reserved.
//


import UIKit
import Firebase
import FirebaseAuth

class forum: UIViewController, UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate{
    
    //@IBOutlet weak var reviewBtn: UIButton!
    @IBOutlet weak var movieReviewsTableView: UITableView!
    
   
    
    @IBOutlet weak var post: UIButton!
    var send: String!
    
    @IBAction func logout(_ sender: Any) {
        if FIRAuth.auth()?.currentUser == nil {
            //super.showMsg("未登入", showMsgStatus: .loginFail, handler: nil)
        }
        
        do {
            // 登出
            try FIRAuth.auth()?.signOut()
            self.handler()
            //super.showMsg("登出成功", showMsgStatus: .loginSuccess, handler: handler)
        } catch let error as NSError {
            //super.showMsg(error.localizedDescription, showMsgStatus: .loginFail, handler: nil)
        }
    }
    func handler() -> Void {
        let loginViewController = self.storyboard!.instantiateViewController(withIdentifier: "Login")
        UIApplication.shared.keyWindow?.rootViewController = loginViewController
    }
    
    var movieReviews = [String]()
    var dataList2 = [String]()
    var dataList3 = [String]()
    var dataList4 = [String]()
    var Reviews: [MovieReviewItem] = [MovieReviewItem]()
    override func viewDidLoad() {
        
    
        
        super.viewDidLoad()
        self.post.isEnabled = false
        let reference: FIRDatabaseReference! = FIRDatabase.database().reference()
        
        
        // 查詢節點資料
        var dataList = [String]()
        var dataList2 = [String]()
        var dataList3 = [String]()
        var dataList4 = [String]()
        reference.child("movieReviews").observe(.childAdded, with: { (snapshot) in
            
            // childAdded逐筆呈現
            if let dictionaryData = snapshot.value as? [String: AnyObject] {
                
                
                let data = snapshot.key
                    dataList.append(data) 
                
                self.movieReviews = dataList
                self.movieReviewsTableView.reloadData()
               
                print(dictionaryData)
                print(snapshot.key)
                
            }
        self.post.isEnabled = true
            reference.child("movieReviews").child(snapshot.key).queryOrderedByKey().observeSingleEvent(of: .value, with: { snapshot in
                if snapshot.childrenCount > 0 {
                    
                    
                    for item in snapshot.children {
                        let data1 = MovieReviewItem(snapshot: item as! FIRDataSnapshot)
                        if snapshot.key == data1.childId{
                            dataList2.append(data1.title)
                            dataList3.append(data1.image)
                            dataList4.append(data1.userEmail)
                        }
                        
                    }
                    
                    self.dataList2 = dataList2
                    self.dataList3 = dataList3
                    self.movieReviewsTableView.reloadData()
                }
                
                
            })
            
        }, withCancel: nil)
        
        
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        //判斷segue.identifier
        if segue.identifier == "showDetail"{
            //例項化第二個頁面
            let page2:MovieReviewViewController = segue.destination as! MovieReviewViewController
            //傳值
            page2.id = movieReviews[movieReviewsTableView.indexPathForSelectedRow!.row]
            
        }
    }
    // MARK: - DataSource
    // ---------------------------------------------------------------------
    // 設定表格section的列數
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.dataList2.count
    }
    
    
    // 表格的儲存格設定
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "forumCell", for: indexPath) as! forumCell
        
        let imgURL = NSURL(string: dataList3[indexPath.row]) 
        if let imageData = NSData(contentsOf: imgURL! as URL){
            let image = UIImage(data: imageData as Data)
            //翻转图片的方向
            var flipImageOrientation = ((image?.imageOrientation.rawValue)! + 4) % 8
            flipImageOrientation += flipImageOrientation%2==0 ? 1 : -1
            //翻转图片
            let flipImage =  UIImage(cgImage:(image?.cgImage!)!,
                                     scale:image!.scale,
                                     orientation:UIImage.Orientation(rawValue: flipImageOrientation)!
            )
            cell.img?.image = flipImage
        }
        
        
        
        
        cell.user?.text = self.dataList4[indexPath.row]
        cell.reViewTextView?.text = self.dataList2[indexPath.row]
        
        
        
        return cell
    }
    
    
}



