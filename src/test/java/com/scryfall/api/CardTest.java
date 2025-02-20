package com.scryfall.api;

import com.scyrfall.api.ScryfallObject.Color;
import com.scyrfall.api.field.Images;
import com.scyrfall.api.object.Card;
import com.scyrfall.api.object.Card.*;
import com.scyrfall.api.object.Card.Frame;
import com.scyrfall.api.object.ScryfallError;
import org.json.JSONObject;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static com.scryfall.api.ScryfallTest.*;

public class CardTest {

    private final JSONObject emptyJSONObject = new JSONObject("{}");

    @Test
    public void random() {
        Card card = Card.random();
        assertNotEquals(card.getName(), "");
        assertNotEquals(card.getCmc(), -1.0);
        assertNotNull(card.getLegalities());
        assertNotNull(card.getPrices());
        assertNotNull(card.getPrintsSearchURL());
        assertNotNull(card.getRulingsURL());
        assertNotNull(card.getScryfallURL());
        assertNotNull(card.getUrl());
        assertNotNull(card.getSetURL());
        assertNotNull(card.getPurchaseURLs());
        assertNotNull(card.getRelatedURLs());
        assertNotNull(card.getReleaseDate());
        assertNotNull(card.getPrintsSearchURL());
    }

    @Test
    public void nonexistent() {
        Card card = Card.namedExactly("Lightnin Bolt");
        assertTrue(card.isError());
        ScryfallError error = card.getError();
        assertNotNull(error);

        assertEquals(error.getCode(), "not_found");
        assertEquals(error.getStatus(), 404);
    }

    @Test
    public void foreignLanguage() {
        // Spanish Wheel of Fate
        Card card = Card.fromID(UUID.fromString("481e9f7a-ecbb-4697-9c2e-c30967015eff"));

        assertEquals(card.getPrintedName(), "Rueda del destino");
        assertEquals(card.getPrintedText(), "Suspender 4—{1}{R}. (En lugar de lanzar esta carta de tu mano, " +
                "paga {1}{R} y exíliala con cuatro contadores de tiempo sobre ella. Al comienzo de tu mantenimiento, " +
                "remueve un contador de tiempo. Cuando remuevas el último, lánzala sin pagar su coste de maná.)\nCada" +
                " jugador descarta su mano, luego roba siete cartas.");
        assertEquals(card.getPrintedTypeLine(), "Conjuro");
        assertEquals(card.getColorIndicator(), List.of(Color.RED));
        // edhrec rank can change over time, so just check that it has been set to some value, and didn't default to -1
        assertNotEquals(card.getEdhrecRank(), -1);
        assertEquals(card.getCardBackID(), UUID.fromString("0aeebaf5-8c7d-4636-9e82-8c27447861f7"));

    }

    @Test
    public void pony() {
        Card card = Card.fromID(UUID.fromString("5646ea19-0025-4f88-ad22-36968a1d3b89"));
        assertEquals(card.getFrameEffects().get(1), FrameEffect.WAXING_AND_WANING_MOON_DFC);
    }


    @Test
    public void funny() {
        // Accessories to Murder
        Card card = Card.fromID(UUID.fromString("e8ca02a0-5acf-4d89-847b-bad0d7560682"));
        assertEquals(card.getWatermark(), "agentsofsneak");
        assertEquals(card.getArtist(), "Ralph Horsley");
        assertEquals(card.getMultiverseIDs(), List.of(439556));
        assertEquals(card.getTcgplayerID(), 153169);
        assertEquals(card.getManaCost(), "");
        assertFalse(card.isVariation());
        assertEquals(card.getIllustrationID(), UUID.fromString("c2377b58-4b29-4671-b43c-a9d90a3e288c"));
        assertTrue(card.isInBoosters());
        assertFalse(card.isTextless());

        basicCard(card, UUID.fromString("e8ca02a0-5acf-4d89-847b-bad0d7560682"), "en",
                UUID.fromString("fe6726a1-2aa9-492d-82f8-9e9b3125a991"), 0.0, arrayOf(),
                "Accessories to Murder", true, Layout.NORMAL, false, false,
                "Artifact — Contraption", BorderColor.BORDERLESS, "167",
                false, arrayOf(), Frame.M15, false, arrayOf(Game.PAPER),
                true, Rarity.UNCOMMON, false, "ust");

    }

