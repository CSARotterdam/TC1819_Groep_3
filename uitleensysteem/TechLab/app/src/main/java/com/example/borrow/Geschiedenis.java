package com.example.borrow;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.borrow.database.BorrowDatabase;
import com.example.borrow.view.Adpter;
import com.example.borrow.view.ViewItemContents;

import java.util.ArrayList;


public class Geschiedenis extends AppCompatActivity {
    BorrowDatabase mydb;
    private Adpter adpter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrow_list);
        ListView listView=(ListView)findViewById(R.id.List_view);
        mydb=new BorrowDatabase(this);
        ArrayList<Row_list> thelist = new ArrayList<>();
        Cursor data = mydb.getListContents();
        if (data.getCount() == 0 ){
            Toast.makeText(Geschiedenis.this, "The database was empty", Toast.LENGTH_LONG).show();}
        else {
            while (data.moveToNext())
            {
                String Itemname= (data.getString(1));
                String Description = (data.getString(2));
                thelist.add(new Row_list(Itemname,Description));
//                thelist.add(data.getString(Itemname,Description));
                adpter = new Adpter(this,thelist);
//                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, thelist);
                listView.setAdapter(adpter);
            }
        }

    }
}




