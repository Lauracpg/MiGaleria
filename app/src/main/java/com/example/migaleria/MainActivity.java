package com.example.migaleria;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private PreviewView viewFinder;
    private ImageCapture imageCapture;
    private DBHelper dbHelper;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitudActual;
    private double longitudActual;
    private boolean camaraFrontal = false;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_LOCATION_PERMISSION = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(Color.BLACK);
        getWindow().setNavigationBarColor(Color.BLACK);
        getWindow().getDecorView().setSystemUiVisibility(0);

        dbHelper = new DBHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        viewFinder = findViewById(R.id.viewFinder);
        Button captureButton = findViewById(R.id.captureButton);
        captureButton.setOnClickListener(v -> {
            obtenerUbicacion();
        });

        Button switchCameraButton = findViewById(R.id.switchCameraButton);
        switchCameraButton.setOnClickListener(v -> {
            camaraFrontal = !camaraFrontal;
            iniciarCamara();
        });

        Button galeriaButton = findViewById(R.id.galeriaButton);
        galeriaButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GaleriaActivity.class);
            startActivity(intent);
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {

            iniciarCamara();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    private void iniciarCamara() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();
                imageCapture = new ImageCapture.Builder().build();
                CameraSelector cameraSelector;

                if (camaraFrontal) {
                    cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
                } else {
                    cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                }

                preview.setSurfaceProvider(viewFinder.getSurfaceProvider());
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarCamara();
            }
        }

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacion();
            }
        }
    }

    private void tomarFoto() {
        String nombreFoto = "MiGaleria_" + System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, nombreFoto);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,
                "Pictures/MiGaleria");

        ImageCapture.OutputFileOptions outputOptions =
                new ImageCapture.OutputFileOptions.Builder(
                        getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                ).build();

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Uri uri = outputFileResults.getSavedUri();
                        if (uri != null) {
                            String ruta = uri.toString();

                            SimpleDateFormat formatoFecha =
                                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                            String fecha = formatoFecha.format(new Date());
                            guardarFoto(fecha, ruta, latitudActual, longitudActual);
                        }

                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(MainActivity.this,
                                "Error al hacer la foto: " + exception.getMessage(),
                                Toast.LENGTH_LONG).show();
                        exception.printStackTrace();
                    }
                }
        );
    }

    private void guardarFoto(String fecha, String ruta, double latitud, double longitud) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fecha", fecha);
        values.put("ruta", ruta);
        values.put("latitud", latitud);
        values.put("longitud", longitud);
        db.insert("fotos", null, values);
        db.close();
    }

    private boolean tienePermisoUbicacion() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void pedirPermisoUbicacion() {
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, REQUEST_LOCATION_PERMISSION);
    }

    private void obtenerUbicacion() {
        if (!tienePermisoUbicacion()) {
            pedirPermisoUbicacion();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,
                null).addOnSuccessListener(location -> {

            if (location != null) {
                latitudActual = location.getLatitude();
                longitudActual = location.getLongitude();
                tomarFoto();

            } else {
                Toast.makeText(this, "No se ha podido obtener la ubicación",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}