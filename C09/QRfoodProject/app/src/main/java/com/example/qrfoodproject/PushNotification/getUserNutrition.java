package com.example.qrfoodproject.PushNotification;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.qrfoodproject.MySingleton;
import com.example.qrfoodproject.Profile.checkNutrition_push;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class getUserNutrition {

    //將「單純要透過JSON從資料庫，把需要的資料壓成JSON傳進去SharedPreference」的request動作獨立出來
    //拜託不要笑我這個方法很智障，也是有我的苦衷才這麼做的...
    //會需要這樣把資料庫的資料拉出來再送入SharedPreferences，這樣即使APP在背景並且session掛掉還是會有資料在本地端

    static String nutrition_URL = "http://120.110.112.96/using/get_user_nutrition_data.php";


    public static void getNeededData_viaJSON(Context context){
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, nutrition_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("response", response);
                try {
                    //解析JSON檔傳入array
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");

                    checkNutrition_push.readAndWriteInPref(data, context);

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
                SharedPreferences pref = context.getSharedPreferences("Data", Context.MODE_PRIVATE);
                String session = pref.getString("sessionID", "");

                Map<String, String> map = new HashMap<String, String>();
                map.put("sessionID", session);
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
