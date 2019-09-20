package tests;

import com.scyrfall.api.ScryfallObject.Color;
import com.scyrfall.api.field.Images;
import com.scyrfall.api.object.Card;
import com.scyrfall.api.object.Card.*;
import org.json.JSONObject;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;
import static tests.ScryfallTest.assertArrayEqualsIgnoreOrder;

public class CardTest {

    private JSONObject emptyJSONObject = new JSONObject("{}");

    @Test
    public void allCards() {
        transform();
        basic();
        borderless();
        oversized();
    }

    @Test
    public void transform() {
        System.out.print("Test Transform (Delver of Secrets): ");
        Card card = Card.fromID(UUID.fromString("11bf83bb-c95b-4b4f-9a56-ce7a1816307a"));
        assertEquals(card.getFaces()[1].getName(), "Insectile Aberration");
        assertTrue(card.hasMultipleFaces());
        assertEquals(card.getImageURI(Images.Size.NORMAL), card.getFaces()[0].getImages().getNormalURL().toString());
        assertEquals(card.getImages(), new Images(emptyJSONObject));
        assertEquals(card.getManaCost(), "");

        basicCard(card, UUID.fromString("11bf83bb-c95b-4b4f-9a56-ce7a1816307a"), "en",
                UUID.fromString("edd531b9-f615-4399-8c8c-1c5e18c4acbf"), 1.0, new Color[]{Color.BLUE},
                "Delver of Secrets // Insectile Aberration", true, Layout.TRANSFORM, false, false,
                "Creature — Human Wizard // Creature — Human Insect", BorderColor.BLACK, "51",
                false, new FrameEffect[]{FrameEffect.SUN_MOON_DFC}, Frame.MODERN, false, new Game[]{Game.MTGO, Game.PAPER},
                true, Rarity.COMMON, false, "isd");
        System.out.println("success");
    }

    @Test
    public void basic() {
        System.out.print("Test Basic (Absorb): ");
        Card card = Card.fromID(UUID.fromString("c1a316a5-04a3-4128-8d62-58192e2265a5"));
        assertEquals(card.getManaCost(), "{W}{U}{U}");

        basicCard(card, UUID.fromString("c1a316a5-04a3-4128-8d62-58192e2265a5"), "fr",
                UUID.fromString("132ca99a-a3c7-4ed6-b4d0-0edcd7140ca2"), 3.0, new Color[]{Color.BLUE, Color.WHITE},
                "Absorb", true, Layout.NORMAL, false, false, "Instant", BorderColor.BLACK, "151",
                false, new FrameEffect[]{FrameEffect.NONE}, Frame.M15, false, new Game[]{Game.ARENA, Game.MTGO, Game.PAPER}, false,
                Rarity.RARE, true, "rna");
        System.out.println("success");
    }

    @Test
    public void oversized() {
        System.out.print("Test Oversized (Silver Queen): ");
        Card card = Card.fromID(UUID.fromString("ab68bd00-7151-4a6b-ad98-134ca02d7d59"));
        assertEquals(card.getManaCost(), "{W}{U}{B}{R}{G}");


        basicCard(card, UUID.fromString("ab68bd00-7151-4a6b-ad98-134ca02d7d59"), "en",
                UUID.fromString("b8376cca-ea96-478a-8e98-c4482031300a"), 5.0, new Color[]{Color.BLACK, Color.GREEN, Color.RED, Color.BLUE, Color.WHITE},
                "Sliver Queen", false, Layout.NORMAL, true, true, "Legendary Creature — Sliver",
                BorderColor.BLACK, "9", false, new FrameEffect[]{FrameEffect.NONE}, Frame.MODERN, false, new Game[]{},
                true, Rarity.RARE, true, "ocm1");
        System.out.println("success");
    }

    @Test
    public void borderless() {
        System.out.print("Test Borderless (UST Forest): ");
        Card card = Card.fromID(UUID.fromString("f8772631-d4a1-440d-ac89-ac6659bdc073"));
        assertEquals(card.getManaCost(), "");
        basicCard(card, UUID.fromString("f8772631-d4a1-440d-ac89-ac6659bdc073"), "en",
                UUID.fromString("b34bb2dc-c1af-4d77-b0b3-a0fb342a5fc6"), 0.0, new Color[]{Color.GREEN},
                "Forest", true, Layout.NORMAL, false, false, "Basic Land — Forest",
                BorderColor.BORDERLESS, "216", false, new FrameEffect[]{FrameEffect.NONE}, Frame.M15, true, new Game[]{Game.PAPER},
                true, Rarity.COMMON, true, "ust");

        System.out.println("success");

    }

    private void basicCard(Card card, UUID id, String lang, UUID oracleID, double cmc, Color[] colorIdentity,
                           String name, boolean nonfoil, Layout layout, boolean oversized, boolean reserved, String typeline,
                           Card.BorderColor borderColor, String collectorNumber, boolean digital, Card.FrameEffect[] effects,
                           Card.Frame frame, boolean fullArt, Card.Game[] games, boolean hiRes, Card.Rarity rarity, boolean reprint, String set) {
        assertEquals(card.getId(), id);
        assertEquals(card.getLang(), lang);
        assertEquals(card.getOracleID(), oracleID);
        assertEquals(card.getCmc(), cmc, ScryfallTest.DELTA);
        assertArrayEqualsIgnoreOrder(colorIdentity, card.getColorIdentity());
        assertEquals(card.getName(), name);
        assertEquals(card.isNonfoil(), nonfoil);
        assertEquals(card.getLayout(), layout);
        assertEquals(card.isOversized(), oversized);
        assertEquals(card.isReserved(), reserved);
        assertEquals(card.getTypeLine(), typeline);
        assertEquals(card.getBorderColor(), borderColor);
        assertEquals(card.getCollectorNumber(), collectorNumber);
        assertEquals(card.isDigital(), digital);
        assertArrayEqualsIgnoreOrder(card.getFrameEffects(), effects);
        assertEquals(card.getFrame(), frame);
        assertEquals(card.isFullArt(), fullArt);
        assertArrayEqualsIgnoreOrder(card.getGames(), games);
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
