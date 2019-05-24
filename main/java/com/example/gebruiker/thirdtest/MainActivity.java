package com.example.gebruiker.thirdtest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnEncouragement;
    private Button btnDailyTip;
    private Button btnGoToSubmit;
    private TextView txtUpper;
    private TextView txtLower;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(@Nullable Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_main);


        btnEncouragement = (Button) findViewById(R.id.EncouragementButton);
        btnDailyTip = (Button) findViewById(R.id.DailyTipButton);
        btnGoToSubmit = (Button)findViewById(R.id.gotosubmit);
        txtUpper = (TextView) findViewById(R.id.MainMenuUpperTxt);
        txtLower = (TextView) findViewById(R.id.MainMenuMiddleTxt);
        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!= null){
                }
                else{
                    Toast.makeText(MainActivity.this, "User is signed out", Toast.LENGTH_SHORT).show();
                    sendToLogin();
                }
            }
        };

        btnGoToSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SubIntent = new Intent(MainActivity.this, SubmitEncouragementScreen.class);
                startActivity(SubIntent);
                finish();
            }
        });

        btnEncouragement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EncIntent = new Intent(MainActivity.this, ReadEncouragementScreen.class);
                startActivity(EncIntent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_about:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_logout:
                logOut();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut(){
        mAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin(){
        Intent logIntent = new Intent(MainActivity.this, LoginScreen.class);
        startActivity(logIntent);
        finish();
    }
}

