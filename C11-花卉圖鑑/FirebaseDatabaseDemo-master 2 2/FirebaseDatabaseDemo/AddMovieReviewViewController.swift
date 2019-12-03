//
//  AddMovieReviewViewController.swift
//  FirebaseDatabaseDemo
//
//  Created by Frank.Chen on 2017/4/21.
//  Copyright © 2017年 Frank.Chen. All rights reserved.
//

import UIKit
import Firebase
import FirebaseAuth

class AddMovieReviewViewController: BaseViewController {

    @IBOutlet weak var reViewTxtView: UITextView!
    var id:String?
    override func viewDidLoad() {        
        self.edgesForExtendedLayout = []
    }
    
    // MARK: - Callback
    // 新增節點資料
    @IBAction func goSave(_ sender: Any) {
        // 評論為必填資料
        if self.reViewTxtView.text == "" {
            super.showMsg("請輸入評論", showMsgStatus: .loginFail, handler: nil)
            return
        }
        // 獲取當前時間
        let now:Date = Date()
        // 建立時間格式
        let dateFormat:DateFormatter = DateFormatter()
        dateFormat.dateFormat = "yyyy/MM/dd/ HH:mm:ss"
        // 將當下時間轉換成設定的時間格式
        let dateString:String = dateFormat.string(from: now)
        
        // 新增節點資料
        let reference: FIRDatabaseReference! = FIRDatabase.database().reference().child("movieReviews").child(id ?? "movieId-0000001")
        let childRef: FIRDatabaseReference = reference.childByAutoId() // 隨機生成的節點唯一識別碼，用來當儲存時的key值
        
        var movieReview: [String : AnyObject] = [String : AnyObject]()
        
        movieReview["childId"] = childRef.key as AnyObject
        movieReview["movieReview"] = self.reViewTxtView.text as AnyObject
        movieReview["title"] = "null" as AnyObject
        movieReview["userId"] = (Properties.user?.uid)! as AnyObject
        movieReview["userEmail"] = (Properties.user?.email)! as AnyObject
        movieReview["createDate"] = dateString as AnyObject
        movieReview["image"] = "" as AnyObject
        movieReview["longitube"] = "" as AnyObject
        movieReview["latitube"] = "" as AnyObject
        let movieReviewReference = reference.child(childRef.key)
        movieReviewReference.updateChildValues(movieReview) { (err, ref) in
            if err != nil{
                print("err： \(err!)")
                return
            }
            
            print(ref.description())
            
            // 返回上一頁
            _ = self.navigationController?.popViewController(animated: true)
        }
    }
    
    // 登出並返回登入頁面
    @IBAction func goLogin(_ sender: Any) {
        // 未登入
        if FIRAuth.auth()?.currentUser == nil {
            super.showMsg("未登入", showMsgStatus: .loginFail, handler: nil)
        }
        
        do {
            // 登出
            try FIRAuth.auth()?.signOut()
            super.showMsg("登出成功", showMsgStatus: .loginSuccess, handler: handler)
        } catch let error as NSError {
            super.showMsg(error.localizedDescription, showMsgStatus: .loginFail, handler: nil)
        }
    }
    
    // 返回登入頁面
    func handler() -> Void {        
        let loginViewController = self.storyboard!.instantiateViewController(withIdentifier: "Login")
        UIApplication.shared.keyWindow?.rootViewController = loginViewController
    }
    
}
