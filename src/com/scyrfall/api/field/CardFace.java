package com.scyrfall.api.field;

import com.scyrfall.api.ScryfallObject;
import com.scyrfall.api.object.Card;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * Magic cards can have multiple faces. The faces could be shown divided on the front of the card as in split cards and
 * flip cards, or the card can be double-sided as in transform cards and double-sided tokens.
 *
 * @see Card#getFaces()
 */
public class CardFace extends ScryfallObject {

    private String artist, flavorText, manaCost, name, oracleText, power, toughness, loyalty, printedName, printedTypeLine, typeLine;
    private Color[] colors, colorIndicator;
    private UUID illustrationID;
    private Images images;


    public CardFace(JSONObject data) {
        super(data);
        artist = getString("artist");
        flavorText = getString("flavor_text");
        manaCost = getString("mana_cost");
        name = getString("name");
        oracleText = getString("oracle_text");
        power = getString("power");
        toughness = getString("toughness");
        loyalty = getString("loyalty");
        printedName = getString("printed_name");
        printedTypeLine = getString("printed_type_line");
        typeLine = getString("type_line");

        illustrationID = getUUID("illustration_id");

        JSONArray colors = getJSONArray("colors");
        this.colors = new Color[colors.length()];
        for (int i = 0; i < colors.length(); i++) {
            this.colors[i] = Color.fromString(colors.getString(i));
        }

        JSONArray colorIndicator = getJSONArray("color_indicator");
        this.colorIndicator = new Color[colorIndicator.length()];
        for (int i = 0; i < colorIndicator.length(); i++) {
            this.colorIndicator[i] = Color.fromString(colorIndicator.getString(i));
        }

        images = new Images(getJSONObject("image_uris"));
    }

    public String getArtist() {
        return artist;
    }

    public String getFlavorText() {
        return flavorText;
    }

    public String getManaCost() {
        return manaCost;
    }

    public String getName() {
        return name;
    }

    public String getOracleText() {
        return oracleText;
    }

    public String getPower() {
        return power;
    }

    public String getToughness() {
        return toughness;
    }

    public String getLoyalty() {
        return loyalty;
    }

    public String getPrintedName() {
        return printedName;
    }

    public String getPrintedTypeLine() {
        return printedTypeLine;
    }

    public String getTypeLine() {
        return typeLine;
    }

    public Color[] getColors() {
        return colors;
    }

    public Color[] getColorIndicator() {
        return colorIndicator;
    }

    public UUID getIllustrationID() {
        return illustrationID;
    }

    public Images getImages() {
        return images;
    }

    @Override
    public String toString() {
        return "CardFace{" +
                "artist='" + artist + '\'' +
                ", flavorText='" + flavorText + '\'' +
                ", manaCost='" + manaCost + '\'' +
                ", name='" + name + '\'' +
                ", oracleText='" + oracleText + '\'' +
                ", power='" + power + '\'' +
                ", toughness='" + toughness + '\'' +
                ", loyalty='" + loyalty + '\'' +
                ", printedName='" + printedName + '\'' +
                ", printedTypeLine='" + printedTypeLine + '\'' +
                ", typeLine='" + typeLine + '\'' +
                ", colors=" + Arrays.toString(colors) +
                ", colorIndicator=" + Arrays.toString(colorIndicator) +
                ", illustrationID=" + illustrationID +
                ", images=" + images +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardFace cardFace = (CardFace) o;
        return Objects.equals(artist, cardFace.artist) &&
                Objects.equals(flavorText, cardFace.flavorText) &&
                Objects.equals(manaCost, cardFace.manaCost) &&
                Objects.equals(name, cardFace.name) &&
                Objects.equals(oracleText, cardFace.oracleText) &&
                Objects.equals(power, cardFace.power) &&
                Objects.equals(toughness, cardFace.toughness) &&
                Objects.equals(loyalty, cardFace.loyalty) &&
                Objects.equals(printedName, cardFace.printedName) &&
                Objects.equals(printedTypeLine, cardFace.printedTypeLine) &&
                Objects.equals(typeLine, cardFace.typeLine) &&
                Arrays.equals(colors, cardFace.colors) &&
                Arrays.equals(colorIndicator, cardFace.colorIndicator) &&
                Objects.equals(illustrationID, cardFace.illustrationID) &&
                Objects.equals(images, cardFace.images);
    }

}
