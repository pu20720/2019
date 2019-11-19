package com.example.qrfoodproject.FoodDairy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.qrfoodproject.MySingleton;
import com.example.qrfoodproject.Profile.checkNutrition_push;
import com.example.qrfoodproject.R;

import com.github.mikephil.charting.charts.PieChart;


import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FoodDairy_intake_nutrition extends AppCompatActivity {
    TextView calorie,protein,fat,saturatedFat,transFat,cholesterol,carbohydrate,sugar,dietaryFiber,sodium,calcium,potassium
            ,ferrum,total_calorie,total_protein,total_fat,total_saturatedFat,total_transFat,total_cholesterol,total_carbohydrate
            ,total_sugar,total_dietaryFiber,total_sodium,total_calcium,total_potassium,total_ferrum;
    private String[] id = {"calorie","protein","fat","saturatedFat","transFat","cholesterol","carbohydrate","sugar"
            ,"dietaryFiber","sodium","calcium","potassium"
            ,"ferrum","total_calorie","total_protein","total_fat","total_saturatedFat","total_transFat"
            ,"total_cholesterol","total_carbohydrate"
            ,"total_sugar","total_dietaryFiber","total_sodium","total_calcium","total_potassium","total_ferrum"};
    private TextView[] textViews = new TextView[26];
    String url = "http://120.110.112.96/using/get_user_nutrition_data.php";
    String strDate;
    PieChart pieChart,pieChart2,pieChart3;
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.fooddairy_intake_nutrition);

        TextView_convert_array();//把TextView 做成 array
        setView();
        getIntentExtra();
        print(); //顯示數值及圖表



    }
    private void TextView_convert_array(){
        int temp;
        for(int i=0; i<id.length; i++){
            temp = getResources().getIdentifier(id[i], "id", getPackageName());
            textViews[i] = (TextView)findViewById(temp);
        }
    }
    private void getIntentExtra(){
        Intent intent = getIntent();
        strDate = intent.getStringExtra("strDate");

    }
    private void setView() {
        pieChart = (PieChart) findViewById(R.id.chart1);
        pieChart2 = (PieChart) findViewById(R.id.chart2);
        pieChart3 = (PieChart) findViewById(R.id.chart3);
        total_calorie=findViewById(R.id.total_calorie);
        total_protein=findViewById(R.id.total_protein);
        total_fat =findViewById(R.id.total_fat);
        total_saturatedFat  =findViewById(R.id.total_saturatedFat);
        total_transFat =findViewById(R.id.total_transFat);
        total_cholesterol=findViewById(R.id.total_cholesterol);
        total_carbohydrate=findViewById(R.id.total_carbohydrate);
        total_sugar=findViewById(R.id.total_sugar);
        total_dietaryFiber=findViewById(R.id.total_dietaryFiber);
        total_sodium=findViewById(R.id.total_sodium);
        total_calcium=findViewById(R.id.total_calcium);
        total_potassium =findViewById(R.id.total_potassium);
        total_ferrum=findViewById(R.id.total_ferrum);
        calorie = findViewById(R.id.calorie);
        protein = findViewById(R.id.protein);
        fat = findViewById(R.id.fat);
        saturatedFat= findViewById(R.id.saturatedFat);
        transFat= findViewById(R.id.transFat);
        cholesterol= findViewById(R.id.cholesterol);
        carbohydrate= findViewById(R.id.carbohydrate);
        sugar= findViewById(R.id.sugar);
        dietaryFiber= findViewById(R.id.dietaryFiber);
        sodium= findViewById(R.id.sodium);
        calcium= findViewById(R.id.calcium);
        potassium= findViewById(R.id.potassium);
        ferrum= findViewById(R.id.ferrum);
    }
    private void print() {
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("response", response);
                try {
                  //解析JSON檔傳入array
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    setData(data);
                    setChart(data);


                    //TODO  cant work if using getUserNutrition instead of checkNutrition_push without this page?
                    checkNutrition_push.readAndWriteInPref(data, getApplicationContext());


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //取得sessionID
                SharedPreferences pref = getSharedPreferences("Data", MODE_PRIVATE);
                String session = pref.getString("sessionID", "");
//                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
//                Date date = new Date();
//                strDate = sdFormat.format(date);

                Map<String, String> map = new HashMap<String, String>();
                map.put("sessionID", session);
                map.put("date",strDate);
                return map;
            }
        };
        MySingleton.getInstance(FoodDairy_intake_nutrition.this).addToRequestQueue(jsonObjectRequest);
    }
    private void setData(JSONObject data){
        try {

            total_calorie.setText(data.getString("total_calorie"));
            total_protein.setText(data.getString("total_protein"));
            total_fat.setText(data.getString("total_fat"));
            total_saturatedFat.setText(data.getString("total_saturatedFat"));
            total_transFat.setText(data.getString("total_transFat"));
            total_cholesterol.setText(data.getString("total_cholesterol"));
            total_carbohydrate.setText(data.getString("total_carbohydrate"));
            total_sugar.setText(data.getString("total_sugar"));
            total_dietaryFiber.setText(data.getString("total_dietaryFiber"));
            total_sodium.setText(data.getString("total_sodium"));
            total_calcium.setText(data.getString("total_calcium"));
            total_potassium.setText(data.getString("total_potassium"));
            total_ferrum.setText(data.getString("total_ferrum"));
            calorie.setText(data.getString("calorie"));
            protein.setText(data.getString("protein"));
            fat.setText(data.getString("fat"));
            saturatedFat.setText(data.getString("saturatedFat"));
            transFat.setText(data.getString("transFat"));
            cholesterol.setText(data.getString("cholesterol"));
            carbohydrate.setText(data.getString("carbohydrate"));
            sugar.setText(data.getString("sugar"));
            dietaryFiber.setText(data.getString("dietaryFiber"));
            sodium.setText(data.getString("sodium"));
            calcium.setText(data.getString("calcium"));
            potassium.setText(data.getString("potassium"));
            ferrum.setText(data.getString("ferrum"));
         //如果營養攝取量超過建議攝取值，則顯示紅字
            for(int i = 0 ;i<13;i++){
                setRedText(id[i],id[i+13],data,i);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setChart(JSONObject data){
        try{
            chartModel model = new chartModel(pieChart, data.getString("calorie"),  data.getString("total_calorie"));
            chartModel mode2 = new chartModel(pieChart2, data.getString("sodium"), data.getString("total_sodium"));
            chartModel mode3 = new chartModel(pieChart3, data.getString("protein"), data.getString("total_protein"));
            model.makeChart();
            mode2.makeChart();
            mode3.makeChart();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void setRedText(String nutrition ,String total_nutrition,JSONObject data,int index){
        try{
            Log.v("Red",data.getString(nutrition.toString()));
            if(data.getDouble(nutrition) > data.getDouble(total_nutrition))
                textViews[index].setTextColor(android.graphics.Color.RED);
        }
        catch (Exception e){

        }

    }
}
