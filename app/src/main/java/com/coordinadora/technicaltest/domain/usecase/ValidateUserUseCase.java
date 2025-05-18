package com.coordinadora.technicaltest.domain.usecase;

import io.reactivex.rxjava3.core.Single;

public interface ValidateUserUseCase {
    Single<Boolean> execute(String user, String pass);
}