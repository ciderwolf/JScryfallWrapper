package com.scyrfall.api.field;

import com.scyrfall.api.ScryfallObject;
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
     * @return A computer-readable string indicating which company produced this ruling, either wotc or scryfall.
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
     * Possible sources for a ruling. The only values should be <code>wotc</code> and <code>scryfall</code>.
     * If a different value is provided in the constructor, {@link #source} will have the vaule <code>other</code>.
     */
    enum RulingSource {
        wotc, scryfall, other;

        private static RulingSource fromString(String value) {
            try {
                return valueOf(value);
            } catch (IllegalArgumentException e) {
                return other;
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
