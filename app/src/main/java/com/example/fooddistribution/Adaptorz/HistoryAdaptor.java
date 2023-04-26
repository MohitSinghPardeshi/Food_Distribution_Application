package com.example.fooddistribution.Adaptorz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddistribution.Activities.StatusActivity;
import com.example.fooddistribution.Models.DonReqModel;
import com.example.fooddistribution.Models.HistoryModel;
import com.example.fooddistribution.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryAdaptor extends RecyclerView.Adapter<HistoryAdaptor.myHistoryViewHolder> {
    private List<DonReqModel> historyModelList;
    private Context context;


    public HistoryAdaptor(List<DonReqModel> mandiModelList, Context context) {
        this.historyModelList = mandiModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public myHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        myHistoryViewHolder viewHolder = new myHistoryViewHolder(view);
        return viewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull myHistoryViewHolder holder, int position) {
        DonReqModel donModel = historyModelList.get(position);
        Glide.with(context).load(donModel.getFoodImage()).error(R.drawable.foodplaceholder).into(holder.imageViewFood);

        holder.foodDetail_underImg.setText(donModel.getFoodName());
        holder.foodDetails.setText(donModel.getFoodName());
        holder.noOfPeople.setText("Food for "+donModel.getExpPepCnt()+" People.");
        holder.cookinTime.setText("Cooking Time: "+donModel.getCookTime());
        holder.location.setText("Location: "+donModel.getLoc());
        holder.status.setText(donModel.getStatus());

        holder.datentime.setText(holder.getDateNTime(donModel.getUploadTime()));

        holder.linearLayout.setOnClickListener(t->{

        });



//        holder.linearLayout.setOnLongClickListener(t ->{
//            holder.cardViewDelete.setVisibility(View.VISIBLE);
//            return true;
//        });

    }



    @Override
    public int getItemCount() {
        return historyModelList.size();
    }

    public class myHistoryViewHolder extends RecyclerView.ViewHolder {

        TextView foodDetail_underImg,foodDetails,noOfPeople,cookinTime,location,status,datentime;
        ImageView imageViewFood;
        CardView cardView;
        CardView cardViewDelete;
        LinearLayout linearLayout;
        LinearLayout linearLayoutMain;
        Dialog mDialog;
        TextView no;
        TextView yes;
        TextView text;

        private void setDialog(DonReqModel model){
            mDialog= new Dialog(context);
            mDialog.setContentView(R.layout.logout_popup);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            no = mDialog.findViewById(R.id.noTV);
            yes = mDialog.findViewById(R.id.yesTV);
            text = mDialog.findViewById(R.id.textTV);

            text.setText("Are You Sure You Want To Delete This Donation Permanently!!");
            yes.setText("Delete");
            no.setText("Cancel");
            yes.setTextColor(context.getResources().getColor(R.color.red));

            no.setOnClickListener(t->{
                mDialog.dismiss();
                if(cardViewDelete.getVisibility() == View.VISIBLE){
                    cardViewDelete.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayoutMain.setWeightSum(3);
                }
            });
            yes.setOnClickListener(t->{
                deleteDonation(model);
            });
            mDialog.show();
        }

        private String getDateNTime(long timeInMillis){
            Date date = new Date(timeInMillis);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy | HH:mm");
            String formattedDate = dateFormat.format(date);
            return formattedDate;
        }

        private void deleteDonation(DonReqModel model) {
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
                if(cardViewDelete.getVisibility() == View.VISIBLE){
                    cardViewDelete.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayoutMain.setWeightSum(3);
                }
                historyModelList.remove(getAbsoluteAdapterPosition());
                notifyDataSetChanged();
            }else{
                text.setText("Approved Request Cannot be Deleted");
                yes.setVisibility(View.GONE);
                mDialog.show();
                return;
            }

        }

        public myHistoryViewHolder(@NonNull View itemView) {
            super(itemView);


            foodDetail_underImg = itemView.findViewById(R.id.tvFoodDetail);
            foodDetails = itemView.findViewById(R.id.foodDetails);
            noOfPeople = itemView.findViewById(R.id.noOfPeopleTV);
            cookinTime = itemView.findViewById(R.id.cookinTimeTv);
            datentime = itemView.findViewById(R.id.dateTextHistoryItem);
            location =  itemView.findViewById(R.id.locatonTv);
            linearLayout = itemView.findViewById(R.id.statusHistoryLL);
            linearLayoutMain = itemView.findViewById(R.id.linearLayoutMainHistory);
            status = itemView.findViewById(R.id.statusHistoryTV);

            imageViewFood = itemView.findViewById(R.id.imageHistory);
            cardView = itemView.findViewById(R.id.cardViewHistory);
            cardViewDelete = itemView.findViewById(R.id.cardViewDelete);

            cardView.setOnClickListener(t ->{
                Intent intent = new Intent(context,StatusActivity.class);
                intent.putExtra("model",historyModelList.get(getAbsoluteAdapterPosition()));
                context.startActivity(intent);

            });
            cardView.setOnLongClickListener(t->{
                if(cardViewDelete.getVisibility() == View.GONE){
                    cardViewDelete.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    linearLayoutMain.setWeightSum(2);
                }else{
                    cardViewDelete.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayoutMain.setWeightSum(3);
                }
                return true;
            });

            cardViewDelete.setOnClickListener(t->{
                DonReqModel model = historyModelList.get(getAbsoluteAdapterPosition());
                setDialog(model);
            });
        }
    }


}




