package com.scyrfall.api.object;

import com.scyrfall.api.ScryfallObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;

/**
 * An Error object represents a failure to find information or understand the input you provided to the API. Error
 * objects are always transmitted with the appropriate 4XX or 5XX HTTP status code.
 */
public class ScryfallError extends ScryfallObject {

    private int status;
    private String code, details, type;
    String[] warnings;

    public ScryfallError(JSONObject data) {
        this.data = data;

        status = getInt("status");
        code = getString("code");
        details = getString("details");
        type = getString("type");

        if(data.has("warnings")) {
            JSONArray warnings = getJSONArray("warnings");
            this.warnings = new String[warnings.length()];
            for (int i = 0; i < warnings.length(); i++) {
                this.warnings[i] = warnings.getString(i);
            }
        }
    }

    /**
     * @return An integer HTTP status code for this error.
     */
    public int getStatus() {
        return status;
    }

    /**
     * @return A computer-friendly string representing the appropriate HTTP status code.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return A human-readable string explaining the error.
     */
    public String getDetails() {
        return details;
    }

    /**
     * @return A computer-friendly string that provides additional context for the main error. For example, an endpoint
     * many generate HTTP 404 errors for different kinds of input. This field will provide a label for the specific kind
     * of 404 failure, such as ambiguous.
     */
    public String getType() {
        return type;
    }

    /**
     * @return If your input also generated non-failure warnings, they will be provided as human-readable strings in
     * this array.
     */
    public String[] getWarnings() {
        return warnings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScryfallError that = (ScryfallError) o;
        return status == that.status &&
                Objects.equals(code, that.code) &&
                Objects.equals(details, that.details) &&
                Objects.equals(type, that.type) &&
                Arrays.equals(warnings, that.warnings);
    }

    @Override
    public String toString() {
        return "ScryfallError{" +
                "status=" + status +
                ", code='" + code + '\'' +
                ", details='" + details + '\'' +
                ", type='" + type + '\'' +
                ", warnings=" + Arrays.toString(warnings) +
                '}';
    }
}
