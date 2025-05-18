package com.coordinadora.technicaltest.di;

import android.content.Context;

import com.coordinadora.technicaltest.common.scheduler.AppSchedulerProvider;
import com.coordinadora.technicaltest.common.scheduler.SchedulerProvider;
import com.coordinadora.technicaltest.domain.usecase.ValidateUserUseCase;
import com.coordinadora.technicaltest.domain.usecase.ValidateUserUseCaseImpl;
import com.coordinadora.technicaltest.data.api.LoginService;
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

    @Singleton
    @Provides
    LoginService provideLoginService(Context context) {
        return new LoginService(context);
    }

    @Singleton
    @Provides
    UserRepository provideUserRepository(UserRepositoryImpl repo) {
        return repo;
    }

    @Singleton
    @Provides
    ValidateUserUseCase provideValidateUserUseCase(UserRepository repository) {
        return new ValidateUserUseCaseImpl(repository);
    }

    @Provides
    @Singleton
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }
}
