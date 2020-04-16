package com.scyrfall.api.object;

import com.scyrfall.api.ScryfallObject;
import com.scyrfall.api.query.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

/**
 * A Catalog object contains an array of Magic datapoints (words, card values, etc). Catalog objects are provided by the
 * API as aids for building other Magic software and understanding possible values for a field on Card objects.
 */
public class Catalog extends ScryfallObject {

    private URL url;
    private int totalValues;
    private String[] values;

    public Catalog(JSONObject data) {
        super(data);

        url = getURL("uri");
        totalValues = getInt("total_values");

        if (data.has("data")) {
            JSONArray values = getJSONArray("data");
            this.values = new String[values.length()];
            for (int i = 0; i < values.length(); i++) {
                this.values[i] = values.getString(i);
            }
        }
    }

    /**
     * @param identifier the identifier of the catalog to be retrieved
     * @return The catalog located at the specified identifier in Scryfall's API
     * @see Name
     */
    public static Catalog fromIdentifier(Name identifier) {
        return new Catalog(Query.dataFromPath("catalog/" + identifier.toParameterString()));
    }

    /**
     * @return A link to the current catalog on Scryfall’s API.
     */
    public URL getUrl() {
        return url;
    }

    /**
     * @return The number of items in the <code>values</code> array.
     * @see #getValues()
     */
    public int getTotalValues() {
        return totalValues;
    }

    /**
     * @return An array of datapoints, as strings.
     */
    public String[] getValues() {
        return values;
    }

    /**
     * The name of a catalog to be retrieved. All values are updated as soon as a new card is entered for spoiler seasons.
     * <table><thead><tr><th>Name</th><th>Description</th></tr></thead>
     * <tbody><tr><td><p><code>CARD_NAMES</code></p></td><td><p>A list of all nontoken English card names in Scryfall’s database.</p>
     * </td></tr><tr><td><p><code>ARTIST_NAMES</code></p></td><td><p>A list of all canonical artist names in Scryfall’s
     * database. This catalog won’t include duplicate, misspelled, or FUNNY names for artists. </p>
     * </td></tr><tr><td><p><code>WORD_BANK</code></p></td><td><p>A list of all English words, of length 2 or more, that
     * could appear in a card name. Values are drawn from cards currently in Scryfall’s database. </p>
     * </td></tr><tr><td><p><code>CREATURE_TYPES</code></p></td><td><p>A list of all creature types in Scryfall’s database.</p>
     * </td></tr><tr><td><p><code>PLANESWALKER_TYPES</code></p></td><td><p>A list of all Planeswalker types in Scryfall’s database. </p>
     * </td></tr><tr><td><p><code>LAND_TYPES</code></p></td><td><p>A list of all Land types in Scryfall’s database. </p>
     * </td></tr><tr><td><p><code>ARTIFACT_TYPES</code></p></td><td><p>A list of all artifact types in Scryfall’s database. </p>
     * </td></tr><tr><td><p><code>ENCHANTMENT_TYPES</code></p></td><td><p>A list of all enchantment types in Scryfall’s database. </p>
     * </td></tr><tr><td><p><code>SPELL_TYPES</code></p></td><td><p>A list of all spell types in Scryfall’s database. </p>
     * </td></tr><tr><td><p><code>POWERS</code></p></td><td><p>A list of all possible values for a creature or vehicle’s
     * power in Scryfall’s database. </p>
     * </td></tr><tr><td><p><code>TOUGHNESSES</code></p></td><td><p>A list of all possible values for a creature or
     * vehicle’s toughness in Scryfall’s database</p>
     * </td></tr><tr><td><p><code>LOYALTIES</code></p></td><td><p>A list of all possible values for a Planeswalker’s
     * loyalty in Scryfall’s database. </p>
     * </td></tr><tr><td><p><code>WATERMARKS</code></p></td><td><p>A list of all card WATERMARKS in Scryfall’s database</p>
     * </td></tr></tbody></table>
     */
    @SuppressWarnings("unused")
    public enum Name {
        CARD_NAMES, ARTIST_NAMES, WORD_BANK, CREATURE_TYPES, PLANESWALKER_TYPES, LAND_TYPES, ARTIFACT_TYPES, ENCHANTMENT_TYPES,
        SPELL_TYPES, POWERS, TOUGHNESSES, LOYALTIES, WATERMARKS;

        public String toParameterString() {
            return this.toString().toLowerCase();
        }
    }

    @Override
    public String toString() {
        return "Catalog{" +
                "url=" + url +
                ", totalValues=" + totalValues +
                ", values=" + Arrays.toString(values) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Catalog catalog = (Catalog) o;
        return totalValues == catalog.totalValues &&
                Objects.equals(url, catalog.url) &&
                Arrays.equals(values, catalog.values);
    }

}
