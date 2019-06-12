package nl.group3.techlab.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.net.URL;

public class BitmapHelper {

    public static Bitmap LoadImageFromWebURL(String url) {
        try {
            return LoadImageFromWebURL(new URL(url));
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static Bitmap LoadImageFromWebURL(URL url) {
        try {
            InputStream is = (InputStream) url.openStream();
            Bitmap d = BitmapFactory.decodeStream(is);
            is.close();
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    }
