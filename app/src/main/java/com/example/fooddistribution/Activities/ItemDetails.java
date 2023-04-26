package com.example.fooddistribution.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.fooddistribution.R;

public class ItemDetails extends AppCompatActivity {

    RadioGroup groupVegN;
    RadioGroup wetndry;
    Button done;
    EditText namedesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Init();
    }

    private void Init() {
        groupVegN = findViewById(R.id.vegnonvegRG);
        wetndry = findViewById(R.id.wetndryRG);
        done = findViewById(R.id.doneBtItemDetail);
        namedesc = findViewById(R.id.namedescEt);

        done.setOnClickListener(t ->{

            String str = namedesc.getText().toString();
            int foodtype = groupVegN.getCheckedRadioButtonId();
            foodtype = (foodtype == R.id.vegRB)?0:1;
            int wetndryInt = wetndry.getCheckedRadioButtonId();
            wetndryInt = (wetndryInt == R.id.wetRB)?0:1;

            if(str.equals("")){
                namedesc.setError("Name/Description is Required!!");
                namedesc.requestFocus();
                return;
            }else{

                Intent intent= new Intent(ItemDetails.this, DonateFoodItem.class);
                intent.putExtra("FOODTYPE", foodtype);
                intent.putExtra("WETNDRY",wetndryInt);
                intent.putExtra("NAMENDESC",str);
                setResult(RESULT_OK, intent);
                finish();
            }


        });


    }
}