package nl.group3.techlab.models;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.ByteBuffer;

import nl.group3.techlab.helpers.BitmapHelper;

public class Item implements Serializable {
    //TODO: CLEANUP / Create missing models

    String type;
    String id;
    String name;
    String description;
    int borrowDays;
    int stock;
    int broken;
    URL imageUrl;


    @Deprecated
    String productId;
    @Deprecated
    String manufacturer;
    @Deprecated
    String category;
    @Deprecated
    int quantity;

    public Item(int id, String productId, String manufacturer, String category, String name){
        //this.id = id;
        this.productId = productId;
        this.manufacturer = manufacturer;
        this.category = category;
        this.name = name;
    }

    public Item(String type, String id, String name, String description, int borrowDays, URL imageUrl, int stock, int broken){
        this.type = type;
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.broken = broken;
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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getStock() { return stock; }

    @Deprecated
    public int getQuantity() {
        return quantity;
    }

    public URL getImageUrl() {
        return imageUrl;
    }

    public Bitmap getImage(final Context ctx) {
        return getImage(ctx, this.imageUrl);
    }

    public static Bitmap getImage(final Context ctx, final URL imageUrl) {

        final Bitmap[] bmpf = new Bitmap[1];
        Thread thread;
        thread = new Thread(new Runnable() {
            public void run() {
                if(imageUrl != null){
                    if (loadImageFromStorage(imageUrl.getFile(), ctx) != null) {
                        bmpf[0] = loadImageFromStorage(imageUrl.getFile(), ctx);
                    } else {
                        try {
                            Bitmap bmp = BitmapHelper.LoadImageFromWebURL(imageUrl);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            bmpf[0] = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.toByteArray().length);
                            saveToInternalStorage(bmpf[0], imageUrl.getFile(), ctx);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
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

    private static void saveToInternalStorage(Bitmap bitmapImage, String name, Context ctx){
        // Create imageDir
        File file = new File(ctx.getFilesDir(),name);
        file.getParentFile().mkdirs();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Bitmap loadImageFromStorage(String name, Context ctx)
    {
        try {
            File file = new File(ctx.getFilesDir(),name);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(file));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
