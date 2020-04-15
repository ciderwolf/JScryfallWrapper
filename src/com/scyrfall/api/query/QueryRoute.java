package com.scyrfall.api.query;

import com.scyrfall.api.ScryfallObject;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

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
        for(String key : required) {
            if(!key.isEmpty() && !params.containsKey(key)) {
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

}
