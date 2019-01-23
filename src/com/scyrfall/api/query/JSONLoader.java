package com.scyrfall.api.query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class JSONLoader {

    public static JSONObject JSONObjectFromURL(String uri) {
        try {
            return new JSONObject(loadStringFromURL(uri));
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    static JSONObject JSONObjectFromURL(URL url) {
        try {
            return new JSONObject(loadStringFromURL(url));
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    static JSONArray JSONArrayFromURL(String uri) {
        try {
            return new JSONArray(getText(uri));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private static String loadStringFromURL(String uri) {
        try {
            return getText(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String loadStringFromURL(URL url) {
        try {
            return loadStringFromInputStream(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String loadStringFromInputStream(InputStream is) {
        return new Scanner(is).useDelimiter("\\Z").next();
    }

    static String getText(String url) throws IOException {
        URL website = new URL(url);
        HttpsURLConnection connection = ((HttpsURLConnection) website.openConnection());
        InputStreamReader reader;
        if (connection.getResponseCode() < HttpsURLConnection.HTTP_BAD_REQUEST) {
            reader = new InputStreamReader(connection.getInputStream());
        } else {
            reader = new InputStreamReader((connection).getErrorStream());
        }
        BufferedReader in = new BufferedReader(reader);

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine).append("\n");
        }
        response.deleteCharAt(response.lastIndexOf("\n"));
        in.close();

        return response.toString();
    }
}
