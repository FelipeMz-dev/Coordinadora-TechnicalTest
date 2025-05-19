package com.coordinadora.technicaltest.domain.usecase;

import com.coordinadora.technicaltest.data.db.entity.BackupEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface ObserveBackupsUseCase {
    Flowable<List<BackupEntity>> execute();
}
