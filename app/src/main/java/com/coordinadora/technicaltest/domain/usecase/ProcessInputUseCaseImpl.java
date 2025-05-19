package com.coordinadora.technicaltest.domain.usecase;

import com.coordinadora.technicaltest.data.repository.MainRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class ProcessInputUseCaseImpl implements ProcessInputUseCase {

    private final MainRepository repository;

    @Inject
    public ProcessInputUseCaseImpl(MainRepository repository) {
        this.repository = repository;
    }

    @Override
    public Completable execute(String input) {
        return repository.processInput(input);
    }
}