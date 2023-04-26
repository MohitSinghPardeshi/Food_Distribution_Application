package com.example.fooddistribution.Adaptorz;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddistribution.Models.FoodItem;
import com.example.fooddistribution.R;
import java.util.ArrayList;


public class FoodItmAdaptor extends RecyclerView.Adapter {

    Context mContext;
    ArrayList<FoodItem> listF;

    public FoodItmAdaptor(Context mContext, ArrayList<FoodItem> list) {
        this.mContext = mContext;
        this.listF = list;
    }

    public void addFoodItem(FoodItem item){
        listF.add(item);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.donation_item, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FoodItem item = listF.get(position);

        FoodViewHolder viewHolder = (FoodViewHolder) holder;
        if(item.foodtype == 0){
            viewHolder.foodtype.setText("Veg");
            viewHolder.vegnnonIv.setImageResource(R.drawable.vegicon);
        }else{
            viewHolder.foodtype.setText("Non-Veg");
            viewHolder.vegnnonIv.setImageResource(R.drawable.nonvegicon);
        }
        if(item.wetndry == 0){
            viewHolder.wetndry.setText("Wet Food");
        }else{
            viewHolder.wetndry.setText("Dry Food");
        }
        viewHolder.namedesc.setText(item.nameNdesc);

    }

    @Override
    public int getItemCount() {
        return listF.size();
    }


    class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView namedesc,foodtype,wetndry;
        ImageView vegnnonIv;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            namedesc = itemView.findViewById(R.id.description_NameTV);
            foodtype = itemView.findViewById(R.id.vegnonvegTV);
            wetndry = itemView.findViewById(R.id.dryorwetTV);
            vegnnonIv = itemView.findViewById(R.id.vegnonvegIV);
        }
    }
}
