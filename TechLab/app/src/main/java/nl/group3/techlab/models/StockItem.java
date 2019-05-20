package nl.group3.techlab.models;

import java.io.Serializable;

public class StockItem implements Serializable {
    Item item;
    int stock;
    int broken;

    public StockItem(Item item, int stock, int broken){
        this.item = item;
        this.broken = broken;
        this.stock = stock;
    }

    public Item getItem() {
        return item;
    }

    public int getStock() {
        return stock;
    }

    public int getBroken() {
        return broken;
    }

    public void addOneStock() {
        this.stock += 1;
    }

    public void addOneBroken() {
        this.broken += 1;
    }
}
