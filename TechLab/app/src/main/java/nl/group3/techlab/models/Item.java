package nl.group3.techlab.models;

import java.io.Serializable;

public class Item implements Serializable {

    int id;
    String productId;
    String manufacturer;
    String category;
    String name;
    String description;
    int quantity;

    public Item(int id, String productId, String manufacturer, String category, String name){
        this.id = id;
        this.productId = productId;
        this.manufacturer = manufacturer;
        this.category = category;
        this.name = name;
    }

    public Item(String fItem, String fCat, String fDes, int fQuan){
        name = fItem;
        category = fCat;
        description = fDes;
        quantity = fQuan;

    }

    public int getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }
}
