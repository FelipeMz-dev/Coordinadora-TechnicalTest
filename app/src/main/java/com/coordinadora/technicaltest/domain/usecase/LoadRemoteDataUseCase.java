package com.coordinadora.technicaltest.domain.usecase;

import io.reactivex.rxjava3.core.Completable;

public interface LoadRemoteDataUseCase {
    Completable execute();
}
