package com.example.borrow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.borrow.database.DatabaseHelper;
import com.example.borrow.view.ViewItemContents;

public class addItem extends AppCompatActivity {

    EditText eItem, eItemcat, eItemdes, eItemq;
    Button btnAdd;
    DatabaseHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        eItem = (EditText) findViewById(R.id.eItem);
        eItemcat = (EditText) findViewById(R.id.eItemcat);
        eItemdes = (EditText) findViewById(R.id.eItemdes);
        eItemq = (EditText) findViewById(R.id.eItemq);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        myDB = new DatabaseHelper(this);



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String fItem = eItem.getText().toString();
                String fCat = eItemcat.getText().toString();
                String fDes = eItemdes.getText().toString();
                int fQuan = Integer.parseInt(eItemq.getText().toString());

                if(fItem.length() != 0 && fCat.length() != 0 && fDes.length() != 0){
                    AddData(fItem, fCat, fDes, fQuan);
                    eItem.setText("");
                    eItemcat.setText("");
                    eItemdes.setText("");
                    eItemq.setText("");



                }else{
                    Toast.makeText(addItem.this, "You must put something in all fields!", Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(addItem.this, ViewItemContents.class);
                startActivity(intent);

            }
        });
    }
    public void AddData(String Item, String Categorie, String Description, int fQuan){
        boolean insertData = myDB.addData(Item,Categorie,Description,fQuan);
        if (insertData){
            Toast.makeText(addItem.this, "Item has been added", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(addItem.this, "Adding Item Failed please check your connection", Toast.LENGTH_LONG).show();

        }
    }
}
