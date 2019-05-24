package com.example.gebruiker.thirdtest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NewUser extends AppCompatActivity {

    private Button createAccount;
    private Button goToLogin;

    private EditText txtEmail;
    private EditText txtPassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user_screen);

        txtEmail = (EditText)findViewById(R.id.inputNewEmail);
        txtPassword = (EditText)findViewById(R.id.inputNewPassword);
        createAccount = (Button)findViewById(R.id.createAccountButton);
        goToLogin = (Button)findViewById(R.id.goToLoginButton);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Toast.makeText(NewUser.this, "User is signed in", Toast.LENGTH_SHORT).show();
                    sendToMain();
                }
                else{
                }
            }
        };

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToLogin();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                if (!email.equals("") && !password.equals("")) {
                    //check if password == confirmation password
                    //start progressbar
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(NewUser.this, "User created", Toast.LENGTH_SHORT).show();
                                sendToLogin();
                            }
                            else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(NewUser.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                            //progressbar invisible
                        }
                    });
                }
                else{
                    Toast.makeText(NewUser.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onStart() {
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
    private void sendToLogin(){
        Intent logIntent = new Intent(NewUser.this, LoginScreen.class);
        startActivity(logIntent);
        finish();
    }

    private void sendToMain(){
        Intent mainIntent = new Intent(NewUser.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}