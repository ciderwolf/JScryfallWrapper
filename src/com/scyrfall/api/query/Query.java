package com.scyrfall.api.query;

import com.scyrfall.api.ScryfallObject;
import com.scyrfall.api.object.List;
import com.scyrfall.api.object.Set;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Query {


    /**
     * The base uri for the Scryfall API
     */
    public static final String API_STUB = "https://api.scryfall.com/";

    public static Set[] getSets() {
        List data = new List(dataFromPath("sets"));
        ScryfallObject[] contents = data.getContents();
        return ScryfallObject.convertArray(contents, new Set[0]);
    }

    /**
     * @param url the URL from which data should be retrieved
     * @return a <code>JSONObject</code> formed from the specified URL
     */
    public static JSONObject dataFromURL(URL url) {
        return JSONLoader.JSONObjectFromURL(url);
    }

    /**
     * @param url the URL from which data should be retrieved
     * @return a <code>BufferedImage</code> from the specified URL
     */
    public static BufferedImage imageFromURL(URL url) {
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            System.out.println(url);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param pathAppend path to the requested resource in Scryfall's API
     * @return a <code>JSONObject</code> located at the specified path on Scryfall's API
     */
    public static JSONObject dataFromPath(String pathAppend) {
        return JSONLoader.JSONObjectFromURL(API_STUB + pathAppend);
    }

    /**
     * @param pathAppend path to the requested resource in Scryfall's API
     * @return an image located at the specified path
     */
    public static BufferedImage imageFromPath(String pathAppend) {
        try {
            return ImageIO.read(new URL(API_STUB + pathAppend));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param pathAppend path to the requested resource in Scryfall's API
     * @return an image located at the specified path
     */
    public static String textFromPath(String pathAppend) {
        try {
            return JSONLoader.getText(API_STUB + pathAppend);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
