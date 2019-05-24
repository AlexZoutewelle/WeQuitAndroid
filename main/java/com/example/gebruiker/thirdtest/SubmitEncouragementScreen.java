package com.example.gebruiker.thirdtest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SubmitEncouragementScreen extends AppCompatActivity {

    private Button submitButton;
    private EditText encouragement;


    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseEncouragements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_encouragement_screen);

        submitButton = (Button)findViewById(R.id.submitButton);
        encouragement = (EditText)findViewById(R.id.writeEncouragement);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseEncouragements = mFirebaseDatabase.getReference("Encouragements");

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){

                }
                else{
                    Toast.makeText(SubmitEncouragementScreen.this, "User is signed out", Toast.LENGTH_SHORT).show();
                    sendToLogin();
                }
            }
        };

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitEncouragement();
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
        Intent logIntent = new Intent(SubmitEncouragementScreen.this, LoginScreen.class);
        startActivity(logIntent);
        finish();
    }
    private void submitEncouragement(){
        String text = encouragement.getText().toString().trim();
        if(!text.equals("")){
            FirebaseUser user = mAuth.getCurrentUser();
            String userId = user.getUid();
            String postID = mDatabaseEncouragements.push().getKey();

            Encouragement Post = new Encouragement(text, userId);

            mDatabaseEncouragements.child(postID).setValue(Post);

            Toast.makeText(this, "Encouragement added", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Encouragement is empty!", Toast.LENGTH_SHORT).show();
        }
    }
}
