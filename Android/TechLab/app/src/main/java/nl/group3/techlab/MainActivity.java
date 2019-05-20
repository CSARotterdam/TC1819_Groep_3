package nl.group3.techlab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nl.group3.techlab.database.FillDatabase;
import nl.group3.techlab.database.ItemDatabaseHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_item);
        ItemDatabaseHelper myDB = new ItemDatabaseHelper(this);
        FillDatabase.FillDatabaseWithTestData(myDB);

        Intent i = new Intent(getBaseContext(), ReturnItemActivity.class);
        startActivity(i);
        finish();
    }
}