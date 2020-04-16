package tests;

import com.scyrfall.api.object.Set;
import com.scyrfall.api.object.Set.SetType;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SetTest {

    @Test
    public void expansion() {
        System.out.print("Test Expansion (Aether Revolt): ");
        Set set = Set.fromID(UUID.fromString("a4a0db50-8826-4e73-833c-3fd934375f96"));
        assertEquals(set.getBlock(), "Kaladesh");
        assertEquals(set.getBlockCode(), "kld");
        baseSet(set, 1857, "aer", "Aether Revolt", SetType.EXPANSION, 194, false, false);
        System.out.println("success");
    }

    @Test
    public void token() {
        System.out.print("Test Token (Hour of Devastation Tokens): ");
        Set set = Set.fromID(UUID.fromString("cb890bc8-ec73-449e-9be0-46891f39eea1"));
        assertEquals(set.getParentSetCode(), "hou");
        assertEquals(set.getBlockCode(), "akh");
        assertEquals(set.getBlock(), "Amonkhet");
        baseSet(set, -1,"thou", "Hour of Devastation Tokens", SetType.TOKEN, 14, false, false);
        System.out.println("success");
    }

    @Test
    public void fromTheVault() {
        System.out.print("Test From the Vault (FTV Transform): ");
        Set set = Set.fromID(UUID.fromString("63c89a12-d115-4084-a4af-fceef40ca02f"));
        baseSet(set, 2078,"v17", "From the Vault: Transform", SetType.FROM_THE_VAULT, 16, false, true);
        System.out.println("success");
    }


    @Test
    public void digital() {
        System.out.print("Test Digital (Vintage Masters): ");
        Set set = Set.fromID(UUID.fromString("a944551a-73fa-41cd-9159-e8d0e4674403"));
        assertEquals(set.getMtgoCode(), "vma");
        baseSet(set, -1, "vma", "Vintage Masters", SetType.MASTERS, 325, true, false);
        System.out.println("success");
    }

    private void baseSet(Set set, int tcgPlayerId, String code, String name, SetType type, int cardCount, boolean digital, boolean foil) {
        assertEquals(set.getCode(), code);
        assertEquals(set.getName(), name);
        assertEquals(set.getSetType(), type);
        assertEquals(set.getCardCount(), cardCount);
        assertEquals(set.isDigital(), digital);
        assertEquals(set.isFoilOnly(), foil);
        assertEquals(set.getTcgPlayerID(), tcgPlayerId);
        assertNotNull(set.getReleased());
        assertNotNull(set.getIconSvgURL());
        assertNotNull(set.getSearchURL());
        assertNotNull(set.getUrl());
        assertNotNull(set.getScryfallURL());

        assertEquals(set, Set.fromCode(set.getCode()));
    }
}
