package com.coordinadora.technicaltest.common.util;

import com.coordinadora.technicaltest.data.db.entity.BackupEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class JsonParserUtil {
    public static JSONObject createFirestoreDocument(String serial, String date, List<BackupEntity> localData) throws JSONException {
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

        return new JSONObject().put("fields", documentFields);
    }

    public static JSONArray dataArrayToFirestoreValues(JSONArray datosArray) throws JSONException {
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

    public static JSONObject buildFirestoreQueryBySerial(String serial) throws JSONException {
        JSONObject fieldFilter = new JSONObject()
                .put("field", new JSONObject().put("fieldPath", "serial"))
                .put("op", "EQUAL")
                .put("value", new JSONObject().put("stringValue", serial));

        JSONObject where = new JSONObject().put("fieldFilter", fieldFilter);
        JSONObject from = new JSONObject().put("collectionId", "backup");
        JSONObject structuredQuery = new JSONObject()
                .put("from", new JSONArray().put(from))
                .put("where", where);

        return new JSONObject().put("structuredQuery", structuredQuery);
    }
}
