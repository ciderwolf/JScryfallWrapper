package com.scyrfall.api.query;

import com.scyrfall.api.object.Card;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageLoader {

    public static BufferedImage loadImageFromURL(URL url, URL apiUrl) {
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            Card card = new Card(Query.dataFromURL(apiUrl));
            return loadImageFromURL(card.getImages().getNormalURL());
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
