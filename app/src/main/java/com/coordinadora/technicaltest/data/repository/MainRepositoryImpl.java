package com.coordinadora.technicaltest.data.repository;

import com.coordinadora.technicaltest.data.api.service.BackupRemoteService;
import com.coordinadora.technicaltest.data.api.service.ProcessQRService;
import com.coordinadora.technicaltest.data.db.dao.BackupDao;
import com.coordinadora.technicaltest.data.db.entity.BackupEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class MainRepositoryImpl implements MainRepository {

    private final BackupDao backupDao;
    private final ProcessQRService qrService;
    private final BackupRemoteService backupRemoteService;

    @Inject
    public MainRepositoryImpl(BackupDao dao, ProcessQRService qrService, BackupRemoteService backupRemoteService) {
        this.backupDao = dao;
        this.qrService = qrService;
        this.backupRemoteService = backupRemoteService;
    }

    @Override
    public Completable processInput(String input) {
        return qrService.validateAndStore(input);
    }

    @Override
    public Completable uploadLastFiveBackups() {
        return backupDao.getLastFiveBackups()
                .flatMapCompletable(localData -> {
                    if (localData.isEmpty()) return Completable.complete();
                    return backupRemoteService.uploadBackup(localData);
                });
    }

    @Override
    public Flowable<List<BackupEntity>> observeLocalBackups() {
        return backupDao.observeAll();
    }

    @Override
    public Single<Integer> countBackups() {
        return backupDao.count();
    }

    @Override
    public Completable loadRemoteBackups() {
        return backupRemoteService.loadBackups();
    }

    @Override
    public Completable deleteLocalBackups() {
        return backupDao.deleteAll().andThen(backupRemoteService.deleteBackups());
    }

    @Override
    public Completable deleteRemoteBackups() {
        return backupRemoteService.deleteBackups();
    }
}