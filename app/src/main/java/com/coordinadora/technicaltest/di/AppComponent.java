package com.coordinadora.technicaltest.di;

import com.coordinadora.technicaltest.di.module.DatabaseModule;
import com.coordinadora.technicaltest.di.module.RepositoryModule;
import com.coordinadora.technicaltest.di.module.ServiceModule;
import com.coordinadora.technicaltest.di.module.UseCaseModule;
import com.coordinadora.technicaltest.di.module.ViewModelModule;
import com.coordinadora.technicaltest.ui.login.LoginActivity;
import com.coordinadora.technicaltest.ui.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(
        modules = {
                AppModule.class,
                ServiceModule.class,
                RepositoryModule.class,
                UseCaseModule.class,
                ViewModelModule.class,
                DatabaseModule.class
        }
)
@Singleton
public interface AppComponent {
    void inject(MainActivity activity);

    void inject(LoginActivity activity);
}