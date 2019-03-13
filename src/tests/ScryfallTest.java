package tests;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ScryfallTest {

    static final double DELTA = 0.4;

    @Test
    public void testAll() {
        System.out.println("Testing Cards");
        new CardTest().allCards();
        System.out.println("Testing Sets");
        new SetTest().allSets();
        System.out.println("Testing Symbols");
        new SymbolTest().allSymbols();
    }

    static void assertArrayEqualsIgnoreOrder(Object[] arr1, Object[] arr2) {
        assertEquals(arr1.length, arr2.length);
        for (Object one : arr1) {
            boolean contains = false;
            for (Object two : arr2) {
                if (one.equals(two)) {
                    contains = true;
                }
            }
            assertTrue(contains);
        }
    }
}