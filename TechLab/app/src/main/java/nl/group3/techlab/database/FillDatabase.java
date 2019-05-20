package nl.group3.techlab.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Date;

import nl.group3.techlab.models.BorrowItem;
import nl.group3.techlab.models.Item;
import nl.group3.techlab.models.StockItem;
import nl.group3.techlab.models.User;

public class FillDatabase {
    public static void FillDatabaseWithTestData(ItemDatabaseHelper db){


        Item item = new Item(1, "1", "asdf", "ab, eb, cz", "Holo lens");
        User user = new User(1, "John", "Doe");
        StockItem stockItem = new StockItem(item, 10, 2);
        BorrowItem borrowItem = new BorrowItem(0, item, user, new Date(), null);
        db.InsertItem(item);
        db.InsertUser(user);
        db.InsertStockItem(stockItem);
        db.InsertBorrowItem(borrowItem);

        Log.d("DATABASE", db.getItemProductIds().length + " Items found");
        db.getBorrowedItems(user, false);

    }
}
