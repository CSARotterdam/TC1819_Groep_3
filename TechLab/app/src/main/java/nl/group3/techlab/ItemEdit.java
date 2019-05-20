package nl.group3.techlab;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import nl.group3.techlab.databases.BorrowDatabase;
import nl.group3.techlab.databases.DatabaseHelper;
import nl.group3.techlab.models.Item;

import java.util.ArrayList;


public class ItemEdit extends AppCompatActivity {
    private static final String TAG = "ItemEdit";

    private Button Borrow;
    private FloatingActionButton delButton;
    private TextView eItem, eItemD, Bsk;
    private TextView ID;
    EditText eItemdes,eItemcat;
    DatabaseHelper myDB;
    private String selectedName;
    private int selectedID;
    BorrowDatabase db;

    private int selectedquan;
    private String selectedDesc;


    @Override
    protected void onCreate(@Nullable Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.borrow_item_layout);
//        vBorrow = (Button) findViewById(R.id.vBorrow);
        db = new BorrowDatabase(this);
        ArrayList<Item> theList = new ArrayList<>();
        Cursor data = db.getListContents();

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
                toastMessage("Removed from database");
                Intent intent = new Intent(ItemEdit.this, ItemsAndMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        vBorrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ItemEdit.this, ViewBorrowedItem.class);
//                startActivity(intent);
//            }
//        });


        Borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedquan >= 1 ) {
                    myDB.addBorrow(selectedID,selectedquan);
                    db.insertData(selectedName,selectedDesc);
//                    toastMessage("Data added");
                    toastMessage("Item has been borrowed");
                    startActivity(new Intent(ItemEdit.this, ItemsAndMenuActivity.class));
                    finish();
                } else {
                    toastMessage("Item is not available");
                }
            }
        });

    }
    private void toastMessage (String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
