package com.scyrfall.api.object;

import com.scyrfall.api.ScryfallObject;
import com.scyrfall.api.query.Query;
import org.json.JSONObject;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

// TODO: Implement type query parameter

public class BulkData extends ScryfallObject {

    private UUID id;
    private String type, name, description, contentMimeType, contentEncoding;
    private URL downloadURL, url;
    private ZonedDateTime updated;
    private int size;

    public BulkData(JSONObject data) {
        super(data);
        id = getUUID("id");
        type = getString("type");
        name = getString("name");
        description = getString("description");
        contentEncoding = getString("content_encoding");
        contentMimeType = getString("content_type");
        downloadURL = getURL("download_uri");
        url = getURL("uri");
        updated = ZonedDateTime.parse(getString("released_at"));
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
     * @return The URL that hosts this bulk file for fetching.
     */
    public URL getDownloadURL() {
        return downloadURL;
    }

    /**
     * @return The Scryfall API URL for this file.
     */
    public URL getUrl() {
        return url;
    }

    /**
     * @return The time when this file was last updated.
     */
    public ZonedDateTime getUpdated() {
        return updated;
    }

    /**
     * @return The size of this file in integer bytes.
     * @deprecated Use {@link BulkData#getSize()} instead
     */
    @Deprecated
    public int getCompressedSize() {
        return size;
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
        return new ScryfallList(Query.dataFromPath("bulk-data")).getContents(new BulkData[0]);
    }

    /**
     * @param id The UUID of the Bulk Data object to be retrieved
     * @return a single Bulk Data object with the given id.
     * @see BulkData#getId()
     */
    public static BulkData fromId(UUID id) {
        return new BulkData(Query.dataFromPath("bulk-data/" + id.toString()));
    }

    /**
     * @param type the <code>type</code> of the Bulk Data object to be retrieved
     * @return a single Bulk Data object with the given type.
     * @see BulkData#getType()
     */
    public static BulkData fromType(String type) {
        return new BulkData(Query.dataFromPath("bulk-data/" + type));
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
                Objects.equals(downloadURL, bulkData.downloadURL) &&
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
                ", permalinkURL=" + downloadURL +
                ", updated=" + updated +
                ", size=" + size +
                '}';
    }
}
