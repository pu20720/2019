//
//  MovieReviewViewController.swift
//  FirebaseDatabaseDemo
//
//  Created by Frank.Chen on 2017/4/21.
//  Copyright © 2017年 Frank.Chen. All rights reserved.
//

import UIKit
import Firebase

class MovieReviewViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate{
    
 
    @IBOutlet weak var address: UIButton!
    @IBOutlet weak var reviewBtn: UIButton!
    @IBOutlet weak var movieReviewsTableView: UITableView!
    var movieReviews: [MovieReviewItem] = [MovieReviewItem]()
    
    var id:String?
    var latitube : Double!
    var longitube: Double!
    @IBAction func back(_ sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()        
        self.reviewBtn.isEnabled = false
        
        // 查詢節點資料
        let reference: FIRDatabaseReference! = FIRDatabase.database().reference().child("movieReviews").child(id ?? "movieId-0000001")
        
        reference.queryOrderedByKey().observe(.value, with: { snapshot in
            if snapshot.childrenCount > 0 {
                var dataList: [MovieReviewItem] = [MovieReviewItem]()
                
                for item in snapshot.children {
                    let data = MovieReviewItem(snapshot: item as! FIRDataSnapshot)
                    dataList.append(data)
                }
                
                self.movieReviews = dataList
                self.movieReviewsTableView.reloadData()
            }
            
            self.reviewBtn.isEnabled = true
        })
        
    }
    
    // MARK: - DataSource
    // ---------------------------------------------------------------------    
    // 設定表格section的列數
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        return self.movieReviews.count
    }
    
    // 表格的儲存格設定
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MovieReviewTableViewCell", for: indexPath) as! MovieReviewTableViewCell
        
        cell.reViewTextView?.text = self.movieReviews[indexPath.row].movieReview
        cell.loginUser.text =  self.movieReviews[indexPath.row].userEmail
        
        if indexPath.row == 0{
            cell.backgroundColor = UIColor(red:0.02,green:0.53,blue:0.23,alpha:0.9)
            self.latitube = Double(self.movieReviews[indexPath.row].latitube)
            self.longitube = Double(self.movieReviews[indexPath.row].longitube)
        }
        
        return cell
    }
    
    // MARK: - Navigation
    // ---------------------------------------------------------------------        
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "addMovieReviewSegue" {
            _ = segue.destination as! AddMovieReviewViewController
            let page2:AddMovieReviewViewController = segue.destination as! AddMovieReviewViewController
            page2.id = self.id
            
        }
        if segue.identifier == "GPS"{
            _ = segue.destination as! GPS
            let page3:GPS = segue.destination as! GPS
            page3.latitube = self.latitube
            page3.longitube = self.longitube
            
        }
    }
    
}


