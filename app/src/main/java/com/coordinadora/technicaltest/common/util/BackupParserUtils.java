package com.coordinadora.technicaltest.common.util;

import com.coordinadora.technicaltest.data.db.entity.BackupEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BackupParserUtils {

    public static List<BackupEntity> parseBackupEntitiesFromResponse(JSONArray firestoreResponse) throws JSONException {
        List<BackupEntity> result = new ArrayList<>();

        for (int i = 0; i < firestoreResponse.length(); i++) {
            JSONObject entry = firestoreResponse.getJSONObject(i);
            if (!entry.has("document")) continue;

            JSONObject fields = entry.getJSONObject("document").getJSONObject("fields");

            if (!fields.has("data")) continue;

            JSONObject dataArray = fields.getJSONObject("data").optJSONObject("arrayValue");
            if (dataArray == null) continue;

            JSONArray values = dataArray.optJSONArray("values");
            if (values == null) continue;

            for (int j = 0; j < values.length(); j++) {
                JSONObject mapValue = values.getJSONObject(j).getJSONObject("mapValue");
                JSONObject backupFields = mapValue.getJSONObject("fields");

                BackupEntity entity = new BackupEntity();
                entity.etiqueta1d = backupFields.getJSONObject("etiqueta1d").getString("stringValue");
                entity.latitud = backupFields.getJSONObject("latitud").getDouble("doubleValue");
                entity.longitud = backupFields.getJSONObject("longitud").getDouble("doubleValue");
                entity.observacion = backupFields.getJSONObject("observacion").getString("stringValue");

                result.add(entity);
            }
        }

        return result;
    }
}
