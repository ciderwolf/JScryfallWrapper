package com.scyrfall.api.object;

import com.scyrfall.api.ScryfallObject;
import com.scyrfall.api.query.Query;
import org.json.JSONObject;

import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class BulkData extends ScryfallObject {

    private UUID id;
    private String type, name, description, contentMimeType, contentEncoding;
    private URL permalinkURL;
    private Date updated;
    private int size;

    public BulkData(JSONObject data) {
        super(data);
        id = getUUID("id");
        type = getString("type");
        name = getString("name");
        description = getString("description");
        contentEncoding = getString("content_encoding");
        contentMimeType = getString("content_type");
        permalinkURL = getURL("permalink_uri");
        updated = getDate("released_at");
        size = getInt("size");
    }

    /**
     * @return A unique ID for this bulk item.
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return A computer-readable string for the kind of bulk item.
     */
    public String getType() {
        return type;
    }

    /**
     * @return A human-readable name for this file.
     */
    public String getName() {
        return name;
    }

    /**
     * @return A human-readable description for this file.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return The MIME type of this file.
     */
    public String getContentMimeType() {
        return contentMimeType;
    }

    /**
     * @return The Content-Encoding encoding that will be used to transmit this file when you download it.
     */
    public String getContentEncoding() {
        return contentEncoding;
    }

    /**
     * @return The URL that hosts this bulk file.
     */
    public URL getPermalinkURL() {
        return permalinkURL;
    }

    /**
     * @return The time when this file was last updated.
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * @return The size of this file in integer bytes.
     */
    public int getSize() {
        return size;
    }

    /**
     * @return An array of all bulk data items on Scryfall.
     */
    public static BulkData[] getBulkData() {
        return new List(Query.dataFromPath("bulk-data")).getContents(new BulkData[0]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BulkData bulkData = (BulkData) o;
        return size == bulkData.size &&
                Objects.equals(id, bulkData.id) &&
                Objects.equals(type, bulkData.type) &&
                Objects.equals(name, bulkData.name) &&
                Objects.equals(description, bulkData.description) &&
                Objects.equals(contentMimeType, bulkData.contentMimeType) &&
                Objects.equals(contentEncoding, bulkData.contentEncoding) &&
                Objects.equals(permalinkURL, bulkData.permalinkURL) &&
                Objects.equals(updated, bulkData.updated);
    }

    @Override
    public String toString() {
        return "BulkData{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", contentMimeType='" + contentMimeType + '\'' +
                ", contentEncoding='" + contentEncoding + '\'' +
                ", permalinkURL=" + permalinkURL +
                ", updated=" + updated +
                ", size=" + size +
                '}';
    }
}
