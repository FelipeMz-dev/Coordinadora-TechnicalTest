package com.coordinadora.technicaltest.di.module;

import com.coordinadora.technicaltest.data.repository.MainRepository;
import com.coordinadora.technicaltest.data.repository.MainRepositoryImpl;
import com.coordinadora.technicaltest.data.repository.UserRepository;
import com.coordinadora.technicaltest.data.repository.UserRepositoryImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract UserRepository bindUserRepository(UserRepositoryImpl impl);

    @Binds
    @Singleton
    abstract MainRepository bindMainRepository(MainRepositoryImpl impl);
}
