package tests;

import com.scyrfall.api.ScryfallObject.Color;
import com.scyrfall.api.field.Symbol;
import org.junit.Test;

import static org.junit.Assert.*;
import static tests.ScryfallTest.assertArrayEqualsIgnoreOrder;

public class SymbolTest {

    @Test
    public void allSymbols() {
        symbols();
        costs();
    }

    @Test
    public void symbols() {
        Symbol[] symbols = Symbol.getSymbols();
        final double totalCost = 1000343.5; // subtract 1 because Infinity's cmc = -1
        double sumCost = 0;
        for(Symbol s : symbols) {
            assertEquals(s.isFunny(), isFunny(s));
            sumCost += s.getCmc();
        }
        assertEquals(sumCost, totalCost, ScryfallTest.DELTA);
    }

    private boolean isFunny(Symbol symbol) {
        return symbol.getCmc() == 0.5 || symbol.getCmc() > 99 || symbol.getLooseVariant().equals("Y") ||
                symbol.getLooseVariant().equals("Z") || symbol.getCmc() == -1;
    }

    @Test
    public void costs() {
        baseCost(Symbol.parseManaString("{2}{g}{2}"), "{4}{G}", 5.0, new Color[] {Color.GREEN}, false,
                true, false);
        baseCost(Symbol.parseManaString("XURW"), "{X}{U}{R}{W}", 3.0, new Color[] {Color.WHITE, Color.BLUE, Color.RED},
                false, false, true);
        baseCost(Symbol.parseManaString("½CC"), "{½}{C}{C}", 2.5, new Color[]{}, true, false, false);
    }

    private void baseCost(Symbol symbol, String cost, double cmc, Color[] colors, boolean colorless,
                           boolean monoColored, boolean multiColored) {

        assertEquals(symbol.getCost(), cost);
        assertEquals(symbol.getCmc(), cmc, ScryfallTest.DELTA);
        assertArrayEqualsIgnoreOrder(symbol.getColors(), colors);
        assertEquals(symbol.isColorless(), colorless);
        assertEquals(symbol.isMonoColored(), monoColored);
        assertEquals(symbol.isMultiColored(), multiColored);
    }
}
