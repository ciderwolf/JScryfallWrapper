package com.scyrfall.api.query;

import com.scyrfall.api.ScryfallObject;
import com.scyrfall.api.field.Images;
import com.scyrfall.api.object.Card;
import com.scyrfall.api.object.Catalog;
import com.scyrfall.api.object.List;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

public abstract class QueryRoute {
    protected String name;
    protected Map<String, String> params;
    private String[] required;

    protected QueryRoute(String name, String... required) {
        this.name = name;
        params = new HashMap<>();
        this.required = required == null ? new String[0] : required;
    }

    protected void addParam(String key, String value) {
        params.put(key, value);
    }

    protected boolean allRequired() {
        for (String key : required) {
            if (!key.isEmpty() && !params.containsKey(key)) {
                return false;
            }
        }
        return true;
    }

    protected String buildPath() {
        StringJoiner paramJoiner = new StringJoiner("&");
        params.forEach((key, value) -> {
            paramJoiner.add(key + ":" + value);
        });
        return name + "?" + paramJoiner.toString();
    }

    protected JSONObject getData() {
        return Query.dataFromPath(buildPath());
    }

    public abstract ScryfallObject get();


    /**
     * Specifies a route which finds a card based on a namespace and id. For example:
     * &emsp;<li> /cards/arena/:id
     * &emsp;<li> /cards/mtgo/:id
     * @param <T> The type of the id this route depends on.
     */
    public static class Id<T> extends QueryRoute {

        private T id;
        String routeName;

        protected Id(String name) {
            super("cards/:name/:id", "id");
            this.routeName = name;
        }

        /**
         * @param id the id to use with this request
         * @return This object, for chaining
         */
        public Id<T> id(T id) {
            this.id = id;
            return this;
        }

        /**
         * This query will return the back of the specified card if available, otherwise null.
         *
         * @return This object, for chaining
         */
        public Id<T> backImageFace() {
            addParam("face", "back");
            return this;
        }

        /**
         * @param size The size of the image to fetch from scryfall.
         * @return This object, for chaining
         * @see Images.Size
         */
        public Id<T> imageVersion(Images.Size size) {
            addParam("version", size.toParameterString());
            return this;
        }

        /**
         * @return The image for the specified card. Respects the imageVersion and backImageFace specifiers
         * @see Id#imageVersion
         * @see Id#backImageFace
         */
        public BufferedImage getImage() {
            addParam("format", "image");
            return Query.imageFromPath(buildPath());
        }

        @Override
        protected String buildPath() {
            this.name = "cards/" + routeName + "/" + id.toString();
            return super.buildPath();
        }

        @Override
        public Card get() {
            return new Card(getData());
        }
    }

    /**
     * Returns a single card with the given set code and collector number. You may optionally also append a language
     * code to retrieve a non-English version of the card.
     */
    public static class CodeAndNumber extends QueryRoute {

        private String code, number, lang;

        protected CodeAndNumber() {
            super("cards/:code/:number(/:lang)", "code", "number");
        }

        /**
         * @param code the set code for the card to be retrieved
         * @return This object, for chaining.
         */
        public CodeAndNumber setCode(String code) {
            this.code = code;
            return this;
        }

        /**
         * @param number The collector's number of the card to be retrieved
         * @return This object, for chaining
         */
        public CodeAndNumber collectorNumber(String number) {
            this.number = number;
            return this;
        }

        /**
         * @param lang The language code of the card to be retrieved.
         * @return This object, for chaining
         * @see <a href="https://scryfall.com/docs/api/languages">Scryfall Langauge Codes</a>
         */
        public CodeAndNumber language(String lang) {
            this.lang = lang;
            return this;
        }

        /**
         * This query will return the back of the specified card if available, otherwise null.
         *
         * @return This object, for chaining
         */
        public CodeAndNumber backImageFace() {
            addParam("face", "back");
            return this;
        }

