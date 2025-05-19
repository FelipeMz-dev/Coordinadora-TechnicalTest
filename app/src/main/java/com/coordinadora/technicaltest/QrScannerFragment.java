package com.coordinadora.technicaltest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;


public class QrScannerFragment extends Fragment {

    private DecoratedBarcodeView barcodeView;
    private boolean isScanning = false;

    public interface OnQrScannedListener {
        void onQrScanned(String result);
    }

    public interface OnQrCloseListener {
        void onQrClose();
    }

    private final OnQrScannedListener listener;
    private final OnQrCloseListener closeListener;
    private BeepManager beepManager;

    public QrScannerFragment(OnQrScannedListener listener, OnQrCloseListener closeListener) {
        this.listener = listener;
        this.closeListener = closeListener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qr_scanner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        barcodeView = view.findViewById(R.id.barcode_scanner);

        barcodeView.decodeContinuous(callback);
        barcodeView.resume();
        isScanning = true;

        beepManager = new BeepManager(requireActivity());
        beepManager.setBeepEnabled(true);

        view.findViewById(R.id.closeButton).setOnClickListener(v -> close());
    }

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (isScanning) {
                isScanning = false;
                listener.onQrScanned(result.getText());
                beepManager.playBeepSound();
                close();
            }
        }
    };

    private void close() {
        if (barcodeView != null) barcodeView.pause();
        closeListener.onQrClose();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .remove(this)
                .commit();
    }

    @Override
    public void onPause() {
        barcodeView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume();
    }
}