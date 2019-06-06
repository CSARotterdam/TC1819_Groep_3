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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nl.group3.techlab.database.ItemDatabaseHelper;
import nl.group3.techlab.helpers.JSONHelper;
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


        final JsonObject borrowedItem = (JsonObject) new JsonParser().parse(getIntent().getStringExtra("BorrowedItemObject")).getAsJsonObject();

        TextView tvBorrowedBy = (TextView)findViewById(R.id.borrowedBy);
        TextView tvItemName = (TextView)findViewById(R.id.itemName);

        Button bCancelAction = (Button)findViewById(R.id.cancelAction);
        Button bReturnedAction = (Button)findViewById(R.id.returnAction);
        Button bBrokenAction = (Button)findViewById(R.id.brokenAction);

        tvBorrowedBy.setText(String.format(tvBorrowedBy.getText().toString(),
                borrowedItem.get("user").getAsJsonObject().get("email")));


        String jsonString = JSONHelper.JSONStringFromURL(String.format( "http://84.86.201.7:8000/api/v1/items/%s/", borrowedItem.get("item").getAsJsonObject().get("id").getAsString()), null, 1000, "GET", null);
        JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();


        tvItemName.setText(String.format(obj.get("title").getAsString()));

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
                finish();

                String jsonString = JSONHelper.JSONStringFromURL(String.format( "http://84.86.201.7:8000/api/v1/returnitems/%s/", borrowedItem.get("item").getAsJsonObject().get("id").getAsString()), null, 1000, "PUT", null);
                JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();


                if (obj.get("success").getAsString().equalsIgnoreCase("true")){
                    Toast.makeText(getBaseContext(), R.string.product_teruggebracht, Toast.LENGTH_LONG).show();

                    Intent i = new Intent(getBaseContext(), ReturnItemActivity.class);
                    startActivity(i);

                    finish();
                }

            }
        });

        bReturnedAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                String jsonString = JSONHelper.JSONStringFromURL(String.format( "http://84.86.201.7:8000/api/v1/returnitems/%s/", borrowedItem.get("item").getAsJsonObject().get("id").getAsString()), null, 1000, "PUT", null);
                JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();


                if (obj.get("success").getAsString().equalsIgnoreCase("true")){
                    Toast.makeText(getBaseContext(), R.string.product_teruggebracht, Toast.LENGTH_LONG).show();

                    Intent i = new Intent(getBaseContext(), ReturnItemActivity.class);
                    startActivity(i);

                    finish();
                }

            }
        });
    }
}
