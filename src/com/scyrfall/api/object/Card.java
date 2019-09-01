package com.scyrfall.api.object;

import com.scyrfall.api.ScryfallObject;
import com.scyrfall.api.field.*;
import com.scyrfall.api.query.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

//TODO: implement /cards/search and /cards/collection
//TODO: allow for retrieval in all formats for all endpoints (json, text, image)
//TODO: implement all optional parameters for endpoints

/**
 * Card objects represent individual Magic: The Gathering cards that players could obtain and add to their collection
 * (with a few minor exceptions).
 */
public class Card extends ScryfallObject {

    private static final String[] SUPERTYPES = {"Legendary", "Basic", "Snow"};
    private static final String[] TYPES = {"Creature", "Land", "Instant", "Sorcery", "Enchantment", "Artifact",
            "Planeswalker", "Tribal"};
    private static final String EM_DASH = "—";

    private int arenaID, mtgoID, mtgoFoilID, tcgplayerID, edhrecRank;
    private int[] multiverseIDs;
    private Layout layout;
    private String lang, handModifier, lifeModifier, loyalty, manaCost, name, oracleText, power, toughness, typeLine;
    private UUID id, oracleID, illustrationID, variationID, cardBackID;
    private URL printsSearchURL, rulingsURL, scryfallURL, url;
    private RelatedCard[] allParts;
    private CardFace[] faces;
    private String[] promoTypes;
    private double cmc;
    private Color[] colors, colorIdentity, colorIndicator;
    private boolean foil, nonfoil, oversized, digital, reserved, inBoosters;
    private Legalities legalities;
    private String artist, collectorNumber, flavorText, printedName, printedText, printedTypeLine, watermark;
    private BorderColor borderColor;
    private Frame frame;
    private FrameEffect frameEffect;
    private FrameEffect[] frameEffects;
    private boolean fullArt, highResImage, promo, reprint, storySpotlight, textless, variation;
    private Game[] games;
    private HashMap<String, URL> purchaseURLs, relatedURLs;
    private Rarity rarity;
    private Date releaseDate;
    private URL scryfallSetURL, setSearchURL, setURL;
    private String set, setName;
    private Images images;
    private String euroPrice, tixPrice, usdPrice;
    private Prices prices;
    private Preview preview;

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
        reserved = getBoolean("reserved");
        textless = getBoolean("textless");
        variation = getBoolean("variation");
        inBoosters = getBoolean("booster");

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
        variationID = getUUID("variation_of");
        cardBackID = getUUID("card_back_id");

        releaseDate = getDate("released_at");

        layout = Layout.fromString(getString("layout"));
        frame = Frame.fromString(getString("frame"));
        frameEffect = FrameEffect.fromString(getString("frame_effect"));
        rarity = Rarity.fromString(getString("rarity"));
        borderColor = BorderColor.fromString(getString("border_color"));


        JSONArray games = getJSONArray("games");
        this.games = new Game[games.length()];
        for (int i = 0; i < games.length(); i++) {
            this.games[i] = Game.fromString(games.getString(i));
        }

        JSONArray colors = getJSONArray("colors");
        this.colors = new Color[colors.length()];
        for (int i = 0; i < colors.length(); i++) {
            this.colors[i] = Color.fromString(colors.getString(i));
        }

        JSONArray colorIdentity = getJSONArray("color_identity");
        this.colorIdentity = new Color[colorIdentity.length()];
        for (int i = 0; i < colorIdentity.length(); i++) {
            this.colorIdentity[i] = Color.fromString(colorIdentity.getString(i));
        }

        JSONArray colorIndicator = getJSONArray("color_indicator");
        this.colorIndicator = new Color[colorIndicator.length()];
        for (int i = 0; i < colorIndicator.length(); i++) {
            this.colorIndicator[i] = Color.fromString(colorIndicator.getString(i));
        }

        JSONArray multiverseIDs = getJSONArray("multiverse_ids");
        this.multiverseIDs = new int[multiverseIDs.length()];
        for (int i = 0; i < multiverseIDs.length(); i++) {
            this.multiverseIDs[i] = multiverseIDs.getInt(i);
        }

        JSONArray relatedCards = getJSONArray("all_parts");
        allParts = new RelatedCard[relatedCards.length()];
        for (int i = 0; i < relatedCards.length(); i++) {
            allParts[i] = new RelatedCard(relatedCards.getJSONObject(i));
        }

        JSONArray promos = getJSONArray("promos");
        promoTypes = new String[promos.length()];
        for (int i = 0; i < promos.length(); i++) {
            promoTypes[i] = promos.getString(i);
        }

