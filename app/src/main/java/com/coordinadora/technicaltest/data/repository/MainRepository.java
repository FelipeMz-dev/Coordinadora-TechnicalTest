package com.coordinadora.technicaltest.data.repository;

import com.coordinadora.technicaltest.data.db.entity.BackupEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface MainRepository {
    Completable processInput(String input);

    Completable uploadLastFiveBackups();

    Flowable<List<BackupEntity>> observeLocalBackups();

    Single<Integer> countBackups();

    Completable loadRemoteBackups();

    Completable deleteLocalBackups();

    Completable deleteRemoteBackups();
}