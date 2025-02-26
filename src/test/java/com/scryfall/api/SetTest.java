package com.scryfall.api;

import com.scyrfall.api.object.Set;
import com.scyrfall.api.object.Set.SetType;
import org.json.JSONObject;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SetTest {

    @Test
    public void expansion() {
        System.out.print("Test Expansion (Aether Revolt): ");
        Set set = loadTestSet("aer");
        assertEquals("Kaladesh", set.getBlock());
        assertEquals("kld", set.getBlockCode());
        baseSet(set, 1857, "aer", "Aether Revolt", SetType.EXPANSION, 194, false, false);
        System.out.println("success");
    }

    @Test
    public void token() {
        System.out.print("Test Token (Hour of Devastation Tokens): ");
        Set set = loadTestSet("thou");
        assertEquals("hou", set.getParentSetCode());
        assertEquals("akh", set.getBlockCode());
        assertEquals("Amonkhet", set.getBlock());
        baseSet(set, -1,"thou", "Hour of Devastation Tokens", SetType.TOKEN, 14, false, false);
        System.out.println("success");
    }

    @Test
    public void fromTheVault() {
        System.out.print("Test From the Vault (FTV Transform): ");
        Set set = loadTestSet("v17");
        baseSet(set, 2078,"v17", "From the Vault: Transform", SetType.FROM_THE_VAULT, 16, false, true);
        System.out.println("success");
    }


    @Test
    public void digital() {
        System.out.print("Test Digital (Vintage Masters): ");
        Set set = loadTestSet("vma");
        assertEquals("vma", set.getMtgoCode());
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

    private Set loadTestSet(String name) {
        JSONObject json = ScryfallTest.loadTestJson(name);
        return new Set(json);
    }
}
