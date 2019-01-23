package com.scyrfall.api.field;

import com.scyrfall.api.ScryfallObject;
import com.scyrfall.api.object.Card;
import org.json.JSONObject;

import java.net.URL;
import java.util.Objects;
import java.util.UUID;

/**
 * Cards that are closely related to other cards (because they call them by name, or generate a token, or meld, etc)
 * have a getAllParts() property that contains Related Card objects. Those objects have the following properties:
 * @see Card#getAllParts()
 */
public class RelatedCard extends ScryfallObject {
    private UUID id;
    private String name, typeLine;
    private URL url;
    private Component component;

    public RelatedCard(JSONObject data) {
        super(data);
        url = getURL("url");
        id = getUUID("id");
        component = Component.fromString(getString("component"));
        name = getString("name");
        typeLine = getString("type_line");
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTypeLine() {
        return typeLine;
    }

    public URL getUrl() {
        return url;
    }

    public Component getComponent() {
        return component;
    }

    enum Component {
        token, meldPart, meldResult, comboPiece;

        private static Component fromString(String value) {
            if(value.equals("meld_part")) {
                return meldPart;
            }
            else if(value.equals("meld_result")) {
                return meldResult;
            }
            else if(value.equals("combo_piece")) {
                return comboPiece;
            }
            else {
                return token;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelatedCard that = (RelatedCard) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(typeLine, that.typeLine) &&
                Objects.equals(url, that.url) &&
                component == that.component;
    }

}
