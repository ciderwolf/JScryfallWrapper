package com.scyrfall.api.query;

import com.scyrfall.api.query.QueryRoute.*;

import java.util.UUID;

public class CardQuery {

    public static Search search() {
        return new Search();
    }

    public static Named named() {
        return new Named();
    }

    public static Autocomplete autocomplete() {
        return new Autocomplete();
    }

    public static Random random() {
        return new Random();
    }

    public static Collection collection() {
        return new Collection();
    }

    public static CodeAndNumber setCodeAndCollectorNumber() {
        return new CodeAndNumber();
    }

    public static Id<Integer> multiverseId() {
        return new Id<>("multiverse");
    }

    public static Id<Integer> mtgoId() {
        return new Id<>("mtgo");
    }

    public static Id<Integer> arenaId() {
        return new Id<>("arena");
    }

    public static Id<Integer> tcgplayerId() {
        return new Id<>("tcgplayer");
    }

    public static Id<UUID> scryfallId() {
        return new Id<>("id");
    }
}
