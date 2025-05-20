package com.coordinadora.technicaltest;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.coordinadora.technicaltest.common.scheduler.SchedulerProvider;
import com.coordinadora.technicaltest.common.scheduler.TestSchedulerProvider;
import com.coordinadora.technicaltest.common.util.ResponseState;
import com.coordinadora.technicaltest.data.db.entity.BackupEntity;
import com.coordinadora.technicaltest.domain.usecase.CountBackupsUseCase;
import com.coordinadora.technicaltest.domain.usecase.LoadRemoteDataUseCase;
import com.coordinadora.technicaltest.domain.usecase.LogoutAndDeleteBackupUseCase;
import com.coordinadora.technicaltest.domain.usecase.ObserveBackupsUseCase;
import com.coordinadora.technicaltest.domain.usecase.ProcessInputUseCase;
import com.coordinadora.technicaltest.domain.usecase.UploadLastFiveBackupsUseCase;
import com.coordinadora.technicaltest.model.Backup;
import com.coordinadora.technicaltest.ui.main.MainStateType;
import com.coordinadora.technicaltest.ui.main.MainViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    ObserveBackupsUseCase observeBackupsUseCase;
    @Mock
    ProcessInputUseCase processInputUseCase;
    @Mock
    UploadLastFiveBackupsUseCase uploadLastFiveBackupsUseCase;
    @Mock
    CountBackupsUseCase countBackupsUseCase;
    @Mock
    LogoutAndDeleteBackupUseCase logoutAndDeleteBackupUseCase;
    @Mock
    LoadRemoteDataUseCase loadRemoteDataUseCase;

    private SchedulerProvider schedulerProvider;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        schedulerProvider = new TestSchedulerProvider(Schedulers.trampoline());

        // Setup default behavior for ViewModel constructor
        when(observeBackupsUseCase.execute()).thenReturn(Flowable.just(Collections.emptyList()));
    }

    private MainViewModel createViewModel() {
        return new MainViewModel(
                observeBackupsUseCase,
                processInputUseCase,
                uploadLastFiveBackupsUseCase,
                countBackupsUseCase,
                loadRemoteDataUseCase,
                schedulerProvider,
                logoutAndDeleteBackupUseCase
        );
    }

    @Test
    public void observeLocalBackups_shouldEmitBackupsAndSuccess() {
        BackupEntity backupEntity = new BackupEntity();
        backupEntity.id = 1;
        backupEntity.etiqueta1d = "tag";
        backupEntity.latitud = 1.1;
        backupEntity.longitud = 2.2;
        backupEntity.observacion = "obs";

        when(observeBackupsUseCase.execute()).thenReturn(Flowable.just(List.of(backupEntity)));

        MainViewModel viewModel = createViewModel();

        Observer<ResponseState<MainStateType>> stateObserver = mock(Observer.class);
        Observer<List<Backup>> backupsObserver = mock(Observer.class);

        viewModel.state.observeForever(stateObserver);
        viewModel.backups.observeForever(backupsObserver);

        verify(backupsObserver).onChanged(argThat(list -> !list.isEmpty()));
        verify(stateObserver, atLeastOnce()).onChanged(argThat(
                state -> state.status == ResponseState.Status.SUCCESS &&
                        state.data == MainStateType.LOAD_BACKUP));
    }

    @Test
    public void observeLocalBackups_shouldCallRemoteWhenEmpty() {
        when(observeBackupsUseCase.execute()).thenReturn(Flowable.just(Collections.emptyList()));
        when(loadRemoteDataUseCase.execute()).thenReturn(Completable.complete());

        MainViewModel viewModel = createViewModel();
        Observer<ResponseState<MainStateType>> stateObserver = mock(Observer.class);
        viewModel.state.observeForever(stateObserver);

        ArgumentCaptor<ResponseState<MainStateType>> captor = ArgumentCaptor.forClass(ResponseState.class);
        verify(stateObserver, atLeastOnce()).onChanged(captor.capture());

        assertTrue(captor.getAllValues().stream().anyMatch(
                state -> state.status == ResponseState.Status.SUCCESS &&
                        state.data == MainStateType.LOAD_REMOTE_BACKUP
        ));
    }

    @Test
    public void processInput_shouldEmitSuccessAndCallCountAndUpload() {
        when(processInputUseCase.execute("input")).thenReturn(Completable.complete());
        when(countBackupsUseCase.execute()).thenReturn(Single.just(5));
        when(uploadLastFiveBackupsUseCase.execute()).thenReturn(Completable.complete());

        MainViewModel viewModel = createViewModel();
        Observer<ResponseState<MainStateType>> stateObserver = mock(Observer.class);
        viewModel.state.observeForever(stateObserver);

        viewModel.processInput("input");

        verify(processInputUseCase).execute("input");
        verify(countBackupsUseCase).execute();
        verify(uploadLastFiveBackupsUseCase).execute();

        verify(stateObserver, atLeastOnce()).onChanged(argThat(
                state -> state.status == ResponseState.Status.SUCCESS &&
                        state.data == MainStateType.PROCESS_INPUT));
    }

    @Test
    public void countBackups_shouldCallUploadIfMultipleOfFive() {
        when(countBackupsUseCase.execute()).thenReturn(Single.just(10));
        when(uploadLastFiveBackupsUseCase.execute()).thenReturn(Completable.complete());

        MainViewModel viewModel = createViewModel();
        viewModel.countBackupsToUpload();

        verify(uploadLastFiveBackupsUseCase).execute();
    }

    @Test
    public void logout_shouldEmitLogoutSuccess() {
        when(logoutAndDeleteBackupUseCase.execute()).thenReturn(Completable.complete());

        MainViewModel viewModel = createViewModel();
        Observer<ResponseState<MainStateType>> stateObserver = mock(Observer.class);
        viewModel.state.observeForever(stateObserver);

        viewModel.logout();

        verify(logoutAndDeleteBackupUseCase).execute();
        verify(stateObserver, atLeastOnce()).onChanged(argThat(
                state -> state.status == ResponseState.Status.SUCCESS &&
                        state.data == MainStateType.LOGOUT));
    }

    @Test
    public void processInput_withError_shouldEmitError() {
        String errorMessage = "error";
        when(processInputUseCase.execute("input"))
                .thenReturn(Completable.error(new Exception(errorMessage)));

        MainViewModel viewModel = createViewModel();
        Observer<ResponseState<MainStateType>> stateObserver = mock(Observer.class);
        viewModel.state.observeForever(stateObserver);

        viewModel.processInput("input");

        verify(stateObserver, atLeastOnce()).onChanged(argThat(
                state -> state.status == ResponseState.Status.ERROR &&
                        errorMessage.equals(state.message)));
    }
}