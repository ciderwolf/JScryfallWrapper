package com.scyrfall.api.field;

import com.scyrfall.api.ScryfallObject;
import com.scyrfall.api.query.Query;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Ruling extends ScryfallObject {

    private Date publishedDate;
    private RulingSource source;
    private String comment;
    private UUID oracleID;

    public Ruling(JSONObject data) {
        super(data);

        comment = getString("comment");
        try {
            publishedDate = dateFormat.parse(getString("released_at"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        source = RulingSource.fromString(getString("source"));
        oracleID = getUUID("oracle_id");
    }

    /**
     * @return The date when the ruling or note was published.
     */
    public Date getPublishedDate() {
        return publishedDate;
    }

    /**
     * @return A computer-readable string indicating which company produced this ruling, either WOTC or SCRYFALL.
     */
    public RulingSource getSource() {
        return source;
    }

    /**
     * @return The text of the ruling.
     */
    public String getComment() {
        return comment;
    }

    /**
     * @return the oracle id of the card on which this ruling was made
     */
    public UUID getOracleID() {
        return oracleID;
    }

    /**
     * Retrieves rulings for a card with the given MTGO id.
     *
     * @param id The MTGO id the of the card for which rulings should be retrieved.
     * @return A <code>Card</code> object containing the corresponding card's rulings.
     */
    public static Ruling fromMtgoID(int id) {
        return new Ruling(Query.dataFromPath("cards/MTGO/" + id + "/rulings"));
    }

    /**
     * @param id the MTG Arena id of the card for which rulings should be retrieved
     * @return rulings for the card with the given MTG Arena ID
     */
    public static Ruling fromArenaID(int id) {
        return new Ruling(Query.dataFromPath("cards/ARENA/" + id + "/rulings"));
    }

    /**
     * @param id the Multiverse id of the card for which ruling should be retrieved
     * @return rulings for the card with the given Multiverse ID
     */
    public static Ruling fromMultiverseID(int id) {
        return new Ruling(Query.dataFromPath("cards/multiverse/" + id + "/rulings"));
    }

    /**
     * @param id the Scryfall id of the card for which rulings should be retrieved
     * @return rulings for the card with the given Scryfall ID
     */
    public static Ruling fromID(UUID id) {
        return new Ruling(Query.dataFromPath("cards/" + id + "/rulings"));
    }

    /**
     * @param setCode          the code for the set of the card for which rulings should be retrieved
     * @param collectorsNumber the collectors number of the card for which rulings should be retrieved
     * @return rulings for the card of the specified collectors number from the specified set
     */
    public static Ruling fromSetNumber(String setCode, int collectorsNumber) {
        return new Ruling(Query.dataFromPath("cards/" + setCode + "/" + collectorsNumber + "/rulings"));
    }

    /**
     * Possible sources for a ruling. The only values should be <code>WOTC</code> and <code>SCRYFALL</code>.
     * If a different value is provided in the constructor, {@link #source} will have the vaule <code>OTHER</code>.
     */
    @SuppressWarnings("unused")
    public enum RulingSource {
        WOTC, SCRYFALL, OTHER;

        private static RulingSource fromString(String value) {
            try {
                return valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                return OTHER;
            }
        }
    }

    @Override
    public String toString() {
        return "Ruling{" +
                "publishedDate=" + publishedDate +
                ", source=" + source +
                ", comment='" + comment + '\'' +
                ", oracleID=" + oracleID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ruling ruling = (Ruling) o;
        return Objects.equals(publishedDate, ruling.publishedDate) &&
                source == ruling.source &&
                Objects.equals(comment, ruling.comment) &&
                Objects.equals(oracleID, ruling.oracleID);
    }

}