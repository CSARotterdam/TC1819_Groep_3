package nl.group3.techlab;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import nl.group3.techlab.databases.BorrowDatabase;
import nl.group3.techlab.adapters.HistoryAdapter;
import nl.group3.techlab.models.RowListItem;

import java.util.ArrayList;


public class History extends AppCompatActivity {
    BorrowDatabase mydb;
    SharedPreferences sharedPreferences;

    private HistoryAdapter historyAdapter;
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
        setContentView(R.layout.borrow_list);
        ListView listView=(ListView)findViewById(R.id.List_view);
        mydb=new BorrowDatabase(this);
        ArrayList<RowListItem> thelist = new ArrayList<>();
        Cursor data = mydb.getListContents();
        if (data.getCount() != 0 ){
            while (data.moveToNext())
            {
                String Itemname = (data.getString(1));
                String Description = (data.getString(2));
                thelist.add(new RowListItem(Itemname,Description));
                historyAdapter = new HistoryAdapter(this,thelist);
                listView.setAdapter(historyAdapter);
            }
        }

    }
}




