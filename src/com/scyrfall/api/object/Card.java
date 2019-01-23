package com.scyrfall.api.object;

import com.scyrfall.api.query.Query;
import com.scyrfall.api.ScryfallObject;
import com.scyrfall.api.field.CardFace;
import com.scyrfall.api.field.Images;
import com.scyrfall.api.field.RelatedCard;
import com.scyrfall.api.field.Ruling;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

/**
 * Card objects represent individual Magic: The Gathering cards that players could obtain and add to their collection
 * (with a few minor exceptions).
 */
public class Card extends ScryfallObject {
    private int arenaID, mtgoID, mtgoFoilID, tcgplayerID, multiverseIDs[], edhrecRank;
    private Layout layout;
    private String lang, handModifier, lifeModifier, loyalty, manaCost, name, oracleText, power, toughness, typeLine;
    private UUID id, oracleID, illustrationID;
    private URL printsSearchURL, rulingsURL, scryfallURL, url;
    private RelatedCard[] allParts;
    private CardFace[] faces;
    private double cmc;
    private Color[] colors, colorIdentity, colorIndicator;
    private boolean foil, nonfoil, oversized, digital;
    private HashMap<String, Legality> legalities;

    private String artist, collectorNumber, flavorText, printedName, printedText, printedTypeLine, watermark;
    private String euroPrice, tixPrice, usdPrice;
    private BorderColor borderColor;
    private Frame frame;
    private FrameEffect frameEffect;
    private boolean fullArt, highResImage, promo, reprint, storySpotlight;
    private Game[] games;
    private HashMap<String, URL> purchaseURLs, relatedURLs;
    private Rarity rarity;
    private Date releaseDate;
    private URL scryfallSetURL, setSearchURL, setURL;
    private String set, setName;
    private Images images;


