package com.coordinadora.technicaltest.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.coordinadora.technicaltest.App;
import com.coordinadora.technicaltest.R;
import com.coordinadora.technicaltest.common.util.LiveDataUtils;
import com.coordinadora.technicaltest.common.util.ResponseState;
import com.coordinadora.technicaltest.databinding.ActivityMainBinding;
import com.coordinadora.technicaltest.model.Backup;
import com.coordinadora.technicaltest.ui.login.LoginActivity;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private MainViewModel viewModel;
    private ActivityMainBinding binding;
    private BackupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((App) getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);

        initRecyclerView();
        subscribeToViewModel();
        onClickListener();
    }

    private void initRecyclerView() {
        adapter = new BackupAdapter(item -> {
            // Handle map click
        });
        binding.backupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.backupRecyclerView.setAdapter(adapter);
    }

    private void subscribeToViewModel() {
        viewModel.backups.observe(this, getBackupListObserver());
        viewModel.state.observe(this, getInputProcessedSuccessfully());
    }

    private void onClickListener() {
        binding.cameraButton.setOnClickListener(v -> {
        });
        binding.logoutButton.setOnClickListener(v -> viewModel.logout());
        binding.manualInput.setOnEditorActionListener((v, actionId, event) -> onManualInputSend(actionId));
    }

    @NonNull
    private Observer<List<Backup>> getBackupListObserver() {
        return data -> {
            if (data.isEmpty()) binding.emptyState.setVisibility(View.VISIBLE);
            else {
                binding.emptyState.setVisibility(View.GONE);
                adapter.submitList(data);
                binding.backupRecyclerView.scrollToPosition(0);
            }
        };
    }

    @NonNull
    private Observer<ResponseState<MainStateType>> getInputProcessedSuccessfully() {
        return response ->
                LiveDataUtils.handleResponse(
                        response, binding.loadingOverlay,
                        type -> {
                            switch (type) {
                                case PROCESS_INPUT:
                                    binding.manualInput.setText("");
                                    break;
                                case LOGOUT:
                                    goToLogin();
                                    break;
                                default: break;
                            }
                        },
                        this::showToastError
                );
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean onManualInputSend(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String input = binding.manualInput.getText().toString().trim();
            if (input.isEmpty()) binding.manualInput.setError(getString(R.string.copy_required_field));
            else viewModel.processInput(input);
            return true;
        }
        return false;
    }

    private void showToastError(String message) {
        Toast.makeText(getApplicationContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
    }
}