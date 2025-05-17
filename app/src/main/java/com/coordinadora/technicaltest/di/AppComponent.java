package com.coordinadora.technicaltest.di;

import com.coordinadora.technicaltest.ui.login.LoginActivity;
import com.coordinadora.technicaltest.ui.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(
        modules = {
                AppModule.class,
                ViewModelModule.class,
        }
)
@Singleton
public interface AppComponent {
    void inject(MainActivity activity);

    void inject(LoginActivity activity);
}