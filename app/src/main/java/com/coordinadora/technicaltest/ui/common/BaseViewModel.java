package com.coordinadora.technicaltest.ui.common;

import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class BaseViewModel extends ViewModel {

    protected final Scheduler mainScheduler;
    protected final Scheduler ioScheduler = Schedulers.io();

    public BaseViewModel() {
        this.mainScheduler = AndroidSchedulers.mainThread();
    }

    public BaseViewModel(Scheduler mainScheduler) {
        this.mainScheduler = mainScheduler;
    }
}