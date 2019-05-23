package nl.group3.techlab;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import nl.group3.techlab.database.ItemDatabaseHelper;
import nl.group3.techlab.models.BorrowItem;
import nl.group3.techlab.models.StockItem;

public class HandInConfirmation extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    int intAV;
    int intLE;
    int inttot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_in_confirmation);

        final BorrowItem borrowedItem = (BorrowItem)getIntent().getSerializableExtra("BorrowedItemObject");

        TextView tvBorrowedBy = (TextView)findViewById(R.id.borrowedBy);
        TextView tvItemName = (TextView)findViewById(R.id.itemName);

        Button bCancelAction = (Button)findViewById(R.id.cancelAction);
        Button bReturnedAction = (Button)findViewById(R.id.returnAction);
        Button bBrokenAction = (Button)findViewById(R.id.brokenAction);

        tvBorrowedBy.setText(String.format(tvBorrowedBy.getText().toString(),
                borrowedItem.getUser().getFirstName() + " " + borrowedItem.getUser().getLastName()));

        tvItemName.setText(String.format(borrowedItem.getItem().getName()));

        bCancelAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK, new Intent());

                Intent i = new Intent(getBaseContext(), ReturnItemActivity.class);
                startActivity(i);

                finish();
            }
        });

        bBrokenAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Het voorwerp is teruggebracht.", Toast.LENGTH_LONG).show();
                ItemDatabaseHelper myDB = new ItemDatabaseHelper(getBaseContext());
                Log.d("Logger", myDB.UpdateBorrowedItem(borrowedItem) + "");

                borrowedItem.getUser().addBroken();

                StockItem stockItem = myDB.getStockItem(borrowedItem.getItem());
                stockItem.addOneBroken();
                myDB.UpdateStockItem(stockItem);
                setResult(Activity.RESULT_OK, new Intent());

                Intent i = new Intent(getBaseContext(), ReturnItemActivity.class);
                startActivity(i);

                finish();
            }
        });

        bReturnedAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(getBaseContext(), "Het voorwerp is teruggebracht.", Toast.LENGTH_LONG).show();
                ItemDatabaseHelper myDB = new ItemDatabaseHelper(getBaseContext());
                Log.d("Logger", myDB.UpdateBorrowedItem(borrowedItem) + "");

                AddNewItem.totalQuantity += 1;
                ItemEdit.loanQuantity -= 1;
                editor.putInt("AV", intAV+=1);
                editor.putInt("AV", intLE-=1);
                editor.apply();

                StockItem stockItem = myDB.getStockItem(borrowedItem.getItem());
                stockItem.addOneStock();
                myDB.UpdateStockItem(stockItem);
                setResult(Activity.RESULT_OK, new Intent());

                Intent i = new Intent(getBaseContext(), ReturnItemActivity.class);
                startActivity(i);

                finish();

            }
        });
    }
}
