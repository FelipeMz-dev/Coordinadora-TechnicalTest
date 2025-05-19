package com.coordinadora.technicaltest.di.module;

import com.coordinadora.technicaltest.domain.usecase.CountBackupsUseCase;
import com.coordinadora.technicaltest.domain.usecase.CountBackupsUseCaseImpl;
import com.coordinadora.technicaltest.domain.usecase.LoadRemoteDataUseCase;
import com.coordinadora.technicaltest.domain.usecase.LoadRemoteDataUseCaseImpl;
import com.coordinadora.technicaltest.domain.usecase.LogoutAndDeleteBackupUseCase;
import com.coordinadora.technicaltest.domain.usecase.LogoutAndDeleteBackupUseCaseImpl;
import com.coordinadora.technicaltest.domain.usecase.ObserveBackupsUseCase;
import com.coordinadora.technicaltest.domain.usecase.ObserveBackupsUseCaseImpl;
import com.coordinadora.technicaltest.domain.usecase.ProcessInputUseCase;
import com.coordinadora.technicaltest.domain.usecase.ProcessInputUseCaseImpl;
import com.coordinadora.technicaltest.domain.usecase.UploadLastFiveBackupsUseCase;
import com.coordinadora.technicaltest.domain.usecase.UploadLastFiveBackupsUseCaseImpl;
import com.coordinadora.technicaltest.domain.usecase.ValidateUserUseCase;
import com.coordinadora.technicaltest.domain.usecase.ValidateUserUseCaseImpl;

import dagger.Module;
import dagger.Binds;

@Module
public abstract class UseCaseModule {

    @Binds
    abstract ValidateUserUseCase bindValidateUserUseCase(ValidateUserUseCaseImpl impl);

    @Binds
    abstract ProcessInputUseCase bindProcessInputUseCase(ProcessInputUseCaseImpl impl);

    @Binds
    abstract ObserveBackupsUseCase bindObserveBackupsUseCase(ObserveBackupsUseCaseImpl impl);

    @Binds
    abstract LogoutAndDeleteBackupUseCase bindLogoutUseCase(LogoutAndDeleteBackupUseCaseImpl impl);

    @Binds
    abstract UploadLastFiveBackupsUseCase bindUploadBackupUseCase(UploadLastFiveBackupsUseCaseImpl impl);

    @Binds
    abstract CountBackupsUseCase bindCountBackupsUseCase(CountBackupsUseCaseImpl impl);

    @Binds
    abstract LoadRemoteDataUseCase bindGetBackupsUseCase(LoadRemoteDataUseCaseImpl impl);
}