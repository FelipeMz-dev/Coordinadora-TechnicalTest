package com.coordinadora.technicaltest.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.coordinadora.technicaltest.common.scheduler.SchedulerProvider;
import com.coordinadora.technicaltest.domain.usecase.ValidateUserUseCase;
import com.coordinadora.technicaltest.common.util.ResponseState;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class LoginViewModel extends ViewModel {

    private final ValidateUserUseCase validateUserUseCase;
    private final SchedulerProvider schedulerProvider;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ResponseState<Boolean>> loginResult = new MutableLiveData<>();

    @Inject
    public LoginViewModel(ValidateUserUseCase validateUserUseCase, SchedulerProvider schedulerProvider) {
        this.validateUserUseCase = validateUserUseCase;
        this.schedulerProvider = schedulerProvider;
    }

    public void validateUser(String username, String password) {
        loginResult.setValue(ResponseState.loading());

        disposables.add(
                validateUserUseCase.execute(username, password)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.main())
                        .subscribe(
                                isValid -> loginResult.setValue(ResponseState.success(isValid)),
                                throwable -> loginResult.setValue(ResponseState.error(throwable.getMessage()))
                        )
        );
    }

    public LiveData<ResponseState<Boolean>> getLoginResult() {
        return loginResult;
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}
