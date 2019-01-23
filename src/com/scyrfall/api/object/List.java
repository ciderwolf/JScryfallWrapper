package com.scyrfall.api.object;

import com.scyrfall.api.query.Query;
import com.scyrfall.api.ScryfallObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * A List object represents a requested sequence of other objects (Cards, Sets, etc). List objects may be paginated, and
 * also include information about issues raised when generating the list.
 */
public class List extends ScryfallObject {

    private JSONArray data;
    private boolean hasMore;
    private URL nextPageURL;
    private int totalCards;
    private String[] warnings;

    public List(JSONObject data) {
        super(data);

        this.data = getJSONArray("data");
        hasMore = getBoolean("has_more");
        totalCards = getInt("total_cards");

        if(data.has("warnings")) {
            JSONArray warnings = getJSONArray("warnings");
            this.warnings = new String[warnings.length()];
            for (int i = 0; i < warnings.length(); i++) {
                this.warnings[i] = warnings.getString(i);
            }
        }
        nextPageURL = getURL("next_page");
    }

    /**
     * @return A JSONArray of the requested objects, in a specific order.
     */
    public JSONArray getData() {
        return data;
    }

    /**
     * @return True if this List is paginated and there is a page beyond the current page.
     */
    public boolean hasMore() {
        return hasMore;
    }

    /**
     * @return If there is a page beyond the current page, this field will contain a full API URI to that page. You may
     * submit a HTTP GET request to that URI to continue paginating forward on this List.
     */
    public URL getNextPageURL() {
        return nextPageURL;
    }

    /**
     * @return If this is a list of Card objects, this field will contain the total number of cards found across all pages.
     */
    public int getTotalCards() {
        return totalCards;
    }

    /**
     * @return An array of human-readable warnings issued when generating this list, as strings. Warnings are non-fatal
     * issues that the API discovered with your input. In general, they indicate that the List will not contain the all
     * of the information you requested. You should fix the warnings and re-submit your request.
     */
    public String[] getWarnings() {
        return warnings;
    }

    /**
     * @return If there is a page beyond this page, returns a List object for that page.
     */
    public List getNextPage() {
        if(hasMore) {
            return new List(Query.dataFromURL(nextPageURL));
        }
        else {
            return null;
        }
    }

    /**
     * @return An array of ScryfallObjects constructed from the dataFromPath on this page and on all subsequent pages.
     */
    public ScryfallObject[] getContents() {
        List current = this;
        ArrayList<ScryfallObject> total = new ArrayList<>();
        boolean repeat = true;
        while (repeat) {
            repeat = current.hasMore;
            for(int i = 0; i < current.data.length(); i++) {
                JSONObject object = current.data.getJSONObject(i);
                total.add(ScryfallObject.getObject(object));
            }
            current = getNextPage();
        }
        return total.toArray(new ScryfallObject[0]);
    }

    /**
     * @param out the array
     * @param <T> the type of the contents of the list
     * @return an array of the specified type which contains the contents of the list
     */
    public <T extends ScryfallObject> T[] getContents(T[] out) {
        return ScryfallObject.convertArray(getContents(), out);
    }

    /**
     * @return An array of Cards constructed from the dataFromPath on this page and on all subsequent pages.
     */
    public Card[] getCards() {
        List current = this;
        ArrayList<Card> total = new ArrayList<>();
        boolean repeat = true;
        while (repeat) {
            repeat = current.hasMore;
            for(int i = 0; i < current.data.length(); i++) {
                JSONObject object = current.data.getJSONObject(i);
                if(object.getString("object").equals("card")) {
                    total.add(new Card(object));
                }
            }
            current = getNextPage();
        }
        return total.toArray(new Card[0]);
    }

    public static List fromURL(URL url) {
        return new List(Query.dataFromURL(url));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        List list = (List) o;
        return hasMore == list.hasMore &&
                totalCards == list.totalCards &&
                Objects.equals(data, list.data) &&
                Objects.equals(nextPageURL, list.nextPageURL) &&
                Arrays.equals(warnings, list.warnings);
    }

    @Override
    public String toString() {
        return "List{" +
                "dataFromPath=" + data +
                ", hasMore=" + hasMore +
                ", nextPage=" + nextPageURL +
                ", totalCards=" + totalCards +
                ", warnings=" + Arrays.toString(warnings) +
                '}';
    }
}
