package com.example.fooddistribution.Fragmentz;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddistribution.Adaptorz.HistoryAdaptor;
import com.example.fooddistribution.Models.DonReqModel;
import com.example.fooddistribution.Models.HistoryModel;
import com.example.fooddistribution.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ThirdFragment extends Fragment{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HistoryAdaptor historyAdaptor;
    private RelativeLayout relativeLayout;
    private FrameLayout progressBar;
    ActivityResultLauncher<Intent> someActivityResultLauncher;

    private Dialog mDialog;
    TextView yes;
    TextView no;
    TextView text;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_third, container, false);

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() ==  EditFromText.TEXT_CHANGE_CODE) {
//                            // There are no request codes
//                            String from = result.getData().getStringExtra("RESULT_FROM");
//                            String to = result.getData().getStringExtra("RESULT_TO");
//
//                            String tv = result.getData().getStringExtra("TVNAME");
//                            if(tv.equals("One Way")){
//                                fromOneWay.setText(from);
//                                toOneWay.setText(to);
//                            }else if(tv.equals("Round Trip")){
//                                fromRoundTrip.setText(from);
//                                toRoundTrip.setText(to);
//                            }
//                            //dothispart after submit button click
////                        SaveSharedPreference.setName(ChangeProfileDetails.this, string);
//
//                        }
                    }
                });

        Init(view);
        return view;
    }

    private void Init(View view) {

        //DialogBoxTemp
        mDialog = new Dialog(getContext());
        mDialog.setContentView(R.layout.logout_popup);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        no = mDialog.findViewById(R.id.noTV);
        yes = mDialog.findViewById(R.id.yesTV);
        text = mDialog.findViewById(R.id.textTV);

        progressBar = view.findViewById(R.id.progress_layoutHistory);
        progressBar.setVisibility(View.VISIBLE);


        text.setText("Are You Sure You Want To Delete This Donation Permanently!!");
        yes.setText("Delete");
        no.setText("Cancel");
        yes.setTextColor(view.getResources().getColor(R.color.red));

        no.setOnClickListener(t->{
            mDialog.dismiss();
        });
        yes.setOnClickListener(t->{

        });

        handleClicks(view);
        handleRV(view);

    }

    private void handleRV(View view) {

        recyclerView = view.findViewById(R.id.historyRecycler);
        swipeRefreshLayout = view.findViewById(R.id.idSrlMandi);
        relativeLayout = view.findViewById(R.id.noHistoryRL);

        relativeLayout.setVisibility(View.VISIBLE);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(view);
            }
        });

        getData(view);


    }

    private void getData(View view) {
        ArrayList<DonReqModel> list = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("User_Donation_Info");

        String userId = FirebaseAuth.getInstance().getUid(); // replace with the actual user ID you want to query


        collectionReference.whereEqualTo("userId", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DonReqModel> donations = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DonReqModel donation = document.toObject(DonReqModel.class);
                        //do here
                        list.add(donation);
                        ////till here
                        donations.add(donation);
                    }

                    if(list.size()>0){
                        relativeLayout.setVisibility(View.INVISIBLE);
                    }else{
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    historyAdaptor = new HistoryAdaptor(list,getActivity());
                    recyclerView.setAdapter(historyAdaptor);
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    // do something with the list of donations for this user ID
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });



    }

    private void handleClicks(View view) {






    }

}