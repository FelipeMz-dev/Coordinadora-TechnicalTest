package com.coordinadora.technicaltest.domain.base;

import io.reactivex.rxjava3.core.Single;

public interface UseCase<P, R> {
    Single<R> execute(P params);
}