package com.scyrfall.api.object;

import com.scyrfall.api.ScryfallObject;
import com.scyrfall.api.query.Query;
import org.json.JSONObject;

import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * A Set object represents a group of related Magic cards. All Card objects on Scryfall belong to exactly one set.
 * <br>
 * Due to Magic’s long and complicated history, Scryfall includes many un-official sets as a way to group promotional or
 * outlier cards together. Such sets will likely have a four-letter code that begins with p or t, such as pcel or tori.
 * <br>
 * Official sets always have a three-letter set code, such as zen.
 */
public class Set extends ScryfallObject {

    private String code, mtgoCode, name, blockCode, block, parentSetCode;
    private SetType setType;
    private Date released;
    private int cardCount, tcgPlayerID;
    private boolean digital, foilOnly;
    private URL iconSvgURL, searchURL, scryfallURL, url;
    private UUID id;


    public Set(JSONObject data) {
        super(data);
        id = getUUID("id");
        code = getString("code");
        mtgoCode = getString("mtgo_code");
        name = getString("name");
        setType = SetType.fromString(getString("set_type"));
        blockCode = getString("block_code");
        block = getString("block");
        parentSetCode = getString("parent_set_code");
        released = getDate("released_at");
        tcgPlayerID = getInt("tcgplayer_id");
        cardCount = getInt("card_count");
        digital = getBoolean("digital");
        foilOnly = getBoolean("foil_only");
        iconSvgURL = getURL("icon_svg_uri");
        searchURL = getURL("search_uri");
        scryfallURL = getURL("scryfall_uri");
        url = getURL("uri");
    }

    /**
     * @return The unique three or four-letter code for this set.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return The unique code for this set on MTGO, which may differ from the regular code.
     */
    public String getMtgoCode() {
        return mtgoCode;
    }

    /**
     * @return The English name of the set.
     */
    public String getName() {
        return name;
    }

    /**
     * @return A computer-readable classification for this set.
     * @see SetType
     */
    public SetType getSetType() {
        return setType;
    }

    /**
     * @return The block code for this set, if any.
     */
    public String getBlockCode() {
        return blockCode;
    }

    /**
     * @return The block or group name code for this set, if any.
     */
    public String getBlock() {
        return block;
    }

    /**
     * @return The set code for the parent set, if any. promo and token sets often have a parent set.
     */
    public String getParentSetCode() {
        return parentSetCode;
    }

    /**
     * @return The date the set was released or the first card was printed in the set (in GMT-8 Pacific time).
     */
    public Date getReleased() {
        return released;
    }

    /**
     * @return The number of cards in this set.
     */
    public int getCardCount() {
        return cardCount;
    }

    /**
     * @return True if this set was only released on Magic Online.
     */
    public boolean isDigital() {
        return digital;
    }

    /**
     * @return True if this set contains only foil cards.
     */
    public boolean isFoilOnly() {
        return foilOnly;
    }

    /**
     * @return A URL to an SVG file for this set’s icon on Scryfall’s CDN. Hotlinking this image isn’t recommended,
     * because it may change slightly over time. You should download it and use it locally for your particular user
     * interface needs.
     */
    public URL getIconSvgURL() {
        return iconSvgURL;
    }

    /**
     * @return A Scryfall API URL that you can request to begin paginating over the cards in this set.
     */
    public URL getSearchURL() {
        return searchURL;
    }

    /**
     * @return A link to this set’s permapage on Scryfall’s website.
     */
    public URL getScryfallURL() {
        return scryfallURL;
    }

    /**
     * @return A link to this set object on Scryfall’s API.
     */
    public URL getUrl() {
        return url;
    }

    /**
     * @return An array of all cards in the set.
     * @see Card
     */
    public Card[] getCards() {
        return List.fromURL(searchURL).getCards();
    }

    /**
     * @return This set’s ID on TCGplayer’s API, also known as the groupId.
     */
    public int getTcgPlayerID() {
        return tcgPlayerID;
    }

    /**
     * @return A unique ID for this set on Scryfall that will not change.
     */
    public UUID getId() {
        return id;
    }


    /**
     * @param code The code of the set to be retrieved
     * @return A <code>Set</code> with the specified code on Scryfall's API
     */
    public static Set fromCode(String code) {
        return new Set(Query.dataFromPath("sets/" + code));
    }

    /**
     * @param id the TCGPlayer id of the Set to be retrieved
     * @return Returns a Set with the given tcgPlayerID, also known as the groupId on TCGplayer’s API.
     */
    public static Set fromTCGplayerID(int id) {
        return new Set(Query.dataFromPath("sets/tcgplayer/" + id));
    }

    /**
     * @param id the Scryfall id of the Set to be retrieved
     * @return Returns a Set with the given Scryfall id.
     */
    public static Set fromID(UUID id) {
        return new Set(Query.dataFromPath("sets/" + id));
    }

