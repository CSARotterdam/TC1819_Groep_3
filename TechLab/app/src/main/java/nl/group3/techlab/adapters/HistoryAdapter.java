package nl.group3.techlab.adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nl.group3.techlab.R;
import nl.group3.techlab.helpers.BitmapHelper;
import nl.group3.techlab.helpers.JSONHelper;
import nl.group3.techlab.models.Book;
import nl.group3.techlab.models.Item;
import nl.group3.techlab.models.RowListItem;

import java.net.URL;
import java.util.List;


public class HistoryAdapter extends ArrayAdapter<JsonObject> {
    public HistoryAdapter(@NonNull Context context, List<JsonObject> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, parent,false);
        }

        TextView item_name = convertView.findViewById(R.id.item_name);
        TextView returned = convertView.findViewById(R.id.returned);
        TextView borrowedAt = convertView.findViewById(R.id.borrowedAt);
        final ImageView itemImage = convertView.findViewById(R.id.imageView);
        JsonObject item = getItem(position);

        try{
            if(!item.get("item").getAsJsonObject().get("image").isJsonNull())
                itemImage.setImageBitmap(new Item("", "", "", "", 0, new URL(item.get("item").getAsJsonObject().get("image").getAsString()), 0, 0).getImage(getContext()));
        } catch (Exception ex){}

        item_name.setText(item.get("item").getAsJsonObject().get("name").getAsString());


        returned.setText(getContext().getString(R.string.teruggebracht) + " " + (item.get("hand_in_date").getAsString().equalsIgnoreCase("none") ? getContext().getString(R.string.nee) : getContext().getString(R.string.ja)));
        borrowedAt.setText(item.get("borrow_date").getAsString());
        return convertView;
    }
}
