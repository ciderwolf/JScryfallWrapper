package com.scyrfall.api;

import com.scyrfall.api.field.CardFace;
import com.scyrfall.api.field.RelatedCard;
import com.scyrfall.api.field.Ruling;
import com.scyrfall.api.object.Set;
import com.scyrfall.api.object.Symbol;
import com.scyrfall.api.object.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class ScryfallObject {

    protected JSONObject data;
    private ScryfallError error;
    private boolean isError;
    protected static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    protected ScryfallObject(JSONObject data) {
        this.data = data;
        if (getString("object").equals("error")) {
            this.error = new ScryfallError(data);
            isError = true;
        } else {
            isError = false;
        }
    }

    protected ScryfallObject() {
    }

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
     * @param data the data from which a ScryfallObject should be initialized
     * @return creates a ScryfallObject based on the data's <code>object</code> parameter
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
            case "card_symbol":
                scryfallObject = new Symbol(data);
                break;
            case "bulk_data":
                scryfallObject = new BulkData(data);
                break;
            case "error":
                scryfallObject = new ScryfallError(data);
                break;
            case "catalog":
                scryfallObject = new Catalog(data);
                break;
            case "list":
                scryfallObject = new ScryfallList(data);
                break;
            default:
                scryfallObject = null;
        }
        return scryfallObject;
    }

    public static <T extends ScryfallObject> T getObject(JSONObject data, Class<T> c) {
        return c.cast(getObject(data));
    }

    /**
     * Converts an array of <code>ScryfallObject</code>s to an array of the specified type
     *
     * @param list the list of objects to be converted
     * @param out  the list into which the objects will be inserted
     * @param <T>  the type of the ScryfallObjects to be converted
     * @return the provided list as an array of the specified type
     */
    public static <T extends ScryfallObject> T[] convertArray(ScryfallObject[] list, T[] out) {
        out = Arrays.copyOf(out, list.length);
        for (int i = 0; i < list.length; i++) {
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
     * @param key The key of the date to be returned
     * @return A Date object representing the date at the specified key,
     * formatted from the yyyy-MM-dd date format. If the provided key
     * isn't in the correct format or doesn't exist, this moethod will
     * return <code>null</code>.
     */
    protected Date getDate(String key) {
        try {
            return dateFormat.parse(getString(key));
        } catch (ParseException e) {
            return null;
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
    @SuppressWarnings("SameParameterValue")
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
     *
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

    protected <K, V> List<V> getList(String key, Function<K, V> converter, BiFunction<JSONArray, Integer, K> extractor) {
        if(!data.has(key)) {
            return List.of();
        }
        JSONArray data = getJSONArray(key);
        V[] arr = (V[]) new Object[data.length()];
        for (int i = 0; i < data.length(); i++) {
            arr[i] = converter.apply(extractor.apply(data, i));
        }
        return List.of(arr);
    }

    protected <K, V> HashMap<String, V> getMap(String key, Function<K, V> converter, BiFunction<JSONObject, String, K> extractor) {
        if (!data.has(key)) {
            return new HashMap<>();
        }
        JSONObject data = getJSONObject(key);
        HashMap<String, V> map = new HashMap<>();
        for (int i = 0; i < data.names().length(); i++) {
            String name = data.names().getString(i);
            data.put(name, converter.apply(extractor.apply(data, name)));
        }
        return map;
    }

    /**
     * Represents the different colors which appear on a magic card.
     * Values are <code>WHITE</code>, <code>BLUE</code>, <code>BLACK</code>, <code>RED</code>, and
     * <code>GREEN</code>.
     */
    public enum Color {
        WHITE, BLUE, BLACK, RED, GREEN;

        public static Color fromString(String value) {
            if (value.isEmpty()) {
                return null;
            }
            String colors = "WUBRG";
            return Color.values()[colors.indexOf(value)];
        }
    }
}
