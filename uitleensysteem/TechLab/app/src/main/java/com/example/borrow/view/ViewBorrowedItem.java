package com.example.borrow.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.borrow.BorrowItem;
import com.example.borrow.borrowItemContents;
import com.example.borrow.R;
import com.example.borrow.adapters.ListAdapterBorrowed;
import com.example.borrow.database.DatabaseHelper;

import java.util.ArrayList;


public class ViewBorrowedItem extends AppCompatActivity {

    DatabaseHelper myDB;
    ArrayList<BorrowItem> BorrowedList;
    ListView listView;
    BorrowItem borrowItem;
    private static final String TAG = "ListAdapterBorrowed";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcontents_layout);


        myDB = new DatabaseHelper(this);

        BorrowedList = new ArrayList<>();
        Cursor data = myDB.GetBorrowedContents();
        int numRows = data.getCount();
        if (numRows == 0){
            Toast.makeText(ViewBorrowedItem.this, "Database is empty", Toast.LENGTH_LONG).show();
        }else{

            while(data.moveToNext()){
                borrowItem = new BorrowItem( data.getInt(1));
                BorrowedList.add(borrowItem);
            }
            ListAdapterBorrowed adapter = new ListAdapterBorrowed(this, R.layout.borrowed_items, BorrowedList);
            listView =  findViewById(R.id.listView);
            listView.setAdapter(adapter);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {


                BorrowItem borrowItem  = (BorrowItem)adapterView.getItemAtPosition(i);
                int borrowText = borrowItem.getID();

                Toast.makeText(view.getContext(), "clicked on item[" + borrowText + "]",

                        Toast.LENGTH_SHORT).show();

                Log.d(TAG, "onItemClick You clicked on " + borrowText);

                Cursor data = myDB.getBorrowedID(borrowText);
                int ID = -1;


                while(data.moveToNext()){
                    ID = data.getInt(0);
                }
                if (ID > -1){
                    Log.d(TAG, "onItemClick: The ID is: " + ID);
                    Intent editScreenIntent = new Intent(ViewBorrowedItem.this, borrowItemContents.class);
                    editScreenIntent.putExtra("id", ID);
                    editScreenIntent.putExtra("ITEM", borrowText );
                    startActivity(editScreenIntent);

                }
                else{
                    Toast.makeText(view.getContext(), "no ID associated with that name",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}