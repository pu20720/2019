package com.example.qrfoodproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.qrfoodproject.FoodDairy.FoodDairy_main;
import com.example.qrfoodproject.FoodFile.restaurant.FoodFile_restaurant;
import com.example.qrfoodproject.NutritionInform.NutritionInform;
import com.example.qrfoodproject.Profile.Profile_main;
import com.example.qrfoodproject.Qrcode.ScanQrcode;
import com.example.qrfoodproject.login.sessionCheck;



public class Home_QRfood extends AppCompatActivity {
    Button personalData, btn_qrcode, FoodDairy, nutritionInform, foodDocument;
    TextView account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_qrfood);

        setView();

        setClick();

        //setTitleSession(); 設置主頁面session(測試用)，之後會改成使用者名稱

        new sessionCheck().session_ifExist(this);
    }



    private void setView(){
        account = findViewById(R.id.account);
        btn_qrcode = findViewById(R.id.btn_qrcode);
        FoodDairy = findViewById(R.id.FoodDairy);
        nutritionInform = findViewById(R.id.nutritioninform);
        personalData = findViewById(R.id.personalData);
        foodDocument = findViewById(R.id.foodDocument);
    }

    private void setClick() {

        //進入食物檔案
        foodDocument.setOnClickListener(onclick);

        //進入個人資料
        personalData.setOnClickListener(onclick);

        //進入Qrcode相機
        btn_qrcode.setOnClickListener(onclick);

        //進『營養抓寶站』
        nutritionInform.setOnClickListener(onclick);

        //進入飲食日誌
        FoodDairy.setOnClickListener(onclick);

    }

    Button.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.foodDocument:
                    new sessionCheck().jump_afterSessionCheck(Home_QRfood.this, FoodFile_restaurant.class);
                    break;

                case R.id.FoodDairy:
                    new sessionCheck().jump_afterSessionCheck(Home_QRfood.this, FoodDairy_main.class);
                    break;
                case R.id.nutritioninform:
                    startActivity(new Intent(Home_QRfood.this, NutritionInform.class));
                    break;
                case R.id.personalData:
                    new sessionCheck().jump_afterSessionCheck(Home_QRfood.this, Profile_main.class);
                    break;
                case R.id.btn_qrcode:
                    startActivity(new Intent(Home_QRfood.this, ScanQrcode.class));
                    break;

            }

        }

    };

    private void setTitleSession() {
        SharedPreferences pref = getSharedPreferences("Data", MODE_PRIVATE);
        String session = pref.getString("sessionID", "");
        account.setText(session);
    }


}
