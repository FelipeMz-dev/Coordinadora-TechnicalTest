package com.coordinadora.technicaltest.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.coordinadora.technicaltest.domain.usecase.ValidateUserUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {

    private final ValidateUserUseCase validateUserUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<Boolean> loginResult = new MutableLiveData<>();

    @Inject
    public LoginViewModel(ValidateUserUseCase validateUserUseCase) {
        this.validateUserUseCase = validateUserUseCase;
    }

    public void validateUser(String username, String password) {
        disposables.add(
                validateUserUseCase.execute(username, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                loginResult::setValue,
                                throwable -> loginResult.setValue(false)
                        )
        );
    }

    public LiveData<Boolean> getLoginResult() {
        return loginResult;
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}