        JSONArray frameEffects = getJSONArray("frame_effects");
        this.frameEffects = new FrameEffect[frameEffects.length()];
        for (int i = 0; i < frameEffects.length(); i++) {
            this.frameEffects[i] = FrameEffect.fromString(frameEffects.getString(i));
        }

        if (data.has("card_faces")) {
            JSONArray faces = getJSONArray("card_faces");
            this.faces = new CardFace[faces.length()];
            for (int i = 0; i < faces.length(); i++) {
                this.faces[i] = new CardFace(faces.getJSONObject(i));
            }
        }

        images = new Images(getJSONObject("image_uris"));
        legalities = new Legalities(getJSONObject("legalities"));
        prices = new Prices(getJSONObject("prices"));
        preview = new Preview(getJSONObject("preview"));

        if (data.has("related_uris")) {
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

        if (data.has("purchase_uris")) {
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
     * @return The printing ID of the printing this card is a variation of.
     */
    public UUID getVariationID() {
        return variationID;
    }

    /**
     * @return The Scryfall ID for the card back design present on this card.
     */
    public UUID getCardBackID() {
        return cardBackID;
    }

    /**
     * @return This card’s multiverse IDs on Gatherer, if any, as an array of integers. Note that Scryfall includes many
     * promo cards, tokens, and other esoteric objects that do not have these identifiers.
     */
    public int[] getMultiverseIDs() {
        return multiverseIDs;
    }

    /**
     * @return An array of strings describing what categories of promo cards this card falls into.
     */
    public String[] getPromoTypes() {
        return promoTypes;
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
     * @return This loyalty if any. Note that some cards have LOYALTIES that are not numeric, such as <code>X</code>.
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
     * @return This card’s power, if any. Note that some cards have POWERS that are not numeric, such as <code>*</code>.
     */
    public String getPower() {
        return power;
    }

    /**
     * @return This card’s toughness, if any. Note that some cards have TOUGHNESSES that are not numeric, such as <code>*</code>.
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
     * @return an array of this card's types (eg. Creature, Instant, etc)
     */
    public String[] getTypes() {
        ArrayList<String> types = new ArrayList<>();
        for (String type : TYPES) {
            if (typeLine.contains(type)) {
                types.add(type);
            }
        }
        return types.toArray(new String[0]);
    }

    /**
     * @return an array of this card's supertypes (eg. Legendary, Basic, Snow)
     */
    public String[] getSupertypes() {
        ArrayList<String> types = new ArrayList<>();
        for (String type : SUPERTYPES) {
            if (typeLine.contains(type)) {
                types.add(type);
            }
        }
        return types.toArray(new String[0]);
    }

    /**
     * @return an array of this card's subtypes (eg. Human, Trap, Aura, etc)
     */
    public String[] getSubtypes() {
        if (typeLine.contains(EM_DASH)) {
            return typeLine.substring(typeLine.indexOf(EM_DASH)).split(" ");
        }
        return new String[0];
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
     * @return The card’s converted mana cost. Note that some FUNNY cards have fractional mana costs.
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
     * @return <code>true</code> if this card is on the reserved list, <code>false</code> otherwise.
     */
    public boolean isReserved() {
        return reserved;
    }

    /**
     * @return <code>true</code> if this is a digital card on Magic Online., <code>false</code> otherwise.
     */
    public boolean isDigital() {
        return digital;
    }

    /**
     * @return <code>true</code> if the card is printed without text.
     */
    public boolean isTextless() {
        return textless;
    }

    /**
     * @return Whether this card is found in boosters.
     */
    public boolean isInBoosters() {
        return inBoosters;
    }

    /**
     * @return Whether this card is a variation of another printing.
     */
    public boolean isVariation() {
        return variation;
    }

    /**
     * @return object describing the legality of this card across play formats.
     */
    public Legalities getLegalities() {
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
    @Deprecated
    public String getEuroPrice() {
        return euroPrice;
    }

    /**
     * @return The lowest TIX price from our affiliates, updated daily.
     */
    @Deprecated
    public String getTixPrice() {
        return tixPrice;
    }

    /**
     * @return The lowest USD price from our affiliates, updated daily.
     */
    @Deprecated
    public String getUsdPrice() {
        return usdPrice;
    }

    /**
     * @return a <code>Prices</code> object which has information about
     * this card's prices.
     * @see Prices
     */
    public Prices getPrices() {
        return prices;
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
     * @return A list of games that this card print is available in.
     * @see Game
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
     * @return This card's rarity
     * @see Rarity
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
     * Layout is <code>Layout.DOUBLE_FACED_TOKEN</code> or <code>Layout.TRANSFORM</code>
     *
     * @return True if this card is able to TRANSFORM
     */
    public boolean hasMultipleFaces() {
        return layout == Layout.DOUBLE_FACED_TOKEN || layout == Layout.TRANSFORM;
    }

    /**
     * @return An array of Ruling objects for all of the rulings on this card.
     */
    public Ruling[] getRulings() {
        List rulingsList = List.fromURL(rulingsURL);
        ScryfallObject[] rulingsObjects = rulingsList.getContents();
        Ruling[] rulings = new Ruling[rulingsObjects.length];
        for (int i = 0; i < rulings.length; i++) {
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
        if (hasMultipleFaces()) {
            return faces[0].getImages().getImage(size);
        } else {
            return images.getImage(size);
        }
    }

    public String getImageURI(Images.Size size) {
        if (hasMultipleFaces()) {
            return faces[0].getImages().getURL(size).toString();
        } else {
            return images.getURL(size).toString();
        }
    }

    /**
     * Searches using the <code>exact</code> parameter in the Scryfall API, so the
     * provided card name must match exactly to an existing card. However, this
     * parameter is case insensitive
     *
     * @param name The name of the card for which data should be retrieved
     * @return A <code>Card</code> object containing the corresponding card's data.
     */
    public static Card namedExactly(String name) {
        return new Card(Query.dataFromPath("cards/named?exact=" + name.replace(' ', '+')));
    }

    /**
     * Searches using the <code>fuzzy</code> parameter in the Scryfall API, so the
     * provided card name can differ from existing cards. The closest match will be found.
     * If no card matches closely enough to the given name, an error will be thrown.
     *
     * @param name The name of the card for which data should be retrieved.
     * @return A <code>Card</code> object containing the corresponding card's data.
     */
    public static Card namedFuzzy(String name) {
        return new Card(Query.dataFromPath("cards/named?fuzzy=" + name.replace(' ', '+')));
    }

    /**
     * @param name Name of the card to be retrieved
     * @param type Method to be used for retrieving the card. Either FUZZY or EXACT.
     * @return A card with the given name
     */
    public static Card named(String name, SearchType type) {
        return new Card(Query.dataFromPath("cards/named?" + type.toParameterString() + "=" +
                name.replace(' ', '+')));
    }

    public static List search(String query) {
        return new List(Query.dataFromPath("cards/search?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8)));
    }

    /**
     * Retrieves data for a card with the given MTGO id.
     *
     * @param id The MTGO id the of the card for which data should be retrieved.
     * @return A <code>Card</code> object containing the corresponding card's data.
     */
    public static Card fromMtgoID(int id) {
        return new Card(Query.dataFromPath("cards/MTGO/" + id));
    }

    /**
     * @param id the MTG Arena id of the card to be retrieved
     * @return the card with the given MTG Arena ID
     */
    public static Card fromArenaID(int id) {
        return new Card(Query.dataFromPath("cards/ARENA/" + id));
    }

    /**
     * @param id the Multiverse id of the card to be retrieved
     * @return the card with the given Multiverse ID
     */
    public static Card fromMultiverseID(int id) {
        return new Card(Query.dataFromPath("cards/multiverse/" + id));
    }

    /**
     * @param id the Scryfall id of the card to be retrieved
     * @return the card with the given Scryfall ID
     */
    public static Card fromID(UUID id) {
        return new Card(Query.dataFromPath("cards/" + id));
    }

    /**
     * @return a random Magic card
     */
    public static Card random() {
        return new Card(Query.dataFromPath("cards/random"));
    }

    /**
     * @param setCode          the code for the set of the card to be retrieved
     * @param collectorsNumber the collectors number of the card to be retrieved
     * @return the card of the specified collectors number from the specified set
     */
    public static Card fromSet(String setCode, int collectorsNumber) {
        return new Card(Query.dataFromPath("cards/" + setCode + "/" + collectorsNumber));
    }

    /**
     * @param setCode          the code for the set of the card to be retrieved
     * @param collectorsNumber the collectors number of the card to be retrieved
     * @return the card of the specified collectors number from the specified set
     */
    public static Card fromSet(String setCode, String collectorsNumber) {
        return new Card(Query.dataFromPath("cards/" + setCode + "/" + collectorsNumber));
    }

    /**
     * @param setCode          the code for the set of the card to be retrieved
     * @param collectorsNumber the collectors number of the card to be retrieved
     * @param lang             The 2-3 character language code.
     * @return the card of the specified collectors number from the specified set
     */
    public static Card fromSet(String setCode, int collectorsNumber, String lang) {
        return new Card(Query.dataFromPath("cards/" + setCode + "/" + collectorsNumber + "/" + lang));
    }

    /**
     * @param setCode          the code for the set of the card to be retrieved
     * @param collectorsNumber the collectors number of the card to be retrieved
     * @param lang             The 2-3 character language code.
     * @return the card of the specified collectors number from the specified set
     */
    public static Card fromSet(String setCode, String collectorsNumber, String lang) {
        return new Card(Query.dataFromPath("cards/" + setCode + "/" + collectorsNumber + "/" + lang));
    }

    /**
     * This method is designed for creating assistive UI elements that allow users to free-type card names. The names
     * are sorted with the nearest match first, highly favoring results that begin with your given string. Spaces,
     * punctuation, and capitalization are ignored. If q is less than 2 characters long, or if no names match, the
     * Catalog will contain 0 items (instead of returning any errors).
     *
     * @param substring The string to autocomplete.
     * @return a Catalog object containing up to 20 full English card names that could be autocompletions of the
     * given string parameter.
     */
    public static Catalog autoComplete(String substring) {
        return new Catalog(Query.dataFromPath("cards/autocomplete?q=" + substring));
    }

    /**
     * @param name   Card name for the image to be retrieved
     * @param search Method for retrieving the card. Either FUZZY or EXACT.
     * @param size   The format of the image to be retrieved.
     * @return An image of the specified card in the specified format
     */
    public static BufferedImage getImage(String name, SearchType search, Images.Size size) {
        return Query.imageFromPath("cards/named?" + search.toParameterString() + "=" + name.replace(' ', '+') + "&format=image&size=" +
                size.toParameterString());
    }

    /**
     * @param name   name of the card for which text should be retrieved
     * @param search method for retrieving the card. Either FUZZY or EXACT.
     * @return Scryfall's text representation of the named card.
     */
    public static String getText(String name, SearchType search) {
        return Query.textFromPath("cards/named?" + search.toParameterString() + "=" + name.replace(' ', '+') + "&format=text");
    }

    /**
     * Different parameters for {@link #named(String, SearchType)}.
     */
    public enum SearchType {
        EXACT, FUZZY;

        public String toParameterString() {
            return super.toString().toLowerCase();
        }
    }

    /**
     * Different ways in which a Magic card can be laid out. The default value is NORMAL. The options are:
     * <li><code>NORMAL</code> - A standard Magic card with one face
     * <li><code>SPLIT</code> - A split-faced card (includes cards with fuse and aftermath)
     * <li><code>FLIP</code> - Cards that invert vertically with the flip keyword (like those from Kamigawa)
     * <li><code>TRANSFORM</code> - Double-sided cards that transform (Like those from Innistrad)
     * <li><code>MELD</code> - Cards with meld parts printed on the back
     * <li><code>LEVELER</code> - Cards with Level Up (from Rise of the Eldrazi)
     * <li><code>SAGA</code> - Saga-type cards (From Dominaria)
     * <li><code>PLANAR</code> - Plane and Phenomenon-type cards
     * <li><code>SCHEME</code> - Scheme-type cards
     * <li><code>VANGUARD</code> - Vanguard-type cards
     * <li><code>TOKEN</code> - Token cards
     * <li><code>DOUBLE_FACED_TOKEN</code> - Tokens with another token printed on the back
     * <li><code>EMBLEM</code> - Emblem cards
     * <li><code>AUGMENT</code> - Cards with Augment
     * <li><code>HOST</code> - Host-type cards
     * <li><code>DOUBLE_SIDED</code> - A Magic card with two sides that are unrelated
     */
    public enum Layout {
        NORMAL, SPLIT, FLIP, TRANSFORM, MELD, LEVELER, SAGA, PLANAR, SCHEME, VANGUARD, TOKEN, DOUBLE_FACED_TOKEN, EMBLEM,
        AUGMENT, HOST, DOUBLE_SIDED;

        private static Layout fromString(String value) {
            if (value.equals("double_faced_token")) {
                return DOUBLE_FACED_TOKEN;
            } else {
                try {
                    return valueOf(value.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return NORMAL;
                }
            }
        }
    }

    /**
     * Different border colors a card can have.
     * If the border color field is missing or malformed, the card's border color will be <code>BorderColor.BLACK</code>
     */
    public enum BorderColor {
        BLACK, BORDERLESS, GOLD, SILVER, WHITE;

        private static BorderColor fromString(String value) {
            try {
                return valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                return BLACK;
            }
        }
    }

    /**
     * Different card frames used on magic cards over the years
     * <li><code>ORIGINAL</code> - Corresponds to Scyrfall's <code>1993</code> frame.
     * Used from Alpha to Mirage
     * <li><code>OLD</code> - Corresponds to Scyrfall's <code>1997</code> frame.
     * Used from Mirage to Scourge
     * <li><code>MODERN</code> - Corresponds to Scyrfall's <code>2003</code> frame.
     * Used from Mirrodin to M15
     * <li><code>M15</code> - Corresponds to Scyrfall's <code>2015</code> frame.
     * Used in M15 onwards
     * <li><code>FUTURE</code> - Corresponds to Scyrfall's <code>FUTURE</code> frame.
     * Used on cards from the FUTURE.
     */
    public enum Frame {
        ORIGINAL, OLD, MODERN, M15, FUTURE;

        private static Frame fromString(String value) {
            switch (value) {
                case "1993":
                    return OLD;
                case "1997":
                    return ORIGINAL;
                case "2003":
                    return MODERN;
                case "2015":
                    return M15;
                case "FUTURE":
                    return FUTURE;
                default:
                    return null;
            }
        }
    }

    /**
     * The different possible effects applied to the frame of a card:
     * <li><code>LEGENDARY</code> - The legendary crown introduced in Dominaria
     * <li><code>MIRACLE</code> - The miracle frame effect
     * <li><code>NYXTOUCHED</code> - The Nyx-touched frame effect (from Theros block)
     * <li><code>DRAFT</code> - The DRAFT-matters frame effect (from Conspiracy sets)
     * <li><code>DEVOID</code> - The Devoid frame effect (from Battle for Zendikar block)
     * <li><code>TOMBSTONE</code> - The Odyssey tombstone mark
     * <li><code>COLORSHIFTED</code> - A colorshifted frame (from Planar Chaos)
     * <li><code>SUN_MOON_DFC</code> - The sun and moon transform marks (from Innistrad block)
     * <li><code>COMPASS_LAND_DFC</code> - The compass and land transform marks (from Ixalan block)
     * <li><code>ORIGIN_PW_DFC</code> - The Origins and planeswalker transform marks (from Magic Origins)
     * <li><code>MOON_ELDRAZI_DFC</code> - The moon and Eldrazi transform marks (from Eldritch Moon)
     * <li><code>NONE</code> - No frame effect
     */
    public enum FrameEffect {
        LEGENDARY, MIRACLE, NYXTOUCHED, DRAFT, DEVOID, TOMBSTONE, COLORSHIFTED, SUN_MOON_DFC, COMPASS_LAND_DFC, ORIGIN_PW_DFC,
        MOON_ELDRAZI_DFC, NONE;

        private static FrameEffect fromString(String value) {
            switch (value) {
                case "sunmoondfc":
                    return SUN_MOON_DFC;
                case "compasslanddfc":
                    return COMPASS_LAND_DFC;
                case "originpwdfc":
                    return ORIGIN_PW_DFC;
                case "mooneldrazidfc":
                    return MOON_ELDRAZI_DFC;
                case "":
                    return NONE;
                default:
                    return valueOf(value.toUpperCase());
            }
        }
    }

    /**
     * The possible Magic games where a given card can exist: PAPER, ARENA, and MTGO.
     */
    public enum Game {
        PAPER, ARENA, MTGO;

        public static Game fromString(String value) {
            return valueOf(value.toUpperCase());
        }
    }

    /**
     * Different rarities a card can have: COMMON, UNCOMMON, RARE, and MYTHIC.
     * If a card would have the <code>special</code> rarity, it is assigned to <code>RARE</code>
     * If the rarity field is missing or malformed, the the card's rarity will be <code>Rarity.NONE</code>
     */
    public enum Rarity {
        COMMON, UNCOMMON, RARE, MYTHIC, NONE;

        private static Rarity fromString(String value) {
            try {
                return valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                return NONE;
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
                Objects.equals(images, card.images) &&
                Objects.equals(euroPrice, card.euroPrice) &&
                Objects.equals(tixPrice, card.tixPrice) &&
                Objects.equals(usdPrice, card.usdPrice) &&
                Objects.equals(prices, card.prices);
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
                ", euroPrice='" + euroPrice + '\'' +
                ", tixPrice='" + tixPrice + '\'' +
                ", usdPrice='" + usdPrice + '\'' +
                ", prices=" + prices +
                '}';
    }
}