        /**
         * @param size The size of the image to fetch from scryfall.
         * @return This object, for chaining
         * @see Images.Size
         */
        public CodeAndNumber imageVersion(Images.Size size) {
            addParam("version", size.toParameterString());
            return this;
        }

        /**
         * @return The image for the specified card. Respects the imageVersion and backImageFace specifiers
         * @see CodeAndNumber#imageVersion
         * @see CodeAndNumber#backImageFace
         */
        public BufferedImage getImage() {
            addParam("format", "image");
            return Query.imageFromPath(buildPath());
        }

        @Override
        protected String buildPath() {
            this.name = "cards/" + code + "/" + number;
            if (lang != null && !lang.isEmpty()) {
                this.name += "/" + lang;
            }
            return super.buildPath();
        }

        @Override
        public Card get() {
            return new Card(getData());
        }
    }

    /**
     * <p>Accepts a list of card identifiers, and returns a List object with the collection of requested cards.
     * A maximum of 75 card references may be submitted per request.</p>
     *
     * <p>Multiple identifier schemas may be included in a single request. Each identifier will return up to one card.
     * Identifiers that are not found will be returned in the <code>not_found</code> array. While cards will be returned
     * in the order that they were requested, cards that aren’t found will throw off the mapping of request identifiers
     * to results, so you should not rely on positional index alone while parsing the data.</p>
     */
    public static class Collection extends QueryRoute {

        private JSONArray identifiers;

        Collection() {
            super("cards/collection", "identifiers");
            identifiers = new JSONArray();
        }

        private void addIdentifier(JSONObject obj) {
            if (identifiers.length() >= 75) {
                System.err.println(Collection.class.getName() +
                        "A collection query can have no more than 75 references");
                return;
            }
            identifiers.put(obj);
        }

        @Override
        protected void addParam(String key, String value) {
            addIdentifier(new JSONObject().put(key, value));
        }

        private void addParam(String key, int value) {
            addIdentifier(new JSONObject().put(key, value));
        }

        /**
         * @param id Finds a card with the specified Scryfall id.
         * @return This object, for chaining
         */
        public Collection scryfallId(UUID id) {
            addParam("id", id.toString());
            return this;
        }

        /**
         * @param id Finds a card with the specified mtgo id or mtgo foil id.
         * @return This object, for chaining
         */
        public Collection mtgoId(int id) {
            addParam("mtgo_id", id);
            return this;
        }

        /**
         * @param id Finds a card with the specified value among its multiverse ids.
         * @return This object, for chaining
         */
        public Collection multiverseId(int id) {
            addParam("multiverse_id", id);
            return this;
        }

        /**
         * @param id Finds the newest edition of cards with the specified oracle id.
         * @return This object, for chaining
         */
        public Collection oracleId(UUID id) {
            addParam("oracle_id", id.toString());
            return this;
        }

        /**
         * @param id Finds the preferred scans of cards with the specified illustration id.
         * @return This object, for chaining
         */
        public Collection illustrationId(UUID id) {
            addParam("illustration_id", id.toString());
            return this;
        }

        /**
         * @param name Finds the newest edition of a card with the specified name.
         * @return This object, for chaining
         */
        public Collection name(String name) {
            addParam("name", name);
            return this;
        }

        /**
         * Finds a card matching the specified name and set.
         *
         * @param name The specified name
         * @param set  The code for the specified set
         * @return This object, for chaining
         */
        public Collection nameAndSet(String name, String set) {
            addIdentifier(new JSONObject().put("name", name).put("set", set));
            return this;
        }

        /**
         * Finds a card with the specified collector number and set.
         *
         * @param set             The code for the specified set
         * @param collectorNumber The collector's number for the card
         * @return This object, for chaining
         */
        public Collection setAndCollectorNumber(String set, String collectorNumber) {
            addIdentifier(new JSONObject().put("set", set).put("collector_number", collectorNumber));
            return this;
        }

