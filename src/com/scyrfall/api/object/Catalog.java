package com.scyrfall.api.object;

import com.scyrfall.api.query.Query;
import com.scyrfall.api.ScryfallObject;
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

        if(data.has("data")) {
            JSONArray values = getJSONArray("data");
            this.values = new String[values.length()];
            for(int i = 0; i < values.length(); i++) {
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
     * <tbody><tr><td><p><code>cardNames</code></p></td><td><p>A list of all nontoken English card names in Scryfall’s database.</p>
     * </td></tr><tr><td><p><code>artistNames</code></p></td><td><p>A list of all canonical artist names in Scryfall’s
     * database. This catalog won’t include duplicate, misspelled, or funny names for artists. </p>
     * </td></tr><tr><td><p><code>wordBank</code></p></td><td><p>A list of all English words, of length 2 or more, that
     * could appear in a card name. Values are drawn from cards currently in Scryfall’s database. </p>
     * </td></tr><tr><td><p><code>creatureTypes</code></p></td><td><p>A list of all creature types in Scryfall’s database.</p>
     * </td></tr><tr><td><p><code>planeswalkerTypes</code></p></td><td><p>A list of all Planeswalker types in Scryfall’s database. </p>
     * </td></tr><tr><td><p><code>landTypes</code></p></td><td><p>A list of all Land types in Scryfall’s database. </p>
     * </td></tr><tr><td><p><code>artifactTypes</code></p></td><td><p>A list of all artifact types in Scryfall’s database. </p>
     * </td></tr><tr><td><p><code>enchantmentTypes</code></p></td><td><p>A list of all enchantment types in Scryfall’s database. </p>
     * </td></tr><tr><td><p><code>spellTypes</code></p></td><td><p>A list of all spell types in Scryfall’s database. </p>
     * </td></tr><tr><td><p><code>powers</code></p></td><td><p>A list of all possible values for a creature or vehicle’s
     * power in Scryfall’s database. </p>
     * </td></tr><tr><td><p><code>toughnesses</code></p></td><td><p>A list of all possible values for a creature or
     * vehicle’s toughness in Scryfall’s database</p>
     * </td></tr><tr><td><p><code>loyalties</code></p></td><td><p>A list of all possible values for a Planeswalker’s
     * loyalty in Scryfall’s database. </p>
     * </td></tr><tr><td><p><code>watermarks</code></p></td><td><p>A list of all card watermarks in Scryfall’s database</p>
     * </td></tr></tbody></table>
     */
    public enum Name {
        cardNames, artistNames, wordBank, creatureTypes, planeswalkerTypes, landTypes, artifactTypes, enchantmentTypes,
        spellTypes, powers, toughnesses, loyalties, watermarks;

        public String toParameterString() {
            StringBuilder output = new StringBuilder();
            for(char c : toString().toCharArray()) {
                if(Character.isUpperCase(c)) {
                    output.append("-").append(Character.toLowerCase(c));
                }
                else {
                    output.append(c);
                }
            }
            return output.toString();
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
