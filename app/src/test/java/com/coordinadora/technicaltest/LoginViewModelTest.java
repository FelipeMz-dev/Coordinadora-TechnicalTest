package com.coordinadora.technicaltest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.coordinadora.technicaltest.domain.usecase.ValidateUserUseCase;
import com.coordinadora.technicaltest.ui.login.LoginViewModel;
import com.coordinadora.technicaltest.util.ResponseState;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private ValidateUserUseCase mockUseCase;
    private LoginViewModel viewModel;

    @Before
    public void setUp() {
        mockUseCase = mock(ValidateUserUseCase.class);
        viewModel = new LoginViewModel(mockUseCase, Schedulers.trampoline());
    }

    @Test
    public void login_successfulCredentials_shouldEmitSuccessTrue() {
        testLoginResult("testUser", "testPassword", true, true);
    }

    @Test
    public void login_failedCredentials_shouldEmitSuccessFalse() {
        testLoginResult("testUser", "testPassword", false, false);
    }

    private void testLoginResult(String username, String password, boolean useCaseResult, boolean expectedResult) {
        // Arrange
        when(mockUseCase.execute(username, password)).thenReturn(Single.just(useCaseResult));
        Observer<ResponseState<Boolean>> observer = mock(Observer.class);
        viewModel.getLoginResult().observeForever(observer);

        // Act
        viewModel.validateUser(username, password);

        // Assert
        ArgumentCaptor<ResponseState<Boolean>> captor = ArgumentCaptor.forClass(ResponseState.class);
        verify(observer, atLeastOnce()).onChanged(captor.capture());

        List<ResponseState<Boolean>> values = captor.getAllValues();
        assertEquals(ResponseState.Status.LOADING, values.get(0).status);
        assertEquals(ResponseState.Status.SUCCESS, values.get(1).status);
        assertEquals(expectedResult, values.get(1).data);
    }

    @Test
    public void login_apiError_shouldEmitError() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        String errorMessage = "API Error";
        when(mockUseCase.execute(username, password)).thenReturn(Single.error(new Throwable(errorMessage)));
        Observer<ResponseState<Boolean>> observer = mock(Observer.class);
        viewModel.getLoginResult().observeForever(observer);

        // Act
        viewModel.validateUser(username, password);

        // Assert
        ArgumentCaptor<ResponseState<Boolean>> captor = ArgumentCaptor.forClass(ResponseState.class);
        verify(observer, atLeastOnce()).onChanged(captor.capture());

        List<ResponseState<Boolean>> values = captor.getAllValues();
        assertEquals(ResponseState.Status.LOADING, values.get(0).status);
        assertEquals(ResponseState.Status.ERROR, values.get(1).status);
        assertEquals(errorMessage, values.get(1).message);
    }
}
