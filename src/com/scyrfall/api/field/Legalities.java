package com.scyrfall.api.field;

import com.scyrfall.api.ScryfallObject;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * This object is a member of a card object, and this describes the legality of the parent
 * card across all supported formats.
 */
public class Legalities extends ScryfallObject {
    private HashMap<Format, Legality> legalityHashMap;
    public Legalities(JSONObject data) {
        super(data);
        legalityHashMap = new HashMap<>();
        if(!data.isEmpty()) {
            for(String key : data.keySet()) {
                legalityHashMap.put(Format.valueOf(key), Legality.fromString(data.getString(key)));
            }
        }
    }

    /**
     * If this object doesn't contain information about the requested format,
     * this method will return {@link Legality#notLegal}
     * @param format the format to check
     * @return the legality of this card in the given format
     */
    public Legality getFormatLegality(Format format) {
        if(legalityHashMap.containsKey(legalityHashMap)) {
            return legalityHashMap.get(format);
        }
        return Legality.notLegal;
    }

    /**
     * @return a HashMap containing key value pairs for each format, and the parent card's legality
     * in that format.
     */
    public HashMap<Format, Legality> getLegalityMap() {
        return legalityHashMap;
    }

    /**
     * Different legalities a card can have in a given format.
     * The options are: legal, notLegal, restricted, and banned.
     */
    public enum Legality {
        legal, notLegal, restricted, banned;
        private static Legality fromString(String value) {
            if(value.equals("not_legal")) {
                return notLegal;
            }
            else {
                return valueOf(value);
            }
        }
    }

    /**
     * Possible formats for a card to have legality information about. A given card should have a value for
     * each of these fields.
     * The values are: standard, future, frontier, modern, legacy, pauper, vintage, penny, commander, duel, and oldschool.
     * <br>Note: Brawl and 1v1 Commander were removed from Scryfall's database on February 1, 2018.
     */
    public enum Format {
        standard, future, frontier, modern, legacy, pauper, vintage, penny, commander, duel, oldschool
    }
}
