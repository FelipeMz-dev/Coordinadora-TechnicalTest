package com.coordinadora.technicaltest.repository;

import android.content.Context;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class UserRepositoryImpl implements UserRepository {

    private final Context context;
    private final String BASE_URL = "https://firestore.googleapis.com/";
    private final String PROJECT_ID = "coordinadoratechnicaltest";
    private final String DATABASE_ID = "/databases/(default)/documents/";
    private final String COLLECTION_USERS = "Users";

    @Inject
    public UserRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public Single<Boolean> validateCredentials(String username, String password) {
        return Single.create(emitter -> {
            String url = BASE_URL + PROJECT_ID + DATABASE_ID + COLLECTION_USERS;

            emitter.onSuccess(true);
        });
    }
}