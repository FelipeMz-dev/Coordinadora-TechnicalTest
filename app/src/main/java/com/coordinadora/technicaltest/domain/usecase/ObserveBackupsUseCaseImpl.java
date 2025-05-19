package com.coordinadora.technicaltest.domain.usecase;

import com.coordinadora.technicaltest.data.db.entity.BackupEntity;
import com.coordinadora.technicaltest.data.repository.MainRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class ObserveBackupsUseCaseImpl implements ObserveBackupsUseCase {

    private final MainRepository repository;

    @Inject
    public ObserveBackupsUseCaseImpl(MainRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flowable<List<BackupEntity>> execute() {
        return repository.observeLocalBackups();
    }
}
