package com.example.fooddistribution.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddistribution.AndroidConstants.MyPermission;
import com.example.fooddistribution.AndroidConstants.noInternetDialog;

import com.example.fooddistribution.Models.UserModel;
import com.example.fooddistribution.Models.ImageModel;
import com.example.fooddistribution.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity
{
    private ProgressBar progressBar;

    private EditText email,password1,password2,firstName,lastName,contact,address,pincode,city,state,country;
    private TextView typeTv;
    private Dialog mDialog;
    private ImageView img;


    private Uri imageUri = null;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference firebaseStorage;
    private String userId;
    private String ImageUrl = "";


    TextView yes;
    TextView no;
    TextView text;


    String type="";

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        MyPermission.checkAndRequestPermissions(this);
        type = getIntent().getStringExtra("userType");
        initFun();
    }
    private void initFun ()
    {
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.logout_popup);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        no = mDialog.findViewById(R.id.noTV);
        yes = mDialog.findViewById(R.id.yesTV);
        text = mDialog.findViewById(R.id.textTV);
        yes.setOnClickListener(view ->{
            mDialog.dismiss();
        });
        no.setOnClickListener(view ->{
            //startActivity(new Intent(RegisterActivity.this,RegisterActivity.class));
            finish();
        });

        //firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userId = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance().getReference();

        //Edit Text
        email = findViewById(R.id.emailEtRegister);
        password1 = findViewById(R.id.passwordEtRegister1);
        password2 = findViewById(R.id.passwordEtRegister2);
        firstName = findViewById(R.id.firstNameEtRegister);
        lastName = findViewById(R.id.lastNameEtRegister);
        contact = findViewById(R.id.phoneNumberEtRegister);
        address = findViewById(R.id.addressEtRegister);
        pincode = findViewById(R.id.pincodeEtRegister);
        city = findViewById(R.id.cityEtRegister);
        state = findViewById(R.id.stateEtRegister);
        country = findViewById(R.id.countryEtRegister);

        //TitleTextView
        typeTv = findViewById(R.id.typeTVactiregister);
        typeTv.setText(" as a "+type);

        //progressBar
        progressBar = findViewById(R.id.progressBarRegister);


        //Layouts
        LinearLayout cred = findViewById(R.id.credentials);
        LinearLayout det = findViewById(R.id.personalDetails);
        LinearLayout add = findViewById(R.id.address);

        cred.setVisibility(View.VISIBLE);
        det.setVisibility(View.INVISIBLE);
        add.setVisibility(View.INVISIBLE);

        ImageView proceed1 = findViewById(R.id.proceed1);
        proceed1.setOnClickListener(view -> {
            if(validate1() && cred.isShown()){
                cred.setVisibility(View.INVISIBLE);
                det.setVisibility(View.VISIBLE);
            }
        });

        //ImageView
        img = findViewById(R.id.profilepicIVRegister);
        img.setOnClickListener(i -> {
            ImagePicker.with(this)
                    .crop()                                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)                        //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)        //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });


        ImageView proceed2 = findViewById(R.id.proceed2);
        proceed2.setOnClickListener(view -> {
            if(det.isShown())
            {
                det.setVisibility(View.INVISIBLE);
                add.setVisibility(View.VISIBLE);
            }
        });
        ImageView receed2 = findViewById(R.id.receed2);
        receed2.setOnClickListener(view -> {
            validate1();
            if(det.isShown())
            {
                det.setVisibility(View.INVISIBLE);
                cred.setVisibility(View.VISIBLE);
            }

        });

        ImageView receed3 = findViewById(R.id.receed3);
        receed3.setOnClickListener(view -> {
            if(add.isShown())
            {
                add.setVisibility(View.INVISIBLE);
                det.setVisibility(View.VISIBLE);
            }

        });

        Button submitBt = findViewById(R.id.submitBtRegister);
        submitBt.setOnClickListener(view ->{

            Log.d("submitClicked", "clicked");
            noInternetDialog noInternet = new noInternetDialog(this);
            try {
                if(!noInternet.isConnected()){
                    noInternet.alertDailog();
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String em = email.getText().toString().trim();
            String pass = password1.getText().toString().trim();
            Log.d("emailandPass",em+" "+pass);
            progressBar.setVisibility(View.VISIBLE);
            //disable touch response
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mDialog.setCancelable(true);
            //actual registeration

            mAuth.createUserWithEmailAndPassword(em, pass)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Log.d("userCreated", "Reached database !!");
                            addDataFirebase();
                        }else{
                            Log.d("failedCreation","user creation failed");
                            text.setText(task.getException().getMessage());
                            no.setText("Try Again");
                            no.setVisibility(View.VISIBLE);
                            yes.setVisibility(View.GONE);
                            mDialog.show();
                        }
                        progressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    });


        });
        TextView loginBt = findViewById(R.id.loginbuttonTVRegister);
        loginBt.setOnClickListener(view ->{
            startActivity(new Intent(RegisterActivity.this, SignInActivity.class));
        });
    }



    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

    private void addDataFirebase() {
        String fname = firstName.getText().toString().trim();
        String lname = lastName.getText().toString().trim();
        String contactN = contact.getText().toString().trim();
        String dcity = city.getText().toString().trim();
        String dpincode = pincode.getText().toString().trim();
        String demail = email.getText().toString().trim();
        String dcountry = country.getText().toString().trim();
        String daddress = address.getText().toString().trim();
        String dstate = state.getText().toString().trim();
        String typeofuser = "donar";

        CollectionReference collectionReference = firebaseFirestore.collection(type).document(userId).collection("user_person_info");
        collectionReference.add(new UserModel(fname,lname,demail,contactN,daddress,dpincode,dcity,dstate,dcountry,typeofuser)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"data upload successfull",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(e -> Toast.makeText(RegisterActivity.this,"something went Wrong!!!",Toast.LENGTH_LONG).show());

    }

    private void addDataToFirebase() {
        String em = email.getText().toString().trim();
        Log.d("reachedDatabase", "Reached database !!");
        User user = new User();

        user.email = em;
        user.name = firstName.getText().toString().trim();
        user.Id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("insideAddData", "Success");
        if(imageUri == null){
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(RegisterActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
            user.profileImage = ImageUrl;
            reference.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Successful " , Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegisterActivity.this, "Something went wrong :: " + task.getException(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            setResult(RESULT_OK, new Intent());
            finish();
        }else{
            final StorageReference fileRef = firebaseStorage.child("ProfileImages").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            ImageModel model = new ImageModel(uri.toString());
                            String modelId = reference.push().getKey();
                            progressBar.setVisibility(View.INVISIBLE);
                            user.profileImage = uri.toString();
                            Toast.makeText(RegisterActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();

                            reference.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Successful " , Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(RegisterActivity.this, "Something went wrong :: " + task.getException(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            setResult(RESULT_OK, new Intent());
                            finish();
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(RegisterActivity.this, "Uploading Failed !!" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    reference.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Successful " , Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RegisterActivity.this, "Something went wrong :: " + task.getException(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
            });
        }
        Log.d("reachedEnd", "of firebase Data");
    }

    public boolean validate1(){
        String em = email.getText().toString().trim();
        String pass1 = password1.getText().toString().trim();
        String pass2 = password2.getText().toString().trim();


        if(em.isEmpty()){
            email.setError("Email is required!!");
            email.requestFocus();
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(em).matches()){
            email.setError("Please provide valid email!!");
            email.requestFocus();
            return false;
        }

        if(!pass1.equals(pass2)){
            password1.setError("Both Passwords should be same.");
            password2.setError("Both Passwords should be same.");
            password1.requestFocus();
            password2.requestFocus();
            return false;
        }
        if(pass1.isEmpty()){
            password1.setError("Password is required!!");
            password1.requestFocus();
            return false;
        }
        if(pass2.isEmpty()){
            password2.setError("Password is required!!");
            password2.requestFocus();
            return false;
        }
        if(password1.length() < 6) {
            password1.setError("Enter a valid password!!");
            password1.requestFocus();
            return false;
        }
        if(password2.length() < 6){
            password2.setError("Enter a valid password!!");
            password2.requestFocus();
            return false;
        }

        return true;

    }
    public boolean validate2(){
        String fname = firstName.getText().toString().trim();
        String lname = lastName.getText().toString().trim();
        String contactNum = contact.getText().toString().trim();
        if(fname.isEmpty()){
            firstName.setError("Email is required!!");
            firstName.requestFocus();
            return false;
        }
        if(lname.isEmpty()){
            firstName.setError("Email is required!!");
            firstName.requestFocus();
            return false;
        }
        if(contactNum.isEmpty()){
            firstName.setError("Email is required!!");
            firstName.requestFocus();
            return false;
        }
        return true;
    }
    public boolean validate3(){
        String addressText = address.getText().toString().trim();
        String pin = pincode.getText().toString().trim();
        String cityName = city.getText().toString().trim();
        String stateName = state.getText().toString().trim();
        String countryName = country.getText().toString().trim();
        if(addressText.isEmpty()){
            address.setError("Email is required!!");
            address.requestFocus();
            return false;
        }
        if(pin.isEmpty()){
            pincode.setError("Email is required!!");
            pincode.requestFocus();
            return false;
        }
        if(cityName.isEmpty()){
            city.setError("Email is required!!");
            city.requestFocus();
            return false;
        }
        if(stateName.isEmpty()){
            state.setError("Email is required!!");
            state.requestFocus();
            return false;
        }
        if(countryName.isEmpty()){
            country.setError("Email is required!!");
            country.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            if (data != null) {
                Uri uri = data.getData();
                Log.d("IMage", uri.toString());
                // Use Uri object instead of File to avoid storage permissions

                imageUri = uri;

                img.setImageURI(uri);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

}