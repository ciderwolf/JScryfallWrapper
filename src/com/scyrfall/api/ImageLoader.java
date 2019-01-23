package com.scyrfall.api;

import com.scyrfall.api.object.Card;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageLoader {

    public static BufferedImage loadImageFromURL(URL url, URL apiUrl) {
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            Card card = new Card(Query.contentsOfURL(apiUrl));
//            String uriFromApi = card.getJSONObject("image_uris").getString("normal");
            return loadImageFromURL(card.getImages().getNormalURL());
//            e.printStackTrace();
        }
    }

    public static BufferedImage loadImageFromURL(URL url) {
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
