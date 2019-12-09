package com.example.qrfoodproject.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.qrfoodproject.MySingleton;
import com.example.qrfoodproject.R;
import com.example.qrfoodproject.login.MainActivity;
import com.example.qrfoodproject.login.checkRegister;
import com.example.qrfoodproject.login.sessionCheck;

import java.util.HashMap;
import java.util.Map;

public class Profile_ModifyPassword extends AppCompatActivity {
    EditText edtPassword, edtPassword1, edtoldPassword;
    Button commit;
    private String url = "http://120.110.112.96/using/Profile/edtPassword.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_modifypassword);

        setView(); //設定元件ID

        commit.setOnClickListener(onclick);

        new sessionCheck().session_ifExist(this);

    }

    private void setView() {
        edtoldPassword = findViewById(R.id.edtoldPassword);
        edtPassword = findViewById(R.id.edtPassword);
        edtPassword1 = findViewById(R.id.edtPassword1);
        commit = findViewById(R.id.commit);
    }


    Button.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String previousPassword = edtoldPassword.getText().toString();
            String nowPassword = edtPassword.getText().toString();
            String againPassword = edtPassword1.getText().toString();

            if (previousPassword.isEmpty() || nowPassword.isEmpty() || againPassword.isEmpty()) {
                Toast.makeText(Profile_ModifyPassword.this, "請輸入資料！",Toast.LENGTH_SHORT).show();
            }else{
                if (!previousPassword.equals(nowPassword) && !previousPassword.equals(againPassword)){
                    if (nowPassword.equals(againPassword)) {
                        if (new checkRegister().checkPassword(nowPassword)){

                            changePassword(previousPassword, nowPassword);
                            Toast.makeText(Profile_ModifyPassword.this, "密碼修改成功！", Toast.LENGTH_SHORT).show();
                            finish();

                        }else   Toast.makeText(Profile_ModifyPassword.this, "密碼修改失敗，請檢查是否輸入錯誤", Toast.LENGTH_LONG).show();

                    }
                }else{
                    Toast.makeText(Profile_ModifyPassword.this, "密碼修改失敗，請檢查是否輸入錯誤", Toast.LENGTH_LONG).show();
                }
            }

        }
    };

    private void changePassword(String previousPassword, String nowPassword){

        //get the password from what user inserted
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Change Password", "Password modifying completed");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("error1", error.toString());
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences pref = getSharedPreferences("Data", MODE_PRIVATE);
                String session = pref.getString("sessionID", "");
                Map<String, String> params = new HashMap<>();
                params.put("oldPassword", previousPassword);
                params.put("newPassword", nowPassword);
                params.put("sessionID", session);
                return params;
            }
        };
        MySingleton.getInstance(Profile_ModifyPassword.this).addToRequestQueue(stringRequest);
    }

}