    @Test
    public void vanguard() {
        // Ashling the Pilgrim Avatar
        Card card = Card.fromID(UUID.fromString("aa4ca825-7e52-4f26-9ab8-b68a7e294182"));
        assertEquals(card.getLifeModifier(), "+6");
        assertEquals(card.getHandModifier(), "-1");
        basicCard(card, UUID.fromString("aa4ca825-7e52-4f26-9ab8-b68a7e294182"), "en",
                UUID.fromString("f6c3facb-84ac-4dd9-bce9-6d2779c7f14e"), 0.0, arrayOf(),
                "Ashling the Pilgrim Avatar", true, Layout.VANGUARD, false, false,
                "Vanguard", BorderColor.BLACK, "73",
                true, arrayOf(), Frame.M15, false, arrayOf(Game.MTGO),
                true, Rarity.RARE, false, "pmoa");
    }

    @Test
    public void transform() {
        // Delver of Secrets
        Card card = Card.fromID(UUID.fromString("11bf83bb-c95b-4b4f-9a56-ce7a1816307a"));
        assertEquals(card.getFaces().get(1).getName(), "Insectile Aberration");
        assertTrue(card.hasMultipleFaces());
        assertEquals(card.getImageURI(Images.Size.NORMAL), card.getFaces().get(0).getImages().getNormalURL().toString());
        assertEquals(card.getImages(), new Images(emptyJSONObject));
        assertEquals(card.getManaCost(), "");
        assertEquals(card.getMtgoID(), 42436);
        assertEquals(card.getMtgoFoilID(), 42437);

        basicCard(card, UUID.fromString("11bf83bb-c95b-4b4f-9a56-ce7a1816307a"), "en",
                UUID.fromString("edd531b9-f615-4399-8c8c-1c5e18c4acbf"), 1.0, arrayOf(Color.BLUE),
                "Delver of Secrets // Insectile Aberration", true, Layout.TRANSFORM, false, false,
                "Creature — Human Wizard // Creature — Human Insect", BorderColor.BLACK, "51",
                false, arrayOf(FrameEffect.SUN_MOON_DFC), Frame.MODERN, false, arrayOf(Game.MTGO, Game.PAPER),
                true, Rarity.COMMON, false, "isd");
    }

    @Test
    public void basic() {
        // Absorb
        Card card = Card.fromID(UUID.fromString("c1a316a5-04a3-4128-8d62-58192e2265a5"));
        assertEquals(card.getManaCost(), "{W}{U}{U}");

        basicCard(card, UUID.fromString("c1a316a5-04a3-4128-8d62-58192e2265a5"), "fr",
                UUID.fromString("132ca99a-a3c7-4ed6-b4d0-0edcd7140ca2"), 3.0, arrayOf(Color.BLUE, Color.WHITE),
                "Absorb", true, Layout.NORMAL, false, false, "Instant", BorderColor.BLACK, "151",
                false, arrayOf(), Frame.M15, false, arrayOf(Game.ARENA, Game.MTGO, Game.PAPER), false,
                Rarity.RARE, true, "rna");
    }

