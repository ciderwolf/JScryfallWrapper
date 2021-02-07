package com.scyrfall.api.object;

import com.scyrfall.api.ScryfallObject;
import com.scyrfall.api.query.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Symbol extends ScryfallObject {

    private String symbol, looseVariant, english, gathererAlternates;
    private boolean transposeable, representsMana, appearsInManaCosts, funny, colorless, monoColored, multiColored;
    private double cmc;
    private URL svgURL;
    private List<Color> colors;

    public Symbol(JSONObject data) {
        super(data);
        symbol = getString("symbol");
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

        svgURL = getURL("svg_uri");

        colors = getList("colors", Color::fromString, JSONArray::getString);
    }

    /**
     * @return The plaintext symbol. Often surrounded with curly braces {}. Note that not all symbols are ASCII text
     * (for example, {∞}).
     */
    public String getSymbol() {
        return symbol;
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
    public List<Color> getColors() {
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
        return new ScryfallList(Query.dataFromPath("symbology")).getContents(new Symbol[0]);
    }

    /**
     * @return True if this is a mana symbol.
     */
    public boolean isRepresentsMana() {
        return representsMana;
    }

    /**
     * @return A url to a SVG that depicts this symbol.
     */
    public URL getSvgURL() {
        return svgURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol1 = (Symbol) o;
        return transposeable == symbol1.transposeable && representsMana == symbol1.representsMana && appearsInManaCosts == symbol1.appearsInManaCosts && funny == symbol1.funny && colorless == symbol1.colorless && monoColored == symbol1.monoColored && multiColored == symbol1.multiColored && Double.compare(symbol1.cmc, cmc) == 0 && Objects.equals(symbol, symbol1.symbol) && Objects.equals(looseVariant, symbol1.looseVariant) && Objects.equals(english, symbol1.english) && Objects.equals(gathererAlternates, symbol1.gathererAlternates) && Objects.equals(svgURL, symbol1.svgURL) && Objects.equals(colors, symbol1.colors);
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "symbol='" + symbol + '\'' +
                ", looseVariant='" + looseVariant + '\'' +
                ", english='" + english + '\'' +
                ", gathererAlternates='" + gathererAlternates + '\'' +
                ", transposeable=" + transposeable +
                ", representsMana=" + representsMana +
                ", appearsInManaCosts=" + appearsInManaCosts +
                ", funny=" + funny +
                ", colorless=" + colorless +
                ", monoColored=" + monoColored +
                ", multiColored=" + multiColored +
                ", cmc=" + cmc +
                ", svgURL=" + svgURL +
                ", colors=" + colors +
                '}';
    }
}
