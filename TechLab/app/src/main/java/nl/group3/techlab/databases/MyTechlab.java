package nl.group3.techlab.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyTechlab extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatab.db";
    public static final String TABLE_NAME = "TECHLABUSER";
    private static final int version = 2;
    public static final String id = "ID";
    public static final String email = "Email";
    public static final String password = "Password";
    public static final String rol = "Rol";

    public MyTechlab(Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Email TEXT, Password TEXT, Rol TEXT  )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
    public Cursor AllData(){
        SQLiteDatabase myDb = this.getReadableDatabase();
        Cursor cursor = myDb.rawQuery("select * from TECHLABUSER",null);
        return cursor;
    }
// insert data
    public boolean insertData(String Email, String Password,String Rol) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", Email);
        contentValues.put("password", Password);
        contentValues.put("rol", Rol);

        long success = myDb.insert(TABLE_NAME, null, contentValues);
        if (success == -1) {
            return false;
        }
        else {
            return true;
    }

    }
// Check email and password
    public Boolean Email_Password(String Email, String Password) {
        SQLiteDatabase myDb = this.getReadableDatabase();
        Cursor cursor = myDb.rawQuery("select * from TECHLABUSER where Email=? and Password=?",new String[]{Email,Password});

        if (cursor.getCount()>0) return true;
else return false;
    }

    public Boolean Rol(String Email, String Rol) {
        SQLiteDatabase myDb = this.getReadableDatabase();
        Cursor cursor = myDb.rawQuery("select * from TECHLABUSER where Email=? and Rol=?",new String[]{Email,Rol});

        if (cursor.getCount()>0) return true;
        else return false;
    }
}
