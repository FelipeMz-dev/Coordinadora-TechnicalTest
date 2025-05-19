package com.coordinadora.technicaltest;

import android.app.Application;
import android.content.Context;

import com.coordinadora.technicaltest.di.AppComponent;
import com.coordinadora.technicaltest.di.AppModule;
import com.coordinadora.technicaltest.di.DaggerAppComponent;

public class App extends Application {
    private AppComponent appComponent;

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}