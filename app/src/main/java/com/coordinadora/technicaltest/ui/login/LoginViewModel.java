package com.coordinadora.technicaltest.ui.login;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.coordinadora.technicaltest.domain.usecase.ValidateUserUseCase;
import com.coordinadora.technicaltest.ui.common.BaseViewModel;
import com.coordinadora.technicaltest.util.ResponseState;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class LoginViewModel extends BaseViewModel {

    private final ValidateUserUseCase validateUserUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ResponseState<Boolean>> loginResult = new MutableLiveData<>();

    @Inject
    public LoginViewModel(ValidateUserUseCase validateUserUseCase) {
        this.validateUserUseCase = validateUserUseCase;
    }

    public LoginViewModel(ValidateUserUseCase validateUserUseCase, Scheduler mainScheduler) {
        super(mainScheduler);
        this.validateUserUseCase = validateUserUseCase;
    }

    public void validateUser(String username, String password) {
        loginResult.setValue(ResponseState.loading());

        disposables.add(
                validateUserUseCase.execute(username, password)
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
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
