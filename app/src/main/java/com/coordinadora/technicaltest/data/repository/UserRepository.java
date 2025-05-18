package com.coordinadora.technicaltest.data.repository;

import io.reactivex.rxjava3.core.Single;

public interface UserRepository {
    Single<Boolean> validateCredentials(String username, String password);
}
