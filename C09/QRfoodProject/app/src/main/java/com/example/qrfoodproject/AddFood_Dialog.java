package com.example.qrfoodproject;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class AddFood_Dialog extends DialogFragment {

    public static AddFood_Dialog newInstance(String msg){
        AddFood_Dialog f = new AddFood_Dialog();
        Bundle args = new Bundle();
        args.putString("Intent_fdName",msg);
        f.setArguments(args);//透過setArguments傳值
        return f;
    }
    private String url = "http://120.110.112.96/using/FoodDairy/addrecord.php";
    TextView date_today, cancel, comfirm,fdName;
    Spinner time;
    EditText serving;
    String strDate;
    String time_array[] = {"早", "午", "晚"};
    ArrayAdapter<String> time_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qrcode_foodfile_altertdialog, container, false);
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //String  mNum = getArguments().getString("msg");
        setView(view); //設定元件ID

        setDate(); //設定當天日期

        setButton(); //設定確認取消按鈕

        setBackgroundTransparent();//// 設定背景為透明 這樣才可以顯示圓角

    }

    private void setView(View view){
        cancel = view.findViewById(R.id.cancel);
        comfirm = view.findViewById(R.id.comfirm);
        date_today = view.findViewById(R.id.date);
        serving = view.findViewById(R.id.serving);
        time = view.findViewById(R.id.time);
        fdName = view.findViewById(R.id.foodname);
        serving.setText("1");
        fdName.setText(getArguments().getString("Intent_fdName"));
        time_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, time_array);
        time.setAdapter(time_adapter);
    }
    private void setDate(){
        //取得當天時間
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        strDate = sdFormat.format(date);
        date_today.setText(strDate);
    }
    private void setButton(){
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                        SharedPreferences pref = getActivity().getSharedPreferences("Data", MODE_PRIVATE);
                        String session = pref.getString("sessionID", "");

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("sessionID", session);
                        params.put("time", time.getSelectedItem().toString());
                        params.put("date", strDate);
                        params.put("fdName", fdName.getText().toString());
                        params.put("serving", serving.getText().toString());
                        return params;
                    }
                };
                MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
                Toast.makeText(getActivity(),"新增一筆食物!",Toast.LENGTH_SHORT).show();
                dismiss();
            }

        });
    }
    private void setBackgroundTransparent(){
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
    }

}
