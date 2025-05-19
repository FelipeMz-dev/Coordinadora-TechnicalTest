package com.coordinadora.technicaltest.common.scheduler;

import io.reactivex.rxjava3.core.Scheduler;

public interface SchedulerProvider {
    Scheduler io();
    Scheduler computation();
    Scheduler main();
}