package nl.group3.techlab.models;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.net.URL;

public class Item implements Serializable {
    //TODO: CLEANUP / Create missing models

    String type;
    String id;
    int borrowDays;
    String description;
    URL imageUrl;

    @Deprecated
    String productId;
    @Deprecated
    String manufacturer;
    @Deprecated
    String category;
    @Deprecated
    String name;
    @Deprecated
    int quantity;

    public Item(int id, String productId, String manufacturer, String category, String name){
        //this.id = id;
        this.productId = productId;
        this.manufacturer = manufacturer;
        this.category = category;
        this.name = name;
    }

    public Item(String type, String id, String description, int borrowDays, URL imageUrl){
        this.type = type;
        this.id = id;
        this.description = description;
        this.borrowDays = borrowDays;
        this.imageUrl = imageUrl;
    }

    public Item(String name, String description){
        this.name = name;
        this.description = description;
    }

    public Item(String fItem, String fCat, String fDes, int fQuan){
        name = fItem;
        category = fCat;
        description = fDes;
        quantity = fQuan;

    }

    public String getId() {
        return id;
    }

    @Deprecated
    public String getProductId() {
        return productId;
    }

    @Deprecated
    public String getManufacturer() {
        return manufacturer;
    }

    @Deprecated
    public String getCategory() {
        return category;
    }

    @Deprecated
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Deprecated
    public int getQuantity() {
        return quantity;
    }
}
