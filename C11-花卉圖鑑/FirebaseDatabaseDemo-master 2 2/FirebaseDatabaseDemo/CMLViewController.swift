//
//  ViewController.swift
//  CoreMLDemo
//
//  Created by Sai Kambampati on 14/6/2017.
//  Copyright © 2017 AppCoda. All rights reserved.
//

import UIKit
import CoreML
import Photos
import MapKit
import CoreLocation
import FirebaseAuth

class CMLViewController: BaseViewController,CLLocationManagerDelegate, MKMapViewDelegate ,UINavigationControllerDelegate {
    
    @IBOutlet weak var classifier: UILabel!
    @IBOutlet weak var imageView: UIImageView!
    var model: ImageClassifier! 
    
    @IBOutlet weak var post: UIButton!
    var latitube : Double!
    var longitube : Double!
    
    var latitude : Double!
    var longitude : Double!
    
    var myLocationManager = CLLocationManager()
    var myMapView :MKMapView!

    
    var isCamera :Bool!

    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        myLocationManager.delegate = self //委派給ViewController
        myLocationManager.desiredAccuracy = kCLLocationAccuracyBest  //設定為最佳精度
        myLocationManager.requestWhenInUseAuthorization()  //user授權
        myLocationManager.startUpdatingLocation()  //開始update user位置
    }
    
    func locationManager(_ manager: CLLocationManager,
                         didUpdateLocations locations: [CLLocation]) {
        // 印出目前所在位置座標
        let currentLocation:CLLocation = locations[0] as CLLocation
        
        latitude = currentLocation.coordinate.latitude
        longitude = currentLocation.coordinate.longitude
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        // 首次使用 向使用者詢問定位自身位置權限
        if CLLocationManager.authorizationStatus()
            == .notDetermined {
            // 取得定位服務授權
            myLocationManager.requestWhenInUseAuthorization()
            
            // 開始定位自身位置
            myLocationManager.startUpdatingLocation()
        }
            // 使用者已經拒絕定位自身位置權限
        else if CLLocationManager.authorizationStatus()
            == .denied {
            // 提示可至[設定]中開啟權限
            let alertController = UIAlertController(
                title: "定位權限已關閉",
                message:
                "如要變更權限，請至 設定 > 隱私權 > 定位服務 開啟",
                preferredStyle: .alert)
            let okAction = UIAlertAction(
                title: "確認", style: .default, handler:nil)
            alertController.addAction(okAction)
            self.present(
                alertController,
                animated: true, completion: nil)
        }
            // 使用者已經同意定位自身位置權限
        else if CLLocationManager.authorizationStatus()
            == .authorizedWhenInUse {
            // 開始定位自身位置
            myLocationManager.startUpdatingLocation()
        }
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        model = ImageClassifier()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func camera(_ sender: Any) {
        
        if !UIImagePickerController.isSourceTypeAvailable(.camera) {
            return
        }
        
        let cameraPicker = UIImagePickerController()
        cameraPicker.delegate = self as UIImagePickerControllerDelegate & UINavigationControllerDelegate
        cameraPicker.sourceType = .camera
        cameraPicker.allowsEditing = false
        
    
        
        isCamera = true
        
        present(cameraPicker, animated: true)
    }
    
    @IBAction func openLibrary(_ sender: Any) {
        let picker = UIImagePickerController()
        picker.allowsEditing = false
        picker.delegate = self as UIImagePickerControllerDelegate & UINavigationControllerDelegate
        picker.sourceType = .photoLibrary
        present(picker, animated: true)
        
        isCamera = false
    }
    
}

