package com.example.qrfoodproject.Qrcode;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.View;
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
import com.example.qrfoodproject.login.sessionCheck;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

public class Qrcode_main extends AppCompatActivity {
    private String url = "http://120.110.112.96/using/getFoodInformationById.php?fdId=";
    static String QrcodeResult ;
    private TextView fdName,gram,calorie,protein,fat,saturateFat,transFat,cholesterol,sugar,dietaryFiber,sodium,calcium,potassium,ferrum;
    private String urll;
    private FloatingActionButton FAB;
    private ImageView my_image_view;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrfood_main);

        setView();

        print(); //呈現食物的詳細資料

        setButton(); //onclicklistener

        new sessionCheck().session_ifExist(this);
    }
    private void setButton(){
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = AddFood_Dialog.newInstance(fdName.getText().toString());//將fdName傳送給DialogFragment
                dialog.show(getSupportFragmentManager(),"");
            }
        });
    }
    private void print(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+QrcodeResult, new Response.Listener<String>() {
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
                    Glide.with(Qrcode_main.this).
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
                Toast.makeText(Qrcode_main.this,"查無該資料",Toast.LENGTH_LONG).show();
                finishAffinity();
            }
        });



        MySingleton.getInstance(Qrcode_main.this).addToRequestQueue(stringRequest);
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

    private void setView(){
        my_image_view = findViewById(R.id.my_image_view1);
        fdName = findViewById(R.id.fdName);
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
        FAB = findViewById(R.id.Qrcode_FAB);
    }
}
