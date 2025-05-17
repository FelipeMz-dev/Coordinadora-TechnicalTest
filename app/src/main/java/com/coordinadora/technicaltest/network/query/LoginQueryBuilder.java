package com.coordinadora.technicaltest.network.query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginQueryBuilder {
    public static JSONObject createUserPasswordQuery(String username, String password) throws JSONException {
        JSONObject userFilter = new JSONObject()
                .put("fieldFilter", new JSONObject()
                        .put("field", new JSONObject().put("fieldPath", "username"))
                        .put("op", "EQUAL")
                        .put("value", new JSONObject().put("stringValue", username))
                );

        JSONObject passwordFilter = new JSONObject()
                .put("fieldFilter", new JSONObject()
                        .put("field", new JSONObject().put("fieldPath", "password"))
                        .put("op", "EQUAL")
                        .put("value", new JSONObject().put("stringValue", password))
                );

        JSONArray filters = new JSONArray();
        filters.put(userFilter);
        filters.put(passwordFilter);

        JSONObject compositeFilter = new JSONObject()
                .put("op", "AND")
                .put("filters", filters);

        JSONObject where = new JSONObject()
                .put("compositeFilter", compositeFilter);

        JSONObject from = new JSONObject()
                .put("collectionId", "Users");

        JSONObject structuredQuery = new JSONObject()
                .put("from", new JSONArray().put(from))
                .put("where", where);

        return new JSONObject().put("structuredQuery", structuredQuery);
    }
}
