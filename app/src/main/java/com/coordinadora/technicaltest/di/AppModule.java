package com.coordinadora.technicaltest.di;

import android.content.Context;

import com.coordinadora.technicaltest.domain.usecase.ValidateUserUseCase;
import com.coordinadora.technicaltest.repository.UserRepository;
import com.coordinadora.technicaltest.repository.UserRepositoryImpl;

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
    UserRepository provideUserRepository(UserRepositoryImpl repo) {
        return repo;
    }

    @Singleton
    @Provides
    ValidateUserUseCase provideValidateUserUseCase(UserRepository repository) {
        return new ValidateUserUseCase(repository);
    }
}
