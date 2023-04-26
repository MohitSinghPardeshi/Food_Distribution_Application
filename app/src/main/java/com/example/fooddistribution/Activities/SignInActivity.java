package com.example.fooddistribution.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddistribution.AndroidConstants.MyPermission;
import com.example.fooddistribution.AndroidConstants.SaveSharedPreference;
import com.example.fooddistribution.AndroidConstants.noInternetDialog;
import com.example.fooddistribution.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class SignInActivity extends AppCompatActivity {
    private Button login;
    private TextInputLayout email, password;
    private TextView createAccountTV, forgotPasswordTV;
    private ProgressBar progressBar;
    private Dialog mDialog;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference firebaseStorage;
    private Context context;
    TextView yes;
    TextView no;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (SaveSharedPreference.getUserName(SignInActivity.this).length() != 0) {
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initFun();
    }

    private void initFun() {
        MyPermission.checkAndRequestPermissions(this);
        //firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance().getReference();

        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.logout_popup);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        no = mDialog.findViewById(R.id.noTV);
        yes = mDialog.findViewById(R.id.yesTV);
        text = mDialog.findViewById(R.id.textTV);
        yes.setOnClickListener(viwe -> {
            mDialog.dismiss();
        });
        no.setOnClickListener(view -> {
            startActivity(new Intent(SignInActivity.this, RegisterActivity.class));
            finish();
        });

        forgotPasswordTV = findViewById(R.id.forgotPasswordTv);
        progressBar = findViewById(R.id.progressBarLogin);
        mAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.loginBt);
        user = mAuth.getCurrentUser();
        email = findViewById(R.id.emailEtLogin);
        password = findViewById(R.id.passwordEtLogin);
        createAccountTV = findViewById(R.id.createAccountTv);

        createAccountTV.setOnClickListener(view -> {
            //dialog popup before signin
            Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
//            intent.putExtra("userType", "donor");
            startActivity(intent);
//            beforeSignIn();

        });
        forgotPasswordTV.setOnClickListener(view -> {
            //startActivity(new Intent(SignInActivity.this,ForgotPassword.class));
        });


        login.setOnClickListener(view -> {
            try {
                loginFun();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void beforeSignIn() {
        Log.d("inbeforesignin", "success??");
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.signin_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LinearLayout donor = dialog.findViewById(R.id.donorLLpopup);
        LinearLayout charity = dialog.findViewById(R.id.charityLLpopup);
        LinearLayout volunteer = dialog.findViewById(R.id.volunteerLLpopup);

        Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);

        donor.setOnClickListener(view -> {
            intent.putExtra("userType", "donor");
            startActivity(intent);
        });
        charity.setOnClickListener(view -> {
            intent.putExtra("userType", "charity");
            startActivity(intent);
        });
        volunteer.setOnClickListener(view -> {
            intent.putExtra("userType", "volunteer");
            startActivity(intent);
        });

        dialog.show();
    }

    private void loginFun() throws IOException, InterruptedException {
//        noInternetDialog noInternet = new noInternetDialog(this);
//        if(!noInternet.isConnected()){
//            noInternet.alertDailog();
//            return;
//        }

        String em = email.getEditText().getText().toString().trim();
        String pass = password.getEditText().getText().toString().trim();

        Boolean ticket = true;

        if (em.isEmpty()) {
            email.setError("Email is required!!");
            email.requestFocus();
            ticket = false;
        } else {
            email.setError(null);
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(em).matches()) {
            email.setError("Please provide valid email!!");
            email.requestFocus();
            ticket = false;
        } else {
            email.setError(null);
        }
        if (pass.isEmpty()) {
            password.setError("Password is required!!");
            password.requestFocus();
            ticket = false;
        } else {
            password.setError(null);
        }
        if (pass.length() < 6) {
            password.setError("Enter a valid password!!");
            password.requestFocus();
            ticket = false;
        } else {
            password.setError(null);
        }

        if (!ticket) return;

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mDialog.setCancelable(true);



        mAuth.signInWithEmailAndPassword(em, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Boolean donorDataExists = false;

                            CollectionReference collectionReference = firebaseFirestore.collection("donor").document(mAuth.getUid()).collection("user_person_info");
                            collectionReference.get()
                                    .addOnCompleteListener(taska -> {
                                        if (taska.isSuccessful()) {
                                            if (taska.getResult().isEmpty()) {
                                                // The collection does not exist
                                                text.setText("You Are Registered As A NGO. \n Register As a Donor First Before Login.");
                                                no.setText("Ok");
                                                no.setVisibility(View.VISIBLE);
                                                yes.setVisibility(View.GONE);
                                                no.setOnClickListener(t->{
                                                    mDialog.dismiss();
                                                    startActivity(new Intent(SignInActivity.this, RegisterActivity.class));
                                                });
                                                mDialog.show();
                                            } else {
                                                // The collection exists
                                                user = mAuth.getCurrentUser();
                                                if (user.isEmailVerified()) {

                                                    text.setText("Login\n" + "Successful.");
                                                    no.setText("Ok");
                                                    no.setVisibility(View.VISIBLE);
                                                    yes.setVisibility(View.GONE);
                                                    no.setOnClickListener(view -> {
                                                        Intent intents = new Intent(SignInActivity.this, MainActivity.class);
                                                        intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intents);
                                                        //setting username in shared pref;
                                                        SaveSharedPreference.setUserName(SignInActivity.this, mAuth.getCurrentUser().getUid());
                                                    });
                                                    mDialog.setCancelable(false);
                                                    mDialog.show();

                                                } else {

                                                    user.sendEmailVerification();
                                                    text.setText("Check Your Email\n" + "For Verification.");
                                                    yes.setText("Cancel");
                                                    no.setVisibility(View.GONE);
                                                    yes.setVisibility(View.VISIBLE);
                                                    mDialog.show();
                                                }
                                            }
                                        } else {
                                            // Handle the error
                                            text.setText("Donor Data Check Failed!!");
                                            no.setVisibility(View.GONE);
                                            yes.setText("Cancel");
                                            mDialog.show();
                                        }
                                    });




                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                text.setText("Can't find\n" + "account.");
                                yes.setText("Try Again");
                                no.setText("Sign Up");
                                no.setVisibility(View.VISIBLE);
                                yes.setVisibility(View.VISIBLE);
                                mDialog.show();


                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                email.setError("Invalid Credentials");
                                email.requestFocus();
                            } catch (Exception e) {
                                text.setText("Login\n" + "Failed !!\n (Check Internet Connection)");
                                no.setVisibility(View.GONE);
                                yes.setText("Cancel");
                                mDialog.show();
                            }

                        }
                        progressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });
    }


}