package com.coordinadora.technicaltest.data.repository;

import com.coordinadora.technicaltest.data.api.LoginService;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class UserRepositoryImpl implements UserRepository {

    private final LoginService loginService;

    @Inject
    public UserRepositoryImpl(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public Single<Boolean> validateCredentials(String username, String password) {
        return  loginService.validateCredentials(username, password);
    }
}