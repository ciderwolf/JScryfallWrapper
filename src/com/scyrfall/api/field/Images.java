package com.scyrfall.api.field;

import com.scyrfall.api.ScryfallObject;
import com.scyrfall.api.query.Query;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Objects;

/**
 * A container for image URLs of different sizes which can be retrieved
 * by the Scryfall API
 */
public class Images extends ScryfallObject {

    private URL artCropURL, borderCropURL, pngURL, largeURL, normalURL, smallURL;

    public Images(JSONObject data) {
        super(data);
        artCropURL = getURL("art_crop");
        borderCropURL = getURL("border_crop");
        pngURL = getURL("png");
        largeURL = getURL("large");
        normalURL = getURL("normal");
        smallURL = getURL("small");
    }

    public URL getArtCropURL() {
        return artCropURL;
    }

    public URL getBorderCropURL() {
        return borderCropURL;
    }

    public URL getPngURL() {
        return pngURL;
    }

    public URL getLargeURL() {
        return largeURL;
    }

    public URL getNormalURL() {
        return normalURL;
    }

    public URL getSmallURL() {
        return smallURL;
    }

    public URL getURL(Size size) {
        switch (size) {
            case PNG:
                return getPngURL();
            case LARGE:
                return getLargeURL();
            case NORMAL:
                return getNormalURL();
            case SMALL:
                return getSmallURL();
            case ART_CROP:
                return getArtCropURL();
            case BORDER_CROP:
                return getBorderCropURL();
            default:
                return null;
        }
    }

    public BufferedImage getSmall() {
        return Query.imageFromURL(getSmallURL());
    }

    public BufferedImage getPng() {
        return Query.imageFromURL(getPngURL());
    }

    public BufferedImage getLarge() {
        return Query.imageFromURL(getLargeURL());
    }

    public BufferedImage getNormal() {
        try {
            return Query.imageFromURL(getNormalURL());
        } catch (IllegalArgumentException e) {
            System.out.println(data);
            return null;
        }
    }

    public BufferedImage getArtCrop() {
        return Query.imageFromURL(getArtCropURL());
    }

    public BufferedImage getBorderCrop() {
        return Query.imageFromURL(getBorderCropURL());
    }

    public BufferedImage getImage(Size size) {
        return Query.imageFromURL(getURL(size));
    }

    /**
     * <table>
     * <tr><td><b>Key name</b></td><td><b>Size</b></td><td><b>Description</b></td></tr>
     * <tr><td>png</td><td>745 × 1040</td>A transparent, rounded full card PNG. This is the best image to use for videos
     * or other high-quality content.</tr>
     * <tr><td>border_crop</td><td>480 × 680</td><td>A full card image with the rounded corners and the majority of the
     * border cropped off. Designed for dated contexts where rounded images can’t be used.</td></tr>
     * <tr><td>art_crop</td><td>Varies</td><td>A rectangular crop of the card’s art only. Not guaranteed to be perfect
     * for cards with outlier designs or strange frame arrangements</td></tr>
     * <tr><td>large</td><td>672 × 936</td><td>A large full card image</td></tr>
     * <tr><td>normal</td><td>488 × 680</td><td>A medium-sized full card image</td></tr>
     * <tr><td>small</td><td>146 × 204</td><td>A small full card image. Designed for use as thumbnail or list icon.</td></tr>
     * </table>
     */
    public enum Size {
        SMALL, NORMAL, LARGE, PNG, ART_CROP, BORDER_CROP;

        public String toParameterString() {
            return super.toString().toLowerCase();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Images images = (Images) o;
        return Objects.equals(artCropURL, images.artCropURL) &&
                Objects.equals(borderCropURL, images.borderCropURL) &&
                Objects.equals(pngURL, images.pngURL) &&
                Objects.equals(largeURL, images.largeURL) &&
                Objects.equals(normalURL, images.normalURL) &&
                Objects.equals(smallURL, images.smallURL);
    }

    @Override
    public String toString() {
        return "Images{" +
                "artCropURL=" + artCropURL +
                ", borderCropURL=" + borderCropURL +
                ", pngURL=" + pngURL +
                ", largeURL=" + largeURL +
                ", normalURL=" + normalURL +
                ", smallURL=" + smallURL +
                '}';
    }

}
