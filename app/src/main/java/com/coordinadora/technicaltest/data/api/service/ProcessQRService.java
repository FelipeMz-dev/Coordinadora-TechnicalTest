package com.coordinadora.technicaltest.data.api.service;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.coordinadora.technicaltest.common.util.ApiErrorHandler;
import com.coordinadora.technicaltest.common.util.DataParserUtils;
import com.coordinadora.technicaltest.data.api.ApiClient;
import com.coordinadora.technicaltest.data.api.ApiConstants;
import com.coordinadora.technicaltest.data.db.dao.BackupDao;
import com.coordinadora.technicaltest.data.db.entity.BackupEntity;

import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProcessQRService {

    private final Context context;
    private final BackupDao dao;

    @Inject
    public ProcessQRService(Context context, BackupDao dao) {
        this.context = context;
        this.dao = dao;
    }

    public Completable validateAndStore(String rawInput) {
        return Completable.create(emitter -> {
            CompositeDisposable disposables = new CompositeDisposable();
            emitter.setCancellable(disposables::clear);

            String base64 = Base64.encodeToString(rawInput.getBytes(), Base64.NO_WRAP);

            JSONObject body = new JSONObject();
            body.put("data", base64);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    ApiConstants.VALIDATE_QR_ENDPOINT, body,
                    response -> {

                        try {
                            String status = response.getString(ApiConstants.KEY_CORRECT);
                            String data = response.getString(ApiConstants.KEY_DATA);

                            if (ApiConstants.KEY_CORRECT_STRUCTURE.equals(status)) {
                                try {
                                    BackupEntity entity = DataParserUtils.parseBackupEntityFromRaw(data);
                                    disposables.add(
                                            dao.insert(entity)
                                                    .subscribeOn(Schedulers.io())
                                                    .subscribe(emitter::onComplete, emitter::tryOnError)
                                    );
                                } catch (Exception ex) {
                                    emitter.tryOnError(ex);
                                }
                            } else {
                                emitter.tryOnError(new Exception(ApiConstants.KEY_INCORRECT_STRUCTURE));
                            }
                        } catch (Exception e) {
                            emitter.tryOnError(e);
                        }
                    },
                    error -> {
                        String message = ApiErrorHandler.parseVolleyError(error, context);
                        emitter.tryOnError(new Exception(message));
                    });

            ApiClient.getInstance(context).add(request);
        });
    }
}