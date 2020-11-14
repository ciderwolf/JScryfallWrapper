package tests;

import com.scyrfall.api.ScryfallObject;
import com.scyrfall.api.field.Legalities;
import com.scyrfall.api.field.RelatedCard;
import com.scyrfall.api.field.Ruling;
import com.scyrfall.api.object.Card;
import com.scyrfall.api.object.Set;
import com.scyrfall.api.object.Symbol;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static tests.ScryfallTest.assertArrayEqualsIgnoreOrder;

public class EnumValuesTest {

    @Test
    public void rarity() {
        String[] rarities = {"common", "uncommon", "rare", "mythic", "none"};
        assertValues(rarities, Card.Rarity.values(), "rarity", Card.class, Card::getRarity);
    }

    @Test
    public void game() {
        String[] games = {"paper", "arena", "mtgo"};
        assertArrayValues(games, Card.Game.values(), "games", Card.class, Card::getGames);
    }

    @Test
    public void frameEffect() {
        String[] frameEffects = {"legendary", "miracle", "nyxtouched", "draft", "devoid", "tombstone", "colorshifted",
                "inverted", "sunmoondfc", "compasslanddfc", "originpwdfc", "mooneldrazidfc", "moonreversemoondfc",
                "showcase", "extendedart", "companion", "etched", "none"};
        assertArrayValues(frameEffects, Card.FrameEffect.values(), "frame_effects", Card.class, Card::getFrameEffects);
    }

    @Test
    public void layout() {
        String[] layouts = {"normal", "split", "flip", "transform", "modal_dfc", "meld", "leveler", "saga", "adventure",
                "planar", "scheme", "vanguard", "token", "double_faced_token", "emblem", "augment", "host",
                "art_series", "double_sided"};
        assertValues(layouts, Card.Layout.values(), "layout", Card.class, Card::getLayout);
    }

    @Test
    public void frame() {
        String[] frames = {"1993", "1997", "2003", "2015", "future"};
        assertValues(frames, Card.Frame.values(), "frame", Card.class, Card::getFrame);
    }

    @Test
    public void borderColor() {
        String[] borderColors = {"black", "borderless", "gold", "silver", "white"};
        assertValues(borderColors, Card.BorderColor.values(), "border_color", Card.class, Card::getBorderColor);
    }

    @Test
    public void color() {
        String[] colors = {"W", "U", "B", "R", "G"};
        assertArrayValues(colors, ScryfallObject.Color.values(), "colors", Symbol.class, Symbol::getColors);
    }

    @Test
    public void component() {
        String[] components = {"token", "meld_part", "meld_result", "combo_piece"};
        assertValues(components, RelatedCard.Component.values(), "component", RelatedCard.class, RelatedCard::getComponent);
    }

    @Test
    public void setType() {
        String[] setCodes = {"core", "expansion", "masters", "masterpiece", "from_the_vault", "spellbook",
                "premium_deck", "duel_deck", "draft_innovation", "treasure_chest", "commander", "planechase",
                "archenemy", "vanguard", "funny", "starter", "box", "promo", "token", "memorabilia"};
        assertValues(setCodes, Set.SetType.values(), "set_type", Set.class, Set::getSetType);
    }

    @Test
    public void legality() {
        String[] formats = {"standard", "future", "brawl", "historic", "pioneer", "modern", "legacy", "pauper",
                "vintage", "penny", "commander", "duel", "oldschool"};
        assertEquals(formats.length, Legalities.Format.values().length);
        for(int i = 0; i < formats.length; i++) {
            assertEquals(Legalities.Format.fromString(formats[i]), Legalities.Format.values()[i]);
        }

        String[] legalities = {"legal", "not_legal", "restricted", "banned"};
        assertEquals(legalities.length, Legalities.Legality.values().length);
        JSONObject data = new JSONObject();
        for(int i = 0; i < legalities.length; i++) {
            data.put(formats[i], legalities[i]);
        }
        Legalities dummy = new Legalities(data);
        for(int i = 0; i < legalities.length; i++) {
            Legalities.Legality current = dummy.getFormatLegality(Legalities.Format.fromString(formats[i]));
            assertEquals(current, Legalities.Legality.values()[i]);
        }
    }

    @Test
    public void rulingSource() {
        String[] sources = {"wotc", "scryfall", "other"};
        assertValues(sources, Ruling.RulingSource.values(), "source", Ruling.class, Ruling::getSource);
    }

    private <E extends Enum<E>, S extends ScryfallObject> void assertValues(String[] source, Enum<E>[] values,
                                                                            String key, Class<S> cls,
                                                                            Function<S, E> getter) {
        Constructor<S> constructor;
        try {
            constructor = cls.getConstructor(JSONObject.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            assert false : e.getCause();
            return;
        }
        assertEquals(source.length, values.length);
        for(int i = 0; i < source.length; i++) {
            JSONObject data = new JSONObject().put(key, source[i]);
            try {
                S dummy = constructor.newInstance(data);
                assertEquals(getter.apply(dummy), values[i]);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                assert false : e.getCause();
                e.printStackTrace();
            }
        }
    }

    private <E extends Enum<E>, S extends ScryfallObject> void assertArrayValues(String[] source, Enum<E>[] values,
                                                                                 String key, Class<S> cls,
                                                                                 Function<S, E[]> getter) {

        Constructor<S> constructor;
        try {
            constructor = cls.getConstructor(JSONObject.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            assert false : e.getCause();
            return;
        }
        assertEquals(source.length, values.length);
        JSONObject data = new JSONObject().put(key, new JSONArray(source));
        try {
            S dummy = constructor.newInstance(data);
            assertArrayEqualsIgnoreOrder(getter.apply(dummy), values);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            assert false : e.getCause();
            e.printStackTrace();
        }


    }
}
