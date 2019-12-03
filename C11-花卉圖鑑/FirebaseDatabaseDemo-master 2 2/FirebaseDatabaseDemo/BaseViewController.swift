//
//  BaseViewController.swift
//  FirebaseDatabaseDemo
//
//  Created by Frank.Chen on 2017/4/24.
//  Copyright © 2017年 Frank.Chen. All rights reserved.
//

import UIKit

class BaseViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
        // 註冊tab事件，點選瑩幕任一處可關閉瑩幕小鍵盤
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(RegisterViewController.dismissKeyboard))
        self.view.addGestureRecognizer(tap)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    
    // 提示錯誤訊息
    func showMsg(_ message: String, showMsgStatus: ShowMsgStatus, handler: (() -> Swift.Void)? = nil) {
        let alertController = UIAlertController(title: "提示", message: message, preferredStyle: .alert)
        let cancel: UIAlertAction!
        
        switch showMsgStatus {
        case .loginSuccess:
            cancel = UIAlertAction(title: "確定", style: .default) { action in
                handler!()
            }
        default:
            cancel = UIAlertAction(title: "確定", style: .default, handler: nil)
        }
        
        alertController.addAction(cancel)
        
        self.present(alertController, animated: true, completion: nil)
    }
    
    @objc func dismissKeyboard() {
        self.view.endEditing(true)
    }
}

enum ShowMsgStatus {
    case loginSuccess
    case loginFail
}
