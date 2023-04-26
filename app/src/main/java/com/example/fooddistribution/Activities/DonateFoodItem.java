package com.example.fooddistribution.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fooddistribution.Adaptorz.FoodItmAdaptor;
import com.example.fooddistribution.AndroidConstants.ViewAnimation;
import com.example.fooddistribution.Models.FoodItem;
import com.example.fooddistribution.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DonateFoodItem extends AppCompatActivity {

    FloatingActionButton floatBt,DoneBt;
    ActivityResultLauncher<Intent> someActivityResultLauncher;
    RecyclerView foodItemRV;
    FoodItmAdaptor adaptor;
    ArrayList<FoodItem> list = new ArrayList<>();

    Boolean isRotate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_food_item);
        Init();
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() ==  RESULT_OK) {
                            int foodtype = result.getData().getIntExtra("FOODTYPE",0);
                            int wetndryInt = result.getData().getIntExtra("WETNDRY",0);
                            String namedesc = result.getData().getStringExtra("NAMENDESC");
                            addData(foodtype,wetndryInt,namedesc);
                        }
                    }
                });
    }

    private void addData(int foodtype, int wetndryInt, String namedesc) {
        adaptor.addFoodItem(new FoodItem(foodtype,wetndryInt,namedesc));
        if(adaptor.getItemCount() >= 1){
            DoneBt.setVisibility(View.VISIBLE);
        }
    }

    private void Init() {
        floatBt = findViewById(R.id.floatButtonDonateItem);
        DoneBt = findViewById(R.id.floatButtonDoneBt);
        floatBt.setOnClickListener(t ->{
            isRotate = ViewAnimation.rotateFab(t,!isRotate);
            Intent intent = new Intent(this, ItemDetails.class);
            someActivityResultLauncher.launch(intent);
        });
        DoneBt.setOnClickListener(t ->{
            startActivity(new Intent(DonateFoodItem.this, MainActivity.class));
        });

        handleRV();
    }

    private void handleRV() {
        foodItemRV = findViewById(R.id.foodItemRecycler);
        foodItemRV.setLayoutManager(new LinearLayoutManager(this));
        adaptor = new FoodItmAdaptor(this,list);
        foodItemRV.setAdapter(adaptor);
    }
}