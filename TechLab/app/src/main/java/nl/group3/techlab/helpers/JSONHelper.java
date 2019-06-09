package nl.group3.techlab.helpers;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class JSONHelper {
    //TODO: Add methods for post and delete

    // A GET request for getting a JSON String from a given HTTPS url
    @Deprecated()
    public static String getJSONStringFromURL(String urlString) throws IOException, JSONException {
        // The string parsed to a new URL
        URL url = new URL(urlString);
        // The https url connection
        HttpURLConnection urlConnection = null;
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        // Create a new string from the read input
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        urlConnection.disconnect();

        return sb.toString();
    }

    public static String JSONStringFromURL(String url, HashMap<String, String> formFields, int timeout, String method, String fileUrl) {
        try {
            String charset = "UTF-8";

            MultipartUtility multipart = new MultipartUtility(url, charset, method);

            if(method.equals("PUT") || method.equals("DELETE")){
                multipart.addJsonFormFields(formFields);
            } else if(formFields != null){
                multipart.addFormFields(formFields);
            }

            if(fileUrl != null)
                multipart.addFilePart("image", new File(fileUrl));
            String response = multipart.finish(); // response from server.
            return response;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}