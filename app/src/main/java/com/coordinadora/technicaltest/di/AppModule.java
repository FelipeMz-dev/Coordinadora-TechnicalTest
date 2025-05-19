package com.coordinadora.technicaltest.di;

import android.content.Context;

import com.coordinadora.technicaltest.common.scheduler.AppSchedulerProvider;
import com.coordinadora.technicaltest.common.scheduler.SchedulerProvider;
import com.coordinadora.technicaltest.domain.usecase.ValidateUserUseCase;
import com.coordinadora.technicaltest.domain.usecase.ValidateUserUseCaseImpl;
import com.coordinadora.technicaltest.data.api.service.LoginService;
import com.coordinadora.technicaltest.data.repository.UserRepository;
import com.coordinadora.technicaltest.data.repository.UserRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Context appContext;

    public AppModule(Context appContext) {
        this.appContext = appContext;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return appContext;
    }

    @Provides
    @Singleton
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }
}
