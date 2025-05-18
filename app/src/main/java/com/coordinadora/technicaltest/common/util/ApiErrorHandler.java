package com.coordinadora.technicaltest.common.util;

import android.content.Context;

import com.android.volley.VolleyError;
import com.coordinadora.technicaltest.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiErrorHandler {

    public static String parseVolleyError(VolleyError error, Context context) {
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String errorBody = new String(error.networkResponse.data);
                JSONObject errorJson = new JSONObject(errorBody);

                if (errorJson.has("error") && errorJson.getJSONObject("error").has("message")) {
                    return errorJson.getJSONObject("error").getString("message");
                } else {
                    return "Error HTTP: " + error.networkResponse.statusCode;
                }

            } catch (JSONException e) {
                return context.getString(R.string.copy_unknown_error_json);
            }
        }

        if (error instanceof com.android.volley.TimeoutError) {
            return context.getString(R.string.copy_timeout_error);
        }

        if (error instanceof com.android.volley.NoConnectionError) {
            return context.getString(R.string.copy_no_internet_connection);
        }

        return context.getString(R.string.copy_unexpected_network_error);
    }
}