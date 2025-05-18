package com.coordinadora.technicaltest.domain.usecase;

import com.coordinadora.technicaltest.data.repository.UserRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class ValidateUserUseCaseImpl implements ValidateUserUseCase {

    private final UserRepository userRepository;

    @Inject
    public ValidateUserUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Single<Boolean> execute(String user, String pass) {
        return userRepository.validateCredentials(user, pass);
    }
}
