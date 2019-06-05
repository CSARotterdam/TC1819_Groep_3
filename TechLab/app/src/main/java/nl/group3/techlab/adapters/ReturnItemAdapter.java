package nl.group3.techlab.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import nl.group3.techlab.HandInConfirmation;
import nl.group3.techlab.R;
import nl.group3.techlab.models.BorrowItem;

public class ReturnItemAdapter extends ArrayAdapter<JsonObject> {
    public ReturnItemAdapter(Context context, ArrayList<JsonObject> borrowItems) {
        super(context, 0, borrowItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final JsonObject borrowItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_return_item, parent, false);
        }

        Log.d("ReturnedItemAdapter", borrowItem.toString());

        final View reloadView = convertView;
        // Lookup view for data population
        TextView tvReturnDate = (TextView) convertView.findViewById(R.id.returnDate);
        TextView tvObjectName = (TextView) convertView.findViewById(R.id.objectName);

        Button bReturnButton = (Button) convertView.findViewById(R.id.returnButton);

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        tvObjectName.setText(borrowItem.get("item").getAsJsonObject().get("type").getAsString());
        tvReturnDate.setText(format.format(borrowItem.get("borrow_date").getAsString()));

        if(new Date(borrowItem.get("return_date").getAsString()).getTime() > new Date(1553893014504L).getTime())
            bReturnButton.setText(R.string.teruggebracht);
        else
            bReturnButton.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("BorrowedItemObject", borrowItem.toString());
                    Intent i = new Intent(getContext(), HandInConfirmation.class);
                    i.putExtras(bundle);
                    getContext().startActivity(i);
                    ((Activity)reloadView.getContext()).finish();

                }
            });

        return convertView;
    }
}