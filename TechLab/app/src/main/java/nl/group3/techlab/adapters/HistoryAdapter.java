package nl.group3.techlab.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nl.group3.techlab.R;
import nl.group3.techlab.helpers.JSONHelper;
import nl.group3.techlab.models.Book;
import nl.group3.techlab.models.Item;
import nl.group3.techlab.models.RowListItem;

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

        final TextView item_name = convertView.findViewById(R.id.item_name);
        TextView description = convertView.findViewById(R.id.description);
        TextView returned = convertView.findViewById(R.id.returned);
        final JsonObject item = getItem(position);




        // TODO: change the getting of itemname, this is NOT the right way to do this, maybe review the models on client and server
        Thread thread;
        thread = new Thread(new Runnable() {
            public void run() {
                try {
                    Log.d("Item", item.toString());

                    String jsonString = JSONHelper.JSONStringFromURL(String.format( "http://84.86.201.7:8000/api/v1/items/%s/", item.get("item").getAsJsonObject().get("id").getAsString()),
                            null, 1000, "GET");
                    Log.d("JSON", jsonString);

                    JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();
                    item_name.setText(obj.get("title").getAsString());

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


        description.setText("desc");
        returned.setText(getContext().getString(R.string.teruggebracht) + " " + (item.get("hand_in_date").getAsString().equalsIgnoreCase("none") ? getContext().getString(R.string.nee) : getContext().getString(R.string.ja)));
        return convertView;
    }
}
