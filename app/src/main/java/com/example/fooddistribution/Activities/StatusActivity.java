package com.example.fooddistribution.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fooddistribution.Models.DonReqModel;
import com.example.fooddistribution.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class StatusActivity extends AppCompatActivity {
    ImageView backBt;
    DonReqModel model;
    private TextView openInMap,title,foodName,donName,cookTime,quantity,address,mobNo,cancelDon;
    private TextView DonationId;
    private ImageView backBtn,donImg,foodImg;
    private Button acceptBtn;
    private View statusBarA,statusBarB,pending,completed;

    private Dialog mDialog;
    TextView yes;
    TextView no;
    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        model = (DonReqModel) getIntent().getSerializableExtra("model");

        Init();
        setData();
        handleStatus(model.getStatus());
    }

    private void handleStatus(String status) {
            if(status.equals("Pending")){
                statusBarA.setBackground(getDrawable(R.color.purple_500));
                pending.setBackground(getDrawable(R.drawable.shape_status_current));
            }else if(status.equals("Completed")){
                statusBarA.setBackground(getDrawable(R.color.purple_500));
                pending.setBackground(getDrawable(R.drawable.shape_status_current));
                statusBarB.setBackground(getDrawable(R.color.purple_500));
                completed.setBackground(getDrawable(R.drawable.shape_status_current));
            }


    }

    private void setData() {
        Glide.with(this).load(model.getFoodImage()).error(R.drawable.foodplaceholder).into(foodImg);
        Glide.with(this).load(model.getModel().profileImage).error(R.drawable.profile_image).into(donImg);
        foodName.setText(model.getFoodName());
        donName.setText(model.getModel().getFname()+" "+model.getModel().getLname());
        cookTime.setText("Cooking Time :"+model.getCookTime());
        quantity.setText("For "+model.getExpPepCnt()+" People");
        address.setText(model.getLoc());
        mobNo.setText("Mobile No :"+model.getModel().getContactNo());

        DonationId.setText(model.getUserId().substring(0,5)+String.valueOf(model.getUploadTime()).substring(0,5));
    }

    private void Init() {

        //DialogBoxTemp
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.logout_popup);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        no = mDialog.findViewById(R.id.noTV);
        yes = mDialog.findViewById(R.id.yesTV);
        text = mDialog.findViewById(R.id.textTV);

        text.setText("Are You Sure You Want To Delete This Donation Permanently!!");
        yes.setText("Delete");
        no.setText("Cancel");
        yes.setTextColor(getResources().getColor(R.color.red));

        no.setOnClickListener(t->{
            mDialog.dismiss();
        });
        yes.setOnClickListener(t->{
            deleteDonation();
        });

        statusBarA = findViewById(R.id.statusBarA);
        statusBarB = findViewById(R.id.statusBarB);
        pending = findViewById(R.id.viewOrderApproved);
        completed = findViewById(R.id.orderDelivered);

        foodName= findViewById(R.id.donDetailfoodN);
        donName= findViewById(R.id.donDetaildonName);
        cookTime= findViewById(R.id.donDetailCookTime);
        quantity= findViewById(R.id.donDetailQnty);
        address= findViewById(R.id.donDetailLoc);
        mobNo= findViewById(R.id.donDetaildonMobNo);

        donImg= findViewById(R.id.donDetaildonIMg);
        foodImg= findViewById(R.id.donDetailfoodImg);
        acceptBtn= findViewById(R.id.donDetailBt);

        DonationId = findViewById(R.id.orderidID);
        cancelDon = findViewById(R.id.cancelTrackOrderTV);


        backBt = findViewById(R.id.trackOrderBackBtIV);
        backBt.setOnClickListener(t->{
            onBackPressed();
        });

        cancelDon.setOnClickListener(t ->{
            mDialog.show();
        });

    }

    private void deleteDonation() {
        String status = model.getStatus().toString();
        if(status.equals("Not Accepted")){
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            CollectionReference collectionReference = firebaseFirestore.collection("User_Donation_Info");
            Query query = collectionReference.whereEqualTo("userId", model.getUserId()).whereEqualTo("uploadTime", model.getUploadTime());

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        documentSnapshot.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("TAG", "Document deleted successfully.");
                                })
                                .addOnFailureListener(e -> {
                                    Log.d("TAG", "Error deleting document: ", e);
                                });
                    } else {
                        Log.d("TAG", "No documents matched the query.");
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            });
            mDialog.dismiss();
            finish();
        }else{
            mDialog.dismiss();
            text.setText("Approved Request Cannot be Deleted");
            yes.setVisibility(View.GONE);
            mDialog.show();
            return;
        }
    }
}