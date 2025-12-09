package utn.ac.cr.fitwoman_management;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import utn.ac.cr.fitwoman_management.Entities.FotoProgreso;
import utn.ac.cr.fitwoman_management.data.MemoryDataManager;

public class TomarFotoActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    private ImageView ivPreview;
    private Button btnTakePhoto, btnSelectGallery, btnSavePhoto, btnCancel;
    private EditText etMes, etPeso, etNotas;
    private Spinner spinnerCategoria;

    private Bitmap selectedBitmap;
    private MemoryDataManager dataManager;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_foto);

        initializeViews();
        setupSpinner();
        setupActivityLaunchers();
        setupListeners();

        dataManager = MemoryDataManager.getInstance();
    }

    private void initializeViews() {
        ivPreview = findViewById(R.id.ivPreview);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnSelectGallery = findViewById(R.id.btnSelectGallery);
        btnSavePhoto = findViewById(R.id.btnSavePhoto);
        btnCancel = findViewById(R.id.btnCancel);
        etMes = findViewById(R.id.etMes);
        etPeso = findViewById(R.id.etPeso);
        etNotas = findViewById(R.id.etNotas);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
    }

    private void setupSpinner() {
        String[] categories = {
                "Select Category",
                "Front View",
                "Side View",
                "Back View",
                "Weight Progress",
                "Measurements"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);
    }

    private void setupActivityLaunchers() {
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        selectedBitmap = (Bitmap) extras.get("data");
                        ivPreview.setImageBitmap(selectedBitmap);
                    }
                }
        );

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        try {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(),
                                    selectedImageUri
                            );
                            ivPreview.setImageBitmap(selectedBitmap);
                        } catch (IOException e) {
                            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private void setupListeners() {
        btnTakePhoto.setOnClickListener(v -> checkCameraPermissionAndOpen());
        btnSelectGallery.setOnClickListener(v -> checkStoragePermissionAndOpen());
        btnSavePhoto.setOnClickListener(v -> savePhoto());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    private void checkStoragePermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    STORAGE_PERMISSION_CODE);
        } else {
            openGallery();
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(cameraIntent);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    private void savePhoto() {
        String mes = etMes.getText().toString().trim();
        String pesoStr = etPeso.getText().toString().trim();
        String categoria = spinnerCategoria.getSelectedItem().toString();
        String notas = etNotas.getText().toString().trim();

        if (selectedBitmap == null) {
            Toast.makeText(this, "Please select or take a photo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mes.isEmpty()) {
            etMes.setError("Month is required");
            etMes.requestFocus();
            return;
        }

        if (pesoStr.isEmpty()) {
            etPeso.setError("Weight is required");
            etPeso.requestFocus();
            return;
        }

        if (categoria.equals("Select Category")) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }

        double peso;
        try {
            peso = Double.parseDouble(pesoStr);
        } catch (NumberFormatException e) {
            etPeso.setError("Please enter a valid weight");
            etPeso.requestFocus();
            return;
        }

        String imageBase64 = bitmapToBase64(selectedBitmap);
        FotoProgreso newFoto = new FotoProgreso(imageBase64, mes, notas, peso, categoria);

        boolean saved = dataManager.createFotoProgreso(newFoto);

        if (saved) {
            Toast.makeText(this, "Photo saved successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving photo", Toast.LENGTH_SHORT).show();
        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}