package nl.group3.techlab.helpers;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class JSONHelper {
    //TODO: Add methods for post and delete

    // A GET request for getting a JSON String from a given HTTPS url
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

    public static String postJSONStringFromURL(String urlString, String jsonString) throws IOException, JSONException {
        // The string parsed to a new URL
        URL url = new URL(urlString);
        // The https url connection
        HttpURLConnection urlConnection = null;
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type","application/json");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        byte[] outputInBytes = jsonString.getBytes("UTF-8");
        OutputStream os = urlConnection.getOutputStream();
        os.write( outputInBytes );
        os.close();
        urlConnection.disconnect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        // Create a new string from the read input
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        return sb.toString();
    }

    public static String JSONStringFromURL(String url, String json, int timeout, String method) {
        HttpURLConnection connection = null;
        try {

            URL u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();

            //set the sending type and receiving type to json
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            connection.setAllowUserInteraction(false);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            if (json != null) {
                //set the content length of the body
                connection.setRequestProperty("Content-length", json.getBytes().length + "");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                //send the json as body of the request
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(json.getBytes("UTF-8"));
                outputStream.close();
            }
            connection.setRequestMethod(method);

            //Connect to the server
            connection.connect();

            int status = connection.getResponseCode();
            Log.i("HTTP Client", "HTTP status code : " + status);
            switch (status) {
                case 200:
                case 201:
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    bufferedReader.close();
                    Log.i("HTTP Client", "Received String : " + sb.toString());
                    //return received string
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Log.e("HTTP Client", "Error in http connection" + ex.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e("HTTP Client", "Error in http connection" + ex.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("HTTP Client", "Error in http connection" + ex.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.e("HTTP Client", "Error in http connection" + ex.toString());
                }
            }
        }
        return null;
    }

}
