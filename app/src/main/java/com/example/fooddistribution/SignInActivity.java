package com.example.fooddistribution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity
{
    EditText email,password;
    Button img;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth=FirebaseAuth.getInstance();
        initFun();

        email = findViewById(R.id.loginEmailId);
        password = findViewById(R.id.loginPassword);
        img = findViewById(R.id.loginButton);
        img.setOnClickListener(view -> {
            if (validateData()){
                login();
            }
        });
    }
    private void login() {
        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(SignInActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignInActivity.this,firstPage.class));


                    } else {
                        Toast.makeText(SignInActivity.this, "Entered details are not correct",
                                Toast.LENGTH_SHORT).show();

                    }
                });
    }
    private boolean validateData() {


        if (email.getText().toString().isEmpty()) {
            email.setError("Enter E-mail ID");
            return false;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError("Enter Password");
            return false;
        }


        return true;
    }
    private void initFun ()
    {
        TextView reg = findViewById(R.id.directToRegister);
        reg.setOnClickListener(view -> startActivity(new Intent(SignInActivity.this,RegisterActivity.class)));
    }
}