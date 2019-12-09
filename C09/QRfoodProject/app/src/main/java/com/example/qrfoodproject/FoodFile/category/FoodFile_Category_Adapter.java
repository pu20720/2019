package com.example.qrfoodproject.FoodFile.category;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrfoodproject.FoodFile.food.FoodFile_Food;
import com.example.qrfoodproject.R;

import java.util.ArrayList;
import java.util.HashMap;

public class FoodFile_Category_Adapter  extends RecyclerView.Adapter<FoodFile_Category_Adapter.ViewHolder>  {
    private ArrayList<HashMap<String, String>> mData;
    public Context mContext;
    LayoutInflater layoutInflater;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item_CategoryName;

        public ViewHolder(View v) {
            super(v);
            item_CategoryName = (TextView) v.findViewById(R.id.item_CategoryName);
        }
    }

    public FoodFile_Category_Adapter(Context mContext, ArrayList<HashMap<String, String>> mData) {
        this.mData = mData;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public FoodFile_Category_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater
                .inflate(R.layout.foodfile_category_item, parent, false);
        FoodFile_Category_Adapter.ViewHolder vh = new FoodFile_Category_Adapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final FoodFile_Category_Adapter.ViewHolder holder, final int position) {

        holder.item_CategoryName.setText(mData.get(position).get("cName"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),mData.get(position).get("cName"),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, FoodFile_Food.class);
                intent.putExtra("rsId", mData.get(position).get("rsId"));
                intent.putExtra("cId", mData.get(position).get("cId"));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
