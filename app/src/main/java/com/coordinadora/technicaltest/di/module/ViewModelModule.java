package com.coordinadora.technicaltest.di.module;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.coordinadora.technicaltest.di.ViewModelKey;
import com.coordinadora.technicaltest.ui.common.GenericViewModelFactory;
import com.coordinadora.technicaltest.ui.login.LoginViewModel;
import com.coordinadora.technicaltest.ui.main.MainViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(GenericViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindLoginViewModel(LoginViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel vm);
}
