package com.example.qrfoodproject.Profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.qrfoodproject.MySingleton;
import com.example.qrfoodproject.PushNotification.ManageAlarm;
import com.example.qrfoodproject.R;
import com.example.qrfoodproject.login.sessionCheck;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class Profile_main extends AppCompatActivity {
    Button modifyData, modifyPassword, logout, pushNotification;
    TextView account, name, gender, height, weight, exercise, email,BMI;
    DecimalFormat df = new DecimalFormat("##.00");
    private String print_profile_url = "http://120.110.112.96/using/Profile/getUserinformation.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);

        setView();//設定元件ID

        setButtonListener(); //設定按鈕監聽

        print_profile(); //顯示個人資料


    }

    private void setView() {
        modifyData = findViewById(R.id.modifyData);
        modifyPassword = findViewById(R.id.modifyPassword);
        logout = findViewById(R.id.logout);
        account = findViewById(R.id.account);
        name = findViewById(R.id.name);
        gender = findViewById(R.id.gender);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        email = findViewById(R.id.email);
        exercise = findViewById(R.id.exercise);
        pushNotification = findViewById(R.id.PushNotification);
        BMI = findViewById(R.id.BMI);

        //add an underline on TextView feelConfused
        SpannableString context = new SpannableString("推播功能是什麼東西？");
        context.setSpan(new UnderlineSpan(), 0 ,context.length(), 0);

    }

    private void setButtonListener() {
        //設定「修改資料」、「修改密碼」、「登出」按鈕
        modifyData.setOnClickListener(onclick);
        modifyPassword.setOnClickListener(onclick);
        logout.setOnClickListener(onclick);
        pushNotification.setOnClickListener(onclick);
    }

    Button.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //前往「修改資料」
                case R.id.modifyData:
                    new sessionCheck().jump_afterSessionCheck(Profile_main.this, Profile_ModifyData.class);
                    break;

                //前往「修改密碼」
                case R.id.modifyPassword:
                    new sessionCheck().jump_afterSessionCheck(Profile_main.this, Profile_ModifyPassword.class);
                    break;

                //前往「登出」
                case R.id.logout:
                    new sessionCheck().logout(Profile_main.this);
                    break;

                //啟用推播功能，將來會有關閉
                case R.id.PushNotification:
                    new ManageAlarm();
                    ManageAlarm.addSpecificTime(Profile_main.this);
                    Toast.makeText( Profile_main.this, "Push Notification activated!!",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void print_profile() {
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, print_profile_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    account.setText(data.getString("account"));
                    name.setText(data.getString("name"));
                    email.setText(data.getString("email"));

                    if (data.getString("gender").equals(true)) {
                        gender.setText("Female");
                    } else {
                        gender.setText("Male");
                    }

                    height.setText(data.getString("height"));
                    weight.setText(data.getString("weight"));
                    exercise.setText(data.getString("exercise"));
                    double bmi = data.getDouble("weight")/
                            (data.getDouble("height")/100)/(data.getDouble("height")/100);
                    bmi = Double.parseDouble(df.format(bmi));
                    if(bmi < 18.5){
                        String str = String.valueOf(bmi)+ " (體重過輕)";
                        BMI.setText(str);
                    }
                    else if(bmi <24){
                        String str = String.valueOf(bmi)+ " (體重適中)";
                        BMI.setText(str);
                    }else if(bmi<27){
                        String str = String.valueOf(bmi)+ " (體重過重)";
                        BMI.setText(str);
                    }else if(bmi <30){
                        String str = String.valueOf(bmi)+ " (輕度肥胖)";
                        BMI.setText(str);
                    }else if(bmi <35){
                        String str = String.valueOf(bmi)+ " (中度肥胖)";
                        BMI.setText(str);
                    }else{
                        String str = String.valueOf(bmi)+ " (重度肥胖)";
                        BMI.setText(str);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new sessionCheck().informing(Profile_main.this, error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //取得sessionID
                SharedPreferences pref = getSharedPreferences("Data", MODE_PRIVATE);
                String session = pref.getString("sessionID", "");


                Map<String, String> map = new HashMap<>();
                map.put("sessionID", session);
                return map;
            }
        };
        MySingleton.getInstance(Profile_main.this).addToRequestQueue(jsonObjectRequest);
    }
}