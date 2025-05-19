package com.coordinadora.technicaltest.di.module;

import android.content.Context;

import com.coordinadora.technicaltest.data.api.service.BackupRemoteService;
import com.coordinadora.technicaltest.data.api.service.LoginService;
import com.coordinadora.technicaltest.data.api.service.ProcessQRService;
import com.coordinadora.technicaltest.data.db.dao.BackupDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    @Provides
    @Singleton
    LoginService provideLoginService(Context context) {
        return new LoginService(context);
    }

    @Provides
    @Singleton
    ProcessQRService provideProcessQRService(Context context, BackupDao dao) {
        return new ProcessQRService(context, dao);
    }

    @Provides
    @Singleton
    BackupRemoteService provideBackupRemoteService(Context context, BackupDao dao) {
        return new BackupRemoteService(context, dao);
    }
}