package com.scyrfall.api.field;

import com.scyrfall.api.ScryfallObject;
import com.scyrfall.api.query.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;

public class ManaCost extends ScryfallObject {

    private final String cost;
    private final double cmc;
    private final Color[] colors;
    private final boolean colorless;
    private final boolean monoColored;
    private final boolean multiColored;

    public ManaCost(JSONObject data) {
        super(data);
        cost = getString("cost");
        cmc = getDouble("cmc");
        colorless = getBoolean("colorless");
        monoColored = getBoolean("monocolored");
        multiColored = getBoolean("multicolored");

        JSONArray colors = getJSONArray("colors");
        this.colors = new Color[colors.length()];
        for (int i = 0; i < colors.length(); i++) {
            this.colors[i] = Color.fromString(colors.getString(i));
        }
    }

    /**
     * @return The normalized cost, with correctly-ordered and wrapped mana symbols.
     */
    public String getCost() {
        return cost;
    }

    /**
     * @return The converted mana cost. If you submit Un-set mana symbols, this decimal could include fractional parts.
     */
    public double getCmc() {
        return cmc;
    }

    /**
     * @return The colors of the given cost.
     */
    public Color[] getColors() {
        return colors;
    }

    /**
     * @return True if the cost is colorless.
     */
    public boolean isColorless() {
        return colorless;
    }

    /**
     * @return True if the cost is monocolored.
     */
    public boolean isMonoColored() {
        return monoColored;
    }

    /**
     * @return True if the cost is multicolored.
     */
    public boolean isMultiColored() {
        return multiColored;
    }

    /**
     * Parses the given mana cost parameter and returns Scryfall’s interpretation.
     *
     * <p>The server understands most community shorthand for mana costs (such as <code>2WW</code> for
     * <code>{2}{W}{W}</code>). Symbols can also be out of order, lowercase, or have multiple colorless costs (such as
     * <code>2{g}2</code> for <code>{4}{G}</code>).</p>
     *
     * @param mana The mana string to parse.
     * @return A Symbol object describing the parsed string.
     */
    public static ManaCost parseManaCost(String mana) {
        return new ManaCost(Query.dataFromPath("symbology/parse-mana?cost=" + mana));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManaCost manaCost = (ManaCost) o;
        return Double.compare(manaCost.cmc, cmc) == 0 &&
                colorless == manaCost.colorless &&
                monoColored == manaCost.monoColored &&
                multiColored == manaCost.multiColored &&
                Objects.equals(cost, manaCost.cost) &&
                Arrays.equals(colors, manaCost.colors);
    }

    @Override
    public String toString() {
        return "ManaCost{" +
                "cost='" + cost + '\'' +
                ", cmc=" + cmc +
                ", colors=" + Arrays.toString(colors) +
                ", colorless=" + colorless +
                ", monoColored=" + monoColored +
                ", multiColored=" + multiColored +
                '}';
    }
}
