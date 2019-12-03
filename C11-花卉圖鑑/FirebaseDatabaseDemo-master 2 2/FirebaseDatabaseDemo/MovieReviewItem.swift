//
//  MovieReviewItem.swift
//  FirebaseDatabaseDemo
//
//  Created by Frank.Chen on 2017/4/24.
//  Copyright © 2017年 Frank.Chen. All rights reserved.
//

import Foundation
import Firebase

struct MovieReviewItem {
    
    var childId: String
    var movieReview: String
    var userId: String
    var userEmail: String    
    var createDate: String
    var image : String
    var title : String
    
    var latitube:String
    var longitube:String
    init(snapshot: FIRDataSnapshot) {
        print(snapshot)
        // 取出snapshot的值(JSON)
        
        let snapshotValue: [String: AnyObject] = snapshot.value as! [String: AnyObject]
        self.childId = snapshotValue["childId"] as! String
        self.movieReview = snapshotValue["movieReview"] as! String
        self.userId = snapshotValue["userId"] as! String
        self.userEmail = snapshotValue["userEmail"] as! String
        self.createDate = snapshotValue["createDate"] as! String
        self.title = snapshotValue["title"] as! String
        self.image = snapshotValue["image"] as! String
        self.latitube = snapshotValue["latitube"] as! String
        self.longitube = snapshotValue["longitube"] as! String
    }

}
