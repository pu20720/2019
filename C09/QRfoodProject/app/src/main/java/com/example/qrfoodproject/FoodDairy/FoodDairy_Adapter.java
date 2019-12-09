package com.example.qrfoodproject.FoodDairy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.qrfoodproject.MySingleton;
import com.example.qrfoodproject.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FoodDairy_Adapter extends RecyclerView.Adapter<FoodDairy_Adapter.ViewHolder> {
    private ArrayList<HashMap<String, String>> mData;
    public Context mContext;
    LayoutInflater layoutInflater;
    String sn;
    String url = "http://120.110.112.96/using/FoodDairy/removeFoodDairyRecord.php";

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item_restaurant, item_food, item_serving,item_cal;
        public Button item_remove;

        public ViewHolder(View v) {
            super(v);

            item_restaurant = (TextView) v.findViewById(R.id.item_restaurant);
            item_food = (TextView) v.findViewById(R.id.item_food);
            item_serving = (TextView) v.findViewById(R.id.item_serving);
            item_remove = (Button) v.findViewById(R.id.item_remove);
            item_cal = (TextView)v.findViewById(R.id.item_cal);

            item_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder
                            .setTitle("刪除 : "+(mData.get(getAdapterPosition()).get("fdName").toString()))
 .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                            removeItem(getAdapterPosition());
                        }
                    })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    // 移除項目，getAdapterPosition為點擊的項目位置
                 //   DialogFragment dd = new removeItem_Dialog();
                  //  dd.show(((FragmentActivity)mContext).getSupportFragmentManager(),"");
                   // removeItem(getAdapterPosition());
                }
            });
        }


    }

    public FoodDairy_Adapter(Context mContext, ArrayList<HashMap<String, String>> mData) {
        this.mData = mData;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public FoodDairy_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater
                .inflate(R.layout.fooddairy_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FoodDairy_Adapter.ViewHolder holder, final int position) {
        holder.item_restaurant.setText(mData.get(position).get("location")+" - "+mData.get(position).get("rsName"));
        holder.item_food.setText(mData.get(position).get("fdName"));
        holder.item_serving.setText(mData.get(position).get("serving")+"份");
        holder.item_cal.setText(mData.get(position).get("total_cal")+"大卡");


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void removeItem(int position) {
        sn = mData.get(position).get("sn");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("LOG", "remove position :" + sn);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sn", sn);
                return params;
            }
        };
        MySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
        mData.remove(position);
        notifyItemRemoved(position);
    }
}
