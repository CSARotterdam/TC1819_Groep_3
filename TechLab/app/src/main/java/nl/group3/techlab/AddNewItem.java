package nl.group3.techlab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import nl.group3.techlab.databases.DatabaseHelper;
import nl.group3.techlab.statistic;

public class AddNewItem extends AppCompatActivity {
    EditText eItem, eItemcat, eItemdes, eItemq;
    Button btnAdd;
    DatabaseHelper myDB;
    static int totalQuantity;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    int intAV;
    int intLE;
    int inttot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        setContentView(R.layout.add_item);
        setTitle(R.string.product_toevoegen);

        sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        intAV = sharedPreferences.getInt("AV", 0);
        intLE = sharedPreferences.getInt("LE", 0);
        inttot = sharedPreferences.getInt("TOT", 0);



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
                    totalQuantity += fQuan;
                    editor.putInt("AV", intAV+=fQuan);
                    editor.apply();
                } else{
                    Toast.makeText(AddNewItem.this, getString(R.string.alle_velden_invullen), Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(AddNewItem.this, ItemsAndMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void AddData(String Item, String Categorie, String Description, int fQuan){
        boolean insertData = myDB.addData(Item,Categorie,Description,fQuan);
        if (insertData){
            Toast.makeText(AddNewItem.this, getString(R.string.product_toegevoegd), Toast.LENGTH_LONG).show();
        } else{
                Toast.makeText(AddNewItem.this, getString(R.string.pruduct_toevoegen_mislukt), Toast.LENGTH_LONG).show();

        }
    }

    static public int getTotalQuantity(){
        return totalQuantity;
    }
}
