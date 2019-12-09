package com.example.qrfoodproject.login;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qrfoodproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText account, password, confirm_password, name, email, height, weight, exercise;
    private RadioGroup genderRadioGroup;
    private Button btn_register;
    private static String URL_REGISTER = "http://120.110.112.96/using/Login/register.php";
    int gender;

    checkRegister check = new checkRegister();
    //function using for checking the information from this class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);

        setView();


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doubleCheck())     register();
            }
        });


    }

    private void setView() {
        account = this.findViewById(R.id.Account);
        password = this.findViewById(R.id.Password);
        confirm_password = this.findViewById(R.id.Confirm_Password);
        name = this.findViewById(R.id.Name);
        email = this.findViewById(R.id.Email);
        genderRadioGroup = this.findViewById(R.id.gender_radio_group);
        height = this.findViewById(R.id.Height);
        weight = this.findViewById(R.id.Weight);
        exercise = this.findViewById(R.id.Exercise);
        btn_register = this.findViewById(R.id.btn_register);

        TextView link_login = this.findViewById(R.id.link_login);
        link_login.setOnClickListener(link_loginListener);
    }

    private boolean doubleCheck(){

        String Account = account.getText().toString();
        String Password = password.getText().toString();
        String Confirm_password = confirm_password.getText().toString();
        String Name = name.getText().toString();
        String Email = email.getText().toString();
        String Height = height.getText().toString();
        String Weight = weight.getText().toString();
        String Exercise = exercise.getText().toString();

        if (!check.checkAccount(Account)){
            account.setError("帳號大小需介於4到10，且不可含特殊符號");
            return false;
        }else   account.setError(null);

        if (!check.checkPassword(Password)){
            password.setError("密碼大小需介於4到10，且不可含特殊符號");
            return false;
        }else   password.setError(null);

        if (!check.ifPasswordSame(Password, Confirm_password)){
            confirm_password.setError("密碼與原先輸入不符");
            return false;
        }else   confirm_password.setError(null);

        if (!check.checkName(Name)){
            name.setError("名稱長度超出範圍，或含有特殊符號");
            return false;
        }else   name.setError(null);

        if (!check.checkMail(Email)){
            email.setError("信箱不符合標準規範");
            return false;
        }else   email.setError(null);

        if (!check.checkHeight(Height)){
            height.setError("請確認填寫是否正確");
            return false;
        }else   height.setError(null);

        if (!check.checkWeight(Weight)){
            weight.setError("請確認填寫是否正確");
            return false;
        }else   weight.setError(null);

        if (!check.checkExercise(Exercise)){
            exercise.setError("運動量不可輸入1~4以外數值");
            return false;
        }else   exercise.setError(null);

        return true;
    }

    private void register(){

        final String account = this.account.getText().toString().trim();
        final String password = this.password.getText().toString().trim();
        final String name = this.name.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String height = this.height.getText().toString().trim();
        final String weight = this.weight.getText().toString().trim();
        final String exercise = this.exercise.getText().toString().trim();

        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        if(selectedId == R.id.female_radio_btn)
            gender = 0;
        else
            gender = 1;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final ProgressDialog progressDialog = new ProgressDialog(Register.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Creating Account...");
                progressDialog.show();

                Toast.makeText(Register.this, "Register success!", Toast.LENGTH_SHORT).show();
                finish();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(Register.this, "ErrorResponse! " + error.toString(), Toast.LENGTH_SHORT).show();
                        btn_register.setVisibility(View.VISIBLE);

                        error.printStackTrace();
                        try {
                            String responseBody = new String(error.networkResponse.data, Charset.forName("utf-8"));
                            JSONObject data = new JSONObject(responseBody);
                            String message = data.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.getMessage();
                        }
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("account", account);
                params.put("password", password);
                params.put("name", name);
                params.put("email", email);
                params.put("gender", Integer.toString(gender));
                params.put("height", height);
                params.put("weight", weight);
                params.put("exercise", exercise);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private TextView.OnClickListener link_loginListener = new TextView.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
