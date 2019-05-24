package com.example.gebruiker.thirdtest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class ReadEncouragementScreen extends AppCompatActivity {

    private Button mainMenuButton;
    private Button submitEncouragementButton;
    private Button upvoteButton;

    private TextView EncouragementTxt;
    private TextView EncouragementUser;
    private TextView EncouragementDate;
    private TextView EncouragementUpvotes;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseEncouragements;

    private Encouragement theChosenOne;
    private String chosenKey;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encouragement_screen);

        mainMenuButton = (Button)findViewById(R.id.BackToMainButton);
        submitEncouragementButton = (Button)findViewById(R.id.SubmitEncouragementButton);
        upvoteButton = (Button)findViewById(R.id.upVoteButton);
        EncouragementTxt = (TextView)findViewById(R.id.EncouragementTextView);
        EncouragementUser = (TextView)findViewById(R.id.encSubmittedBy);
        EncouragementDate = (TextView)findViewById(R.id.encSubmittedDate);
        EncouragementUpvotes = (TextView)findViewById(R.id.encSubmittedUpvotes);

        chosenKey = "";

        mAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseEncouragements = mFirebaseDatabase.getReference("Encouragements");

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    //Toast.makeText(ReadEncouragementScreen.this, "User is signed out", Toast.LENGTH_SHORT).show();
                    sendToLogin();
                }else{
                }
            }
        };


        //Gets the Encouragement to be displayed on screen.
        mDatabaseEncouragements.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                displayEncouragement(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //upvote
        upvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upvoteEncouragement();
            }
        });

        //navigation
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ReadEncouragementScreen.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

        submitEncouragementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent submitIntent = new Intent(ReadEncouragementScreen.this, SubmitEncouragementScreen.class);
                startActivity(submitIntent);
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
    public void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    private void displayEncouragement(DataSnapshot dataSnapshot){

        long childrenCount = dataSnapshot.getChildrenCount();
        Toast.makeText(ReadEncouragementScreen.this, "total amount of encouragements: " + childrenCount, Toast.LENGTH_LONG).show();

        int count = (int)childrenCount;
        int randomNumber = new Random().nextInt(count);
        int i = 0;
        for(DataSnapshot EncouragementSnapshot : dataSnapshot.getChildren()){
            i = i + 1;
            if(i == randomNumber){
                Encouragement theChosenOne = EncouragementSnapshot.getValue(Encouragement.class);
                String tekst = theChosenOne.getText();
                EncouragementTxt.setText(tekst);
                String datetext = "Submitted on: " + theChosenOne.getDateTime();
                EncouragementDate.setText(datetext);
                String usertext =  "Submitted by: " + theChosenOne.getUserID();
                EncouragementUser.setText(usertext);
                String upvotes = "Current amount of upvotes: " + theChosenOne.getUpvotes();
                EncouragementUpvotes.setText(upvotes);
                //chosenKey = EncouragementSnapshot.getKey();
                break;
            }
        }
    }

    private void upvoteEncouragement(){
        //
    }

    public void sendToLogin(){
        Intent logIntent = new Intent(ReadEncouragementScreen.this, LoginScreen.class);
        startActivity(logIntent);
        finish();
    }
}
