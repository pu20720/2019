package com.example.qrfoodproject.FoodDairy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.qrfoodproject.FoodDairy.calen.FoodDairy_Calen;
import com.example.qrfoodproject.FoodDairy.calen.FoodDairy_Calen_date;
import com.example.qrfoodproject.MySingleton;
import com.example.qrfoodproject.PushNotification.getUserNutrition;
import com.example.qrfoodproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FoodDairy_AddFood extends AppCompatActivity {

    Spinner Addfood_time, Addfood_location, Addfood_restaurant, Addfood_category, Addfood_food;
    EditText Addfood_serving;
    Button Addfood_input;

    ArrayList<String> cId,rsId;
    private String getRestaurant = "http://120.110.112.96/using/Common_FF_FD/getrsNameByLocation.php";
    private String getRestaurantCategory = " http://120.110.112.96/using/get_cName_ByrsName.php";
    private String getRestaurantFood = "http://120.110.112.96/using/Common_FF_FD/getResFood.php";
    private String addRecord = "http://120.110.112.96/using/FoodDairy/addrecord.php";
    private String[] time = {"早", "午", "晚"};
    private String[] location = {"靜園", "宜園", "至善"};
    private String times; //接收使用者在哪個頁面新增餐點
    ArrayAdapter<String> time_adapter, location_adapter, restaurant_adapter, food_adapter;
    ArrayList<String> array1 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fooddairy_addfood);

        //用來提供time spinner的預設值
        Intent intent = getIntent();
        times = intent.getStringExtra("times");

        setView(); //設定元件ID

        setAdapter(); //設定time、location 的spinner Adapter

        setOnItemSelectedListener(); //設定restaurant、food spinner 的值

        setCommitButtonListener(); //設定提交按鈕

    }

    private void setView() {
        Addfood_time = (Spinner) findViewById(R.id.Addfood_time);
        Addfood_location = (Spinner) findViewById(R.id.Addfood_location);
        Addfood_restaurant = (Spinner) findViewById(R.id.Addfood_restaurant);
        Addfood_category = (Spinner) findViewById(R.id.Addfood_category);
        Addfood_food = (Spinner) findViewById(R.id.Addfood_food);
        Addfood_serving = (EditText) findViewById(R.id.Addfood_serving);
        Addfood_input = (Button) findViewById(R.id.Addfood_input);

        cId = new ArrayList<String>();
        rsId = new ArrayList<String>();
    }

    private void setAdapter() {
        time_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, time);
        Addfood_time.setAdapter(time_adapter);
        Addfood_time.setSelection(FindTimeIndex(times));
        location_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, location);
        Addfood_location.setAdapter(location_adapter);

    }

    private void setOnItemSelectedListener() {
        //設定選擇地點，餐廳欄位跳出相對應的餐廳
        Addfood_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = Addfood_location.getSelectedItemPosition();

                getRestaurant(pos, new VolleyCallback() {
                    @Override
                    public void onSuccess(ArrayList<String> callback_rsId) {

                        rsId = callback_rsId;
                           Toast.makeText(FoodDairy_AddFood.this,callback_rsId.get(1),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void getcId(ArrayList<String> cId) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        設定選擇餐廳，分類欄位跳出該餐廳分類
        Addfood_restaurant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int pos = Addfood_restaurant.getSelectedItemPosition();
                getRestaurantCategory(pos, new VolleyCallback() {
                    @Override
                    public void onSuccess(ArrayList<String> rsId) {

                    }

                    @Override
                    public void getcId(ArrayList<String> callback_cId) {
                      //  cId.clear();
                        cId = callback_cId;
                   //     Toast.makeText(FoodDairy_AddFood.this, cId.get(0), Toast.LENGTH_LONG).show();

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //設定選擇分類，食物欄位跳出相對應的食物
        Addfood_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = Addfood_category.getSelectedItemPosition();
                getRestaurantFood(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setCommitButtonListener() {
        Addfood_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, addRecord, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //關閉FoodDairy_Calen_date的頁面
                        if (FoodDairy_Calen_date.instance != null) {
                            try {
                                FoodDairy_Calen_date.instance.finish();
                                Intent intent = new Intent(FoodDairy_AddFood.this, FoodDairy_Calen_date.class);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                            }
                        } else { //關掉FoodDairy_main的頁面
                            try {
                                FoodDairy_main.instance.finish();
                                Intent intent = new Intent(FoodDairy_AddFood.this, FoodDairy_main.class);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                            }
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("ErrorResponse", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        //取得session
                        SharedPreferences pref = FoodDairy_AddFood.this.getSharedPreferences("Data", MODE_PRIVATE);
                        String session = pref.getString("sessionID", "");

                        String strDate;
                        //取得日曆時間
                        if (FoodDairy_Calen_date.instance != null) {
                            strDate = FoodDairy_Calen.date_format;
                        } else { //取得當天時間
                            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();
                            strDate = sdFormat.format(date);
                        }


                        Map<String, String> params = new HashMap<String, String>();
                        params.put("sessionID", session);
                        params.put("time", Addfood_time.getSelectedItem().toString());
                        params.put("date", strDate);
                        params.put("fdName", Addfood_food.getSelectedItem().toString());
                        params.put("serving", Addfood_serving.getText().toString());
                        return params;
                    }
                };
                MySingleton.getInstance(FoodDairy_AddFood.this).addToRequestQueue(stringRequest);

            }
        });
    }

    private void getRestaurantCategory(int pos, VolleyCallback callback) {
        final int position = pos;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getRestaurantCategory, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("onResponse", response.toString());
                try {
                    Log.v("success_getResFood", response);
                    ArrayList<String> array = new ArrayList<String>();
                    ArrayList<String> cId = new ArrayList<String>();
                    //解析JSON檔傳入array
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonObject1 = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonObject1.length(); i++) {

                        JSONObject c = jsonObject1.getJSONObject(i);
                        cId.add(c.getString("cId"));
                        array.add(c.getString("cName"));
                    }

                    food_adapter = new ArrayAdapter<String>(FoodDairy_AddFood.this, R.layout.support_simple_spinner_dropdown_item, array);
                    Addfood_category.setAdapter(food_adapter);
                    callback.getcId(cId);
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Error_getResFood", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("rsId", rsId.get(position));
                return params;
            }
        };
        MySingleton.getInstance(FoodDairy_AddFood.this).addToRequestQueue(stringRequest);
    }

    private void getRestaurant(int pos, VolleyCallback callback) {
        final int position = pos;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getRestaurant, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.v("success_getRes", response);
                    ArrayList<String> array = new ArrayList<String>();
                    ArrayList<String> rsId = new ArrayList<String>();
                    //解析JSON檔傳入array
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonObject1 = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonObject1.length(); i++) {

                        JSONObject c = jsonObject1.getJSONObject(i);
                        rsId.add(c.getString("rsId"));

                        array.add(c.getString("rsName"));
                    }

                    restaurant_adapter = new ArrayAdapter<String>(FoodDairy_AddFood.this, R.layout.support_simple_spinner_dropdown_item, array);
                    Addfood_restaurant.setAdapter(restaurant_adapter);
                    callback.onSuccess(rsId);
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("ErrorResponse", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("location", location[position]);
                return params;
            }
        };

        MySingleton.getInstance(FoodDairy_AddFood.this).addToRequestQueue(stringRequest);

    }

    private void getRestaurantFood(int pos) {
        final int position = pos;
        Log.v("getRestaurant", Addfood_restaurant.getSelectedItem().toString());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getRestaurantFood, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("onResponse", response.toString());
                try {
                    Log.v("success_getResFood", response);
                    ArrayList<String> array = new ArrayList<String>();

                    //解析JSON檔傳入array
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonObject1 = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonObject1.length(); i++) {
                        JSONObject c = jsonObject1.getJSONObject(i);
                        array.add(c.getString("fdName"));
                    }
                    food_adapter = new ArrayAdapter<String>(FoodDairy_AddFood.this, R.layout.support_simple_spinner_dropdown_item, array);
                    Addfood_food.setAdapter(food_adapter);
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Error_getResFood", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("rsId", rsId.get(Addfood_restaurant.getSelectedItemPosition()));
                params.put("cId", cId.get(position));
                return params;
            }
        };
        MySingleton.getInstance(FoodDairy_AddFood.this).addToRequestQueue(stringRequest);
    }

    private int FindTimeIndex(String times) {
        int i;
        for (i = 0; i < time.length; i++) {
            if (times.equals(time[i])) {
                break;
            }
        }
        return i;
    }

}

