package com.scyrfall.api.field;

import com.scyrfall.api.ScryfallObject;
import org.json.JSONObject;

import java.net.URL;
import java.util.Date;
import java.util.Objects;

public class Preview extends ScryfallObject {

    private final Date previewedAt;
    private final String source;
    private final URL sourceURL;

    public Preview(JSONObject data) {
        super(data);
        previewedAt = getDate("previewed_at");
        source = getString("source");
        sourceURL = getURL("source_uri");
    }

    /**
     * @return The date this card was previewed.
     */
    public Date getPreviewedAt() {
        return previewedAt;
    }

    /**
     * @return The name of the source that previewed this card.
     */
    public String getSource() {
        return source;
    }

    /**
     * @return A link to the preview for this card.
     */
    public URL getSourceURL() {
        return sourceURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Preview preview = (Preview) o;
        return Objects.equals(previewedAt, preview.previewedAt) &&
                Objects.equals(source, preview.source) &&
                Objects.equals(sourceURL, preview.sourceURL);
    }

    @Override
    public String toString() {
        return "Preview{" +
                "previewedAt=" + previewedAt +
                ", source='" + source + '\'' +
                ", sourceURL=" + sourceURL +
                '}';
    }
}
