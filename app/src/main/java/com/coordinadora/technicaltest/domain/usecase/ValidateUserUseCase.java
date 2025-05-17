package com.coordinadora.technicaltest.domain.usecase;

import com.coordinadora.technicaltest.repository.UserRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class ValidateUserUseCase {

    private final UserRepository repository;

    @Inject
    public ValidateUserUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public Single<Boolean> execute(String username, String password) {
        return repository.validateCredentials(username, password);
    }
}