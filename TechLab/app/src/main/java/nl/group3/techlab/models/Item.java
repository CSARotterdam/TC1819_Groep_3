package nl.group3.techlab.models;

public class Item {
    private String ItemName;
    private String ItemCategorie;
    private String ItemDescription;
    private int ItemQuantity;


    public Item(String fItem, String fCat, String fDes, int fQuan){
        ItemName = fItem;
        ItemCategorie = fCat;
        ItemDescription = fDes;
        ItemQuantity = fQuan;

    }

    public String getItem() {
        return ItemName;
    }

    public String getItemCategorie() {
        return ItemCategorie;
    }

    public String getItemDescription() {
        return ItemDescription;
    }


    public int getItemQuantity() {
        return ItemQuantity;
    }
}
