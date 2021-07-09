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
import java.util.List;
import java.util.*;
import java.util.function.Function;

//TODO: implement /cards/search and /cards/collection
//TODO: allow for retrieval in all formats for all endpoints (json, text, image)
//TODO: implement all optional parameters for endpoints

/**
 * Card objects represent individual Magic: The Gathering cards that players could obtain and add to their collection
 * (with a few minor exceptions).
 */
@SuppressWarnings("unused")
public class Card extends ScryfallObject {

    private static final String[] SUPERTYPES = {"Legendary", "Basic", "Snow"};
    private static final String[] TYPES = {"Creature", "Land", "Instant", "Sorcery", "Enchantment", "Artifact",
            "Planeswalker", "Tribal"};
    private static final String EM_DASH = "—";

    private int arenaID, mtgoID, mtgoFoilID, tcgplayerID, cardmarketID, edhrecRank;
    private List<Integer> multiverseIDs;
    private Layout layout;
    private String lang, handModifier, lifeModifier, loyalty, manaCost, name,
            flavorName, oracleText, power, toughness, typeLine;
    private UUID id, oracleID, illustrationID, variationID, cardBackID, setID;
    private URL printsSearchURL, rulingsURL, scryfallURL, url;
    private List<RelatedCard> allParts;
    private List<CardFace> faces;
    private List<String> promoTypes, keywords;
    private double cmc;
    private List<Color> colors, colorIdentity, colorIndicator, producedMana;
    private boolean foil, nonfoil, oversized, digital, reserved, inBoosters, contentWarning;
    private Legalities legalities;
    private String artist, collectorNumber, flavorText, printedName, printedText, printedTypeLine, watermark;
    private BorderColor borderColor;
    private Frame frame;
    private FrameEffect frameEffect;
    private List<FrameEffect> frameEffects;
    private boolean fullArt, highResImage, promo, reprint, storySpotlight, textless, variation;
    private List<Game> games;
    private HashMap<String, URL> purchaseURLs, relatedURLs;
    private Rarity rarity;
    private Date releaseDate;
    private URL scryfallSetURL, setSearchURL, setURL;
    private String set, setName;
    private Images images;
    private String euroPrice, tixPrice, usdPrice;
    private Prices prices;
    private Preview preview;
    private ImageStatus imageStatus;

    public Card(JSONObject data) {
        super(data);

        arenaID = getInt("arena_id");

        mtgoID = getInt("mtgo_id");
        mtgoFoilID = getInt("mtgo_foil_id");
        tcgplayerID = getInt("tcgplayer_id");
        cardmarketID = getInt("cardmarket_id");
        edhrecRank = getInt("edhrec_rank");

        lang = getString("lang");
        handModifier = getString("hand_modifier");
        lifeModifier = getString("life_modifier");
        loyalty = getString("loyalty");
        manaCost = getString("mana_cost");
        name = getString("name");
        flavorName = getString("flavor_name");
        oracleText = getString("oracle_text");
        power = getString("power");
        toughness = getString("toughness");
        typeLine = getString("type_line");
        artist = getString("artist");
        collectorNumber = getString("collector_number");
        flavorText = getString("flavor_text");
        printedName = getString("printed_name");
        printedText = getString("printed_text");
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
        contentWarning = getBoolean("content_warning");

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
        setID = getUUID("set_id");

        releaseDate = getDate("released_at");

        layout = Layout.fromString(getString("layout"));
        frame = Frame.fromString(getString("frame"));
        frameEffect = FrameEffect.fromString(getString("frame_effect"));
        rarity = Rarity.fromString(getString("rarity"));
        borderColor = BorderColor.fromString(getString("border_color"));
        imageStatus = ImageStatus.fromString(getString("image_status"));

        games = getList("games", Game::fromString, JSONArray::getString);
        colors = getList("colors", Color::fromString, JSONArray::getString);
        colorIdentity = getList("color_identity", Color::fromString, JSONArray::getString);
        producedMana = getList("produced_mana", Color::fromString, JSONArray::getString);
        colorIndicator = getList("color_indicator", Color::fromString, JSONArray::getString);
        multiverseIDs = getList("multiverse_ids", Function.identity(), JSONArray::getInt);
        allParts = getList("all_parts", RelatedCard::new, JSONArray::getJSONObject);
        promoTypes = getList("promo_types", Function.identity(), JSONArray::getString);
        keywords = getList("keywords", Function.identity(), JSONArray::getString);
        frameEffects = getList("frame_effects", FrameEffect::fromString, JSONArray::getString);
        faces = getList("card_faces", CardFace::new, JSONArray::getJSONObject);
        relatedURLs = getMap("related_uris", this::makeURL, JSONObject::getString);
        purchaseURLs = getMap("purchase_uris", this::makeURL, JSONObject::getString);

        images = new Images(getJSONObject("image_uris"));
        legalities = new Legalities(getJSONObject("legalities"));
        prices = new Prices(getJSONObject("prices"));
        preview = new Preview(getJSONObject("preview"));
    }

