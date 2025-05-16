package com.coordinadora.technicaltest;

import android.app.Application;

import com.coordinadora.technicaltest.di.AppComponent;
import com.coordinadora.technicaltest.di.AppModule;
import com.coordinadora.technicaltest.di.DaggerAppComponent;

public class application extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
