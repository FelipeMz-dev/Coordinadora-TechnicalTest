package com.coordinadora.technicaltest.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.coordinadora.technicaltest.domain.usecase.ValidateUserUseCase;

import javax.inject.Inject;

public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private final ValidateUserUseCase validateUserUseCase;

    @Inject
    public LoginViewModelFactory(ValidateUserUseCase validateUserUseCase) {
        this.validateUserUseCase = validateUserUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(validateUserUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}