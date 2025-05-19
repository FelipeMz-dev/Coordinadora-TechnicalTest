package com.coordinadora.technicaltest.data.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity(tableName = "backup_local")
public class BackupEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "etiqueta1d")
    public String etiqueta1d;

    @ColumnInfo(name = "latitud")
    public double latitud;

    @ColumnInfo(name = "longitud")
    public double longitud;

    @ColumnInfo(name = "observacion")
    public String observacion;

    public void parseData(String raw) throws IllegalArgumentException {

    }

}