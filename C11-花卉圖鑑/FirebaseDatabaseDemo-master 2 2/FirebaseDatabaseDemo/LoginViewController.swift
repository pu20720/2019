//
//  ViewController.swift
//  FirebaseDatabaseDemo
//
//  Created by Frank.Chen on 2017/4/21.
//  Copyright © 2017年 Frank.Chen. All rights reserved.
//

import UIKit
import Firebase
import FirebaseAuth

class LoginViewController: BaseViewController {
    
    @IBOutlet weak var emailTxtFld: UITextField!
    @IBOutlet weak var passwordTxtFld: UITextField!
    
    @IBOutlet weak var NoLog: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    // 登入
    @IBAction func goLogin(_ sender: Any) {
        // email和密碼為必填欄位
        if self.emailTxtFld.text == "" || self.passwordTxtFld.text == "" {
            super.showMsg("請輸入email和密碼", showMsgStatus: .loginFail, handler: nil)
            return
        }
        
        FIRAuth.auth()?.signIn(withEmail: self.emailTxtFld.text!, password: self.passwordTxtFld.text!) { (user, error) in
            // 登入失敗
            if error != nil {
                super.showMsg((error?.localizedDescription)!, showMsgStatus: .loginFail, handler: nil)
                return
            }
            
            // 將登入的user存放起來
            Properties.user = User(authData: user!)
            
            // 登入成功並顯示已登入
            self.performSegue(withIdentifier: "forum", sender: nil)
        }
        
    }
}




