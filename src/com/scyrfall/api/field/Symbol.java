package com.scyrfall.api.field;

import com.scyrfall.api.ScryfallObject;
import com.scyrfall.api.object.List;
import com.scyrfall.api.query.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;

public class Symbol extends ScryfallObject {

    private String cost, looseVariant, english, gathererAlternates;
    private boolean transposeable, representsMana, appearsInManaCosts, funny, colorless, monoColored, multiColored;
    private double cmc;
    private Color[] colors;

    public Symbol(JSONObject data) {
        super(data);
        cost = getString("cost");
        looseVariant = getString("loose_variant");
        english = getString("english");
        gathererAlternates = getString("gatherer_alternates");

        transposeable = getBoolean("transposable");
        representsMana = getBoolean("represents_mana");
        appearsInManaCosts = getBoolean("appears_in_mana_costs");
        funny = getBoolean("funny");
        colorless = getBoolean("colorless");
        monoColored = getBoolean("monocolored");
        multiColored = getBoolean("multicolored");

        cmc = getDouble("cmc");

        JSONArray colors = getJSONArray("colors");
        this.colors = new Color[colors.length()];
        for(int i = 0; i < colors.length(); i++) {
            this.colors[i] = Color.fromString(colors.getString(i));
        }
    }

    /**
     * @return The plaintext symbol. Often surrounded with curly braces {}. Note that not all symbols are ASCII text
     * (for example, {∞}).
     */
    public String getCost() {
        return cost;
    }

    /**
     * @return An alternate version of this symbol, if it is possible to write it without curly braces.
     */
    public String getLooseVariant() {
        return looseVariant;
    }

    /**
     * @return An English snippet that describes this symbol. Appropriate for use in <code>alt</code> text or other
     * accessible communication formats.
     */
    public String getEnglish() {
        return english;
    }

    /**
     * @return An array of plaintext versions of this symbol that Gatherer uses on OLD cards to describe ORIGINAL
     * printed text. For example: {W} has ["oW", "ooW"] as alternates.
     */
    public String getGathererAlternates() {
        return gathererAlternates;
    }

    /**
     * @return True if it is possible to write this symbol “backwards”. For example, the official symbol {U/P} is
     * sometimes written as {P/U} or {P\U} in informal settings. Note that the Scryfall API never writes symbols
     * backwards in other responses. This field is provided for informational purposes.
     */
    public boolean isTransposeable() {
        return transposeable;
    }

    /**
     * @return True if this is a mana symbol.
     */
    public boolean representsMana() {
        return representsMana;
    }

    /**
     * @return True if this symbol appears in a mana cost on any Magic card. For example {20} has this field set to
     * false because {20} only appears in Oracle text, not mana costs.
     */
    public boolean isAppearsInManaCosts() {
        return appearsInManaCosts;
    }

    /**
     * @return True if this symbol is only used on FUNNY cards or Un-cards.
     */
    public boolean isFunny() {
        return funny;
    }

    /**
     * @return A decimal number representing this symbol’s converted mana cost. Note that mana symbols from FUNNY sets
     * can have fractional converted mana costs.
     */
    public double getCmc() {
        return cmc;
    }

    /**
     * @return An array of colors that this symbol represents.
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
     * @return an array of all card symbols.
     */
    public static Symbol[] getSymbols() {
        return new List(Query.dataFromPath("symbology")).getContents(new Symbol[0]);
    }

    /**
     * Parses the given mana cost parameter and returns Scryfall’s interpretation.
     *
     * The server understands most community shorthand for mana costs (such as 2WW for {2}{W}{W}). Symbols can also be
     * out of order, lowercase, or have multiple colorless costs (such as 2{g}2 for {4}{G}).
     * @param mana The mana string to parse.
     * @return A Symbol object describing the parsed string.
     */
    public static Symbol parseManaString(String mana) {
        return new Symbol(Query.dataFromPath("symbology/parse-mana?cost=" + mana));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol1 = (Symbol) o;
        return transposeable == symbol1.transposeable &&
                representsMana == symbol1.representsMana &&
                appearsInManaCosts == symbol1.appearsInManaCosts &&
                funny == symbol1.funny &&
                Double.compare(symbol1.cmc, cmc) == 0 &&
                Objects.equals(cost, symbol1.cost) &&
                Objects.equals(looseVariant, symbol1.looseVariant) &&
                Objects.equals(english, symbol1.english) &&
                Objects.equals(gathererAlternates, symbol1.gathererAlternates) &&
                Arrays.equals(colors, symbol1.colors);
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "cost='" + cost + '\'' +
                ", looseVariant='" + looseVariant + '\'' +
                ", english='" + english + '\'' +
                ", gathererAlternates='" + gathererAlternates + '\'' +
                ", transposeable=" + transposeable +
                ", representsMana=" + representsMana +
                ", appearsInManaCosts=" + appearsInManaCosts +
                ", funny=" + funny +
                ", cmc=" + cmc +
                ", colors=" + Arrays.toString(colors) +
                '}';
    }
}
