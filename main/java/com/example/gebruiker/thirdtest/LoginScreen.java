package com.example.gebruiker.thirdtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginScreen extends AppCompatActivity {

    private Button btnLogin;
    private Button btncreateNewAccount;
    private EditText loginEmailText;
    private EditText loginPasswordText;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        btnLogin = (Button)findViewById(R.id.loginButton);
        btncreateNewAccount = (Button)findViewById(R.id.createNewAccount);
        loginEmailText = (EditText)findViewById(R.id.inputemail);
        loginPasswordText = (EditText)findViewById(R.id.inputPassword);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if(user != null){
                Toast.makeText(LoginScreen.this, "User is signed in", Toast.LENGTH_SHORT).show();
                sendToMain();
            }
            else{
            }
            }
        };

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            String loginEmail = loginEmailText.getText().toString();
            String loginPassword = loginPasswordText.getText().toString();

            if (!loginEmail.equals("") && !loginPassword.equals("")) {

                //progressBar visible

                mAuth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendToMain();
                            Toast.makeText(LoginScreen.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(LoginScreen.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }

                        //progressBar invisible
                    }
                });

            }
            else{
                Toast.makeText(LoginScreen.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
            }
            }
        });

        btncreateNewAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent createIntent = new Intent(LoginScreen.this, NewUser.class);
                startActivity(createIntent);
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    private void sendToMain(){
        Intent mainIntent = new Intent(LoginScreen.this, MainActivity.class);
        startActivity(mainIntent);
    }
}



