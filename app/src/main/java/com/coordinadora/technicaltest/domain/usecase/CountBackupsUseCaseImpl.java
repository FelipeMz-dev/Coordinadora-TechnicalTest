package com.coordinadora.technicaltest.domain.usecase;

import com.coordinadora.technicaltest.data.repository.MainRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class CountBackupsUseCaseImpl implements CountBackupsUseCase {

    private final MainRepository repository;

    @Inject
    public CountBackupsUseCaseImpl(MainRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<Integer> execute() {
        return repository.countBackups();
    }
}
