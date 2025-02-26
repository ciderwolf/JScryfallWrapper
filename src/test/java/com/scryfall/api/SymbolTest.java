package com.scryfall.api;

import com.scyrfall.api.ScryfallObject.Color;
import com.scyrfall.api.field.ManaCost;
import com.scyrfall.api.object.ScryfallList;
import com.scyrfall.api.object.Symbol;
import org.json.JSONObject;
import org.junit.Test;

import static com.scryfall.api.ScryfallTest.*;
import static org.junit.Assert.assertEquals;

public class SymbolTest {

    @Test
    public void symbols() {
        Symbol[] symbols = loadTestSymbols();
        final double totalCost = 1000360.5; // subtract 1 because Infinity's cmc = -1
        double sumCost = 0;
        for (Symbol s : symbols) {
            assertEquals(s.isFunny(), isFunny(s));
            assertEquals(representsMana(s), s.representsMana());
            sumCost += s.getManaValue();
        }
        assertEquals(totalCost, sumCost, ScryfallTest.DELTA);
    }

    private boolean isFunny(Symbol symbol) {
        return symbol.getManaValue() == 0.5 || symbol.getManaValue() > 99 || symbol.getLooseVariant().equals("Y") ||
                symbol.getLooseVariant().equals("Z") || symbol.getManaValue() == -1 || symbol.getSymbol().equals("{A}") ||
                symbol.getSymbol().equals("{TIX}") || symbol.getSymbol().equals("{C/P}") || symbol.getSymbol().equals("{L}") ||
                symbol.getSymbol().equals("{D}");
    }

    private boolean representsMana(Symbol symbol) {
        return symbol.getEnglish().contains("mana");
    }

    @Test
    public void costs() {
        baseCost(loadTestManaCost("2g2"), "{4}{G}", 5.0, arrayOf(Color.GREEN), false,
                true, false);
        baseCost(loadTestManaCost("XURW"), "{X}{U}{R}{W}", 3.0, arrayOf(Color.WHITE, Color.BLUE, Color.RED),
                false, false, true);
        baseCost(loadTestManaCost("halfcc"), "{½}{C}{C}", 2.5, arrayOf(), true, false, false);
    }

    private void baseCost(ManaCost symbol, String cost, double cmc, Color[] colors, boolean colorless,
                          boolean monoColored, boolean multiColored) {

        assertEquals(symbol.getCost(), cost);
        assertEquals(symbol.getCmc(), cmc, ScryfallTest.DELTA);
        assertArrayEqualsIgnoreOrder(symbol.getColors(), colors);
        assertEquals(symbol.isColorless(), colorless);
        assertEquals(symbol.isMonoColored(), monoColored);
        assertEquals(symbol.isMultiColored(), multiColored);
    }

    private Symbol[] loadTestSymbols() {
        JSONObject json = loadTestJson("symbology");
        ScryfallList list = new ScryfallList(json);
        return list.getContents(new Symbol[0]);
    }

    private ManaCost loadTestManaCost(String cost) {
        JSONObject json = loadTestJson("symbology-" + cost);
        return new ManaCost(json);
    }
}
