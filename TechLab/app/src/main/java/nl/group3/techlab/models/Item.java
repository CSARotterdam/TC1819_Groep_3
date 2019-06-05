package nl.group3.techlab.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.ByteBuffer;

import nl.group3.techlab.helpers.BitmapHelper;

public class Item implements Serializable {
    //TODO: CLEANUP / Create missing models

    String type;
    String id;
    int borrowDays;
    String description;
    URL imageUrl;
    byte[] image;


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
        if(imageUrl != null){
            Bitmap bmp = BitmapHelper.LoadImageFromWebURL(imageUrl);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            this.image = stream.toByteArray();
        } else {
            this.image = null;
        }
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

    public URL getImageUrl() {
        return imageUrl;
    }

    public Bitmap getImage() {
        if (image == null)
            return null;
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
