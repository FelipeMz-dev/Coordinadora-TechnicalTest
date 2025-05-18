package com.coordinadora.technicaltest.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.coordinadora.technicaltest.model.BackupEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainViewModel extends ViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<BackupEntity>> _backups = new MutableLiveData<>();
    public LiveData<List<BackupEntity>> backups = _backups;

    @Inject
    public MainViewModel() {
        // Constructor
        loadBackups();
    }

    private void loadBackups() {
        // fake loading backups
        List<BackupEntity> backupList = List.of(
                new BackupEntity(1, "720024556544001", "Aquí va la observación 1"),
                new BackupEntity(2, "720024556544002", "Aquí va la observación 2")
        );
        _backups.postValue(backupList);
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}