    /**
     * <table><thead><tr><th>Type</th><th>Description</th></tr></thead>
     * <tbody><tr><td><p><code>CORE</code></p></td><td><p>A yearly Magic CORE set (Tenth Edition, etc)</p>
     * </td></tr><tr><td><p><code>EXPANSION</code></p></td><td><p>A rotational EXPANSION set in a block (Zendikar, etc)</p>
     * </td></tr><tr><td><p><code>MASTERS</code></p></td><td><p>A reprint set that contains no new cards (Modern Masters, etc)</p>
     * </td></tr><tr><td><p><code>MASTERPIECE</code></p></td><td><p>Masterpiece Series premium foil cards</p>
     * </td></tr><tr><td><p><code>FROM_THE_VAULT</code></p></td><td><p>From the Vault gift sets</p>
     * </td></tr><tr><td><p><code>SPELLBOOK</code></p></td><td><p>Spellbook series gift sets</p>
     * </td></tr><tr><td><p><code>PREMIUM_DECK</code></p></td><td><p>Premium Deck Series decks</p>
     * </td></tr><tr><td><p><code>DUEL_DECK</code></p></td><td><p>Duel Decks</p>
     * </td></tr><tr><td><p><code>DRAFT_INNOVATION</code></p></td><td><p>Special draft sets, like Conspiracy and Battlebond</p>
     * </td></tr><tr><td><p><code>TREASURE_CHEST</code></p></td><td><p>Magic Online treasure chest prize sets</p>
     * </td></tr><tr><td><p><code>COMMANDER</code></p></td><td><p>Commander preconstructed decks</p>
     * </td></tr><tr><td><p><code>PLANECHASE</code></p></td><td><p>Planechase sets</p>
     * </td></tr><tr><td><p><code>ARCHENEMY</code></p></td><td><p>Archenemy sets</p>
     * </td></tr><tr><td><p><code>VANGUARD</code></p></td><td><p>Vanguard card sets</p>
     * </td></tr><tr><td><p><code>FUNNY</code></p></td><td><p>A FUNNY un-set or set with FUNNY promos (Unglued, Happy Holidays, etc)</p>
     * </td></tr><tr><td><p><code>STARTER</code></p></td><td><p>A STARTER/introductory set (Portal, etc)</p>
     * </td></tr><tr><td><p><code>BOX</code></p></td><td><p>A gift BOX set</p>
     * </td></tr><tr><td><p><code>PROMO</code></p></td><td><p>A set that contains purely promotional cards</p>
     * </td></tr><tr><td><p><code>TOKEN</code></p></td><td><p>A set made up of tokens and emblems.</p>
     * </td></tr><tr><td><p><code>MEMORABILIA</code></p></td><td><p>A set made up of GOLD-bordered, oversize, or trophy cards that are not legal</p>
     * </td></tr></tbody></table>
     * Default value is <code>CORE</code>.
     */
    public enum SetType {
        CORE, EXPANSION, MASTERS, MASTERPIECE, FROM_THE_VAULT, SPELLBOOK, PREMIUM_DECK, DUEL_DECK, DRAFT_INNOVATION,
        TREASURE_CHEST, COMMANDER, PLANECHASE, ARCHENEMY, VANGUARD, FUNNY, STARTER, BOX, PROMO, TOKEN, MEMORABILIA;

        private static SetType fromString(String value) {
            switch (value) {
                case "from_the_vault":
                    return FROM_THE_VAULT;
                case "premium_deck":
                    return PREMIUM_DECK;
                case "duel_deck":
                    return DUEL_DECK;
                case "draft_innovation":
                    return DRAFT_INNOVATION;
                case "treasure_chest":
                    return TREASURE_CHEST;
                default:
                    try {
                        return valueOf(value.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        return CORE;
                    }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Set set = (Set) o;
        return cardCount == set.cardCount &&
                digital == set.digital &&
                foilOnly == set.foilOnly &&
                Objects.equals(code, set.code) &&
                Objects.equals(mtgoCode, set.mtgoCode) &&
                Objects.equals(name, set.name) &&
                Objects.equals(blockCode, set.blockCode) &&
                Objects.equals(block, set.block) &&
                Objects.equals(parentSetCode, set.parentSetCode) &&
                setType == set.setType &&
                Objects.equals(released, set.released) &&
                Objects.equals(iconSvgURL, set.iconSvgURL) &&
                Objects.equals(searchURL, set.searchURL);
    }

    @Override
    public String toString() {
        return "Set{" +
                "code='" + code + '\'' +
                ", mtgoCode='" + mtgoCode + '\'' +
                ", name='" + name + '\'' +
                ", blockCode='" + blockCode + '\'' +
                ", block='" + block + '\'' +
                ", parentSetCode='" + parentSetCode + '\'' +
                ", setType=" + setType +
                ", released=" + released +
                ", cardCount=" + cardCount +
                ", digital=" + digital +
                ", foilOnly=" + foilOnly +
                ", iconSvgURL=" + iconSvgURL +
                ", searchURL=" + searchURL +
                '}';
    }
}
