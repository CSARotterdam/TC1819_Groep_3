package com.example.borrow;
import android.view.View;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.borrow.database.DatabaseHelper;
import com.example.borrow.models.Item;
import com.example.borrow.view.ViewBorrowedItem;
import com.example.borrow.view.ViewItemContents;


public class borrowItemContents extends AppCompatActivity {
    private static final String TAG = "borrowItemContents";

    private Button Borrow;
    private FloatingActionButton delButton;
    private TextView eItem, eItemD, Bsk;
    private TextView ID;

    DatabaseHelper myDB;
    private String selectedName;
    private int selectedID;
    private int selectedquan;
    private String selectedDesc;


    @Override
    protected void onCreate(@Nullable Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.borrow_item_layout);
//        vBorrow = (Button) findViewById(R.id.vBorrow);
        Borrow = (Button) findViewById(R.id.Borrow);
        eItem = (TextView) findViewById(R.id.eItem);
        eItemD = (TextView) findViewById(R.id.eItemD);
        Bsk = (TextView) findViewById(R.id.Bsk);
        delButton = (FloatingActionButton) findViewById(R.id.delButton);

        myDB = new DatabaseHelper(this);
        ID = (TextView) findViewById(R.id.ID);

        Intent receivedIntent = getIntent();

        selectedID = receivedIntent.getIntExtra("id", -1);
        selectedName = receivedIntent.getStringExtra("ITEM");
        selectedquan = receivedIntent.getIntExtra("quantity", 0);
        selectedDesc = receivedIntent.getStringExtra("Description");


        eItem.setText(selectedName);
        eItemD.setText(selectedDesc);

        if (selectedquan > 0){
            Bsk.setText("Beschikbaar");
        }
        else {
            Bsk.setText("Niet beschikbaar");
        }

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDB.deleteName(selectedID, selectedName);
                eItem.setText("");
                toastMessage("removed from database");
                Intent intent = new Intent(borrowItemContents.this, ViewItemContents.class);
                startActivity(intent);
            }
        });





//
//        vBorrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(borrowItemContents.this, ViewBorrowedItem.class);
//                startActivity(intent);
//            }
//        });


        Borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (selectedquan >= 1) {
                    myDB.addBorrow(selectedID,selectedquan);

                    toastMessage("Item has been borrowed");
                }
                else{
                    toastMessage("Item is not available");

                }



            }
        });



    }
    private void toastMessage (String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}