    public Card(JSONObject data) {
        super(data);

        arenaID = getInt("arena_id");

        mtgoID = getInt("mtgo_id");
        mtgoFoilID = getInt("mtgo_foil_id");
        tcgplayerID = getInt("tcgplayer_id");
        edhrecRank = getInt("edhrec_rank");

        lang = getString("lang");
        handModifier = getString("hand_modifier");
        lifeModifier = getString("life_modifier");
        loyalty = getString("loyalty");
        manaCost = getString("mana_cost");
        name = getString("name");
        oracleText = getString("oracle_text");
        power = getString("power");
        toughness = getString("toughness");
        typeLine = getString("type_line");
        artist = getString("artist");
        collectorNumber = getString("collector_number");
        flavorText = getString("flavor_text");
        printedName = getString("printed_name");
        printedText = getString("printed_name");
        printedTypeLine = getString("printed_type_line");
        watermark = getString("watermark");
        euroPrice = getString("eur");
        tixPrice = getString("tix");
        usdPrice = getString("usd");
        set = getString("set");
        setName = getString("set_name");

        cmc = getDouble("cmc");

        foil = getBoolean("foil");
        nonfoil = getBoolean("nonfoil");
        oversized = getBoolean("oversized");
        digital = getBoolean("digital");
        fullArt = getBoolean("full_art");
        highResImage = getBoolean("highres_image");
        promo = getBoolean("promo");
        reprint = getBoolean("reprint");
        storySpotlight = getBoolean("story_spotlight");

        printsSearchURL = getURL("prints_search_uri");
        rulingsURL = getURL("rulings_uri");
        scryfallURL = getURL("scryfall_uri");
        url = getURL("uri");
        scryfallSetURL = getURL("scryfall_set_uri");
        setSearchURL = getURL("set_search_uri");
        setURL = getURL("set_uri");

        id = getUUID("id");
        illustrationID = getUUID("illustration_id");
        oracleID = getUUID("oracle_id");

        try {
            releaseDate = dateFormat.parse(getString("released_at"));
        } catch (ParseException e) {
            releaseDate = null;
        }

        layout = Layout.fromString(getString("layout"));
        frame = Frame.fromString(getString("frame"));
        frameEffect = FrameEffect.fromString(getString("frame_effect"));
        rarity = Rarity.fromString(getString("rarity"));
        borderColor = BorderColor.fromString(getString("border_color"));


        JSONArray games = getJSONArray("games");
        this.games = new Game[games.length()];
        for(int i = 0; i < games.length(); i++) {
            this.games[i] = Game.valueOf(games.getString(i));
        }

        JSONArray colors = getJSONArray("colors");
        this.colors = new Color[colors.length()];
        for(int i = 0; i < colors.length(); i++) {
            this.colors[i] = Color.fromString(colors.getString(i));
        }

        JSONArray colorIdentity = getJSONArray("color_identity");
        this.colorIdentity = new Color[colorIdentity.length()];
        for(int i = 0; i < colorIdentity.length(); i++) {
            this.colorIdentity[i] = Color.fromString(colorIdentity.getString(i));
        }

        JSONArray colorIndicator = getJSONArray("color_indicator");
        this.colorIndicator = new Color[colorIndicator.length()];
        for(int i = 0; i < colorIndicator.length(); i++) {
            this.colorIndicator[i] = Color.fromString(colorIndicator.getString(i));
        }

        JSONArray multiverseIDs = getJSONArray("multiverse_ids");
        this.multiverseIDs = new int[multiverseIDs.length()];
        for(int i = 0; i < multiverseIDs.length(); i++) {
            this.multiverseIDs[i] = multiverseIDs.getInt(i);
        }

        JSONArray relatedCards = getJSONArray("all_cards");
        allParts = new RelatedCard[relatedCards.length()];
        for(int i = 0; i < relatedCards.length(); i++) {
            allParts[i] = new RelatedCard(relatedCards.getJSONObject(i));
        }

        if(data.has("card_faces")) {
            JSONArray faces = getJSONArray("card_faces");
            this.faces = new CardFace[faces.length()];
            for (int i = 0; i < faces.length(); i++) {
                this.faces[i] = new CardFace(faces.getJSONObject(i));
            }
        }

        images = new Images(getJSONObject("image_uris"));

        if(data.has("related_uris")) {
            JSONObject relatedURLs = getJSONObject("related_uris");
            this.relatedURLs = new HashMap<>();
            for (int i = 0; i < relatedURLs.names().length(); i++) {
                String name = relatedURLs.names().getString(i);
                try {
                    relatedURLs.put(name, new URL(relatedURLs.getString(name)));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        if(data.has("purchase_uris")) {
            JSONObject purchaseURLs = getJSONObject("purchase_uris");
            this.purchaseURLs = new HashMap<>();
            for (int i = 0; i < purchaseURLs.names().length(); i++) {
                String name = purchaseURLs.names().getString(i);
                try {
                    purchaseURLs.put(name, new URL(purchaseURLs.getString(name)));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        if(data.has("legalities")) {
            JSONObject legalities = getJSONObject("legalities");
            this.legalities = new HashMap<>();
            for (int i = 0; i < legalities.names().length(); i++) {
                String name = legalities.names().getString(i);
                legalities.put(name, legalities.getString(name));
            }
        }
    }

    /**
     * @return This card’s Arena ID, if any. A large percentage of cards are not available on Arena and do not have this ID.
     */
    public int getArenaID() {
        return arenaID;
    }

    /**
     * @return This card’s Magic Online ID (also known as the Catalog ID), if any. A
     * large percentage of cards are not available on Magic Online and do not have this ID.
     */
    public int getMtgoID() {
        return mtgoID;
    }

    /**
     * @return This card’s foil Magic Online ID (also known as the Catalog ID), if any. A large percentage of cards
     * are not available on Magic Online and do not have this ID.
     */
    public int getMtgoFoilID() {
        return mtgoFoilID;
    }

    /**
     * @return This card’s ID on <a href="https://docs.tcgplayer.com/docs">TCGplayer’s API</a>, also known as the productId.
     */
    public int getTcgplayerID() {
        return tcgplayerID;
    }

    /**
     * @return This card’s multiverse IDs on Gatherer, if any, as an array of integers. Note that Scryfall includes many
     * promo cards, tokens, and other esoteric objects that do not have these identifiers.
     */
    public int[] getMultiverseIDs() {
        return multiverseIDs;
    }

    /**
     * @return This card’s overall rank/popularity on EDHREC. Not all cards are ranked.
     */
    public int getEdhrecRank() {
        return edhrecRank;
    }

    /**
     * @return A {@link Layout} representing this card's print layout.
     */
    public Layout getLayout() {
        return layout;
    }

    /**
     * @return The language code for this printing.
     */
    public String getLang() {
        return lang;
    }

    /**
     * @return This card’s hand modifier, if it is Vanguard card. This value will contain a delta, such as <code>-1</code>.
     */
    public String getHandModifier() {
        return handModifier;
    }

    /**
     * @return This card’s life modifier, if it is Vanguard card. This value will contain a delta, such as <code>+2</code>.
     */
    public String getLifeModifier() {
        return lifeModifier;
    }

    /**
     * @return This loyalty if any. Note that some cards have loyalties that are not numeric, such as <code>X</code>.
     */
    public String getLoyalty() {
        return loyalty;
    }

    /**
     * @return The mana cost for this card. This value will be any empty string "" if the cost is absent. Remember that
     * per the game rules, a missing mana cost and a mana cost of <code>{0}</code> are different values. Multi-faced cards will
     * report this value in card faces.
     */
    public String getManaCost() {
        return manaCost;
    }

    /**
     * @return The name of this card. If this card has multiple faces, this field will contain both names separated by
     * <code>␣//␣</code>.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The Oracle text for this card, if any.
     */
    public String getOracleText() {
        return oracleText;
    }

    /**
     * @return This card’s power, if any. Note that some cards have powers that are not numeric, such as <code>*</code>.
     */
    public String getPower() {
        return power;
    }

    /**
     * @return This card’s toughness, if any. Note that some cards have toughnesses that are not numeric, such as <code>*</code>.
     */
    public String getToughness() {
        return toughness;
    }

    /**
     * @return The type line of this card.
     */
    public String getTypeLine() {
        return typeLine;
    }

    /**
     * @return A unique ID for this card in Scryfall’s database.
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return A unique ID for this card’s oracle identity. This value is consistent across reprinted card editions, and
     * unique among different cards with the same name (tokens, Unstable variants, etc).
     */
    public UUID getOracleID() {
        return oracleID;
    }

    /**
     * @return A unique identifier for the card artwork that remains consistent across reprints. Newly spoiled cards may
     * not have this field yet.
     */
    public UUID getIllustrationID() {
        return illustrationID;
    }

    /**
     * @return A link to where you can begin paginating all re/prints for this card on Scryfall’s API.
     */
    public URL getPrintsSearchURL() {
        return printsSearchURL;
    }

    /**
     * @return A link to this card’s rulings list on Scryfall’s API.
     */
    public URL getRulingsURL() {
        return rulingsURL;
    }

    /**
     * @return A link to this card’s permapage on Scryfall’s website.
     */
    public URL getScryfallURL() {
        return scryfallURL;
    }

    /**
     * @return A link to this card object on Scryfall’s API.
     */
    public URL getUrl() {
        return url;
    }

    /**
     * @return If this card is closely related to other cards, this property will be an array with {@link RelatedCard}s.
     */
    public RelatedCard[] getAllParts() {
        return allParts;
    }

    /**
     * @return An array of {@link CardFace}s, if this card is multifaced.
     */
    public CardFace[] getFaces() {
        return faces;
    }

    /**
     * @return The card’s converted mana cost. Note that some funny cards have fractional mana costs.
     */
    public double getCmc() {
        return cmc;
    }

    /**
     * @return This card’s colors, if the overall card has colors defined by the rules. Otherwise the colors will be on
     * the {@link CardFace} objects, from {@link #getFaces()}.
     */
    public Color[] getColors() {
        return colors;
    }

    /**
     * @return This card’s color identity.
     */
    public Color[] getColorIdentity() {
        return colorIdentity;
    }

    /**
     * @return The colors in this card’s color indicator, if any. A null value for this field indicates the card does
     * not have one.
     */
    public Color[] getColorIndicator() {
        return colorIndicator;
    }

    /**
     * @return <code>true</code> if this printing exists in a foil version, <code>false</code> otherwise.
     */
    public boolean isFoil() {
        return foil;
    }

    /**
     * @return <code>true</code> if this printing exists in a nonfoil version, <code>false</code> otherwise.
     */
    public boolean isNonfoil() {
        return nonfoil;
    }

    /**
     * @return <code>true</code> if this card is oversized, <code>false</code> otherwise.
     */
    public boolean isOversized() {
        return oversized;
    }


    /**
     * @return <code>true</code> if this is a digital card on Magic Online., <code>false</code> otherwise.
     */
    public boolean isDigital() {
        return digital;
    }

    /**
     * @see Legality
     * @return HashMap describing the legality of this card across play formats.
     */
    public HashMap<String, Legality> getLegalities() {
        return legalities;
    }

    /**
     * @return The name of the illustrator of this card. Newly spoiled cards may not have this field yet.
     */
    public String getArtist() {
        return artist;
    }

    /**
     * @return This card's {@link BorderColor}
     */
    public BorderColor getBorderColor() {
        return borderColor;
    }

    /**
     * @return This card’s collector number. Note that collector numbers can contain non-numeric characters, such as
     * letters or ★.
     */
    public String getCollectorNumber() {
        return collectorNumber;
    }

    /**
     * @return The flavor text, if any.
     */
    public String getFlavorText() {
        return flavorText;
    }

    /**
     * @return The localized name printed on this card, if any.
     */
    public String getPrintedName() {
        return printedName;
    }

    /**
     * @return The localized text printed on this card, if any.
     */
    public String getPrintedText() {
        return printedText;
    }

    /**
     * @return The localized type line printed on this card, if any.
     */
    public String getPrintedTypeLine() {
        return printedTypeLine;
    }

    /**
     * @return This card’s watermark, if any.
     */
    public String getWatermark() {
        return watermark;
    }

    /**
     * @return The lowest EUR price from our affiliates, updated daily.
     */
    public String getEuroPrice() {
        return euroPrice;
    }

    /**
     * @return The lowest TIX price from our affiliates, updated daily.
     */
    public String getTixPrice() {
        return tixPrice;
    }

    /**
     * @return The lowest USD price from our affiliates, updated daily.
     */
    public String getUsdPrice() {
        return usdPrice;
    }

    /**
     * @return This card's {@link Frame}.
     */
    public Frame getFrame() {
        return frame;
    }

    /**
     * @return This card's {@link FrameEffect}.
     */
    public FrameEffect getFrameEffect() {
        return frameEffect;
    }

    /**
     * @return True if this card’s artwork is larger than normal.
     */
    public boolean isFullArt() {
        return fullArt;
    }

    /**
     * @return True if this card’s imagery is high resolution.
     */
    public boolean isHighResImage() {
        return highResImage;
    }

    /**
     * @return True if this card is a promotional print.
     */
    public boolean isPromo() {
        return promo;
    }

    /**
     * @return True if this card is a reprint.
     */
    public boolean isReprint() {
        return reprint;
    }

    /**
     * @return True if this card is a story spotlight card.
     */
    public boolean isStorySpotlight() {
        return storySpotlight;
    }

    /**
     * @see Game
     * @return A list of games that this card print is available in.
     */
    public Game[] getGames() {
        return games;
    }

    /**
     * @return an <code>Images</code> object which contains information
     * about the artwork for this card.
     */
    public Images getImages() {
        return images;
    }

    /**
     * @return A HashMap providing URLs to this card’s listing on major marketplaces.
     */
    public HashMap<String, URL> getPurchaseURLs() {
        return purchaseURLs;
    }

    /**
     * @return A HashMap providing URLs to this card’s listing on other Magic: The Gathering online resources.
     */
    public HashMap<String, URL> getRelatedURLs() {
        return relatedURLs;
    }

    /**
     * @see Rarity
     * @return This card's rarity
     */
    public Rarity getRarity() {
        return rarity;
    }

    /**
     * @return The date this card was first released.
     */
    public Date getReleaseDate() {
        return releaseDate;
    }

    /**
     * @return A link to this card’s set on Scryfall’s website.
     */
    public URL getScryfallSetURL() {
        return scryfallSetURL;
    }

    /**
     * @return A link to where you can begin paginating this card’s set on the Scryfall API.
     */
    public URL getSetSearchURL() {
        return setSearchURL;
    }

    /**
     * @return A link to this card’s {@link Set} on Scryfall’s API.
     */
    public URL getSetURL() {
        return setURL;
    }

    /**
     * @return This card’s set code.
     */
    public String getSet() {
        return set;
    }

    /**
     * @return This card’s full set name.
     */
    public String getSetName() {
        return setName;
    }

    /**
     * Layout is <code>Layout.doubleFacedToken</code> or  <code>Layout.transform</code>
     * @return True if this card is able to transform
     */
    public boolean hasMultipleFaces() {
        return layout == Layout.doubleFacedToken || layout == Layout.transform;
    }

    /**
     * @return An array of Ruling objects for all of the rulings on this card.
     */
    public Ruling[] getRulings() {
        List rulingsList = List.fromURL(rulingsURL);
        ScryfallObject[] rulingsObjects = rulingsList.getContents();
        Ruling[] rulings = new Ruling[rulingsObjects.length];
        for(int i = 0; i < rulings.length; i++) {
            rulings[i] = ((Ruling) rulingsObjects[i]);
        }
        return rulings;
    }

    /**
     * @param size the <code>Size</code> of the image to be retrieved
     * @return an image of this card of the specified size. If this card has
     * multiple faces, an image of the front side is returned.
     */
    public BufferedImage getImage(Images.Size size) {
        if(hasMultipleFaces()) {
            return faces[0].getImages().getImage(size);
        } else {
            return images.getImage(size);
        }
    }

    public String getImageURI(Images.Size size) {
        if(hasMultipleFaces()) {
            return faces[0].getImages().getURL(size).toString();
        } else {
            return images.getURL(size).toString();
        }
    }

    /**
     * Searches using the <code>exact</code> parameter in the Scryfall API, so the
     * provided card name must match exactly to an existing card. However, this
     * parameter is case insensitive
     * @param name The name of the card for which dataFromPath should be retrieved
     * @return A <code>Card</code> object containing the corresponding card's dataFromPath.
     */
    public static Card namedExactly(String name) {
        return new Card(Query.dataFromPath("cards/named?exact=" + name.replace(' ', '+')));
    }

    /**
     * Searches using the <code>fuzzy</code> parameter in the Scryfall API, so the
     * provided card name can differ from existing cards. The closest match will be found.
     * If no card matches closely enough to the given name, an error will be thrown.
     * @param name The name of the card for which dataFromPath should be retrieved.
     * @return A <code>Card</code> object containing the corresponding card's dataFromPath.
     */
    public static Card namedFuzzy(String name) {
        return new Card(Query.dataFromPath("cards/named?fuzzy=" + name.replace(' ', '+')));
    }

    /**
     * Retrieves dataFromPath for a card with the given MTGO id. If no card has the given id,
     * an error whill be thrown.
     * @param id The MTGO id the of the card for which dataFromPath should be retrieved.
     * @return A <code>Card</code> object containing the corresponding card's dataFromPath.
     */
    public static Card fromMtgoID(int id) {
        return new Card(Query.dataFromPath("cards/mtgo/" + id));
    }

    public static BufferedImage getImage(String name, SearchType search, Images.Size size) {
        return Query.imageFromPath("cards/named?" + search.toParameterString() + "=" + name.replace(' ', '+') + "&format=image&size=" +
                size.toParameterString());
    }

    public static String getText(String name, SearchType search) {
        return Query.textFromPath("cards/named?" + search.toParameterString() + "=" + name.replace(' ', '+') + "&format=text");
    }



    public enum SearchType {
        EXACT, FUZZY;

        public String toParameterString() {
            return super.toString().toLowerCase();
        }
    }

    /**
     * Different ways in which a Magic card can be laid out. The options are:
     * <li><code>normal</code> - A standard Magic card with one face
     * <li><code>split</code> - A split-faced card (includes cards with fuse and aftermath)
     * <li><code>flip</code> - Cards that invert vertically with the flip keyword (like those from Kamigawa)
     * <li><code>transform</code> - Double-sided cards that transform (Like those from Innistrad)
     * <li><code>meld</code> - Cards with meld parts printed on the back
     * <li><code>leveler</code> - Cards with Level Up (from Rise of the Eldrazi)
     * <li><code>saga</code> - Saga-type cards (From Dominaria)
     * <li><code>planar</code> - Plane and Phenomenon-type cards
     * <li><code>scheme</code> - Scheme-type cards
     * <li><code>vanguard</code> - Vanguard-type cards
     * <li><code>token</code> - Token cards
     * <li><code>doubleFacedToken</code> - Tokens with another token printed on the back
     * <li><code>emblem</code> - Emblem cards
     * <li><code>augment</code> - Cards with Augment
     * <li><code>host</code> - Host-type cards
     */
    enum Layout {
        normal, split, flip, transform, meld, leveler, saga, planar, scheme, vanguard, token, doubleFacedToken, emblem,
        augment, host;

        private static Layout fromString(String value) {
            if(value.equals("double_faced_token")) {
                return doubleFacedToken;
            }
            else {
                try {
                    return valueOf(value);
                } catch (IllegalArgumentException e) {
                    return normal;
                }
            }
        }
    }

    /**
     * Different border colors a card can have.
     * If the border color field is missing or malformed, the card's border color will be <code>BorderColor.black</code>
     */
    enum BorderColor {
        black, borderless, gold, silver, white;
        private static BorderColor fromString(String value) {
            try {
                return valueOf(value);
            } catch (IllegalArgumentException e) {
                return black;
            }
        }
    }

    /**
     * Different card frames used on magic cards over the years
     * <li><code>original</code> - Corresponds to Scyrfall's <code>1993</code> frame.
     * Used from Alpha to Mirage
     * <li><code>old</code> - Corresponds to Scyrfall's <code>1997</code> frame.
     * Used from Mirage to Scourge
     * <li><code>modern</code> - Corresponds to Scyrfall's <code>2003</code> frame.
     * Used from Mirrodin to M15
     * <li><code>m15</code> - Corresponds to Scyrfall's <code>2015</code> frame.
     * Used in M15 onwards
     * <li><code>future</code> - Corresponds to Scyrfall's <code>future</code> frame.
     * Used on cards from the future.
     */
    enum Frame {
        original, old, modern, m15, future;

        private static Frame fromString(String value) {
            switch (value) {
                case "1993":
                    return old;
                case "1997":
                    return original;
                case "2003":
                    return modern;
                case "2015":
                    return m15;
                case "future":
                    return future;
                default:
                    return null;
            }
        }
    }

    /**
     * The different possible effects applied to the frame of a card:
     * <li><code>legendary</code> - The legendary crown introduced in Dominaria
     * <li><code>miracle</code> - The miracle frame effect
     * <li><code>nyxtouched</code> - The Nyx-touched frame effect (from Theros block)
     * <li><code>draft</code> - The draft-matters frame effect (from Conspiracy sets)
     * <li><code>devoid</code> - The Devoid frame effect (from Battle for Zendikar block)
     * <li><code>tombstone</code> - The Odyssey tombstone mark
     * <li><code>colorshift</code> - A colorshifted frame (from Planar Chaos)
     * <li><code>sunMoonDFC</code> - The sun and moon transform marks (from Innistrad block)
     * <li><code>compassLandDFC</code> - The compass and land transform marks (from Ixalan block)
     * <li><code>originPwdDFC</code> - The Origins and planeswalker transform marks (from Magic Origins)
     * <li><code>moonEldraziDFC</code> - The moon and Eldrazi transform marks (from Eldritch Moon)
     * <li><code>none</code> - No frame effect
     */
    enum FrameEffect {
        legendary, miracle, nyxtouched, draft, devoid, tombstone, colorshift, sunMoonDFC, compassLandDFC, originPwdDFC,
        moonEldraziDFC, none;

        private static FrameEffect fromString(String value) {
            switch (value) {
                case "sunmoondfc":
                    return sunMoonDFC;
                case "compasslanddfc":
                    return compassLandDFC;
                case "originpwdfc":
                    return originPwdDFC;
                case "mooneldrazidfc":
                    return moonEldraziDFC;
                case "":
                    return none;
                default:
                    return valueOf(value);
            }
        }
    }

    /**
     * Different legalities a card can have in a given format.
     * The options are: legal, notLegal, restricted, and banned.
     */
    enum Legality {
        legal, notLegal, restricted, banned;
        private static Legality fromString(String value) {
            if(value.equals("not_legal")) {
                return notLegal;
            }
            else {
                return valueOf(value);
            }
        }
    }

    /**
     * The possible Magic games where a given card can exist: paper, arena, and mtgo.
     */
    enum Game {
        paper, arena, mtgo;
    }

    /**
     * Different rarities a card can have: common, uncommon, rare, and mythic.
     * If a card would have the <code>special</code> rarity, it is assigned to <code>rare</code>
     * If the rarity field is missing or malformed, the the card's rarity will be <code>Rarity.none</code>
     */
    enum Rarity {
        common, uncommon, rare, mythic, none;

        private static Rarity fromString(String value) {
            try {
                return valueOf(value);
            } catch (IllegalArgumentException e) {
                return none;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return arenaID == card.arenaID &&
                mtgoID == card.mtgoID &&
                mtgoFoilID == card.mtgoFoilID &&
                tcgplayerID == card.tcgplayerID &&
                edhrecRank == card.edhrecRank &&
                Double.compare(card.cmc, cmc) == 0 &&
                foil == card.foil &&
                nonfoil == card.nonfoil &&
                oversized == card.oversized &&
                digital == card.digital &&
                fullArt == card.fullArt &&
                highResImage == card.highResImage &&
                promo == card.promo &&
                reprint == card.reprint &&
                storySpotlight == card.storySpotlight &&
                Arrays.equals(multiverseIDs, card.multiverseIDs) &&
                layout == card.layout &&
                Objects.equals(lang, card.lang) &&
                Objects.equals(handModifier, card.handModifier) &&
                Objects.equals(lifeModifier, card.lifeModifier) &&
                Objects.equals(loyalty, card.loyalty) &&
                Objects.equals(manaCost, card.manaCost) &&
                Objects.equals(name, card.name) &&
                Objects.equals(oracleText, card.oracleText) &&
                Objects.equals(power, card.power) &&
                Objects.equals(toughness, card.toughness) &&
                Objects.equals(typeLine, card.typeLine) &&
                Objects.equals(id, card.id) &&
                Objects.equals(oracleID, card.oracleID) &&
                Objects.equals(illustrationID, card.illustrationID) &&
                Objects.equals(printsSearchURL, card.printsSearchURL) &&
                Objects.equals(rulingsURL, card.rulingsURL) &&
                Objects.equals(scryfallURL, card.scryfallURL) &&
                Objects.equals(url, card.url) &&
                Arrays.equals(allParts, card.allParts) &&
                Arrays.equals(faces, card.faces) &&
                Arrays.equals(colors, card.colors) &&
                Arrays.equals(colorIdentity, card.colorIdentity) &&
                Arrays.equals(colorIndicator, card.colorIndicator) &&
                Objects.equals(legalities, card.legalities) &&
                Objects.equals(artist, card.artist) &&
                Objects.equals(collectorNumber, card.collectorNumber) &&
                Objects.equals(flavorText, card.flavorText) &&
                Objects.equals(printedName, card.printedName) &&
                Objects.equals(printedText, card.printedText) &&
                Objects.equals(printedTypeLine, card.printedTypeLine) &&
                Objects.equals(watermark, card.watermark) &&
                Objects.equals(euroPrice, card.euroPrice) &&
                Objects.equals(tixPrice, card.tixPrice) &&
                Objects.equals(usdPrice, card.usdPrice) &&
                borderColor == card.borderColor &&
                frame == card.frame &&
                frameEffect == card.frameEffect &&
                Arrays.equals(games, card.games) &&
                Objects.equals(purchaseURLs, card.purchaseURLs) &&
                Objects.equals(relatedURLs, card.relatedURLs) &&
                rarity == card.rarity &&
                Objects.equals(releaseDate, card.releaseDate) &&
                Objects.equals(scryfallSetURL, card.scryfallSetURL) &&
                Objects.equals(setSearchURL, card.setSearchURL) &&
                Objects.equals(setURL, card.setURL) &&
                Objects.equals(set, card.set) &&
                Objects.equals(setName, card.setName) &&
                Objects.equals(images, card.images);
    }

    @Override
    public String toString() {
        return "Card{" +
                "arenaID=" + arenaID +
                ", mtgoID=" + mtgoID +
                ", mtgoFoilID=" + mtgoFoilID +
                ", tcgplayerID=" + tcgplayerID +
                ", multiverseIDs=" + Arrays.toString(multiverseIDs) +
                ", edhrecRank=" + edhrecRank +
                ", layout=" + layout +
                ", lang='" + lang + '\'' +
                ", handModifier='" + handModifier + '\'' +
                ", lifeModifier='" + lifeModifier + '\'' +
                ", loyalty='" + loyalty + '\'' +
                ", manaCost='" + manaCost + '\'' +
                ", name='" + name + '\'' +
                ", oracleText='" + oracleText + '\'' +
                ", power='" + power + '\'' +
                ", toughness='" + toughness + '\'' +
                ", typeLine='" + typeLine + '\'' +
                ", id=" + id +
                ", oracleID=" + oracleID +
                ", illustrationID=" + illustrationID +
                ", printsSearchURL=" + printsSearchURL +
                ", rulingsURL=" + rulingsURL +
                ", scryfallURL=" + scryfallURL +
                ", url=" + url +
                ", allParts=" + Arrays.toString(allParts) +
                ", faces=" + Arrays.toString(faces) +
                ", cmc=" + cmc +
                ", colors=" + Arrays.toString(colors) +
                ", colorIdentity=" + Arrays.toString(colorIdentity) +
                ", colorIndicator=" + Arrays.toString(colorIndicator) +
                ", foil=" + foil +
                ", nonfoil=" + nonfoil +
                ", oversized=" + oversized +
                ", digital=" + digital +
                ", legalities=" + legalities +
                ", artist='" + artist + '\'' +
                ", collectorNumber='" + collectorNumber + '\'' +
                ", flavorText='" + flavorText + '\'' +
                ", printedName='" + printedName + '\'' +
                ", printedText='" + printedText + '\'' +
                ", printedTypeLine='" + printedTypeLine + '\'' +
                ", watermark='" + watermark + '\'' +
                ", euroPrice='" + euroPrice + '\'' +
                ", tixPrice='" + tixPrice + '\'' +
                ", usdPrice='" + usdPrice + '\'' +
                ", borderColor=" + borderColor +
                ", frame=" + frame +
                ", frameEffect=" + frameEffect +
                ", fullArt=" + fullArt +
                ", highResImage=" + highResImage +
                ", promo=" + promo +
                ", reprint=" + reprint +
                ", storySpotlight=" + storySpotlight +
                ", games=" + Arrays.toString(games) +
                ", purchaseURLs=" + purchaseURLs +
                ", relatedURLs=" + relatedURLs +
                ", rarity=" + rarity +
                ", releaseDate=" + releaseDate +
                ", scryfallSetURL=" + scryfallSetURL +
                ", setSearchURL=" + setSearchURL +
                ", setURL=" + setURL +
                ", set='" + set + '\'' +
                ", setName='" + setName + '\'' +
                ", images=" + images +
                '}';
    }
}