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
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import nl.group3.techlab.HandInConfirmation;
import nl.group3.techlab.R;
import nl.group3.techlab.helpers.JSONHelper;
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
        final TextView tvObjectName = (TextView) convertView.findViewById(R.id.objectName);
        TextView tvBorrowedBy = (TextView) convertView.findViewById(R.id.borrowedBy);


        Button bReturnButton = (Button) convertView.findViewById(R.id.returnButton);


        // TODO: change the getting of itemname, this is NOT the right way to do this, maybe review the models on client and server
        Thread thread;
        thread = new Thread(new Runnable() {
            public void run() {
                try {

                    String jsonString = JSONHelper.JSONStringFromURL(String.format( "http://84.86.201.7:8000/api/v1/items/%s/", borrowItem.get("item").getAsJsonObject().get("id").getAsString()),
                            null, 1000, "GET", null);


                    JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();
                    tvObjectName.setText(obj.get("title").getAsString());
                    borrowItem.add("item", obj);

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


        tvReturnDate.setText(borrowItem.get("borrow_date").getAsString());
        tvBorrowedBy.setText(borrowItem.get("user").getAsJsonObject().get("email").getAsString());

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