package nl.group3.techlab.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import nl.group3.techlab.ItemEdit;
import nl.group3.techlab.models.BorrowItem;
import nl.group3.techlab.R;
import nl.group3.techlab.adapters.ListAdapterBorrowed;
import nl.group3.techlab.databases.DatabaseHelper;

import java.util.ArrayList;


public class ViewBorrowedItem extends AppCompatActivity {

    DatabaseHelper myDB;
    ArrayList<BorrowItem> BorrowedList;
    ListView listView;
    BorrowItem borrowItem;
    private static final String TAG = "ListAdapterBorrowed";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("Techlab", 0);
        int d_color = sharedPreferences.getInt("d_color", 1);
        switch (d_color) {
            case 1:
                setTheme(R.style.theme1);
                break;
            case 2:
                setTheme(R.style.theme2);
                break;
            default:
                break;
        }
        setContentView(R.layout.viewcontents_layout);

        myDB = new DatabaseHelper(this);

        BorrowedList = new ArrayList<>();
        Cursor data = myDB.GetBorrowedContents();
        int numRows = data.getCount();
        if (numRows == 0){
            Toast.makeText(ViewBorrowedItem.this, getString(R.string.geen_producten), Toast.LENGTH_LONG).show();
        }else{

            while(data.moveToNext()){
                borrowItem = new BorrowItem(data.getInt(1));
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
                int borrowText = borrowItem.getId();

                Cursor data = myDB.getBorrowedID(borrowText);
                int ID = -1;

                while(data.moveToNext()){
                    ID = data.getInt(0);
                }
                if (ID > -1){
                    Log.d(TAG, "onItemClick: The ID is: " + ID);
                    Intent editScreenIntent = new Intent(ViewBorrowedItem.this, ItemEdit.class);
                    editScreenIntent.putExtra("id", ID);
                    editScreenIntent.putExtra("ITEM", borrowText );
                    startActivity(editScreenIntent);
//                } else{
//                    Toast.makeText(view.getContext(), "No ID associated with that name",
//                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}