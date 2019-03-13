package tests;

import com.scyrfall.api.object.Set;
import com.scyrfall.api.object.Set.SetType;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SetTest {

    @Test
    public void allSets() {
        expansion();
        token();
        promo();
    }

    @Test
    public void expansion() {
        System.out.print("Test Expansion (Aether Revolt): ");
        Set set = Set.fromID(UUID.fromString("a4a0db50-8826-4e73-833c-3fd934375f96"));
        assertEquals(set.getBlock(), "Kaladesh");
        assertEquals(set.getBlockCode(), "kld");
        baseSet(set, "aer", "Aether Revolt", SetType.EXPANSION, 194, false, false);
        System.out.println("success");
    }

    @Test
    public void token() {
        System.out.print("Test Token (Hour of Devastation Tokens): ");
        Set set = Set.fromID(UUID.fromString("cb890bc8-ec73-449e-9be0-46891f39eea1"));
        assertEquals(set.getParentSetCode(), "hou");
        assertEquals(set.getBlockCode(), "akh");
        assertEquals(set.getBlock(), "Amonkhet");
        baseSet(set, "thou", "Hour of Devastation Tokens", SetType.TOKEN, 14, false, false);
        System.out.println("success");
    }

    @Test
    public void promo() {
        System.out.print("Test Promo (Magic Online Promos): ");
        Set set = Set.fromID(UUID.fromString("638940fb-6be9-4be3-b83f-68d3902fbbe5"));
        assertEquals(set.getMtgoCode(), "prm");
        baseSet(set, "prm", "Magic Online Promos", SetType.PROMO, 1229, true, true);
        System.out.println("success");
    }

    private void baseSet(Set set, String code, String name, SetType type, int cardCount, boolean digital, boolean foil) {
        assertEquals(set.getCode(), code);
        assertEquals(set.getName(), name);
        assertEquals(set.getSetType(), type);
        assertEquals(set.getCardCount(), cardCount);
        assertEquals(set.isDigital(), digital);
        assertEquals(set.isFoilOnly(), foil);

        assertNotNull(set.getReleased());
        assertNotNull(set.getIconSvgURL());
        assertNotNull(set.getSearchURL());
        assertNotNull(set.getUrl());
        assertNotNull(set.getScryfallURL());

        assertEquals(set, Set.fromCode(set.getCode()));
    }
}
