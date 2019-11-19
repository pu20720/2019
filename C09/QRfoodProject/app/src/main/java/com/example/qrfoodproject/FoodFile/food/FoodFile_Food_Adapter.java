package com.example.qrfoodproject.FoodFile.food;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrfoodproject.FoodFile.FoodFile_FoodcInformation;
import com.example.qrfoodproject.R;

import java.util.ArrayList;
import java.util.HashMap;

public class FoodFile_Food_Adapter extends RecyclerView.Adapter<FoodFile_Food_Adapter.ViewHolder> {
    private ArrayList<HashMap<String, String>> mData;
    public Context mContext;
    LayoutInflater layoutInflater;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item_fdName;

        public ViewHolder(View v) {
            super(v);
            item_fdName = (TextView) v.findViewById(R.id.item_FoodName);
        }
    }

    public FoodFile_Food_Adapter(Context mContext, ArrayList<HashMap<String, String>> mData) {
        this.mData = mData;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public FoodFile_Food_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater
                .inflate(R.layout.foodfile_food_item, parent, false);
        FoodFile_Food_Adapter.ViewHolder vh = new FoodFile_Food_Adapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final FoodFile_Food_Adapter.ViewHolder holder, final int position) {

        holder.item_fdName.setText(mData.get(position).get("fdName"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), mData.get(position).get("fdName"), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, FoodFile_FoodcInformation.class);
                intent.putExtra("fdId", mData.get(position).get("fdId"));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
