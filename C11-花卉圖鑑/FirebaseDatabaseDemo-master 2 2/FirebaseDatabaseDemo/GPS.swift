//
//  GPS.swift
//  FirebaseDatabaseDemo
//
//  Created by 植物昆蟲圖鑑 on 2019/10/16.
//  Copyright © 2019 Frank.Chen. All rights reserved.
//

import UIKit
import CoreLocation
import MapKit

class GPS: UIViewController, CLLocationManagerDelegate, MKMapViewDelegate {
    
    @IBOutlet weak var map: MKMapView!
    var locationManger = CLLocationManager()
    var latitube : Double!
    var longitube : Double!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //print(latitube)
        // Do any additional setup after loading the view.
        if((latitube != 0.000000) && (longitube != 0.000000)){
            let objectAnnotation = MKPointAnnotation()
            objectAnnotation.coordinate = CLLocation(
                latitude: latitube,
                longitude: longitube).coordinate
            objectAnnotation.title = "拍照位置"
            map.addAnnotation(objectAnnotation)
            
            //latitude.text = String(format:"%f", Latitude)
            //longitude.text = String(format:"%f", Longitude)
        }else{
            
            let controller = UIAlertController(title: "無定位紀錄", message: "請確圖片有無ＧＰＳ定位圖片有無紀錄", preferredStyle: .alert)
            let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
            controller.addAction(okAction)
            present(controller, animated: true, completion: nil)
            
        }
        
    }
    
    
    
    
    
}