    @Test
    public void oversized() {
        // Sliver Queen
        Card card = Card.fromID(UUID.fromString("ab68bd00-7151-4a6b-ad98-134ca02d7d59"));
        assertEquals(card.getManaCost(), "{W}{U}{B}{R}{G}");
        assertEquals(card.getPower(), "7");
        assertEquals(card.getToughness(), "7");
        assertArrayEqualsIgnoreOrder(card.getTypes(), new String[]{"Creature"});
        assertArrayEqualsIgnoreOrder(card.getSubtypes(), new String[]{"Sliver"});
        assertArrayEqualsIgnoreOrder(card.getSupertypes(), new String[]{"Legendary"});

        basicCard(card, UUID.fromString("ab68bd00-7151-4a6b-ad98-134ca02d7d59"), "en",
                UUID.fromString("b8376cca-ea96-478a-8e98-c4482031300a"), 5.0, arrayOf(Color.BLACK, Color.GREEN, Color.RED, Color.BLUE, Color.WHITE),
                "Sliver Queen", false, Layout.NORMAL, true, true, "Legendary Creature — Sliver",
                BorderColor.BLACK, "9", false, arrayOf(), Frame.MODERN, false, arrayOf(Game.PAPER),
                true, Rarity.RARE, true, "ocm1");
        System.out.println("success");
    }

    @Test
    public void borderless() {
        // Unstable Forest
        Card card = Card.fromID(UUID.fromString("f8772631-d4a1-440d-ac89-ac6659bdc073"));
        assertEquals(card.getManaCost(), "");
        assertListArrayEqualsIgnoreOrder(card.getColors(), arrayOf());
        basicCard(card, UUID.fromString("f8772631-d4a1-440d-ac89-ac6659bdc073"), "en",
                UUID.fromString("b34bb2dc-c1af-4d77-b0b3-a0fb342a5fc6"), 0.0, arrayOf(Color.GREEN),
                "Forest", true, Layout.NORMAL, false, false, "Basic Land — Forest",
                BorderColor.BORDERLESS, "216", false, arrayOf(), Frame.M15, true, arrayOf(Game.PAPER),
                true, Rarity.COMMON, true, "ust");
    }

    private void basicCard(Card card, UUID id, String lang, UUID oracleID, double cmc, Color[] colorIdentity,
                           String name, boolean nonfoil, Layout layout, boolean oversized, boolean reserved, String typeline,
                           Card.BorderColor borderColor, String collectorNumber, boolean digital, Card.FrameEffect[] effects,
                           Card.Frame frame, boolean fullArt, Card.Game[] games, boolean hiRes, Card.Rarity rarity, boolean reprint, String set) {
        assertEquals(card.getId(), id);
        assertEquals(card.getLang(), lang);
        assertEquals(card.getOracleID(), oracleID);
        assertEquals(card.getCmc(), cmc, ScryfallTest.DELTA);
        assertListArrayEqualsIgnoreOrder(card.getColorIdentity(), colorIdentity);
        assertEquals(card.getName(), name);
        assertEquals(card.getFinishes().contains(Finish.NONFOIL), nonfoil);
        assertEquals(card.getLayout(), layout);
        assertEquals(card.isOversized(), oversized);
        assertEquals(card.isReserved(), reserved);
        assertEquals(card.getTypeLine(), typeline);
        assertEquals(card.getBorderColor(), borderColor);
        assertEquals(card.getCollectorNumber(), collectorNumber);
        assertEquals(card.isDigital(), digital);
        assertListArrayEqualsIgnoreOrder(card.getFrameEffects(), effects);
        assertEquals(card.getFrame(), frame);
        assertEquals(card.isFullArt(), fullArt);
        assertListArrayEqualsIgnoreOrder(card.getGames(), games);
        assertEquals(card.isHighResImage(), hiRes);
        assertEquals(card.getRarity(), rarity);
        assertEquals(card.isReprint(), reprint);
        assertEquals(card.getSet(), set);

        assertNotNull(card.getLegalities());
        assertNotNull(card.getPrices());
        assertNotNull(card.getPrintsSearchURL());
        assertNotNull(card.getRulingsURL());
        assertNotNull(card.getScryfallURL());
        assertNotNull(card.getUrl());
        assertNotNull(card.getSetURL());
        assertNotNull(card.getPurchaseURLs());
        assertNotNull(card.getRelatedURLs());
        assertNotNull(card.getReleaseDate());
        assertNotNull(card.getPrintsSearchURL());

        assertEquals(card, Card.fromSet(card.getSet(), card.getCollectorNumber(), card.getLang()));
    }
}
