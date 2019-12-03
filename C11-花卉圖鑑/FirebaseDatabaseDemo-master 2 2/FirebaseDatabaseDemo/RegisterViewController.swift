//
//  RegisterViewController.swift
//  FirebaseDatabaseDemo
//
//  Created by Frank.Chen on 2017/4/21.
//  Copyright © 2017年 Frank.Chen. All rights reserved.
//

import UIKit
import Firebase
import FirebaseAuth

class RegisterViewController: BaseViewController {

    @IBOutlet weak var emailTxtFld: UITextField!
    @IBOutlet weak var passwordTxtFld: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()        
        // Do any additional setup after loading the view.                
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.        
    }
    
    @IBAction func goRegister(_ sender: Any) {
        // email和密碼為必填欄位
        if self.emailTxtFld.text == "" || self.passwordTxtFld.text == "" {
            super.showMsg("請輸入email和密碼", showMsgStatus: .loginFail, handler: nil)
            return
        }
        
        // 建立帳號
        FIRAuth.auth()?.createUser(withEmail: self.emailTxtFld.text!, password: self.passwordTxtFld.text!) { (user, error) in
            
            // 註冊失敗
            if error != nil {
                super.showMsg((error?.localizedDescription)!, showMsgStatus: .loginFail, handler: nil)                           
                return
            }
            
            // 註冊成功並返回首頁
            super.showMsg("註冊成功", showMsgStatus: .loginSuccess, handler: self.handler)
        }
    }
    
    // 返回首頁
    @IBAction func goHomePage(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    // 註冊成功關閉此頁面
    func handler() -> Void {
        self.dismiss(animated: true, completion: nil)
    }
    
}
