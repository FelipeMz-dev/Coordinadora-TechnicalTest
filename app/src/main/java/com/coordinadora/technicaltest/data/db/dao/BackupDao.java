package com.coordinadora.technicaltest.data.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.coordinadora.technicaltest.data.db.entity.BackupEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface BackupDao {

    @Query("SELECT * FROM backup_local ORDER BY id DESC")
    Flowable<List<BackupEntity>> observeAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(BackupEntity backup);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<BackupEntity> backups);

    @Query("DELETE FROM backup_local")
    Completable deleteAll();

    @Query("SELECT COUNT(*) FROM backup_local")
    Single<Integer> count();

    @Query("SELECT * FROM backup_local ORDER BY id DESC LIMIT 5")
    Single<List<BackupEntity>> getLastFiveBackups();
}