package com.coordinadora.technicaltest.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.coordinadora.technicaltest.data.db.dao.BackupDao;
import com.coordinadora.technicaltest.data.db.entity.BackupEntity;

@Database(entities = {BackupEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BackupDao backupDao();
}