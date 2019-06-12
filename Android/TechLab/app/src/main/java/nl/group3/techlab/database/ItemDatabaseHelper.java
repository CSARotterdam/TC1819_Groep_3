package nl.group3.techlab.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.Date;

import nl.group3.techlab.models.Item;
import nl.group3.techlab.models.StockItem;
import nl.group3.techlab.models.BorrowItem;
import nl.group3.techlab.models.User;

public class ItemDatabaseHelper extends SQLiteOpenHelper {
    public static class ItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "item";
        public static final String ID = "id";
        public static final String PRODUCT_ID = "product_id";
        public static final String NAME = "item_name";
        public static final String MANUFACTURER = "manufacturer";
        public static final String CATEGORY = "category";
    }

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String ID = "id";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";

    }

    public static class StockItemEntry implements BaseColumns{
        public static final String TABLE_NAME = "stock_item";
        public static final String ID = "id";
        public static final String STOCK = "stock";
        public static final String BROKEN = "broken";
        public static final String ITEM_ID = "item_id";
    }

    public static class BorrowItemEntry implements BaseColumns{
        public static final String TABLE_NAME = "borrow_item";
        public static final String ID = "id";
        public static final String ITEM = "item_id";
        public static final String USER = "user_id";
        public static final String BORROW_DATE = "borrow_date";
        public static final String RETURN_DATE = "return_date";
    }


    public static final String DATABASE_NAME = "ItemDB";

    public ItemDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createBorrowTable(sqLiteDatabase);
        createUserTable(sqLiteDatabase);
        createItemTable(sqLiteDatabase);
        createStockItemTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static void createUserTable(SQLiteDatabase db){
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s VARCHAR, " +
                        "%s VARCHAR)",
                UserEntry.TABLE_NAME,
                UserEntry.ID,
                UserEntry.FIRST_NAME,
                UserEntry.LAST_NAME));
    }

    public static void createItemTable(SQLiteDatabase db){
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s VARCHAR, " +
                        "%s VARCHAR, " +
                        "%s VARCHAR, " +
                        "%s VARCHAR)",
                        ItemEntry.TABLE_NAME,
                        ItemEntry.ID,
                        ItemEntry.PRODUCT_ID,
                        ItemEntry.NAME,
                        ItemEntry.MANUFACTURER,
                        ItemEntry.CATEGORY));
    }

    public static void createStockItemTable(SQLiteDatabase db){
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s INTEGER, " +
                        "%s INTEGER, " +
                        "%s INTEGER, " +

                        "CONSTRAINT fk_%s FOREIGN KEY (%s) " +
                        "REFERENCES %s(%s) " +
                        "ON UPDATE CASCADE " +
                        "ON DELETE RESTRICT) ",

                StockItemEntry.TABLE_NAME,
                StockItemEntry.ID,
                StockItemEntry.STOCK,
                StockItemEntry.BROKEN,
                StockItemEntry.ITEM_ID,

                ItemEntry.TABLE_NAME,
                StockItemEntry.ITEM_ID,
                ItemEntry.TABLE_NAME,
                ItemEntry.ID
        ));
    }

    public static void createBorrowTable(SQLiteDatabase db){
        //TODO: Finish creation of database object
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s INTEGER, " +
                        "%s INTEGER, " +

                        "%s DATETIME, " +
                        "%s DATETIME NULL, " +

                        "CONSTRAINT fk_%s FOREIGN KEY (%s) " +
                        "REFERENCES %s(%s) " +
                        "ON UPDATE CASCADE " +
                        "ON DELETE RESTRICT, " +

                        "CONSTRAINT fk_%s FOREIGN KEY (%s) " +
                        "REFERENCES %s(%s) " +
                        "ON UPDATE CASCADE " +
                        "ON DELETE RESTRICT) ",

                BorrowItemEntry.TABLE_NAME,
                BorrowItemEntry.ID,
                BorrowItemEntry.ITEM,
                BorrowItemEntry.USER,

                BorrowItemEntry.BORROW_DATE,
                BorrowItemEntry.RETURN_DATE,

                ItemEntry.TABLE_NAME,
                BorrowItemEntry.ITEM,
                ItemEntry.TABLE_NAME,
                ItemEntry.ID,

                UserEntry.TABLE_NAME,
                BorrowItemEntry.USER,
                UserEntry.TABLE_NAME,
                UserEntry.ID));

    }



    public void InsertItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ItemEntry.PRODUCT_ID, item.getProductId());
        contentValues.put(ItemEntry.NAME, item.getName());
        contentValues.put(ItemEntry.MANUFACTURER, item.getManufacturer());
        contentValues.put(ItemEntry.CATEGORY, item.getCategory());

        db.insert(ItemEntry.TABLE_NAME, null, contentValues);
    }

    public void InsertUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(UserEntry.FIRST_NAME, user.getFirstName());
        contentValues.put(UserEntry.LAST_NAME, user.getLastName());

        db.insert(UserEntry.TABLE_NAME, null, contentValues);
    }


    public void InsertBorrowItem(BorrowItem borrowItem){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(BorrowItemEntry.USER, borrowItem.getUser().getId());
        contentValues.put(BorrowItemEntry.ITEM, borrowItem.getItem().getId());
        contentValues.put(BorrowItemEntry.BORROW_DATE, new Date().getTime());
        contentValues.put(BorrowItemEntry.RETURN_DATE, new Date(0).getTime());

        db.insert(BorrowItemEntry.TABLE_NAME, null, contentValues);
    }

    public void InsertStockItem(StockItem stockItem){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(StockItemEntry.ITEM_ID, stockItem.getItem().getId());
        contentValues.put(StockItemEntry.STOCK, stockItem.getStock());
        contentValues.put(StockItemEntry.BROKEN, stockItem.getBroken());

        db.insert(StockItemEntry.TABLE_NAME, null, contentValues);
    }

    public boolean UpdateBorrowedItem(BorrowItem borrowItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(BorrowItemEntry.USER, borrowItem.getUser().getId());
        contentValues.put(BorrowItemEntry.ITEM, borrowItem.getItem().getId());
        contentValues.put(BorrowItemEntry.BORROW_DATE, borrowItem.getBorrowDate().getTime());
        contentValues.put(BorrowItemEntry.RETURN_DATE, new Date().getTime());

        boolean database = db.update(BorrowItemEntry.TABLE_NAME,
                contentValues,
                String.format("%s=?", BorrowItemEntry.ID),
                new String[]{borrowItem.getId()+""}) > 0 ? true : false;
        db.close();
        return database;
    }

    public boolean UpdateStockItem(StockItem stockItem){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(StockItemEntry.ITEM_ID, stockItem.getItem().getId());
        contentValues.put(StockItemEntry.STOCK, stockItem.getStock());
        contentValues.put(StockItemEntry.BROKEN, stockItem.getBroken());

        boolean database = db.update(StockItemEntry.TABLE_NAME,
                contentValues,
                String.format("%s=?", StockItemEntry.ITEM_ID),
                new String[]{stockItem.getItem().getId()+""}) > 0 ? true : false;
        db.close();
        return database;
    }


    public User getUser(String firstName, String lastName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s=%s AND %s=%s",
                "*",
                UserEntry.TABLE_NAME,
                UserEntry.FIRST_NAME,
                firstName,

                UserEntry.LAST_NAME,
                lastName
                ), null);

        if(data != null && data.getCount() > 0)
            return new User(data.getInt(data.getColumnIndexOrThrow(UserEntry.ID)),
                    data.getString(data.getColumnIndexOrThrow(UserEntry.FIRST_NAME)),
                    data.getString(data.getColumnIndexOrThrow(UserEntry.LAST_NAME))
            );
        else
            throw new SQLException();

    }

    public Item getItemById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s=%s",
                "*",
                ItemEntry.TABLE_NAME,
                ItemEntry.ID,
                id), null);

        if(data != null && data.getCount() > 0) {
            data.moveToFirst();
            return new Item(data.getInt(data.getColumnIndexOrThrow(ItemEntry.ID)),
                    data.getString(data.getColumnIndexOrThrow(ItemEntry.ID)),
                    data.getString(data.getColumnIndexOrThrow(ItemEntry.MANUFACTURER)),
                    data.getString(data.getColumnIndexOrThrow(ItemEntry.CATEGORY)),
                    data.getString(data.getColumnIndexOrThrow(ItemEntry.NAME))
            );
        }
        else
            throw new SQLException();

    }

    public Item getItemByProductId(String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(String.format("SELECT * FROM %s WHERE %s=%s",
                ItemEntry.TABLE_NAME,
                ItemEntry.PRODUCT_ID,
                productId), null);

        if(data != null && data.getCount() > 0)
            return new Item(data.getInt(data.getColumnIndexOrThrow(ItemEntry.ID)),
                    data.getString(data.getColumnIndexOrThrow(ItemEntry.ID)),
                    data.getString(data.getColumnIndexOrThrow(ItemEntry.MANUFACTURER)),
                    data.getString(data.getColumnIndexOrThrow(ItemEntry.CATEGORY)),
                    data.getString(data.getColumnIndexOrThrow(ItemEntry.NAME))
                    );
        else
            throw new SQLException();
    }

    public String[] getItemProductIds(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(String.format("SELECT %s FROM %s WHERE 1",
                ItemEntry.PRODUCT_ID,
                ItemEntry.TABLE_NAME), null);

        String[] names = new String[data.getCount()];
        if(data != null && data.getCount() > 0)
            data.moveToFirst();
        for(int i = 0; i < data.getCount(); i++) {
            names[i] = data.getString(data.getColumnIndex(ItemEntry.PRODUCT_ID));
            data.moveToNext();
        }
        data.close();
        return names;
    }

    public BorrowItem[] getBorrowedItems(User user, boolean just_borrowed){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s=%s AND %s IS NULL",
                "*",
                BorrowItemEntry.TABLE_NAME,
                BorrowItemEntry.USER,
                user.getId(),

                just_borrowed ? BorrowItemEntry.RETURN_DATE : "NULL"
        ), null);

        BorrowItem[] borrowedItems = new BorrowItem[data.getCount()];

        if(data != null && data.getCount() > 0) {
            data.moveToFirst();

            for (int i = 0; i < borrowedItems.length; i++) {
                Item dbItem = getItemById(data.getInt(data.getColumnIndex(BorrowItemEntry.ITEM)));
                borrowedItems[i] = new BorrowItem(data.getInt(data.getColumnIndex(BorrowItemEntry.ID)), dbItem, user, new Date(data.getLong(data.getColumnIndex(BorrowItemEntry.BORROW_DATE))), new Date(data.getLong(data.getColumnIndex(BorrowItemEntry.RETURN_DATE))));

                Log.d("TEST", borrowedItems[i].getId() + " - " + borrowedItems[i].getBorrowDate() + " - " + borrowedItems[i].getReturnDate());

                data.moveToNext();
            }
        }
        data.close();
        return borrowedItems;
    }

    public StockItem getStockItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s=%s",
                "*",
                StockItemEntry.TABLE_NAME,
                ItemEntry.ID,
                item.getId()), null);

        if(data != null && data.getCount() > 0) {
            data.moveToFirst();
            return new StockItem(item, data.getInt(data.getColumnIndex(StockItemEntry.STOCK)), data.getInt(data.getColumnIndex(StockItemEntry.BROKEN)));
        }

        return null;
    }
}
