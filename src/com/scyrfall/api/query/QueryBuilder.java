package com.scyrfall.api.query;

import com.scyrfall.api.ScryfallObject;
import com.scyrfall.api.object.Card;

import java.util.HashMap;

public class QueryBuilder<T extends ScryfallObject> {

    public static CardQuery cardQuery() {
        return new CardQuery();
    }



}
