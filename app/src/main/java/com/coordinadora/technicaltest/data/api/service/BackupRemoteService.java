package com.coordinadora.technicaltest.data.api.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.coordinadora.technicaltest.common.util.ApiErrorHandler;
import com.coordinadora.technicaltest.common.util.BackupParserUtils;
import com.coordinadora.technicaltest.common.util.DateUtils;
import com.coordinadora.technicaltest.common.util.DeviceUtils;
import com.coordinadora.technicaltest.data.api.ApiClient;
import com.coordinadora.technicaltest.data.api.ApiConstants;
import com.coordinadora.technicaltest.data.api.request.JsonObjectToArrayRequest;
import com.coordinadora.technicaltest.data.db.dao.BackupDao;
import com.coordinadora.technicaltest.data.db.entity.BackupEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BackupRemoteService {

    private final Context context;
    private final BackupDao dao;

    @Inject
    public BackupRemoteService(Context context, BackupDao dao) {
        this.context = context;
        this.dao = dao;
    }

    public Completable uploadBackup(List<BackupEntity> localData) {
        return Completable.create(emitter -> {
            String serial = DeviceUtils.getSerial();
            String date = DateUtils.getTodayDateFormatted();

            JSONArray dataArray = new JSONArray();
            for (BackupEntity entity : localData) {
                JSONObject obj = new JSONObject();
                obj.put("etiqueta1d", entity.etiqueta1d);
                obj.put("latitud", entity.latitud);
                obj.put("longitud", entity.longitud);
                obj.put("observacion", entity.observacion);
                dataArray.put(obj);
            }

            JSONObject documentFields = new JSONObject();
            documentFields.put("serial", new JSONObject().put("stringValue", serial));
            documentFields.put("date", new JSONObject().put("stringValue", date));
            documentFields.put("data", new JSONObject().put("arrayValue", new JSONObject()
                    .put("values", dataArrayToFirestoreValues(dataArray))));

            JSONObject document = new JSONObject().put("fields", documentFields);

            String url = ApiConstants.FIREBASE_URL + "documents/backup";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, document,
                    response -> emitter.onComplete(),
                    error -> {
                        String msg = ApiErrorHandler.parseVolleyError(error, context);
                        emitter.tryOnError(new Exception(msg));
                    });

            ApiClient.getInstance(context).add(request);
        });
    }

    private JSONArray dataArrayToFirestoreValues(JSONArray datosArray) throws JSONException {
        JSONArray values = new JSONArray();
        for (int i = 0; i < datosArray.length(); i++) {
            JSONObject item = datosArray.getJSONObject(i);

            JSONObject firestoreMap = new JSONObject();
            firestoreMap.put("mapValue", new JSONObject()
                    .put("fields", new JSONObject()
                            .put("etiqueta1d", new JSONObject().put("stringValue", item.getString("etiqueta1d")))
                            .put("latitud", new JSONObject().put("doubleValue", item.getDouble("latitud")))
                            .put("longitud", new JSONObject().put("doubleValue", item.getDouble("longitud")))
                            .put("observacion", new JSONObject().put("stringValue", item.getString("observacion")))
                    ));

            values.put(firestoreMap);
        }
        return values;
    }

    public Completable loadBackups() {
        return Completable.create(emitter -> {
            CompositeDisposable disposables = new CompositeDisposable();
            emitter.setCancellable(disposables::clear);

            disposables.add(
                    getBackupsBySerial()
                            .subscribeOn(Schedulers.io())
                            .flatMapCompletable(
                                    jsonArray -> {
                                        List<BackupEntity> backupEntities = BackupParserUtils.parseBackupEntitiesFromResponse(jsonArray);
                                        return dao.insertAll(backupEntities).subscribeOn(Schedulers.io());
                                    }
                            ).subscribe(
                                    emitter::onComplete,
                                    emitter::tryOnError
                            )
            );
        });
    }

    public Completable deleteBackups() {
        return getBackupsBySerial()
                .subscribeOn(Schedulers.io())
                .flatMapCompletable(documents -> {

                    List<Completable> deleteRequests = new ArrayList<>();

                    for (int i = 0; i < documents.length(); i++) {
                        try {
                            if (!documents.getJSONObject(i).has("document")) continue;
                            JSONObject object = documents.getJSONObject(i);
                            JSONObject document = object.getJSONObject("document");
                            String fullPath = document.getString("name");
                            String deleteUrl = "https://firestore.googleapis.com/v1/" + fullPath;

                            Log.println(Log.DEBUG, "DELETE", deleteUrl);

                            Completable deleteRequest = Completable.create(emitter -> {
                                StringRequest request = new StringRequest(Request.Method.DELETE, deleteUrl,
                                        response -> emitter.onComplete(),
                                        error -> emitter.tryOnError(new Exception(
                                                ApiErrorHandler.parseVolleyError(error, context)))
                                );

                                ApiClient.getInstance(context).add(request);
                            });

                            deleteRequests.add(deleteRequest);

                        } catch (JSONException e) {
                            Log.e("BackupRemoteService", "Error parsing JSON", e);
                            return Completable.error(e);
                        }
                    }

                    return Completable.merge(deleteRequests);
                });
    }

    private Single<JSONArray> getBackupsBySerial() {
        return Single.create(emitter -> {
            CompositeDisposable disposables = new CompositeDisposable();
            emitter.setCancellable(disposables::clear);

            String serial = DeviceUtils.getSerial();
            String url = ApiConstants.FIREBASE_URL + ApiConstants.DOCUMENTS_QUERY;

            JSONObject query = new JSONObject();
            JSONObject fieldFilter = new JSONObject()
                    .put("field", new JSONObject().put("fieldPath", "serial"))
                    .put("op", "EQUAL")
                    .put("value", new JSONObject().put("stringValue", serial));

            JSONObject where = new JSONObject().put("fieldFilter", fieldFilter);
            JSONObject from = new JSONObject().put("collectionId", "backup");
            JSONObject structuredQuery = new JSONObject()
                    .put("from", new JSONArray().put(from))
                    .put("where", where);

            query.put("structuredQuery", structuredQuery);

            JsonObjectToArrayRequest request = new JsonObjectToArrayRequest(Request.Method.POST, url, query,
                    emitter::onSuccess,
                    error -> {
                        String msg = ApiErrorHandler.parseVolleyError(error, context);
                        emitter.tryOnError(new Exception(msg));
                    },
                    null
            );

            ApiClient.getInstance(context).add(request);
        });
    }
}