        @Override
        public List get() {
            JSONObject data = Query.postToPath(buildPath(), new JSONObject().put("identifiers", identifiers));
            return new List(data);
        }
    }

    /**
     * Returns a single random Card object.
     */
    public static class Random extends QueryRoute {

        Random() {
            super("cards/random", "");
        }

        /**
         * @param query An optional fulltext search query to filter the pool of random cards.
         *              Make sure that your parameter is properly encoded.
         * @return This object, for chaining
         */
        public Random query(String query) {
            addParam("q", query);
            return this;
        }

        /**
         * This query will return the back of the specified card if available, otherwise null.
         *
         * @return This object, for chaining
         */
        public Random backImageFace() {
            addParam("face", "back");
            return this;
        }

        /**
         * @param size The size of the image to fetch from scryfall.
         * @return This object, for chaining
         * @see Images.Size
         */
        public Random imageVersion(Images.Size size) {
            addParam("version", size.toParameterString());
            return this;
        }

        /**
         * @return The image for the specified card. Respects the imageVersion and backImageFace specifiers
         * @see Random#imageVersion
         * @see Random#backImageFace
         */
        public BufferedImage getImage() {
            addParam("format", "image");
            return Query.imageFromPath(buildPath());
        }

        @Override
        public Card get() {
            return new Card(getData());
        }
    }


    /**
     * <p>Returns a Catalog object containing up to 20 full English card names that could be autocompletions of the given
     * string parameter.</p>
     * <p>This method is designed for creating assistive UI elements that allow users to free-type card names.</p>
     * <p>The names are sorted with the nearest match first, highly favoring results that begin with your
     * given string.</p>
     * <p>Spaces, punctuation, and capitalization are ignored.</p>
     * <p>If <code>q</code> is less than 2 characters long, or if no names match, the Catalog will contain 0 items
     * (instead of returning any errors).</p>
     */
    public static class Autocomplete extends QueryRoute {

        Autocomplete() {
            super("cards/autocomplete", "q");
        }

        /**
         * @param query The string to autocomplete.
         * @return This object, for chaining
         */
        public Autocomplete query(String query) {
            addParam("query", query);
            return this;
        }

        /**
         * @param include If true, extra cards (tokens, planes, vanguards, etc) will be included. Defaults to false.
         * @return This object, for chaining
         */
        public Autocomplete includeExtras(boolean include) {
            addParam("include_extras", Boolean.toString(include));
            return this;
        }

        @Override
        public Catalog get() {
            return new Catalog(getData());
        }
    }


    /**
     * <p>Returns a Card based on a name search string. This method is designed for building chat bots, forum bots, and
     * other services that need card details quickly.</p>
     * <p>If you provide the exact parameter, a card with that exact name is returned. Otherwise, a 404 Error is
     * returned because no card matches.</p>
     * <p>If you provide the fuzzy parameter and a card name matches that string, then that card is returned. If not, a
     * fuzzy search is executed for your card name. The server allows misspellings and partial words to be provided.
     * For example: jac bele will match Jace Beleren.</p>
     * <p>When fuzzy searching, a card is returned if the server is confident that you unambiguously identified a unique
     * name with your string. Otherwise, you will receive a 404 Error object describing the problem: either more than 1
     * one card matched your search, or zero cards matched.</p>
     * <p>You may also provide a set code in the set parameter, in which case the name search and the returned card print
     * will be limited to the specified set.</p>
     * <p>For both exact and fuzzy, card names are case-insensitive and punctuation is optional (you can drop apostrophes
     * and periods etc). For example: fIReBALL is the same as Fireball and smugglers copter is the same as Smuggler's
     * Copter.</p>
     *
     * @see Card
     */
    public static class Named extends QueryRoute {

        Named() {
            super("cards/named", "");
        }

