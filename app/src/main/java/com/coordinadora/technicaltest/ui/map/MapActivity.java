package com.coordinadora.technicaltest.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.coordinadora.technicaltest.R;
import com.coordinadora.technicaltest.databinding.ActivityMapBinding;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapBinding binding;

    private double destinationLat, destinationLng;
    private String observation;

    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient settingsClient;

    private static final int REQUEST_LOCATION_PERMISSION = 200;
    private static final int REQUEST_CHECK_SETTINGS = 101;

    public static final String LATITUDE_KEY = "latitud";
    public static final String LONGITUDE_KEY = "longitud";
    public static final String OBSERVATION_KEY = "observacion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        destinationLat = getIntent().getDoubleExtra(LATITUDE_KEY, 0.0);
        destinationLng = getIntent().getDoubleExtra(LONGITUDE_KEY, 0.0);
        observation = getIntent().getStringExtra(OBSERVATION_KEY);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        settingsClient = LocationServices.getSettingsClient(this);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        verifyGPSAndLocation();
    }

    private void verifyGPSAndLocation() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest settingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true)
                .build();

        settingsClient.checkLocationSettings(settingsRequest)
                .addOnSuccessListener(response -> verifyPermissions())
                .addOnFailureListener(e -> {
                    if (e instanceof ResolvableApiException) {
                        try {
                            ((ResolvableApiException) e).startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException ex) {
                            Toast.makeText(this, "Error al activar GPS", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Ubicación no disponible", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void verifyPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            showLocation();
        }
    }

    private void showLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Se requiere permiso de ubicación", Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        showInMap(location);
                    } else {
                        // Ubicación no disponible → solicita activamente una
                        LocationRequest locationRequest = LocationRequest.create()
                                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                                .setInterval(1000)
                                .setNumUpdates(1);

                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                    }
                });
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            Location location = locationResult.getLastLocation();
            if (location != null) {
                showInMap(location);
                // Detén después de obtener una sola ubicación
                fusedLocationClient.removeLocationUpdates(this);
            }
        }
    };

    private void showInMap(Location location) {
        LatLng actual = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng destino = new LatLng(destinationLat, destinationLng);

        mMap.addMarker(new MarkerOptions().position(actual).title("Ubicación actual"));
        mMap.addMarker(new MarkerOptions().position(destino).title("Destino").snippet(observation));

        mMap.addPolyline(new PolylineOptions()
                .add(actual, destino)
                .width(6f)
                .color(Color.BLUE));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(actual, 15f));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                verifyPermissions();
            } else {
                Toast.makeText(this, "La ubicación está desactivada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showLocation();
        } else {
            Toast.makeText(this, "Se necesita permiso de ubicación", Toast.LENGTH_SHORT).show();
        }
    }
}