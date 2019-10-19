package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ScryfallTest {

    static final double DELTA = 0.4;

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