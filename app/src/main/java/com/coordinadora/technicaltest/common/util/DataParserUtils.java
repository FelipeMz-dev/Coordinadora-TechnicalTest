package com.coordinadora.technicaltest.common.util;

import com.coordinadora.technicaltest.data.db.entity.BackupEntity;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataParserUtils {

    public static BackupEntity parseBackupEntityFromRaw(String raw) throws IllegalArgumentException {

        Pattern pattern = Pattern.compile("(etiqueta1d|latitud|longitud|observacion):(-?[\\d.]+|[^-]+)");
        Matcher matcher = pattern.matcher(raw);

        BackupEntity entity = new BackupEntity();

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);

            switch (Objects.requireNonNull(key)) {
                case "etiqueta1d":
                    entity.etiqueta1d = value;
                    break;
                case "latitud":
                    assert value != null;
                    entity.latitud = Double.parseDouble(value);
                    break;
                case "longitud":
                    assert value != null;
                    entity.longitud = Double.parseDouble(value);
                    break;
                case "observacion":
                    entity.observacion = value;
                    break;
            }
        }

        if (entity.etiqueta1d == null || entity.observacion == null)
            throw new IllegalArgumentException("Estructura incompleta");

        return entity;
    }
}