package com.coordinadora.technicaltest.common.scheduler;

import io.reactivex.rxjava3.core.Scheduler;

public class TestSchedulerProvider implements SchedulerProvider {

    private final Scheduler scheduler;

    public TestSchedulerProvider(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Scheduler io() {
        return scheduler;
    }

    @Override
    public Scheduler computation() {
        return scheduler;
    }

    @Override
    public Scheduler main() {
        return scheduler;
    }
}