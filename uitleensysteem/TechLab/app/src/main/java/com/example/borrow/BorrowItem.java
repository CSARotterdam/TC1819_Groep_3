package com.example.borrow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.borrow.models.User;

public class BorrowItem {
//    public String firstName;
    public int ItemID;



    public BorrowItem(int fID){
        ItemID = fID;
//        firstName = user;



    }
    public int getID() {
        return ItemID;
    }

//    public String getFirstName() {
//        return firstName;
//    }





}
