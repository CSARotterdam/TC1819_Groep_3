package nl.group3.techlab.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import nl.group3.techlab.R;
import nl.group3.techlab.models.Item;

import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter<Item> {

    private LayoutInflater mInflater;
    private ArrayList<Item> items;
    private int mViewResourceID;

    public ProductListAdapter(Context context, int textViewResourceId, ArrayList<Item> items){
        super(context,textViewResourceId,items);
        this.items = items;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceID = textViewResourceId;

    }
    public View getView(int position, View convertView, ViewGroup parent){
        convertView = mInflater.inflate(mViewResourceID,null);

        Item item = items.get(position);

        if(item != null){
            ImageView ItemImage = (ImageView) convertView.findViewById(R.id.eItem);
            TextView ItemName = (TextView) convertView.findViewById(R.id.eItemname);
 //           TextView ItemCategorie = (TextView) convertView.findViewById(R.id.eItemcat);
            TextView ItemDescription = (TextView) convertView.findViewById(R.id.eItemdes);
  //          TextView ItemQuantity = (TextView) convertView.findViewById(R.id.eItemq);


            if(ItemImage != null) {
                ItemImage.setImageResource(R.drawable.ic_launcher_background);
            }
            if(ItemName != null){
                ItemName.setText((item.getItem()));
            }
            if(ItemDescription != null){
                ItemDescription.setText("Beschikbaar");
            }

//            if(ItemCategorie != null){
//                ItemCategorie.setText((item.getItemCategorie()));
//            }
//            if(ItemDescription != null){
//                ItemDescription.setText((item.getItemDescription()));
//            }
//            if(ItemQuantity != null){
//                ItemQuantity.setText(Integer.toString(item.getItemQuantity()));
//
//            }
        }
        return convertView;
    }

}
