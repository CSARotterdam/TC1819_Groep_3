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

import nl.group3.techlab.ItemEdit;
import nl.group3.techlab.ItemsAndMenuActivity;
import nl.group3.techlab.R;
import nl.group3.techlab.models.Book;
import nl.group3.techlab.models.Item;
import nl.group3.techlab.models.Writer;

import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter<Book> /* , ArrayAdapter<Electronic> */ {
    //TODO: CLEANUP, add electronics

    private LayoutInflater mInflater;
    private ArrayList<Book> books;
    private int mViewResourceID;

    public ProductListAdapter(Context context, int textViewResourceId, ArrayList<Book> books){
        super(context,textViewResourceId,books);
        this.books = books;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceID = textViewResourceId;
    }

    public View getView(int position, View convertView, final ViewGroup parent){
        convertView = mInflater.inflate(mViewResourceID,null);

        final Book item = books.get(position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editScreenIntent = new Intent(getContext(), ItemEdit.class);
                editScreenIntent.putExtra("item", item);
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
            if(item.getImage(getContext()) != null)
                ItemImage.setImageBitmap(item.getImage(getContext()));
            else
                ItemImage.setImageResource(R.drawable.ic_launcher_background);
        }
        if(ItemName != null){
            ItemName.setText((item.getName()));
        }
        if(ItemDescription != null){
            ItemDescription.setText(item.getStock() > 0 ? R.string.beschikbaar : R.string.niet_beshikbaar);
        }
        return convertView;
    }

}
