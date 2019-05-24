package com.example.gebruiker.thirdtest;


import java.util.Date;

public class Encouragement {
    private String text;
    private String userID;
    private String dateTime;
    private int upvotes;

    public Encouragement(){

    }

    Encouragement(String text, String userID){
        this.text = text;
        this.userID = userID;
        this.upvotes = 0;
        this.dateTime = new Date().toString();
    }

    Encouragement(String text, String userID, int upvotes, String dateTime){
        this.text = text;
        this.userID = userID;
        this.upvotes = upvotes;
        this.dateTime = dateTime;

    }

    public String getText() {
        return text;
    }

    public String getUserID() {
        return userID;
    }

    public int getUpvotes() {
        return upvotes; }

    public String getDateTime(){
        return dateTime;
    }
}
