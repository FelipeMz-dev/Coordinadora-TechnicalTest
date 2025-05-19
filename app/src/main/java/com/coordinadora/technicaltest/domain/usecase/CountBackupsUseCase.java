package com.coordinadora.technicaltest.domain.usecase;

import io.reactivex.rxjava3.core.Single;

public interface CountBackupsUseCase {
    Single<Integer> execute();
}
