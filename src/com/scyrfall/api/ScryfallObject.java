package com.scyrfall.api;

import com.scyrfall.api.field.CardFace;
import com.scyrfall.api.field.RelatedCard;
import com.scyrfall.api.object.Card;
import com.scyrfall.api.field.Ruling;
import com.scyrfall.api.object.Set;
import com.scyrfall.api.object.ScryfallError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

public abstract class ScryfallObject {

    protected JSONObject data;
    protected ScryfallError error;
    private boolean isError;
    protected static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    protected ScryfallObject(JSONObject data){
        this.data = data;
        if(getString("object").equals("error")) {
            this.error = new ScryfallError(data);
            isError = true;
        }
        isError = false;
    }

    protected ScryfallObject() {}

    /**
     * @return true if the Scryfall API returned an error when retrieving this object. False if this object was retrieved
     * successfully.
     */
    public boolean isError() {
        return isError;
    }

    /**
     * @return The error produced by this object when it was retrieved from the Scryfall API. If no error was produced,
     * this field will be null.
     */
    public ScryfallError getError() {
        return error;
    }

    /**
     * @param data the dataFromPath from which a ScryfallObject should be initialized
     * @return creates a ScryfallObject based on the dataFromPath's <code>object</code> parameter
     */
    public static ScryfallObject getObject(JSONObject data) {
        String type = data.getString("object");
        ScryfallObject scryfallObject;
        switch (type) {
            case "card":
                scryfallObject = new Card(data);
                break;
            case "set":
                scryfallObject = new Set(data);
                break;
            case "related_card":
                scryfallObject = new RelatedCard(data);
                break;
            case "card_face":
                scryfallObject = new CardFace(data);
                break;
            case "ruling":
                scryfallObject = new Ruling(data);
                break;
            default:
                scryfallObject = null;
        }
        return scryfallObject;
    }

    public static <T extends ScryfallObject> T[] convertArray(ScryfallObject[] list, T[] out) {
        out = Arrays.copyOf(out, list.length);
        for(int i = 0; i < list.length; i++) {
            out[i] = (T) out.getClass().getComponentType().cast(list[i]);
        }
        return out;
    }

    /**
     * @param key The key of the integer to be returned
     * @return The int at the specified key. If the key is invalid,
     * <code>-1</code> is returned instead.
     */
    protected int getInt(String key) {
        try {
            return data.getInt(key);
        } catch (JSONException e) {
            return -1;
        }
    }

    /**
     * @param key The key of the JSONArray to be returned
     * @return The JSONArray at the specified key. If the key is invalid,
     * an empty JSONArray is returned instead.
     */
    protected JSONArray getJSONArray(String key) {
        try {
            return data.getJSONArray(key);
        } catch (JSONException e) {
            return new JSONArray();
        }
    }

    /**
     * @param key The key of the JSONObject to be returned
     * @return The JSONObject at the specified key. If the key is invalid,
     * an empty JSONObject is returned instead.
     */
    protected JSONObject getJSONObject(String key) {
        try {
            return data.getJSONObject(key);
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    /**
     * @param key The key of the double to be returned
     * @return The double at the specified key. If the key is invalid,
     * <code>-1.0</code> is returned instead.
     */
    protected double getDouble(String key) {
        try {
            return data.getDouble(key);
        } catch (JSONException e) {
            return -1.0;
        }
    }

    /**
     * @param key The key of the boolean to be returned
     * @return The boolean at the specified key. If the key is invalid,
     * <code>false</code> is returned instead.
     */
    protected boolean getBoolean(String key) {
        try {
            return data.getBoolean(key);
        } catch (JSONException e) {
            return false;
        }
    }

    /**
     * @param key The key of the string to be returned
     * @return The String at the specified key. If the key is invalid,
     * an empty string is returned instead.
     */
    protected String getString(String key) {
        try {
            return data.getString(key);
        } catch (JSONException e) {
            return "";
        }
    }

    /**
     * Creates a <code>URL</code> from the string located at the specified key.
     * Returns null if they key is invalid or if an exception is thrown during
     * <code>URL</code> initialization.
     * @param key The key of the <code>URL</code> to be created
     * @return A <code>URL</code> from the string located at the specified key
     */
    protected URL getURL(String key) {
        try {
            return new URL(data.getString(key));
        } catch (JSONException | MalformedURLException e) {
            return null;
        }
    }

    protected UUID getUUID(String key) {
        try {
            return UUID.fromString(data.getString(key));
        } catch (JSONException | IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Represents the different colors which appear on a magic card.
     * Values are <code>white</code>, <code>blue</code>, <code>black</code>, <code>red</code>, and
     * <code>green</code>.
     */
    protected enum Color {
        white, blue, black, red, green;

        public static Color fromString(String value) {
            if(value.isEmpty()) {
                return null;
            }
            String colors = "WUBRG";
            return Color.values()[colors.indexOf(value)];
        }
    }
}
