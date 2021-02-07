package com.scyrfall.api.field;

import com.scyrfall.api.ScryfallObject;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

/**
 * This object is a member of a card object, and this describes the legality of the parent
 * card across all supported formats.
 */
public class Legalities extends ScryfallObject {
    private HashMap<Format, Legality> legalityHashMap;

    public Legalities(JSONObject data) {
        super(data);
        legalityHashMap = new HashMap<>();
        if (!data.isEmpty()) {
            for (String key : data.keySet()) {
                legalityHashMap.put(Format.fromString(key), Legality.fromString(data.getString(key)));
            }
        }
    }

    /**
     * If this object doesn't contain information about the requested format,
     * this method will return {@link Legality#NOT_LEGAL}
     *
     * @param format the format to check
     * @return the legality of this card in the given format
     */
    public Legality getFormatLegality(Format format) {
        if (legalityHashMap.containsKey(format)) {
            return legalityHashMap.get(format);
        }
        return Legality.NOT_LEGAL;
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
     * The options are: LEGAL, NOT_LEGAL, RESTRICTED, and BANNED.
     */
    @SuppressWarnings("unused")
    public enum Legality {
        LEGAL, NOT_LEGAL, RESTRICTED, BANNED;

        private static Legality fromString(String value) {
            return valueOf(value.toUpperCase());
        }
    }

    /**
     * Possible formats for a card to have legality information about. A given card should have a value for
     * each of these fields.
     * The values are: STANDARD, FUTURE, BRAWL, HISTORIC, PIONEER, MODERN, LEGACY, PAUPER, VINTAGE, PENNY_DREADFUL,
     * COMMANDER, DUEL, and OLDSCHOOL.
     */
    @SuppressWarnings("unused")
    public enum Format {
        STANDARD, FUTURE, BRAWL, HISTORIC, GLADIATOR, PIONEER, MODERN, LEGACY, PAUPER, VINTAGE, PENNY_DREADFUL, COMMANDER, DUEL, OLDSCHOOL, PREMODERN;

        public static Format fromString(String value) {
            if (value.equalsIgnoreCase("penny")) {
                return PENNY_DREADFUL;
            } else {
                return valueOf(value.toUpperCase());
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Legalities that = (Legalities) o;
        return Objects.equals(legalityHashMap, that.legalityHashMap);
    }

    @Override
    public String toString() {
        return "Legalities{" +
                "legalityHashMap=" + legalityHashMap +
                '}';
    }
}
