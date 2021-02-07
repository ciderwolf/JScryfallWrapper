package tests;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ScryfallTest {

    static final double DELTA = 0.4;

    static <T> void assertArrayEqualsIgnoreOrder(T[] arr1, T[] arr2) {
        assertEquals(arr1.length, arr2.length);
        for (T one : arr1) {
            boolean contains = false;
            for (T two : arr2) {
                if (one.equals(two)) {
                    contains = true;
                    break;
                }
            }
            assertTrue(contains);
        }
    }

    static <T> void assertListArrayEqualsIgnoreOrder(List<T> arr1, T[] arr2) {
        assertEquals(arr1.size(), arr2.length);
        for (T one : arr1) {
            boolean contains = false;
            for (T two : arr2) {
                if (one.equals(two)) {
                    contains = true;
                    break;
                }
            }
            assertTrue(contains);
        }
    }

    @SafeVarargs
    static <T> T[] arrayOf(T... values) {
        return values;
    }
}