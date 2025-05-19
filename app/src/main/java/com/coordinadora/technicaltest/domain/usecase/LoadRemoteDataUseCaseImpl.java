package com.coordinadora.technicaltest.domain.usecase;

import com.coordinadora.technicaltest.data.repository.MainRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class LoadRemoteDataUseCaseImpl implements LoadRemoteDataUseCase{

    private final MainRepository repository;

    @Inject
    public LoadRemoteDataUseCaseImpl(MainRepository repository) {
        this.repository = repository;
    }

    @Override
    public Completable execute() {
        return repository.loadRemoteBackups();
    }
}
