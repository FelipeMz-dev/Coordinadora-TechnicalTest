package com.coordinadora.technicaltest.ui.login;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.coordinadora.technicaltest.App;
import com.coordinadora.technicaltest.R;
import com.coordinadora.technicaltest.databinding.ActivityLoginBinding;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity {

    @Inject
    LoginViewModelFactory viewModelFactory;
    private LoginViewModel viewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ((App) getApplication()).getAppComponent().inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);

        onClickListener();
        subscribeToViewModel();
    }

    private void onClickListener() {
        binding.buttonLogin.setOnClickListener(v -> {
            String username = binding.editTextUsername.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            viewModel.validateUser(username, password);
        });
    }

    private void subscribeToViewModel() {
        viewModel.getLoginResult().observe(this, result -> {
            if (result) {
                Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}