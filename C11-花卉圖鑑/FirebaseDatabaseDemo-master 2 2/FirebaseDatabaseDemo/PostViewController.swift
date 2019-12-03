
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
import FirebaseStorage
import Photos
class PostViewController: BaseViewController {
    
    @IBOutlet weak var reViewTxtView: UITextView!
    
    @IBOutlet weak var imgView: UIImageView!
    
    @IBOutlet weak var content: UITextView!
    var UIimage : UIImageView?
    var longitube: Double?
    var latitube: Double?
    var text : String?
    override func viewDidLoad() {
        self.edgesForExtendedLayout = []
        if text != nil{
            reViewTxtView.text = "我發現了:" + text! ?? nil
        }
    }
    
    @IBAction func uploadBtnAction(_ sender: UIButton) {
        
        // 建立一個 UIImagePickerController 的實體
        let imagePickerController = UIImagePickerController()
        
        // 委任代理
        imagePickerController.delegate = self as! UIImagePickerControllerDelegate & UINavigationControllerDelegate
        
        // 建立一個 UIAlertController 的實體
        // 設定 UIAlertController 的標題與樣式為 動作清單 (actionSheet)
        let imagePickerAlertController = UIAlertController(title: "上傳圖片", message: "請選擇要上傳的圖片", preferredStyle: .actionSheet)
        
        // 建立三個 UIAlertAction 的實體
        // 新增 UIAlertAction 在 UIAlertController actionSheet 的 動作 (action) 與標題
        let imageFromLibAction = UIAlertAction(title: "照片圖庫", style: .default) { (Void) in
            
            // 判斷是否可以從照片圖庫取得照片來源
            if UIImagePickerController.isSourceTypeAvailable(.photoLibrary) {
                
                // 如果可以，指定 UIImagePickerController 的照片來源為 照片圖庫 (.photoLibrary)，並 present UIImagePickerController
                imagePickerController.sourceType = .photoLibrary
                self.present(imagePickerController, animated: true, completion: nil)
            }
        }
       
        
        // 新增一個取消動作，讓使用者可以跳出 UIAlertController
        let cancelAction = UIAlertAction(title: "取消", style: .cancel) { (Void) in
            
            imagePickerAlertController.dismiss(animated: true, completion: nil)
        }
        
        // 將上面三個 UIAlertAction 動作加入 UIAlertController
        imagePickerAlertController.addAction(imageFromLibAction)
        imagePickerAlertController.addAction(cancelAction)
        
        // 當使用者按下 uploadBtnAction 時會 present 剛剛建立好的三個 UIAlertAction 動作與
        present(imagePickerAlertController, animated: true, completion: nil)
    }
    

    
    // MARK: - Callback    // 新增節點資料
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
        let reference: FIRDatabaseReference! = FIRDatabase.database().reference().child("movieReviews")
       
        let childRef: FIRDatabaseReference = reference.childByAutoId() // 隨機生成的節點唯一識別碼，用來當儲存時的key值
        
        
        
        var movieReview: [String : AnyObject] = [String : AnyObject]()
        
       
        movieReview["childId"] = childRef.key as AnyObject
        movieReview["movieReview"] = self.content.text as AnyObject
        movieReview["title"] = self.reViewTxtView.text as AnyObject
        movieReview["userId"] = (Properties.user?.uid)! as AnyObject
        movieReview["userEmail"] = (Properties.user?.email)! as AnyObject
        movieReview["createDate"] = dateString as AnyObject
        movieReview["image"] = imageUrl as AnyObject
        
        movieReview["longitube"] = String(format:"%f",longitube ?? "") as AnyObject? ?? "" as AnyObject
        movieReview["latitube"] = String(format:"%f",latitube ?? "") as AnyObject? ?? "" as AnyObject
        
        let movieReviewReference = reference.child(childRef.key).child(childRef.key)
        movieReviewReference.updateChildValues(movieReview) { (err, ref) in
            if err != nil{
                print("err： \(err!)")
                return
            }
            
            print(ref.description())
            
            // 返回上一頁
            _ = self.navigationController?.popViewController(animated: true)
        }
        //dismiss(animated: true, completion: nil)
    }
    

    
}
var imageUrl = ""
var image = ""
extension PostViewController: UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        
        var selectedImageFromPicker: UIImage?
        
        // 取得從 UIImagePickerController 選擇的檔案
        if let pickedImage = info[UIImagePickerController.InfoKey.originalImage] as? UIImage {
            
            if imgView == UIimage{
                self.imgView.image = UIimage?.image
            }
            self.imgView.image = pickedImage
            selectedImageFromPicker = pickedImage
        }
        if let url = info[.referenceURL] as? URL {
            if let asset: PHAsset = PHAsset.fetchAssets(withALAssetURLs: [url], options: nil)[0] {
                self.longitube = asset.location?.coordinate.longitude
                
                self.latitube = asset.location?.coordinate.latitude
                print("asset.location",asset.location?.coordinate.latitude ?? "None")
                
            }
        }
        
        // 可以自動產生一組獨一無二的 ID 號碼，方便等一下上傳圖片的命名
        let uniqueString = NSUUID().uuidString
        
        // 當判斷有 selectedImage 時，我們會在 if 判斷式裡將圖片上傳
        if let selectedImage = selectedImageFromPicker {
            
            let storageRef = FIRStorage.storage().reference().child("AppCodaFireUpload").child("\(uniqueString).png")
            
            if let uploadData = selectedImage.pngData() {
                // 這行就是 FirebaseStroge 關鍵的存取方法。
                storageRef.put(uploadData, metadata: nil, completion: { (data, error) in
                    
                    if error != nil {
                        
                        // 若有接收到錯誤，我們就直接印在 Console 就好，在這邊就不另外做處理。
                        print("Error: \(error!.localizedDescription)")
                        return
                    }
                    
                    // 連結取得方式就是：data?.downloadURL()?.absoluteString。
                    if let uploadImageUrl = data?.downloadURL()?.absoluteString {
                        
                        // 我們可以 print 出來看看這個連結事不是我們剛剛所上傳的照片。
                        print("Photo Url: \(uploadImageUrl)")
                        imageUrl = uploadImageUrl
                        
                        
                    }
                })
            }
            
        }
        
        dismiss(animated: true, completion: nil)
    }
}
