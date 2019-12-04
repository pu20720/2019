//
//  HomePageViewController.swift
//  TimeGears
//
//  Created by Kirk Hsieh on 2019/12/5.
//  Copyright © 2019 ChangHaoHuang. All rights reserved.
//

import UIKit

class HomePageViewController: UIViewController, UIPickerViewDataSource, UIPickerViewDelegate {
    
    var weatherImage: [UIImage] = [#imageLiteral(resourceName: "01"), #imageLiteral(resourceName: "02"), #imageLiteral(resourceName: "03"), #imageLiteral(resourceName: "04"), #imageLiteral(resourceName: "05"), #imageLiteral(resourceName: "06"), #imageLiteral(resourceName: "07"), #imageLiteral(resourceName: "08"), #imageLiteral(resourceName: "09"), #imageLiteral(resourceName: "10"), #imageLiteral(resourceName: "01"), #imageLiteral(resourceName: "02"), #imageLiteral(resourceName: "03"), #imageLiteral(resourceName: "04"), #imageLiteral(resourceName: "05"), #imageLiteral(resourceName: "06"), #imageLiteral(resourceName: "07"), #imageLiteral(resourceName: "08"), #imageLiteral(resourceName: "09"), #imageLiteral(resourceName: "10"), #imageLiteral(resourceName: "01"), #imageLiteral(resourceName: "02"), #imageLiteral(resourceName: "03"), #imageLiteral(resourceName: "04"), #imageLiteral(resourceName: "05"), #imageLiteral(resourceName: "06"), #imageLiteral(resourceName: "07"), #imageLiteral(resourceName: "08"), #imageLiteral(resourceName: "09"), #imageLiteral(resourceName: "10"), #imageLiteral(resourceName: "01")]
    var temperature: [String] = ["32", "26", "23", "20", "18", "24", "22", "21", "16", "12", "32", "26", "23", "20", "18", "24", "22", "21", "16", "12", "32", "26", "23", "20", "18", "24", "22", "21", "16", "12", "32"]
    var pm25: [Int] = [11]
    let monthNum: [Int] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
    let dayNum: [Int] = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
    let dayCount: [String] = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"]
    var monthTitle = 12
    
    @IBOutlet weak var rightMonthButton: UIButton!
    @IBOutlet weak var leftMonthButton: UIButton!
    @IBOutlet weak var monthTitleLabel: UILabel!
    @IBOutlet weak var dayPickerView: UIPickerView!
    @IBOutlet weak var temperatureLabel: UILabel!
    @IBOutlet weak var weatherImageView: UIImageView!
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return dayNum[monthTitle-1]
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return dayCount[row]
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        temperatureLabel?.text = temperature[row]+"度C"
        weatherImageView?.image = weatherImage[row]
    }
    
    
    @IBAction func monthMinus(_ sender: Any) {
        if(monthTitle != 1){
           monthTitle = monthTitle - 1
            if(monthTitle == 1) {
                leftMonthButton.isEnabled = false
            }
            else if(monthTitle != 1 && monthTitle != 12) {
                rightMonthButton.isEnabled = true
                leftMonthButton.isEnabled = true
            }
            else {
                rightMonthButton.isEnabled = false
            }
        }
        monthTitleLabel?.text = "\(monthTitle)月"
        dayPickerView.reloadAllComponents()
    }
    
    @IBAction func monthPlus(_ sender: Any) {
        if(monthTitle != 12){
            monthTitle = monthTitle + 1
            if(monthTitle == 1) {
                leftMonthButton.isEnabled = false
            }
            else if(monthTitle != 1 && monthTitle != 12) {
                rightMonthButton.isEnabled = true
                leftMonthButton.isEnabled = true
            }
            else {
                rightMonthButton.isEnabled = false
            }
        }
        monthTitleLabel?.text = "\(monthTitle)月"
        dayPickerView.reloadAllComponents()
    }
    

    
    override func viewDidLoad() {
        super.viewDidLoad()
        monthTitleLabel?.text = "\(monthTitle)月"
        if(monthTitle == 1) {
            leftMonthButton.isEnabled = false
        }
        else if(monthTitle != 1 && monthTitle != 12) {
            rightMonthButton.isEnabled = true
            leftMonthButton.isEnabled = true
        }
        else {
            rightMonthButton.isEnabled = false
        }
        // Do any additional setup after loading the view.
    }
}
