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

            if(formFields != null){
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

//    public static String JSONStringFromURL(String url, String json, int timeout, String method, String fileUrl) {
//        HttpURLConnection connection = null;
//        try {
//
//            URL u = new URL(url);
//            connection = (HttpURLConnection) u.openConnection();
//
//            //set the sending type and receiving type to json
//            connection.setRequestProperty("Accept", "application/json");
//            connection.setRequestProperty("Connection", "Keep-Alive");
//
//            connection.setAllowUserInteraction(false);
//            connection.setConnectTimeout(timeout);
//            connection.setReadTimeout(timeout);
//            connection.setRequestMethod(method);
//
//
//            if (json != null) {
//                //set the content length of the body
////                connection.setRequestProperty("Content-length", json.getBytes().length + "");
//                connection.setDoInput(true);
//                connection.setDoOutput(true);
//                connection.setUseCaches(false);
//
//                String boundary = "SwA" + Long.toString(System.currentTimeMillis()) + "SwA";
//                connection.addRequestProperty("content-type", "multipart/form-data; boundary=" + boundary);
//
//                if(fileUrl != null){
//
//                    byte[] buffer;
//                    int bytesRead, bytesAvailable, bufferSize;
//
//                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
//
//                    String lineEnd = "\r\n";
//                    String twoHyphens = "--";
//                    outputStream.writeBytes((twoHyphens + boundary + "\r\n"));
//                    outputStream.writeBytes("Content-Disposition: form-data; name=\"reference\""+ lineEnd);
//                    outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "image" + "\"; filename=\"" + fileUrl + "\"\r\n");
//                    outputStream.writeBytes("Content-Type: mimetype\r\n");
//                    outputStream.writeBytes("Content-Transfer-Encoding: UTF-8\r\n\r\n");
//                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
////                    outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadFile\";filename=\"" + imagePath +"\"" + lineEnd);
//                    outputStream.writeBytes(lineEnd);
//
//
//
//
//                    FileInputStream fileInputStream = new FileInputStream(fileUrl);
//                    bytesAvailable = fileInputStream.available();
//                    bufferSize = Math.min(bytesAvailable, 1024 * 1024);
//                    buffer = new byte[bufferSize];
//
//                    // Read file
//                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                    while (bytesRead > 0) {
//                        outputStream.write(buffer, 0, bufferSize);
//                        bytesAvailable = fileInputStream.available();
//                        bufferSize = Math.min(bytesAvailable, 1024 * 1024);
//                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//                    }
//
//                    outputStream.writeBytes(lineEnd);
//                    outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//                    outputStream.write(json.getBytes("UTF-8"));
//                    outputStream.close();
//
//                } else {
//
//                    OutputStream outputStream = connection.getOutputStream();
//                    outputStream.write(json.getBytes("UTF-8"));
//                    outputStream.close();
//
//                    //Connect to the server
//                    connection.connect();
//                }
//                //send the json as body of the request
//            }
//
//
//            //Connect to the server
//            connection.connect();
//
//            int status = connection.getResponseCode();
//            Log.i("HTTP Client", "HTTP status code : " + status);
//            switch (status) {
//                case 200:
//                case 201:
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                    StringBuilder sb = new StringBuilder();
//                    String line;
//                    while ((line = bufferedReader.readLine()) != null) {
//                        sb.append(line);
//                    }
//                    bufferedReader.close();
//                    Log.i("HTTP Client", "Received String : " + sb.toString());
//
//                    //return received string
//                    return sb.toString();
//            }
//
//        } catch (MalformedURLException ex) {
//            ex.printStackTrace();
//            Log.e("HTTP Client", "Error in http connection" + ex.toString());
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            Log.e("HTTP Client", "Error in http connection" + ex.toString());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            Log.e("HTTP Client", "Error in http connection" + ex.toString());
//        } finally {
//            if (connection != null) {
//                try{
//                    connection.disconnect();
//
//                } catch (Exception ex){
//                    ex.printStackTrace();
//                }
//            }
//        }
//        return null;
//    }

}