        /**
         * @param name The exact card name to search for, case insensitive.
         * @return This object, for chaining
         */
        public Named exact(String name) {
            addParam("exact", name);
            return this;
        }

        /**
         * @param name A fuzzy card name to search for.
         * @return This object, for chaining
         */
        public Named fuzzy(String name) {
            addParam("fuzzy", name);
            return this;
        }

        /**
         * @param set A set code to limit the search to one set.
         * @return This object, for chaining
         */
        public Named set(String set) {
            addParam("set", set);
            return this;
        }

        /**
         * This query will return the back of the specified card if available, otherwise null.
         *
         * @return This object, for chaining
         */
        public Named backImageFace() {
            addParam("face", "back");
            return this;
        }

        /**
         * @param size The size of the image to fetch from scryfall.
         * @return This object, for chaining
         * @see Images.Size
         */
        public Named imageVersion(Images.Size size) {
            addParam("version", size.toParameterString());
            return this;
        }

        /**
         * @return The image for the specified card. Respects the imageVersion and backImageFace specifiers
         * @see CodeAndNumber#imageVersion
         * @see CodeAndNumber#backImageFace
         */
        public BufferedImage getImage() {
            addParam("format", "image");
            return Query.imageFromPath(buildPath());
        }

        @Override
        public Card get() {
            return new Card(getData());
        }
    }

    /**
     * Returns a List object containing Cards found using a fulltext search string. This string supports the same
     * fulltext search system that the main site uses.
     * <p>
     * This method is paginated, returning 175 cards at a time. Review the documentation for paginating the List
     * type and and the Error type type to understand all of the possible output from this method.
     * <p>
     * If only one card is found, this method will still return a List.
     */
    public static class Search extends QueryRoute {

        Search() {
            super("cards/search", "q");
        }

        /**
         * @param query A fulltext search query.
         * @return This object, for chaining
         */
        public Search query(String query) {
            addParam("q", query);
            return this;
        }

        /**
         * @param mode The strategy for omitting similar cards.
         * @return This object, for chaining
         * @see UniqueMode
         */
        public Search unique(UniqueMode mode) {
            addParam("unique", mode.toParamString());
            return this;
        }

        /**
         * @param order The method to sort returned cards.
         * @return This object, for chaining
         * @see SortingOrder
         */
        public Search order(SortingOrder order) {
            addParam("order", order.toParamString());
            return this;
        }

        /**
         * @param direction The direction to sort cards.
         * @return This object, for chaining
         * @see ResultsDirection
         */
        public Search direction(ResultsDirection direction) {
            addParam("dir", direction.toParamString());
            return this;
        }

        /**
         * @param include If true, extra cards (tokens, planes, etc) will be included. Equivalent to adding
         *                include:extras to the fulltext search. Defaults to false.
         * @return This object, for chaining
         */
        public Search includeExtras(boolean include) {
            addParam("include_extras", Boolean.toString(include));
            return this;
        }

        /**
         * @param include If true, cards in every language supported by Scryfall will be included. Defaults to false.
         * @return This object, for chaining
         */
        public Search includeMultilingual(boolean include) {
            addParam("include_multilingual", Boolean.toString(include));
            return this;
        }

        /**
         * @param include If true, rare care variants will be included, like the Hairy Runesword. Defaults to false.
         * @return This object, for chaining
         * @see <a href="https://scryfall.com/card/drk/107%E2%80%A0/runesword">Hairy Runesword</a>
         */
        public Search includeVariations(boolean include) {
            addParam("include_variations", Boolean.toString(include));
            return this;
        }

        /**
         * @param page The page number to return, default 1.
         * @return This object, for chaining
         */
        public Search page(int page) {
            addParam("page", Integer.toString(page));
            return this;
        }

        @Override
        public List get() {
            return new List(getData());
        }
    }


