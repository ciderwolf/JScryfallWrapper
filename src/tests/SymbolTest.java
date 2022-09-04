package tests;

import com.scyrfall.api.ScryfallObject.Color;
import com.scyrfall.api.field.ManaCost;
import com.scyrfall.api.object.Symbol;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static tests.ScryfallTest.arrayOf;
import static tests.ScryfallTest.assertArrayEqualsIgnoreOrder;

public class SymbolTest {

    @Test
    public void symbols() {
        Symbol[] symbols = Symbol.getSymbols();
        final double totalCost = 1000353.5; // subtract 1 because Infinity's cmc = -1
        double sumCost = 0;
        for (Symbol s : symbols) {
            assertEquals(s.isFunny(), isFunny(s));
            assertEquals(representsMana(s), s.representsMana());
            sumCost += s.getCmc();
        }
        assertEquals(sumCost, totalCost, ScryfallTest.DELTA);
    }

    private boolean isFunny(Symbol symbol) {
        return symbol.getCmc() == 0.5 || symbol.getCmc() > 99 || symbol.getLooseVariant().equals("Y") ||
                symbol.getLooseVariant().equals("Z") || symbol.getCmc() == -1 || symbol.getSymbol().equals("{A}") ||
                symbol.getSymbol().equals("{TIX}");
    }

    private boolean representsMana(Symbol symbol) {
        return symbol.getEnglish().contains("mana");
    }

    @Test
    public void costs() {
        baseCost(ManaCost.parseManaCost("{2}{g}{2}"), "{4}{G}", 5.0, arrayOf(Color.GREEN), false,
                true, false);
        baseCost(ManaCost.parseManaCost("XURW"), "{X}{U}{R}{W}", 3.0, arrayOf(Color.WHITE, Color.BLUE, Color.RED),
                false, false, true);
        baseCost(ManaCost.parseManaCost("½CC"), "{½}{C}{C}", 2.5, arrayOf(), true, false, false);
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
}
