package com.example.qrfoodproject.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.qrfoodproject.MySingleton;

import java.util.HashMap;
import java.util.Map;

public class sessionCheck extends AppCompatActivity {

    //2019,Oct 19Update
    //將原本的sessionCheck.java刪除->informing()轉到sessionCheck之下作為其innerClass
    //每當app任何功能需要檢查session時，只要new sessionCheck().session_ifExist(this)即可

    //備考：若該function的內容較難以規格化(如：更改個人資料)，則不使用這裡的innerClass來輔助判斷，在該原本的class底下寫就好

    private  String session_isExist_url = "http://120.110.112.96/using/session_isExist.php";
    private  String logout_url = "http://120.110.112.96/using/destroy_Session.php";


    public void informing(Context context, VolleyError error){

        //當session判斷得到error時，呼叫此function

        Toast.makeText(context, "請重新登入", Toast.LENGTH_SHORT).show();
        context.startActivity(new Intent(context, MainActivity.class));
        ActivityCompat.finishAffinity((Activity)context);
        Log.d("Volley Session Lost",  error.toString());

    }

    public void session_ifExist(Activity context){

        //此function只進行session判斷，onResponse()不進行任何動作

        StringRequest stringRequest = new StringRequest(Request.Method.POST, session_isExist_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Session", "Session is still valid");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                informing(context, error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences pref = context.getSharedPreferences("Data", MODE_PRIVATE);
                String session = pref.getString("sessionID", "");
                Map<String, String> map = new HashMap<>();
                map.put("sessionID", session);
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    public void jump_afterSessionCheck(Activity context,Class goal){

        //此function不同於session_ifExist()
        //在這裡，當session判斷得到onResponse()後，程式就會進行頁面轉換(intent)

        StringRequest stringRequest = new StringRequest(Request.Method.POST, session_isExist_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                context.startActivity(new Intent(context, goal));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                informing(context, error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences pref = context.getSharedPreferences("Data", MODE_PRIVATE);
                String session = pref.getString("sessionID", "");
                Map<String, String> map = new HashMap<>();
                map.put("sessionID", session);
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
    public void jump_afterSessionCheck_addrecord(Activity context,Class goal,String time){

        //此function不同於session_ifExist()
        //在這裡，當session判斷得到onResponse()後，程式就會進行頁面轉換(intent)
        //用於FoodDairy中addrecord的session判斷
        StringRequest stringRequest = new StringRequest(Request.Method.POST, session_isExist_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(context, goal);
                intent.putExtra("times", time);
                context.startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                informing(context, error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences pref = context.getSharedPreferences("Data", MODE_PRIVATE);
                String session = pref.getString("sessionID", "");
                Map<String, String> map = new HashMap<>();
                map.put("sessionID", session);
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
    public int jump_afterSessionCheck(Activity context){

        //此function不同於session_ifExist()
        //在這裡，當session判斷得到onResponse()後，程式就會進行頁面轉換(intent)

        StringRequest stringRequest = new StringRequest(Request.Method.POST, session_isExist_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               // context.startActivity(new Intent(context, goal));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                informing(context, error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences pref = context.getSharedPreferences("Data", MODE_PRIVATE);
                String session = pref.getString("sessionID", "");
                Map<String, String> map = new HashMap<>();
                map.put("sessionID", session);
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
        return 1;
    }
    public void logout(Activity context){

        //登出，對，就是登出

        SharedPreferences pref = context.getSharedPreferences("Data", MODE_PRIVATE);
        final String session = pref.getString("sessionID", "");
        pref.edit().clear().apply();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, logout_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Anything to do?
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new sessionCheck().informing(context, error);
                ActivityCompat.finishAffinity(context);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("sessionID", session);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
        context.startActivity(new Intent(context, MainActivity.class));
        ActivityCompat.finishAffinity(context);
    }
}