extension CMLViewController: UIImagePickerControllerDelegate {
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        dismiss(animated: true, completion: nil)
    }
    
    
    @objc func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
    
        
        picker.dismiss(animated: true)
        classifier.text = "Analyzing Image..."
        guard let image = info[.originalImage] as? UIImage else {
            return
        } //1
        if picker.sourceType != .photoLibrary{
            UIImageWriteToSavedPhotosAlbum(image, nil, nil, nil)
        }
        UIGraphicsBeginImageContextWithOptions(CGSize(width: 299, height: 299), true, 2.0)
        image.draw(in: CGRect(x: 0, y: 0, width: 299, height: 299))
        let newImage = UIGraphicsGetImageFromCurrentImageContext()!
        UIGraphicsEndImageContext()
        
        let attrs = [kCVPixelBufferCGImageCompatibilityKey: kCFBooleanTrue, kCVPixelBufferCGBitmapContextCompatibilityKey: kCFBooleanTrue] as CFDictionary
        var pixelBuffer : CVPixelBuffer?
        let status = CVPixelBufferCreate(kCFAllocatorDefault, Int(newImage.size.width), Int(newImage.size.height), kCVPixelFormatType_32ARGB, attrs, &pixelBuffer)
        guard (status == kCVReturnSuccess) else {
            return
        }
        
        CVPixelBufferLockBaseAddress(pixelBuffer!, CVPixelBufferLockFlags(rawValue: 0))
        let pixelData = CVPixelBufferGetBaseAddress(pixelBuffer!)
        
        let rgbColorSpace = CGColorSpaceCreateDeviceRGB()
        let context = CGContext(data: pixelData, width: Int(newImage.size.width), height: Int(newImage.size.height), bitsPerComponent: 8, bytesPerRow: CVPixelBufferGetBytesPerRow(pixelBuffer!), space: rgbColorSpace, bitmapInfo: CGImageAlphaInfo.noneSkipFirst.rawValue) //3
        
        context?.translateBy(x: 0, y: newImage.size.height)
        context?.scaleBy(x: 1.0, y: -1.0)
        
        UIGraphicsPushContext(context!)
        newImage.draw(in: CGRect(x: 0, y: 0, width: newImage.size.width, height: newImage.size.height))
        UIGraphicsPopContext()
        CVPixelBufferUnlockBaseAddress(pixelBuffer!, CVPixelBufferLockFlags(rawValue: 0))
        imageView.image = newImage
        
        // Core ML
        guard let prediction = try? model.prediction(image: pixelBuffer!) else {
            return
        }
        
        let con = change(prediction.classLabel)
        classifier.text = con

        
        
        if let url = info[.referenceURL] as? URL {
            if let asset: PHAsset = PHAsset.fetchAssets(withALAssetURLs: [url], options: nil)[0] {
                
                latitube = asset.location?.coordinate.latitude ?? nil
                longitube = asset.location?.coordinate.longitude ?? nil
            }
            
        }
        
    }
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        //判斷segue.identifier
        if segue.identifier == "showDetail"{
            //例項化第二個頁面
            let page2:PostViewController = segue.destination as! PostViewController
            if FIRAuth.auth()?.currentUser == nil {
                super.showMsg("未登入", showMsgStatus: .loginSuccess, handler: handler)
            }
            else{
                //傳值
                if(!isCamera){
                    page2.longitube = longitube
                    page2.latitube = latitube
                }else{
                    page2.longitube = self.longitude
                    page2.latitube = self.latitude
                }
                page2.text = classifier.text
            }
        }
    }
    // 返回登入頁面
    func handler() -> Void {
        let loginViewController = self.storyboard!.instantiateViewController(withIdentifier: "Login")
        UIApplication.shared.keyWindow?.rootViewController = loginViewController
    }
}



func change(_ someone: String ) -> String{
    
    let url = Bundle.main.url(forResource: "flower_name", withExtension: "json")
    
    let content = try! String(contentsOf: url!)
    
    var string = someone
    
    if let data = content.data(using: .utf8){
        
        struct AllData:Decodable{
            let EN:String
            let TC:String
        }
        
        
        do{
            
            let decoder = JSONDecoder()
            //因為最外面是陣列，所以是[AllData]
            let data = try  decoder.decode([AllData].self, from: data)
            for i in 0...data.count-1{
                if(someone == data[i].EN){
                    string = data[i].TC
                    //print(string)
                }
            }
        }catch{
            string = "error"
        }
        
    }
    return string
}


