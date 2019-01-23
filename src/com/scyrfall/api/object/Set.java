package com.scyrfall.api.object;

import com.scyrfall.api.query.JSONLoader;
import com.scyrfall.api.query.Query;
import com.scyrfall.api.ScryfallObject;
import org.json.JSONObject;

import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

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
    private int cardCount;
    private boolean digital, foilOnly;
    private URL iconSvgURL, searchURL;

    public Set(JSONObject data) {
        super(data);
        code = getString("code");
        mtgoCode = getString("mtgo_code");
        name = getString("name");
        setType = SetType.fromString(getString("set_type"));
        blockCode = getString("blockCode");
        block = getString("block");
        parentSetCode = getString("parent_set_code");
        try {
            released = dateFormat.parse(getString("released_at"));
        } catch (ParseException e) {
            released = null;
        }
        cardCount = getInt("card_count");
        digital = getBoolean("digital");
        foilOnly = getBoolean("foil_only");
        iconSvgURL = getURL("icon_svg_uri");
        searchURL = getURL("search_uri");
    }

    public static Set fromCode(String code) {
        return new Set(JSONLoader.JSONObjectFromURL(Query.API_STUB + "sets/" + code));
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
     * @return An array of all cards in the set.
     * @see Card
     */
    public Card[] getCards() {
        return List.fromURL(searchURL).getCards();
    }

    /**
     * <table><thead><tr><th>Type</th><th>Description</th></tr></thead>
     * <tbody><tr><td><p><code>core</code></p></td><td><p>A yearly Magic core set (Tenth Edition, etc)</p>
     * </td></tr><tr><td><p><code>expansion</code></p></td><td><p>A rotational expansion set in a block (Zendikar, etc)</p>
     * </td></tr><tr><td><p><code>masters</code></p></td><td><p>A reprint set that contains no new cards (Modern Masters, etc)</p>
     * </td></tr><tr><td><p><code>masterpiece</code></p></td><td><p>Masterpiece Series premium foil cards</p>
     * </td></tr><tr><td><p><code>fromTheVault</code></p></td><td><p>From the Vault gift sets</p>
     * </td></tr><tr><td><p><code>spellbook</code></p></td><td><p>Spellbook series gift sets</p>
     * </td></tr><tr><td><p><code>premiumDeck</code></p></td><td><p>Premium Deck Series decks</p>
     * </td></tr><tr><td><p><code>duelDeck</code></p></td><td><p>Duel Decks</p>
     * </td></tr><tr><td><p><code>draftInnovation</code></p></td><td><p>Special draft sets, like Conspiracy and Battlebond</p>
     * </td></tr><tr><td><p><code>treasureChest</code></p></td><td><p>Magic Online treasure chest prize sets</p>
     * </td></tr><tr><td><p><code>commander</code></p></td><td><p>Commander preconstructed decks</p>
     * </td></tr><tr><td><p><code>planechase</code></p></td><td><p>Planechase sets</p>
     * </td></tr><tr><td><p><code>archenemy</code></p></td><td><p>Archenemy sets</p>
     * </td></tr><tr><td><p><code>vanguard</code></p></td><td><p>Vanguard card sets</p>
     * </td></tr><tr><td><p><code>funny</code></p></td><td><p>A funny un-set or set with funny promos (Unglued, Happy Holidays, etc)</p>
     * </td></tr><tr><td><p><code>starter</code></p></td><td><p>A starter/introductory set (Portal, etc)</p>
     * </td></tr><tr><td><p><code>box</code></p></td><td><p>A gift box set</p>
     * </td></tr><tr><td><p><code>promo</code></p></td><td><p>A set that contains purely promotional cards</p>
     * </td></tr><tr><td><p><code>token</code></p></td><td><p>A set made up of tokens and emblems.</p>
     * </td></tr><tr><td><p><code>memorabilia</code></p></td><td><p>A set made up of gold-bordered, oversize, or trophy cards that are not legal</p>
     * </td></tr></tbody></table>
     * Default value is <code>core</code>.
     */
    enum SetType {
        core, expansion, masters, masterpiece, fromTheVault, spellbook, premiumDeck, duelDeck, draftInnovation,
        treasureChest, commander, planechase, archenemy, vanguard, funny, starter, box, promo, token, memorabilia;

        private static SetType fromString(String value) {
            switch (value) {
                case "from_the_vault":
                    return fromTheVault;
                case "premium_deck":
                    return premiumDeck;
                case "duel_deck":
                    return duelDeck;
                case "draft_innovation":
                    return draftInnovation;
                case "treasure_chest":
                    return treasureChest;
                default:
                    try {
                        return valueOf(value);
                    } catch (IllegalArgumentException e) {
                        return core;
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
