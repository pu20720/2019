package com.example.qrfoodproject.FoodDairy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.qrfoodproject.FoodFile.FoodFile_FoodcInformation;
import com.example.qrfoodproject.FoodFile.food.FoodFile_Food;
import com.example.qrfoodproject.FoodFile.restaurant.FoodFile_res_Fragment;
import com.example.qrfoodproject.login.sessionCheck;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.qrfoodproject.FoodDairy.calen.FoodDairy_Calen;
import com.example.qrfoodproject.FoodDairy.calen.FoodDairy_Calen_date;
import com.example.qrfoodproject.MySingleton;
import com.example.qrfoodproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FoodDairy_Fragment_breakfast extends Fragment {

    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    String url = "http://120.110.112.96/using/FoodDairy/getFoodDairyRecord.php";

    //  ArrayList<HashMap<String,String>> array = new ArrayList<HashMap<String, String>>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fooddairy_fragment_breakfast, container, false);
        try {
            String str = (String)getArguments().get("DATA");
            Toast.makeText(getContext(),str,Toast.LENGTH_LONG).show();
        }catch (Exception e){

        }


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setView(view); //設定元件ID

        setRecycleViewManager(); //設置RecyclerView Manager屬性

        getBreakfastData(); //取得食物的資料

        setFabButtonListener();//設置「新增食物」按鈕監聽

    }

    private void setView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.BreakfastView);
        fab = view.findViewById(R.id.FAB_breakfast);

    }

    private void setRecycleViewManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void setFabButtonListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new sessionCheck().jump_afterSessionCheck_addrecord(getActivity(),FoodDairy_AddFood.class,"早");

//                Intent intent = new Intent(getActivity(), FoodDairy_AddFood.class);
//                intent.putExtra("times", "早");
//                startActivity(intent);

            }
        });
    }

    public void setRecycleView(ArrayList<HashMap<String, String>> array) {
        FoodDairy_Adapter mAdapter = new FoodDairy_Adapter(getActivity(), array);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void getBreakfastData() {

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
                        total.put("location", c.getString("location"));
                        total.put("fdName", c.getString("fdName"));
                        total.put("serving", c.getString("serving"));
                        total.put("sn", c.getString("sn"));
                        total.put("rsName",c.getString("rsName"));
                        DecimalFormat df = new DecimalFormat("##.00");
                        String total_cal = String.valueOf(df.format(c.getDouble("total_cal")));
                        total.put("total_cal",total_cal);
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
                //取得sessionID
                SharedPreferences pref = getActivity().getSharedPreferences("Data", MODE_PRIVATE);
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
                params.put("date", strDate);
                params.put("time", "早");
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}
