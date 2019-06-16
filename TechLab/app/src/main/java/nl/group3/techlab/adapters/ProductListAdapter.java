package nl.group3.techlab.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import nl.group3.techlab.ItemEdit;
import nl.group3.techlab.ItemsAndMenuActivity;
import nl.group3.techlab.R;
import nl.group3.techlab.models.Book;
import nl.group3.techlab.models.Item;
import nl.group3.techlab.models.Writer;

import java.net.URL;
import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter<JsonObject> /* , ArrayAdapter<Electronic> */ {
    //TODO: CLEANUP, add electronics

    private LayoutInflater mInflater;
    private ArrayList<JsonObject> items;
    private int mViewResourceID;

    public ProductListAdapter(Context context, int textViewResourceId, ArrayList<JsonObject> items){
        super(context,textViewResourceId,items);
        this.items = items;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceID = textViewResourceId;
    }

    public View getView(int position, View convertView, final ViewGroup parent){
        convertView = mInflater.inflate(mViewResourceID,null);

        final JsonObject item = getItem(position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editScreenIntent = new Intent(getContext(), ItemEdit.class);
                editScreenIntent.putExtra("item", item.toString());
                getContext().startActivity(editScreenIntent);
                ((Activity) getContext()).finish();
            }
        });


        ImageView ItemImage = (ImageView) convertView.findViewById(R.id.eItem);
        TextView ItemName = (TextView) convertView.findViewById(R.id.eItemname);
//           TextView ItemCategorie = (TextView) convertView.findViewById(R.id.eItemcat);
        TextView ItemDescription = (TextView) convertView.findViewById(R.id.eItemdes);
//          TextView ItemQuantity = (TextView) convertView.findViewById(R.id.eItemq);

        if(ItemImage != null) {
            if(!item.get("image").isJsonNull())
                try {
                    ItemImage.setImageBitmap(Item.getImage(getContext(), new URL(item.get("image").getAsString())));
                } catch (Exception ex) { ex.printStackTrace(); }
            else
                ItemImage.setImageResource(R.drawable.ic_launcher_background);
        }
        if(ItemName != null){
            ItemName.setText((item.get("name").getAsString()));
        }
        if(ItemDescription != null){
            ItemDescription.setText(item.get("stock").getAsInt() > 0 ? R.string.beschikbaar : R.string.niet_beshikbaar);
        }
        return convertView;
    }

}
