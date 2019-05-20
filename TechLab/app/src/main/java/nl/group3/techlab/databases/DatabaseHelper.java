
package nl.group3.techlab.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
//import com.example.borrow.models.User;


public class DatabaseHelper extends SQLiteOpenHelper {



    public static final String ITEM_DB = "items.db";
    public static final String TABLE_ITEM = "items_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "ITEM";
    public static final String COL3 = "ITEMCATEGORIE";
    public static final String COL4 = "ITEMDESCRIPTION";
    public static final String COL5 = "ITEMQUANTITY";


//    public static final String USER_DB = "user.db";
//    public static final String TABLE_USER = "user_data";
//    public static final String COL6 = "ID_USER";
//    public static final String COL7 = "FIRST_NAME";
//    public static final String COL8 = "LAST_NAME";

    public static final String ORDER_DB = "order.db";
    public static final String TABLE_ORDER = "order_data";
    public static final String COL9 = "ID"; //pk
    public static final String COL10 = "ID_ITEM"; //fk






    public DatabaseHelper(Context context) {
        super(context, ITEM_DB, null, 1);
    }
    private static final String TAG = "DatabaseHelper";
    @Override
    public void onCreate(SQLiteDatabase db) {
        String ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " ITEM TEXT, ITEMCATEGORIE TEXT, ITEMDESCRIPTION TEXT, ITEMQUANTITY INT)";
        db.execSQL(ITEM_TABLE);



//        String USER_TABLE = "CREATE TABLE "
//                + TABLE_USER +
//                " (ID_USER INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                " FIRST_NAME TEXT,LAST_NAME TEXT)";
//
//                db.execSQL(USER_TABLE);


        //naam tabl order_data
        String ORDER_TABLE = "CREATE TABLE " + TABLE_ORDER +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ID_ITEM INTEGER," +
                " CONSTRAINT fk_" + TABLE_ITEM + " FOREIGN KEY ("+COL10+") REFERENCES "+TABLE_ITEM+"("+COL1+"))";
        //ID_ORDER (pk)
        //ID (fk) Pimary from table item


           //     " CONSTRAINT fk_" + TABLE_USER + "  FOREIGN KEY ("+USER_DB+") REFERENCES "+TABLE_USER+"("+COL7+"))";
        db.execSQL(ORDER_TABLE);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF  EXISTS " + TABLE_ITEM);
//        db.execSQL("DROP TABLE IF  EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF  EXISTS " + TABLE_ORDER);
        onCreate(db);
    }

    public boolean addData(String fItem, String fCat, String fDes, int fQuan ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, fItem);
        contentValues.put(COL3, fCat);
        contentValues.put(COL4, fDes);
        contentValues.put(COL5, fQuan);





        long result = db.insert(TABLE_ITEM, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

//    public void InsertUser(User user){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put(COL7, user.getFirstName());
//        contentValues.put(COL8, user.getLastName());
//
//        db.insert(TABLE_USER, null, contentValues);
//    }


    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_ITEM, null);

        return data;
    }



    public Cursor getItemID(String ITEM){
        Log.d(TAG, "In fucntie getItemdID!");

        Cursor data = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT " + COL1 + " FROM " + TABLE_ITEM +
                    " WHERE " + COL2 + " = '" + ITEM + "'";

            Log.d(TAG, "CHECK query: " + query);

            data = db.rawQuery(query, null);

        }
        catch(Exception ex) {


        }
        return data;
    }


    public void updateName(String newName, int ID, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_ITEM + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + ID + "'" +
                " AND " + COL2 + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }
    public void deleteName(int ID, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_ITEM + " WHERE "
                + COL1 + " = '" + ID + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }

//public void insertBorrow(int fID){
//    SQLiteDatabase db = this.getWritableDatabase();
//    ContentValues contentValues = new ContentValues();
//    contentValues.put(COL1, fID);
//
//
//    db.insert(TABLE_ORDER, null, contentValues);
//}

    public void addBorrow(int IDss, int quantity) {


        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO order_data(ID_ITEM) VALUES("+IDss+")";
        Log.d(TAG, query);

        String query1 = " UPDATE " + TABLE_ITEM + " SET " + COL5 + " = " + (quantity -1)  +" WHERE " + "ID" + " = " + IDss;
        Log.d(TAG, query1);
//        String query1 = "UPDATE " + TABLE_ITEM + " SET " + COL5 +
//                " = '" + NewQuan + "' WHERE " + COL1 + " = '" + ID + "'" +
//                " AND " + COL5 + " = '" + -1 + "'";

     //   Log.d(TAG, ": adding " + name + " to order.");
//        db.execSQL(query1);
        db.execSQL(query1);

        db.execSQL(query);
    }

    public Cursor GetBorrowedContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_ORDER, null);
        return data;
    }


    public Cursor getBorrowedID(int ID_ORDER){
        Log.d(TAG, "In fucntie getBorrowedID!");

        Cursor data = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT " + COL9 + " FROM " + TABLE_ORDER +
                    " WHERE " + ID_ORDER + " = '" + ID_ORDER + "'";

            Log.d(TAG, "CHECK query: " + query);

            data = db.rawQuery(query, null);

        }
        catch(Exception ex) {


        }
        return data;
    }

}


