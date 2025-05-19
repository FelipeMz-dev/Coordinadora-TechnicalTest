package com.coordinadora.technicaltest.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.coordinadora.technicaltest.common.scheduler.SchedulerProvider;
import com.coordinadora.technicaltest.common.util.ResponseState;
import com.coordinadora.technicaltest.data.db.entity.BackupEntity;
import com.coordinadora.technicaltest.domain.usecase.CountBackupsUseCase;
import com.coordinadora.technicaltest.domain.usecase.LoadRemoteDataUseCase;
import com.coordinadora.technicaltest.domain.usecase.LogoutAndDeleteBackupUseCase;
import com.coordinadora.technicaltest.domain.usecase.ObserveBackupsUseCase;
import com.coordinadora.technicaltest.domain.usecase.ProcessInputUseCase;
import com.coordinadora.technicaltest.domain.usecase.UploadLastFiveBackupsUseCase;
import com.coordinadora.technicaltest.model.Backup;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainViewModel extends ViewModel {

    private final ObserveBackupsUseCase observeBackupsUseCase;
    private final ProcessInputUseCase processInputUseCase;
    private final UploadLastFiveBackupsUseCase uploadLastFiveBackupsUseCase;
    private final CountBackupsUseCase countBackupsUseCase;
    private final LogoutAndDeleteBackupUseCase logoutAndDeleteBackupUseCase;
    private final LoadRemoteDataUseCase loadRemoteDataUseCase;
    private final SchedulerProvider schedulerProvider;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<Backup>> _backups = new MutableLiveData<>();
    public LiveData<List<Backup>> backups = _backups;

    private final MutableLiveData<ResponseState<MainStateType>> _state = new MutableLiveData<>();
    public LiveData<ResponseState<MainStateType>> state = _state;

    private Boolean logoutState = false;

    @Inject
    public MainViewModel(
            ObserveBackupsUseCase observeBackupsUseCase,
            ProcessInputUseCase processInputUseCase,
            UploadLastFiveBackupsUseCase uploadLastFiveBackupsUseCase,
            CountBackupsUseCase countBackupsUseCase,
            LoadRemoteDataUseCase loadRemoteDataUseCase,
            SchedulerProvider schedulerProvider,
            LogoutAndDeleteBackupUseCase logoutAndDeleteBackupUseCase
    ) {
        this.observeBackupsUseCase = observeBackupsUseCase;
        this.processInputUseCase = processInputUseCase;
        this.uploadLastFiveBackupsUseCase = uploadLastFiveBackupsUseCase;
        this.countBackupsUseCase = countBackupsUseCase;
        this.loadRemoteDataUseCase = loadRemoteDataUseCase;
        this.schedulerProvider = schedulerProvider;
        this.logoutAndDeleteBackupUseCase = logoutAndDeleteBackupUseCase;
        observeLocalBackups();
    }

    private void observeLocalBackups() {
        _state.setValue(ResponseState.loading());
        disposables.add(
                observeBackupsUseCase.execute()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.main())
                        .map(MainViewModel::mapBackupList)
                        .subscribe(
                                backupList -> {
                                    if (backupList.isEmpty() && !logoutState)  loadRemoteBackups();
                                    else _backups.setValue(backupList);
                                    _state.setValue(ResponseState.success(MainStateType.LOAD_BACKUP));
                                },
                                this::setErrorToState
                        )
        );
    }

    public void loadRemoteBackups() {
        _state.setValue(ResponseState.loading());
        disposables.add(loadRemoteDataUseCase.execute()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe(
                        () -> _state.setValue(ResponseState.success(MainStateType.LOAD_REMOTE_BACKUP)),
                        this::setErrorToState
                ));
    }

    public void uploadLastFiveBackupsToRemote() {
        _state.setValue(ResponseState.loading());
        disposables.add(uploadLastFiveBackupsUseCase.execute()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe(
                        () -> _state.setValue(ResponseState.success(MainStateType.UPLOAD_REMOTE_BACKUP)),
                        this::setErrorToState
                ));
    }

    public void countBackupsToUpload() {
        _state.setValue(ResponseState.loading());
        disposables.add(countBackupsUseCase.execute()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe(
                        count -> {
                            if (count % 5 == 0 && !logoutState) uploadLastFiveBackupsToRemote();
                        },
                        this::setErrorToState
                ));
    }

    public void processInput(String input) {
        _state.setValue(ResponseState.loading());
        disposables.add(processInputUseCase.execute(input)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe(
                        () -> {
                            _state.setValue(ResponseState.success(MainStateType.PROCESS_INPUT));
                            if (!logoutState) countBackupsToUpload();
                        },
                        this::setErrorToState
                ));
    }

    public void logout() {
        logoutState = true;
        _state.setValue(ResponseState.loading());
        disposables.add(logoutAndDeleteBackupUseCase.execute()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe(
                        () -> _state.setValue(ResponseState.success(MainStateType.LOGOUT)),
                        error -> {
                            _state.setValue(ResponseState.error(error.getMessage()));
                            logoutState = false;
                        }
                ));
    }

    private void setErrorToState(Throwable throwable) {
        _state.setValue(ResponseState.error(throwable.getMessage()));
    }

    @NonNull
    private static List<Backup> mapBackupList(List<BackupEntity> backupList) {
        return backupList.stream().map(
                backupEntity -> new Backup(
                        backupEntity.id,
                        backupEntity.etiqueta1d,
                        backupEntity.latitud,
                        backupEntity.longitud,
                        backupEntity.observacion
                )
        ).collect(Collectors.toList());
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}