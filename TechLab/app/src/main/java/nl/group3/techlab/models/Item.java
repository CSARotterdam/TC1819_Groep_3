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

    public URL getImageUrl() {
        return imageUrl;
    }

    public Bitmap getImage() {
        final Bitmap[] bmpf = new Bitmap[1];
        Thread thread;
        thread = new Thread(new Runnable() {
            public void run() {
                try{
                    if(imageUrl != null){
                        Bitmap bmp = BitmapHelper.LoadImageFromWebURL(imageUrl);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        bmpf[0] = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.toByteArray().length);
                    }
                }catch(Exception ex){ ex.printStackTrace();}
            }
        });
        // Start the new thread and run the code.
        thread.start();

        // Join the thread when it's done, meaning that the application will wait untill the
        // thread is done.
        try {
            thread.join();
        } catch (Exception ex){}

        return bmpf[0];
    }
}
