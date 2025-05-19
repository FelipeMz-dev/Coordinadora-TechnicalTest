package com.coordinadora.technicaltest.data.api.request;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonObjectToArrayRequest extends Request<JSONArray> {

    private final JSONObject requestBody;
    private final Response.Listener<JSONArray> listener;
    private final String authToken;

    public JsonObjectToArrayRequest(int method, String url, JSONObject requestBody,
                                    Response.Listener<JSONArray> listener,
                                    Response.ErrorListener errorListener,
                                    @Nullable String authToken) {
        super(method, url, errorListener);
        this.requestBody = requestBody;
        this.listener = listener;
        this.authToken = authToken;
    }

    @Override
    public byte[] getBody() {
        return requestBody.toString().getBytes();
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONArray(json), HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new com.android.volley.ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(JSONArray response) {
        listener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        if (authToken != null) {
            headers.put("Authorization", "Bearer " + authToken);
        }
        headers.put("Content-Type", "application/json");
        return headers;
    }
}