    private URL makeURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            return null;
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
     * @return This card’s ID on Cardmarket’s API, also known as the <code>idProduct</code>.
     */
    public int getCardmarketID() {
        return cardmarketID;
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
     * @return This card&rsquo;s Set object UUID.
     */
    public UUID getSetID() {
        return setID;
    }

    /**
     * @return This card’s multiverse IDs on Gatherer, if any, as an array of integers. Note that Scryfall includes many
     * promo cards, tokens, and other esoteric objects that do not have these identifiers.
     */
    public List<Integer> getMultiverseIDs() {
        return multiverseIDs;
    }

    /**
     * @return An array of strings describing what categories of promo cards this card falls into.
     */
    public List<String> getPromoTypes() {
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
     * @return An array of keywords that this card uses, such as <code>&#39;Flying&#39;</code> and
     * <code>&#39;Cumulative upkeep&#39;</code>.
     * @see Catalog.Name#KEYWORD_ABILITIES
     */
    public List<String> getKeywords() {
        return keywords;
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
     * "<code> // </code>".
     */
    public String getName() {
        return name;
    }

    /**
     * @return The just-for-fun name printed on the card (such as for Godzilla series cards).
     */
    public String getFlavorName() {
        return flavorName;
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
            return typeLine.substring(typeLine.indexOf(EM_DASH) + 2).split(" ");
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
     * @return A computer-readable indicator for the state of this card&rsquo;s image, one of <code>missing</code>,
     * <code>placeholder</code>, <code>lowres</code>, or <code>highres_scan</code>.
     */
    public ImageStatus getImageStatus() {
        return imageStatus;
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
    public List<RelatedCard> getAllParts() {
        return allParts;
    }

    /**
     * @return An array of {@link CardFace}s, if this card is multifaced.
     */
    public List<CardFace> getFaces() {
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
    public List<Color> getColors() {
        return colors;
    }

    /**
     * @return This card’s color identity.
     */
    public List<Color> getColorIdentity() {
        return colorIdentity;
    }

    /**
     * @return The colors in this card’s color indicator, if any. An empty array for this field indicates the card does
     * not have one.
     */
    public List<Color> getColorIndicator() {
        return colorIndicator;
    }

    /**
     * @return Colors of mana that this card could produce.
     */
    public List<Color> getProducedMana() {
        return producedMana;
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
     * @return <code>true</code> if this card was only released in a video game, <code>false</code> otherwise.
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
     * @return True if you should consider <a href="https://scryfall.com/blog/220">avoiding use of this print</a>
     * downstream.
     */
    public boolean hasContentWarning() {
        return contentWarning;
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
     * letters or ★ (a star).
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
     * @return The preview information for this card. If Scryfall doesn't
     * have any preview information for this card, this method will return null
     * @see Preview
     */
    public Preview getPreview() {
        return preview;
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
    @Deprecated
    public FrameEffect getFrameEffect() {
        return frameEffect;
    }

    /**
     * @return This card’s frame effects, if any.
     */
    public List<FrameEffect> getFrameEffects() {
        return frameEffects;
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
    public List<Game> getGames() {
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
        return layout == Layout.DOUBLE_FACED_TOKEN || layout == Layout.TRANSFORM || layout == Layout.MODAL_DFC;
    }

    /**
     * @return An array of Ruling objects for all of the rulings on this card.
     */
    public List<Ruling> getRulings() {
        ScryfallList rulingsList = ScryfallList.fromURL(rulingsURL);
        ScryfallObject[] rulingsObjects = rulingsList.getContents();
        Ruling[] rulings = new Ruling[rulingsObjects.length];
        for (int i = 0; i < rulings.length; i++) {
            rulings[i] = ((Ruling) rulingsObjects[i]);
        }
        return List.of(rulings);
    }

    /**
     * @param size the <code>Size</code> of the image to be retrieved
     * @return an image of this card of the specified size. If this card has
     * multiple faces, an image of the front side is returned.
     */
    public BufferedImage getImage(Images.Size size) {
        if (hasMultipleFaces()) {
            return faces.get(0).getImages().getImage(size);
        } else {
            return images.getImage(size);
        }
    }

    public String getImageURI(Images.Size size) {
        if (hasMultipleFaces()) {
            return faces.get(0).getImages().getURL(size).toString();
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

    public static ScryfallList search(String query) {
        return new ScryfallList(Query.dataFromPath("cards/search?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8)));
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
     * @param id The TCGPlayer ID of the card to retrieve.
     * @return A single card with the given <code>tcgplayer_id</code>, also known as the <code>productId</code> on
     * <a href="https://docs.tcgplayer.com/docs">TCGplayer’s API</a>.
     */
    public static Card fromTcgPlayerID(int id) {
        return new Card(Query.dataFromPath("cards/tcgplayer/" + id));
    }


    /**
     * @param id The Cardmarket ID of the card to retrieve.
     * @return A single card with the given <code>cardmarket_id</code>, also known as the <code>productId</code> on
     * Cardmarket's APIs.
     */
    public static Card fromCardmarketID(int id) {
        return new Card(Query.dataFromPath("cards/cardmarket/" + id));
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
        return Query.imageFromPath("cards/named?" + search.toParameterString() + "=" +
                name.replace(' ', '+') + "&format=image&size=" + size.toParameterString());
    }

    /**
     * @param name   name of the card for which text should be retrieved
     * @param search method for retrieving the card. Either FUZZY or EXACT.
     * @return Scryfall's text representation of the named card.
     */
    public static String getText(String name, SearchType search) {
        return Query.textFromPath("cards/named?" + search.toParameterString() + "=" +
                name.replace(' ', '+') + "&format=text");
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
     * <li><code>MODAL_DFC</code> - Double-sided cards that can be played either-side (Like those from Zendikar Rising)
     * <li><code>MELD</code> - Cards with meld parts printed on the back
     * <li><code>LEVELER</code> - Cards with Level Up (from Rise of the Eldrazi)
     * <li><code>SAGA</code> - Saga-type cards (From Dominaria)
     * <li><code>ADVENTURE</code> - Cards with an Adventure spell part (From Throne of Eldraine)
     * <li><code>PLANAR</code> - Plane and Phenomenon-type cards
     * <li><code>SCHEME</code> - Scheme-type cards
     * <li><code>VANGUARD</code> - Vanguard-type cards
     * <li><code>TOKEN</code> - Token cards
     * <li><code>DOUBLE_FACED_TOKEN</code> - Tokens with another token printed on the back
     * <li><code>EMBLEM</code> - Emblem cards
     * <li><code>AUGMENT</code> - Cards with Augment
     * <li><code>HOST</code> - Host-type cards
     * <li><code>ART_SERIES</code> - Art Series collectible double-faced cards
     * <li><code>DOUBLE_SIDED</code> - A Magic card with two sides that are unrelated
     */
    @SuppressWarnings("unused")
    public enum Layout {
        NORMAL, SPLIT, FLIP, TRANSFORM, MODAL_DFC, MELD, LEVELER, SAGA, ADVENTURE, PLANAR, SCHEME, VANGUARD, TOKEN,
        DOUBLE_FACED_TOKEN, EMBLEM, AUGMENT, HOST, ART_SERIES, DOUBLE_SIDED;

        private static Layout fromString(String value) {
            try {
                return valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                return NORMAL;
            }
        }
    }

    /**
     * Different border colors a card can have.
     * If the border color field is missing or malformed, the card's border color will be <code>BorderColor.BLACK</code>
     */
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public enum Frame {
        ORIGINAL, OLD, MODERN, M15, FUTURE;

        private static Frame fromString(String value) {
            switch (value) {
                case "1993":
                    return ORIGINAL;
                case "1997":
                    return OLD;
                case "2003":
                    return MODERN;
                case "2015":
                    return M15;
                case "future":
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
     * <li><code>WAXING_AND_WANING_MOON_DFC</code> - The waxing and waning crescent moon transform marks (From My Little Ponies promotion)
     * <li><code>INVERTED</code> - The FNM-style inverted frame
     * <li><code>SHOWCASE</code> - A custom Showcase frame (from Eldraine)
     * <li><code>EXTENDED_ART</code> - An extended art frame
     * <li><code>COMPANION</code> - The cards have a companion frame
     * <li><code>ETCHED</code> - The cards have an etched foil treatment
     * <li><code>SNOW</code> - The cards have the snowy frame effect (from Kaldheim)
     * <li><code>NONE</code> - No frame effect
     */
    @SuppressWarnings("unused")
    public enum FrameEffect {
        LEGENDARY, MIRACLE, NYXTOUCHED, DRAFT, DEVOID, TOMBSTONE, COLORSHIFTED, SUN_MOON_DFC, COMPASS_LAND_DFC, ORIGIN_PW_DFC,
        MOON_ELDRAZI_DFC, WAXING_AND_WANING_MOON_DFC, INVERTED, SHOWCASE, EXTENDED_ART, COMPANION, ETCHED, SNOW, NONE;

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
                case "waxingandwaningmoondfc":
                    return WAXING_AND_WANING_MOON_DFC;
                case "extendedart":
                    return EXTENDED_ART;
                case "":
                    return NONE;
                default:
                    return valueOf(value.toUpperCase());
            }
        }
    }

    /**
     * The possible Magic games where a given card can exist: PAPER, ARENA, and MTGO.
     * Scryfall's database also includes cards from the Shandalar digital game (ASTRAL),
     * as well cards which were available on the Sega Dreamcast (SEGA).
     */
    @SuppressWarnings("unused")
    public enum Game {
        PAPER, ARENA, MTGO, ASTRAL, SEGA;

        public static Game fromString(String value) {
            return valueOf(value.toUpperCase());
        }
    }

    /**
     * Different rarities a card can have: COMMON, UNCOMMON, RARE, MYTHIC, SPECIAL, and BONUS.
     * If the rarity field is missing or malformed, the the card's rarity will be <code>Rarity.NONE</code>
     */
    public enum Rarity {
        COMMON, UNCOMMON, RARE, MYTHIC, SPECIAL, BONUS, NONE;

        private static Rarity fromString(String value) {
            try {
                return valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                return NONE;
            }
        }
    }

    /**
     * The possible values are:
     * <li><code>MISSING</code> - The card has no image, or the image is being processed.
     * This value should only be temporary for very new cards.
     * <li><code>PLACEHOLDER</code> - Scryfall doesn’t have an image of this card, but we know it exists
     * and we have uploaded a placeholder in the meantime. This value is most common on localized cards.
     * <li><code>LOWRES</code> - The card’s image is low-quality, either because
     * it was just spoiled or we don’t have better photography for it yet.
     * <li><code>HIGHRES_SCAN</code> - This card has a full-resolution scanner image. Crisp and glossy!
     */
    public enum ImageStatus {
        MISSING, PLACEHOLDER, LOWRES, HIGHRES_SCAN;

        private static ImageStatus fromString(String value) {
            try {
                return valueOf(value.toUpperCase());
            } catch(IllegalArgumentException e) {
                return MISSING;
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
                cardmarketID == card.cardmarketID &&
                edhrecRank == card.edhrecRank &&
                Double.compare(card.cmc, cmc) == 0 &&
                foil == card.foil &&
                nonfoil == card.nonfoil &&
                oversized == card.oversized &&
                digital == card.digital &&
                reserved == card.reserved &&
                inBoosters == card.inBoosters &&
                contentWarning == card.contentWarning &&
                fullArt == card.fullArt &&
                highResImage == card.highResImage &&
                promo == card.promo &&
                reprint == card.reprint &&
                storySpotlight == card.storySpotlight &&
                textless == card.textless &&
                variation == card.variation &&
                Objects.equals(multiverseIDs, card.multiverseIDs) &&
                layout == card.layout &&
                Objects.equals(lang, card.lang) &&
                Objects.equals(handModifier, card.handModifier) &&
                Objects.equals(lifeModifier, card.lifeModifier) &&
                Objects.equals(loyalty, card.loyalty) &&
                Objects.equals(manaCost, card.manaCost) &&
                Objects.equals(name, card.name) &&
                Objects.equals(flavorName, card.flavorName) &&
                Objects.equals(oracleText, card.oracleText) &&
                Objects.equals(power, card.power) &&
                Objects.equals(toughness, card.toughness) &&
                Objects.equals(typeLine, card.typeLine) &&
                Objects.equals(id, card.id) &&
                Objects.equals(oracleID, card.oracleID) &&
                Objects.equals(illustrationID, card.illustrationID) &&
                Objects.equals(variationID, card.variationID) &&
                Objects.equals(cardBackID, card.cardBackID) &&
                Objects.equals(printsSearchURL, card.printsSearchURL) &&
                Objects.equals(rulingsURL, card.rulingsURL) &&
                Objects.equals(scryfallURL, card.scryfallURL) &&
                Objects.equals(url, card.url) &&
                Objects.equals(allParts, card.allParts) &&
                Objects.equals(faces, card.faces) &&
                Objects.equals(promoTypes, card.promoTypes) &&
                Objects.equals(keywords, card.keywords) &&
                Objects.equals(colors, card.colors) &&
                Objects.equals(colorIdentity, card.colorIdentity) &&
                Objects.equals(colorIndicator, card.colorIndicator) &&
                Objects.equals(producedMana, card.producedMana) &&
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
                Objects.equals(frameEffects, card.frameEffects) &&
                Objects.equals(games, card.games) &&
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
                Objects.equals(prices, card.prices) &&
                Objects.equals(preview, card.preview);
    }

    @Override
    public String toString() {
        return "Card{" +
                "arenaID=" + arenaID +
                ", mtgoID=" + mtgoID +
                ", mtgoFoilID=" + mtgoFoilID +
                ", tcgplayerID=" + tcgplayerID +
                ", cardmarketID=" + cardmarketID +
                ", edhrecRank=" + edhrecRank +
                ", multiverseIDs=" + multiverseIDs +
                ", layout=" + layout +
                ", lang='" + lang + '\'' +
                ", handModifier='" + handModifier + '\'' +
                ", lifeModifier='" + lifeModifier + '\'' +
                ", loyalty='" + loyalty + '\'' +
                ", manaCost='" + manaCost + '\'' +
                ", name='" + name + '\'' +
                ", flavorName='" + flavorName + '\'' +
                ", oracleText='" + oracleText + '\'' +
                ", power='" + power + '\'' +
                ", toughness='" + toughness + '\'' +
                ", typeLine='" + typeLine + '\'' +
                ", id=" + id +
                ", oracleID=" + oracleID +
                ", illustrationID=" + illustrationID +
                ", variationID=" + variationID +
                ", cardBackID=" + cardBackID +
                ", printsSearchURL=" + printsSearchURL +
                ", rulingsURL=" + rulingsURL +
                ", scryfallURL=" + scryfallURL +
                ", url=" + url +
                ", allParts=" + allParts +
                ", faces=" + faces +
                ", promoTypes=" + promoTypes +
                ", keywords=" + keywords +
                ", cmc=" + cmc +
                ", colors=" + colors +
                ", colorIdentity=" + colorIdentity +
                ", colorIndicator=" + colorIndicator +
                ", producedMana=" + producedMana +
                ", foil=" + foil +
                ", nonfoil=" + nonfoil +
                ", oversized=" + oversized +
                ", digital=" + digital +
                ", reserved=" + reserved +
                ", inBoosters=" + inBoosters +
                ", contentWarning=" + contentWarning +
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
                ", frameEffects=" + frameEffects +
                ", fullArt=" + fullArt +
                ", highResImage=" + highResImage +
                ", promo=" + promo +
                ", reprint=" + reprint +
                ", storySpotlight=" + storySpotlight +
                ", textless=" + textless +
                ", variation=" + variation +
                ", games=" + games +
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
                ", preview=" + preview +
                '}';
    }
}