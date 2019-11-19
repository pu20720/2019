package com.example.qrfoodproject.FoodFile;

import android.content.Intent;
import android.os.Bundle;

import com.example.qrfoodproject.Home_QRfood;
import com.example.qrfoodproject.Profile.Profile_ModifyData;
import com.example.qrfoodproject.Profile.Profile_main;
import com.example.qrfoodproject.login.MainActivity;
import com.example.qrfoodproject.login.sessionCheck;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.qrfoodproject.AddFood_Dialog;
import com.example.qrfoodproject.MySingleton;
import com.example.qrfoodproject.R;

import org.json.JSONException;
import org.json.JSONObject;

public class FoodFile_FoodcInformation extends AppCompatActivity {
    private String url = "http://120.110.112.96/using/getFoodInformationById.php?fdId=";
    private TextView fdName,gram,calorie,protein,fat,saturateFat,transFat,cholesterol,sugar,dietaryFiber,sodium,calcium,potassium,ferrum;
    private String urll;
    private ImageView my_image_view;
    private FloatingActionButton fab;
    private Button btn_home;
    String Intent_fdId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodfile_foodinformation);

        Intent intent = getIntent();
        Intent_fdId = intent.getStringExtra("fdId");

        setView();

        print(); //呈現食物的詳細資料
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FoodFile_FoodcInformation.this, Home_QRfood.class));
                ActivityCompat.finishAffinity(FoodFile_FoodcInformation.this);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AddFood_Dialog a = new AddFood_Dialog();
                new sessionCheck().jump_afterSessionCheck(FoodFile_FoodcInformation.this);
                DialogFragment dialog =AddFood_Dialog.newInstance(fdName.getText().toString());//將fdName傳送給DialogFragment
                dialog.show(getSupportFragmentManager(),"");
            }
        });
    }
    private void setView(){
        my_image_view = findViewById(R.id.my_image_view1);
        fdName = findViewById(R.id.fdName);
        btn_home = findViewById(R.id.btn_home);
        gram = findViewById(R.id.gram);
        calorie = findViewById(R.id.calorie);
        protein = findViewById(R.id.protein);
        fat = findViewById(R.id.fat);
        saturateFat = findViewById(R.id.saturatedFat);
        transFat = findViewById(R.id.transFat);
        cholesterol = findViewById(R.id.cholesterol);
        sugar = findViewById(R.id.sugar);
        dietaryFiber = findViewById(R.id.dietaryFiber);
        sodium = findViewById(R.id.sodium);
        calcium = findViewById(R.id.calcium);
        potassium = findViewById(R.id.potassium);
        ferrum = findViewById(R.id.ferrum);
        fab = findViewById(R.id.Foodfile_FAB);
    }
    private void print(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+Intent_fdId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.v("Log",response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                    doSetText(jsonObject1);

                    urll =jsonObject1.getString("photo");
                    String newURL = urll.replaceAll(
                            "\\\\",""
                    );
                    Glide.with(FoodFile_FoodcInformation.this).
                            load(newURL).
                            centerCrop().
                            into(my_image_view);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FoodFile_FoodcInformation.this,"查無該資料",Toast.LENGTH_LONG).show();
                finishAffinity();
            }
        });



        MySingleton.getInstance(FoodFile_FoodcInformation.this).addToRequestQueue(stringRequest);
    }
    private void doSetText(JSONObject jsonObject1) throws JSONException {

        fdName.setText(jsonObject1.getString("fdName"));
        gram.setText(String.format("%s克", jsonObject1.getString("gram")));
        calorie.setText(String.format("%s大卡", jsonObject1.getString("calorie")));
        protein.setText(jsonObject1.getString("protein"));
        fat.setText(jsonObject1.getString("fat"));
        saturateFat.setText(jsonObject1.getString("saturatedFat"));
        transFat.setText(jsonObject1.getString("transFat"));
        cholesterol.setText(jsonObject1.getString("cholesterol"));
        sugar.setText(jsonObject1.getString("sugar"));
        dietaryFiber.setText(jsonObject1.getString("dietaryFiber"));
        sodium.setText(String.format("%s毫克", jsonObject1.getString("sodium")));
        calcium.setText(String.format("%s毫克", jsonObject1.getString("calcium")));
        potassium.setText(String.format("%s毫克", jsonObject1.getString("potassium")));
        ferrum.setText(String.format("%s毫克", jsonObject1.getString("ferrum")));

    }
}
