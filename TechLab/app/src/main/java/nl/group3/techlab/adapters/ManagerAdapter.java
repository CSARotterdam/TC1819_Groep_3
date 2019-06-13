package nl.group3.techlab.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import nl.group3.techlab.R;
import nl.group3.techlab.helpers.JSONHelper;

public class ManagerAdapter extends ArrayAdapter<JsonObject> {

    public ManagerAdapter(Context context, ArrayList<JsonObject> managers) {
        super(context, 0, managers);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final JsonObject manager = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_managers, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.email)).setText(manager.get("email").getAsString());

        final View finalConvertView = convertView;
        ((Button) convertView.findViewById(R.id.removeManager)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread;
                thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());

                            String jsonString = JSONHelper.JSONStringFromURL(String.format( "http://84.86.201.7:8000/api/v1/managers/%s/", manager.get("id").getAsString()), null, 1000, "DELETE", null);

                            final JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();

                            ((Activity)finalConvertView.getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(finalConvertView.getContext(), jsonObject.get("message").getAsString(), Toast.LENGTH_LONG).show();

                                }
                            });
                            ((Activity)finalConvertView.getContext()).finish();

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

            }
        });

        return convertView;
    }
}
