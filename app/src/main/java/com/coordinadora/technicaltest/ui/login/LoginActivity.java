package com.coordinadora.technicaltest.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.coordinadora.technicaltest.App;
import com.coordinadora.technicaltest.R;
import com.coordinadora.technicaltest.databinding.ActivityLoginBinding;
import com.coordinadora.technicaltest.common.util.ResponseState;
import com.coordinadora.technicaltest.ui.main.MainActivity;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private LoginViewModel viewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((App) getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);

        onClickListener();
        subscribeToViewModel();
    }

    private void onClickListener() {
        binding.buttonLogin.setOnClickListener(v -> onLogin());
    }

    private void subscribeToViewModel() {
        viewModel.getLoginResult().observe(this, this::responseHandler);
    }

    private void onLogin() {
        String username = binding.editTextUsername.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();
        String message = getString(R.string.copy_required_field);

        binding.textViewError.setVisibility(View.GONE);
        if (username.isEmpty()) binding.editTextUsername.setError(message);
        if (password.isEmpty()) binding.editTextPassword.setError(message);
        if (username.isEmpty() || password.isEmpty()) return;

        viewModel.validateUser(username, password);
    }

    private void responseHandler(ResponseState<Boolean> result) {
        switch (result.status) {
            case LOADING:
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.buttonLogin.setEnabled(false);
                break;
            case SUCCESS:
                binding.progressBar.setVisibility(View.GONE);
                binding.buttonLogin.setEnabled(true);
                if (result.data) navigateToMainActivity();
                else binding.textViewError.setVisibility(View.VISIBLE);
                break;
            case ERROR:
                binding.progressBar.setVisibility(View.GONE);
                binding.buttonLogin.setEnabled(true);
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}