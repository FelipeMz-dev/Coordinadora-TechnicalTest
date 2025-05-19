package com.coordinadora.technicaltest.data.api.service;

import android.content.Context;

import com.android.volley.Request;
import com.coordinadora.technicaltest.data.api.ApiClient;
import com.coordinadora.technicaltest.data.api.ApiConstants;
import com.coordinadora.technicaltest.data.api.query.LoginQueryBuilder;
import com.coordinadora.technicaltest.data.api.request.JsonObjectToArrayRequest;
import com.coordinadora.technicaltest.common.util.ApiErrorHandler;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class LoginService {

    private final Context context;

    @Inject
    public LoginService(Context context) {
        this.context = context;
    }

    public Single<Boolean> validateCredentials(String username, String password) {
        return Single.create(emitter -> {
            String url = ApiConstants.FIREBASE_URL + ApiConstants.DOCUMENTS_QUERY;

            JSONObject queryJson = LoginQueryBuilder.createUserPasswordQuery(username, password);

            JsonObjectToArrayRequest request = new JsonObjectToArrayRequest(Request.Method.POST, url, queryJson,
                    response -> {
                        boolean valido = false;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject item = response.getJSONObject(i);
                                if (item.has("document")) {
                                    valido = true;
                                    break;
                                }
                            } catch (JSONException e) {
                                emitter.tryOnError(e);
                                return;
                            }
                        }
                        emitter.onSuccess(valido);
                    },
                    error -> {
                        String message = ApiErrorHandler.parseVolleyError(error, context);
                        emitter.tryOnError(new Exception(message));
                    },
                    null
            );

            ApiClient.getInstance(context).add(request);
        });
    }

}