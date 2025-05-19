package com.coordinadora.technicaltest.domain.usecase;

import com.coordinadora.technicaltest.data.repository.MainRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class LogoutAndDeleteBackupUseCaseImpl implements LogoutAndDeleteBackupUseCase {

    MainRepository repository;

    @Inject
    public LogoutAndDeleteBackupUseCaseImpl(MainRepository repository) {
        this.repository = repository;
    }

    @Override
    public Completable execute() {
        return repository.deleteLocalBackups().andThen(repository.deleteRemoteBackups());
    }
}
