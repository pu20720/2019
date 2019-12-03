//
//  User.swift
//  FirebaseDatabaseDemo
//
//  Created by Frank.Chen on 2017/4/24.
//  Copyright © 2017年 Frank.Chen. All rights reserved.
//

import Foundation
import Firebase

struct User {
    
    let uid: String
    let email: String
    
    init(authData: FIRUser) {
        self.uid = authData.uid
        self.email = authData.email!
    }    
}
