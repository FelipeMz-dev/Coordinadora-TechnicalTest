package com.coordinadora.technicaltest.ui.main;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.coordinadora.technicaltest.App;
import com.coordinadora.technicaltest.databinding.ActivityMainBinding;

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

    private void subscribeToViewModel() {
        viewModel.backups.observe(this, backups -> {
            if (backups == null || backups.isEmpty())
                binding.emptyState.setVisibility(View.VISIBLE);
            else {
                binding.emptyState.setVisibility(View.GONE);
                adapter.submitList(backups);
            }
        });
    }

    private void onClickListener() {
        binding.cameraButton.setOnClickListener(v -> {
        });
        binding.logoutButton.setOnClickListener(v -> {
        });
    }

    private void initRecyclerView() {
        adapter = new BackupAdapter(item -> {
            // Handle map click
        });
        binding.backupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.backupRecyclerView.setAdapter(adapter);
    }
}