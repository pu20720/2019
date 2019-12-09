package com.example.qrfoodproject.FoodFile.restaurant;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrfoodproject.FoodFile.category.FoodFile_Category;
import com.example.qrfoodproject.R;

import java.util.ArrayList;
import java.util.HashMap;

public class FoodFile_Adapter extends RecyclerView.Adapter<FoodFile_Adapter.ViewHolder> {
    private ArrayList<HashMap<String, String>> mData;
    public Context mContext;
    LayoutInflater layoutInflater;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item_restaurant;

        public ViewHolder(View v) {
            super(v);
            item_restaurant = (TextView) v.findViewById(R.id.item_restaurant);
        }
    }

    public FoodFile_Adapter(Context mContext, ArrayList<HashMap<String, String>> mData) {
        this.mData = mData;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public FoodFile_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater
                .inflate(R.layout.foodfile_item, parent, false);
        FoodFile_Adapter.ViewHolder vh = new FoodFile_Adapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final FoodFile_Adapter.ViewHolder holder, final int position) {

        holder.item_restaurant.setText(mData.get(position).get("rsName"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),mData.get(position).get("rsName"),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, FoodFile_Category.class);
                intent.putExtra("rsId", mData.get(position).get("rsId"));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
