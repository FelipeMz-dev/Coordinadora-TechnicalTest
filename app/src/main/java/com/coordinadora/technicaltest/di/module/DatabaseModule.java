package com.coordinadora.technicaltest.di.module;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.coordinadora.technicaltest.data.db.AppDatabase;
import com.coordinadora.technicaltest.data.db.dao.BackupDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    AppDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "app_database")
                .fallbackToDestructiveMigration(false)
                .build();
    }

    @Provides
    @Singleton
    BackupDao provideBackupDao(AppDatabase db) {
        return db.backupDao();
    }
}