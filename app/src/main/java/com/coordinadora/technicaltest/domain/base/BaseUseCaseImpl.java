package com.coordinadora.technicaltest.domain.base;

import java.util.function.Function;

import io.reactivex.rxjava3.core.Single;

public class BaseUseCaseImpl<P, R> implements UseCase<P, R> {

    private final Function<P, Single<R>> executor;

    public BaseUseCaseImpl(Function<P, Single<R>> executor) {
        this.executor = executor;
    }

    @Override
    public Single<R> execute(P params) {
        return executor.apply(params);
    }
}