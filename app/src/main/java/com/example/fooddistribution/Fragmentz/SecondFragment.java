package com.example.fooddistribution.Fragmentz;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fooddistribution.Activities.MainActivity;
import com.example.fooddistribution.AndroidConstants.GpsTracker;
import com.example.fooddistribution.AndroidConstants.SaveSharedPreference;
import com.example.fooddistribution.Models.DonReqModel;
import com.example.fooddistribution.Models.UserModel;
import com.example.fooddistribution.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class SecondFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference firebaseStorage;

    private Uri foodImage;

    private SeekBar sBar;
    private TextView seekBarTv;
    private TextView edit;
    private ImageView foodImgClear;

    private Dialog mDialog;
    TextView yes;
    TextView no;
    TextView text;

    private UserModel model;

    private CheckBox checkBox;

    private ImageView imageFood;
    private LinearLayout imageClickLL;
    private Uri imageUri = null;
    private String foodImageUrl;

    private FrameLayout pbLayout;


    private ImageView userImage;
    private TextView userName;
    private String userLong,userLat;
    private EditText foodDetails;
    private int pval = 20;
    private TextView timeText;
    private EditText pickupLocation;
    private EditText pincode;

    private ImageView cross;

    private Button donateBt;
    private TextView donateTv;

    private String longitudeTxt,latitudeTxt;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        try {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

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

        yes.setVisibility(View.GONE);
        text.setText("\nDonation Successfull!!\n");
        no.setText("Okay");
        no.setOnClickListener(t->{
            mDialog.dismiss();
            ClearFields();
//            Fragment fragment = new FirstFragment();
//            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.idFragContainer, fragment).commit();
//            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.idBotNavi);
//            MenuItem newMenuItem = bottomNavigationView.getMenu().findItem(R.id.idBtmNavHome);
//            newMenuItem.setChecked(true);
        });


        sBar = (SeekBar) view.findViewById(R.id.seekBar1);
        seekBarTv = (TextView) view.findViewById(R.id.seekBarPersonNumTV);
        edit = (TextView) view.findViewById(R.id.timepickerEditTV);
        timeText = (TextView) view.findViewById(R.id.timeTextTV);
        pickupLocation = view.findViewById(R.id.pickupAddressET);
        foodDetails = view.findViewById(R.id.foodDetailsDonateET);
        pincode = view.findViewById(R.id.pincodeFragSecondEt);
        userImage = view.findViewById(R.id.profileImgDonateIV);
        userName = view.findViewById(R.id.userNameDonateTv);
        pbLayout = view.findViewById(R.id.progress_layout);
        cross = view.findViewById(R.id.crossBtDonateIV);



        //firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance().getReference();

        donateBt = view.findViewById(R.id.donateBtFragSecondBt);
        donateTv = view.findViewById(R.id.doneBtFragmentSecondTV);

        imageFood = view.findViewById(R.id.imageHereDonateIV);
        imageClickLL = view.findViewById(R.id.toHideLL);
        foodImgClear = view.findViewById(R.id.foodImageClearIV);

        foodImgClear.setOnClickListener(t ->{
            SaveSharedPreference.setFoodImage(getContext(),null);
            imageFood.setImageResource(0);
        });


        foodImage = SaveSharedPreference.getFoodImage(getContext());
        if(foodImage != null){
            imageFood.setImageURI(foodImage);
            foodImgClear.setVisibility(View.VISIBLE);
        }


        //latlong
        getLocationGPS(false);


        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getContext());


        seekBarTv.setText(sBar.getProgress() + " " + "person");

        checkBox = view.findViewById(R.id.checkboxGetCurrLocationCB);

        HandleSavePref();

        cross.setOnClickListener(t ->{
            getActivity().onBackPressed();
        });


        //Upload Image
        imageClickLL.setOnClickListener(t->{
            ImagePicker.with(this)
                    .crop()                                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)                        //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)        //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });

        //Location and Address
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    pbLayout.setVisibility(View.VISIBLE);
                    //disable touch response
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    getLocationGPS(true);

                }else{
                    pickupLocation.setText("");
                    pincode.setText("");
                }
            }
        });



        //Seek Bar
        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
                seekBarTv.setText(pval + " " + "person");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                seekBarTv.setText(pval + " " + "person");
            }
        });

        //Time Selection
        edit.setOnClickListener(t ->{

            // Get the current time
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            // Create a time picker dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    getContext(), // context
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            // Convert the 24-hour format to 12-hour format
                            int hour = hourOfDay % 12;
                            if (hour == 0) {
                                hour = 12;
                            }
                            String amPm = hourOfDay < 12 ? "AM" : "PM";
                            // Format the time as a string
                            String timeString = String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, amPm);
                            // This code will be executed when the user selects a time
                            timeText.setText(timeString);
                        }
                    }, // listener
                    hour, // initial hour
                    minute, // initial minute
                    false // is24HourView
            );

            // Show the time picker dialog
            timePickerDialog.show();
        });

        donateBt.setOnClickListener(t ->{
            donatePost();
        });
        donateTv.setOnClickListener(t ->{
            donatePost();
        });



    }

    private void ClearFields() {
        imageFood.setImageResource(0);
        foodDetails.setText("");
        pickupLocation.setText("");
        pincode.setText("");
        timeText.setText("00:00 PM");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sBar.setProgress(sBar.getMin());
        }
        checkBox.setChecked(false);
        seekBarTv.setText("20 person");
        SaveSharedPreference.setFoodImage(getContext(),null);
    }

    private void getLocationGPS(boolean setOrNot) {
        GpsTracker gpsTracker = new GpsTracker(getActivity());
        double latitude = 0;
        double longitude = 0;

        if(gpsTracker.canGetLocation()){
            latitude= gpsTracker.getLatitude();
            longitude= gpsTracker.getLongitude();
            userLat = latitude+"";
            userLong = longitude+"";
        }else{
            gpsTracker.showSettingsAlert();
        }

        if(setOrNot){
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = null;

            try {
                // Get the address from the latitude and longitude
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {

                    pickupLocation.setText(addresses.get(0).getAddressLine(0));
                    pincode.setText(addresses.get(0).getPostalCode());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            pbLayout.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }




    }

    private void HandleSavePref() {
        model = SaveSharedPreference.getUserModel(getContext());
        pickupLocation.setText(model.getAddress());
        pincode.setText(model.getPincode());
        userName.setText(model.getFname()+(model.getLname().equals("")?"":" "+model.getLname()));
        Glide.with(getContext())
                .load(model.profileImage)
                .placeholder(R.drawable.profile_image) // Replace with your placeholder drawable
                .into(userImage);
    }

    private void donatePost() {
        if(!validateData())return;
        String fooddet = foodDetails.getText().toString();
        String quantity = pval+"";
        String cookinTime = timeText.getText().toString();
        String pickupAddress = pickupLocation.getText().toString();
        String pinc = pincode.getText().toString();

        pbLayout.setVisibility(View.VISIBLE);
        //disable touch response
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mDialog.setCancelable(true);

        if(foodImage != null){
            imageUri = foodImage;
        }

        if(imageUri == null){
             DonReqModel donModel = new DonReqModel(mAuth.getUid(),model,"",fooddet,cookinTime,quantity,pickupAddress,"Not Accepted",userLat,userLong,System.currentTimeMillis(),null);

            //UploadCode
            CollectionReference collectionReference = firebaseFirestore.collection("User_Donation_Info");
            collectionReference.add(donModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    pbLayout.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if(task.isSuccessful()){
                        mDialog.show();
                    }
                }
            }).addOnFailureListener(e -> {
                pbLayout.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(getContext(),"something went Wrong!!! "+ e.getMessage(),Toast.LENGTH_LONG).show();
            });
            //till here-------------------------
        }else{
            final StorageReference fileRef = firebaseStorage.child("ProfileImages").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
//                                progressBar.setVisibility(View.INVISIBLE);
                            foodImageUrl = uri.toString();
                            DonReqModel donModel = new DonReqModel(mAuth.getUid(),model,foodImageUrl,fooddet,cookinTime,quantity,pickupAddress,"Not Accepted",userLat,userLong,System.currentTimeMillis(),null);
                            Log.d("imageurlUp", uri.toString());
                            //UploadCode
//                            CollectionReference collectionReference = firebaseFirestore.collection("DonationZ").document(mAuth.getUid()).collection("User_Donation_Info");
                            CollectionReference collectionReference = firebaseFirestore.collection("User_Donation_Info");
                            collectionReference.add(donModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    pbLayout.setVisibility(View.GONE);
                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    if(task.isSuccessful()){
                                        mDialog.show();
                                    }
                                }
                            }).addOnFailureListener(e -> {
                                Toast.makeText(getContext(),"something went Wrong!!! "+ e.getMessage(),Toast.LENGTH_LONG).show();
                                pbLayout.setVisibility(View.GONE);
                                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            });
                            //till here-------------------------
                        }
                    });
                }
            });

        }






    }

    private boolean validateData() {
        String fooddet = foodDetails.getText().toString();
        String quantity = pval+"";
        String cookinTime = timeText.getText().toString();
        String pickupAddress = pickupLocation.getText().toString();
        String pinc = pincode.getText().toString();
        boolean ticket = true;

        if(fooddet.isEmpty()){
            foodDetails.setError("Enter Food Details!!");
            foodDetails.requestFocus();
            ticket = false;
        }else {
            foodDetails.setError(null);
        }
        if(pickupAddress.isEmpty()){
            pickupLocation.setError("Enter PickUp Address!!");
            pickupLocation.requestFocus();
            ticket = false;
        }else {
            pickupLocation.setError(null);
        }

        if(pinc.isEmpty() || !isPincodeValid(pinc)){
            pincode.setError("Enter Valid Pin Code!!");
            pincode.requestFocus();
            ticket = false;
        }else {
            pincode.setError(null);
        }
        return ticket;
    }
    public static boolean isPincodeValid(String pincode) {
        String pincodePattern = "^[1-9][0-9]{5}$"; // Regular expression pattern for 6 digit Indian Pincode
        return pincode.matches(pincodePattern);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            if (data != null) {
                Uri uri = data.getData();
                SaveSharedPreference.setFoodImage(getContext(),uri);
                Log.d("IMage", uri.toString());
                // Use Uri object instead of File to avoid storage permissions

                imageUri = uri;
                imageFood.setImageURI(uri);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

}