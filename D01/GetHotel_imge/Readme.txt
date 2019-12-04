1.爬取共有11648筆旅館對應url資料(/user_hotel_rating/uid_and_ratings/city_hotelname_hid_hurl)。

2.透過這些資料再分批爬取旅客對旅館的評分資料(user_hotel_rating/uid_and_ratings/uid_hotel_rat_test0 ~ 3全部總和放在/ToJson/uid_hotel_rat 共有283275筆)。

3.在/ToJson/covertjson.ipynb裡面，將有被評分過的旅館寫入givehotel檔案，共6202間旅館。也將每筆評分給予新的對應id，寫入giveid裡面。


