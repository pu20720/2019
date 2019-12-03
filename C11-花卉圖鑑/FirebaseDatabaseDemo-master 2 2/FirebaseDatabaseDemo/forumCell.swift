//
//  forumCell.swift
//  FirebaseDatabaseDemo
//
//  Created by 植物昆蟲圖鑑 on 2019/10/1.
//  Copyright © 2019 Frank.Chen. All rights reserved.
//

import UIKit

class forumCell: UITableViewCell {
    

    @IBOutlet weak var user: UILabel!
    @IBOutlet weak var img: UIImageView!
    @IBOutlet weak var reViewTextView: UITextView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        // UITapGestureRecognizer is set programatically.
        //        let tap = UITapGestureRecognizer(target: self, action: #selector(MovieReviewTableViewCell.onClickLikeImage(_:)))
        //        tap.numberOfTapsRequired = 1
        //        self.likeImage.addGestureRecognizer(tap)
        //        self.likeImage.isUserInteractionEnabled = true
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
    //    fu÷nc onClickLikeImage(_ sender: UITapGestureRecognizer) {
    
    //        print("on÷ClickLikeImage...")
    // observeSingleEventOfType listens for a tap by the current user.
    
    //        voteRef.observeSingleEventOfType(.Value, withBlock: { snapshot in
    //
    //            if let thumbsUpDown = snapshot.value as? NSNull {
    //                print(thumbsUpDown)
    //                self.thumbVoteImage.image = UIImage(named: "thumb-down")
    //
    //                // addSubtractVote(), in Joke.swift, handles the vote.
    //
    //                self.joke.addSubtractVote(true)
    //
    //                // setValue saves the vote as true for the current user.
    //                // voteRef is a reference to the user's "votes" path.
    //
    //                self.voteRef.setValue(true)
    //            } else {
    //                self.thumbVoteImage.image = UIImage(named: "thumb-up")
    //                self.joke.addSubtractVote(false)
    //                self.voteRef.removeValue()
    //            }
    //
    //        })
    //    }
    
}
