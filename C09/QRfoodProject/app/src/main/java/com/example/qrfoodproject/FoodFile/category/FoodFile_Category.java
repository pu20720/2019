package com.example.qrfoodproject.FoodFile.category;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.qrfoodproject.MySingleton;
import com.example.qrfoodproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FoodFile_Category extends AppCompatActivity {
    TextView rsName;
    String Intent_rsId;
    RecyclerView mRecyclerView;
    private String url = "http://120.110.112.96/using/get_cName_ByrsName.php";
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.foodfile_category);

        setView();

        getIntentExtra();//取得FoodAdapter過來的值

        setRecycleViewManager(); //設置RecyclerView Manager屬性

        getRes_food(); //取得該餐廳的食物
    }
    private void setView(){
       // rsName = findViewById(R.id.rsName);
        mRecyclerView = findViewById(R.id.category_View);
    }
    private void getIntentExtra(){
        Intent intent = getIntent();
        Intent_rsId = intent.getStringExtra("rsId");
        //rsName.setText(Intent_rsName);
    }
    private void setRecycleViewManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(FoodFile_Category.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

    }
    private void getRes_food(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("onResponse", response.toString());
                try {
                    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
                    //解析JSON檔傳入array
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonObject1 = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonObject1.length(); i++) {
                        JSONObject c = jsonObject1.getJSONObject(i);
                        HashMap<String, String> total = new HashMap<String, String>();
                        total.put("rsId", c.getString("rsId"));
                        total.put("cId", c.getString("cId"));
                        total.put("cName", c.getString("cName"));

                        array.add(total);

                        setRecycleView(array); //設定RecycleView
                    }
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
                params.put("rsId", Intent_rsId);
                return params;
            }
        };
        MySingleton.getInstance(FoodFile_Category.this).addToRequestQueue(stringRequest);
    }

    public void setRecycleView(ArrayList<HashMap<String, String>> array) {
        FoodFile_Category_Adapter mAdapter = new FoodFile_Category_Adapter(FoodFile_Category.this, array);
        mRecyclerView.setAdapter(mAdapter);
    }
}
