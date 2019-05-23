package nl.group3.techlab.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BorrowDatabase  extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyDATABASE.db";
    private static final int version = 2;
    public static String item_name;
    public static String item_description;


    public BorrowDatabase(Context context) {
        super(context, DATABASE_NAME , null, version);
//    SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    //Creating the database
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + "Borrow" + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, item_name TEXT, item_description TEXT  )");

    }

    @Override
    //To update the database, when database version is upgraded, onUpgrade is called
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "Borrow");
        onCreate(db);


    }
    //putting data in the database
    public boolean insertData(String item_name,  String item_description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("item_name", item_name);
        contentValues.put("item_description", item_description);
        long result = db.insert("Borrow", null, contentValues);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }

    }
    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data= db.rawQuery("select * from Borrow",null);
        return data;
    }

    public Boolean CheckProduct(String item_name, String item_description) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Borrow where item_name=? and item_description=?",new String[]{item_name,item_description});

        if (cursor.getCount()>0) return false;
        else return true;
    }

}