    /**
     * The <code>unique</code> parameter specifies if Scryfall should remove “duplicate” results in your query.
     * The options are:
     * <li><code>CARDS</code> (default) - Removes duplicate gameplay objects (cards that share a name and have the same
     * functionality). For example, if your search matches more than one print of Pacifism, only one copy of Pacifism
     * will be returned.
     * <li><code>ART</code> - Returns only one copy of each unique artwork for matching cards. For example, if your
     * search matches more than one print of Pacifism, one card with each different illustration for Pacifism will be
     * returned, but any cards that duplicate artwork already in the results will be omitted.
     * <li><code>PRINTS</code> - Returns all prints for all cards matched (disables rollup). For example, if your
     * search matches more than one print of Pacifism, all matching prints will be returned.
     */
    public enum UniqueMode {
        CARDS, ART, PRINTS;

        String toParamString() {
            return toString().toLowerCase();
        }
    }

    /**
     * The <code>order</code> parameter determines how Scryfall should sort the returned cards.
     * <table><thead><tr><th>Order</th><th>Description</th></tr></thead><tbody>
     * <tr><td><code>NAME</code> (default)</td><td>Sort cards by name, A → Z</td></tr>
     * <tr><td><code>SET</code></td><td>Sort cards by their set and collector number: AAA/#1 → ZZZ/#999</td></tr>
     * <tr><td><code>RELEASED</code></td><td>Sort cards by their release date: Newest → Oldest</td></tr>
     * <tr><td><code>RARITY</code></td><td>Sort cards by their rarity: Common → Mythic</td></tr>
     * <tr><td><code>COLOR</code></td><td>Sort cards by their color and color identity: WUBRG → multicolor → colorless</td></tr>
     * <tr><td><code>USD</code></td><td>Sort cards by their lowest known U.S. Dollar price: 0.01 → highest, <code>null</code> last</td></tr>
     * <tr><td><code>TIX</code></td><td>Sort cards by their lowest known TIX price: 0.01 → highest, <code>null</code> last</td></tr>
     * <tr><td><code>EUR</code></td><td>Sort cards by their lowest known Euro price: 0.01 → highest, <code>null</code> last</td></tr>
     * <tr><td><code>CMC</code></td><td>Sort cards by their converted mana cost: 0 → highest</td></tr>
     * <tr><td><code>POWER</code></td><td>Sort cards by their power: <code>null</code> → highest</td></tr>
     * <tr><td><code>TOUGHNESS</code></td><td>Sort cards by their toughness: <code>null</code> → highest</td></tr>
     * <tr><td><code>EDHREC</code></td><td>Sort cards by their EDHREC ranking: lowest → highest</td></tr>
     * <tr><td><code>ARTIST</code></td><td>Sort cards by their front-side artist name: A → Z</td></tr>
     * </tbody></table>
     */
    enum SortingOrder {
        NAME, SET, RELEASED, RARITY, COLOR, USD, TIX, EUR, CMC, POWER, TOUGHNESS, EDHREC, ARTIST;

        String toParamString() {
            return toString().toLowerCase();
        }
    }

    /**
     * Specify which direction the sorting should occur.
     * <table><thead><tr><th>Direction</th><th>Description</th></tr></thead><tbody>
     * <tr><td><code>AUTO</code></td><td>Scryfall will automatically choose the most intuitive direction to sort</td></tr>
     * <tr><td><code>ASCENDING</code></td><td>Sort ascending (the direction of the arrows in the <code>SortingOrder</code> table)</td></tr>
     * <tr><td><code>DESCENDING</code></td><td>Sort descending (flip the direction of the arrows in the <code>SortingOrder</code> table)</td></tr>
     * </tbody></table>
     *
     * @see SortingOrder
     */
    public enum ResultsDirection {
        AUTO, ASCENDING, DESCENDING;

        String toParamString() {
            switch (this) {
                case ASCENDING:
                    return "asc";
                case DESCENDING:
                    return "desc";
                default:
                    return "auto";
            }
        }
    }

}
