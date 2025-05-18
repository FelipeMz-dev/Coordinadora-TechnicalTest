package com.coordinadora.technicaltest.ui.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.coordinadora.technicaltest.databinding.ItemBackupBinding;
import com.coordinadora.technicaltest.model.BackupEntity;

public class BackupAdapter extends ListAdapter<BackupEntity, BackupAdapter.ViewHolder> {

    public interface OnMapClickListener {
        void onMapClick(BackupEntity item);
    }

    private final OnMapClickListener mapClickListener;

    public BackupAdapter(OnMapClickListener listener) {
        super(new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(BackupEntity oldItem, BackupEntity newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(BackupEntity oldItem, BackupEntity newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.mapClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemBackupBinding binding = ItemBackupBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemBackupBinding binding;

        ViewHolder(ItemBackupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(BackupEntity item) {
            binding.labelText.setText("etiqueta1d: " + item.etiqueta1d);
            binding.observationText.setText("Observación: " + item.observacion);
            binding.mapIcon.setOnClickListener(v -> mapClickListener.onMapClick(item));
        }
    }
}

