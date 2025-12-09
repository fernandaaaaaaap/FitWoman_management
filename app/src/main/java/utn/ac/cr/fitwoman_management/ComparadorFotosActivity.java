package utn.ac.cr.fitwoman_management;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import utn.ac.cr.fitwoman_management.Entities.FotoProgreso;
import utn.ac.cr.fitwoman_management.data.MemoryDataManager;

public class ComparadorFotosActivity extends AppCompatActivity {

    private Button btnSelectPhotos, btnChangePhotos;
    private ImageView ivPhoto1, ivPhoto2;
    private TextView tvPhoto1Info, tvPhoto2Info, tvWeightDifference, tvTimeDifference, tvInstructions;
    private LinearLayout comparisonContainer;

    private MemoryDataManager dataManager;
    private List<FotoProgreso> allPhotos;
    private FotoProgreso selectedPhoto1, selectedPhoto2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparador_fotos);

        initializeViews();
        dataManager = MemoryDataManager.getInstance();
        setupListeners();
        loadPhotos();
    }

    private void initializeViews() {
        btnSelectPhotos = findViewById(R.id.btnSelectPhotos);
        btnChangePhotos = findViewById(R.id.btnChangePhotos);
        ivPhoto1 = findViewById(R.id.ivPhoto1);
        ivPhoto2 = findViewById(R.id.ivPhoto2);
        tvPhoto1Info = findViewById(R.id.tvPhoto1Info);
        tvPhoto2Info = findViewById(R.id.tvPhoto2Info);
        tvWeightDifference = findViewById(R.id.tvWeightDifference);
        tvTimeDifference = findViewById(R.id.tvTimeDifference);
        tvInstructions = findViewById(R.id.tvInstructions);
        comparisonContainer = findViewById(R.id.comparisonContainer);
    }

    private void setupListeners() {
        btnSelectPhotos.setOnClickListener(v -> showPhotoSelectionDialog());
        btnChangePhotos.setOnClickListener(v -> showPhotoSelectionDialog());
    }

    private void loadPhotos() {
        allPhotos = dataManager.getAllFotosProgreso();

        if (allPhotos.size() < 2) {
            tvInstructions.setText("You need at least 2 progress photos to compare. Please add more photos first.");
            btnSelectPhotos.setEnabled(false);
        } else {
            tvInstructions.setText("You have " + allPhotos.size() + " photos. Select two to compare.");
            btnSelectPhotos.setEnabled(true);
        }
    }

    private void showPhotoSelectionDialog() {
        if (allPhotos.size() < 2) {
            Toast.makeText(this, "Not enough photos to compare", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] photoOptions = new String[allPhotos.size()];
        for (int i = 0; i < allPhotos.size(); i++) {
            FotoProgreso foto = allPhotos.get(i);
            photoOptions[i] = foto.getMes() + " - " + foto.getPeso() + " kg";
        }

        boolean[] selectedItems = new boolean[allPhotos.size()];
        List<Integer> selectedIndices = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select 2 Photos to Compare");

        builder.setMultiChoiceItems(photoOptions, selectedItems, (dialog, which, isChecked) -> {
            if (isChecked) {
                if (selectedIndices.size() < 2) {
                    selectedIndices.add(which);
                } else {
                    selectedItems[which] = false;
                    ((AlertDialog) dialog).getListView().setItemChecked(which, false);
                    Toast.makeText(this, "You can only select 2 photos", Toast.LENGTH_SHORT).show();
                }
            } else {
                selectedIndices.remove(Integer.valueOf(which));
            }
        });

        builder.setPositiveButton("Compare", (dialog, which) -> {
            if (selectedIndices.size() == 2) {
                selectedPhoto1 = allPhotos.get(selectedIndices.get(0));
                selectedPhoto2 = allPhotos.get(selectedIndices.get(1));
                displayComparison();
            } else {
                Toast.makeText(this, "Please select exactly 2 photos", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void displayComparison() {
        comparisonContainer.setVisibility(View.VISIBLE);
        tvInstructions.setVisibility(View.GONE);
        btnSelectPhotos.setVisibility(View.GONE);

        loadImage(selectedPhoto1, ivPhoto1);
        loadImage(selectedPhoto2, ivPhoto2);

        tvPhoto1Info.setText(selectedPhoto1.getMes() + "\n" + selectedPhoto1.getPeso() + " kg");
        tvPhoto2Info.setText(selectedPhoto2.getMes() + "\n" + selectedPhoto2.getPeso() + " kg");

        calculateDifferences();
    }

    private void loadImage(FotoProgreso foto, ImageView imageView) {
        if (foto.getRutaImagen() != null && !foto.getRutaImagen().isEmpty()) {
            try {
                byte[] decodedBytes = Base64.decode(foto.getRutaImagen(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                imageView.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    private void calculateDifferences() {
        double weightDiff = selectedPhoto2.getPeso() - selectedPhoto1.getPeso();
        String weightChange;

        if (weightDiff > 0) {
            weightChange = "Weight Change: +" + String.format("%.1f", weightDiff) + " kg (gained)";
            tvWeightDifference.setTextColor(0xFFE91E63);
        } else if (weightDiff < 0) {
            weightChange = "Weight Change: " + String.format("%.1f", weightDiff) + " kg (lost)";
            tvWeightDifference.setTextColor(0xFF4CAF50);
        } else {
            weightChange = "Weight Change: No change";
            tvWeightDifference.setTextColor(0xFF666666);
        }

        tvWeightDifference.setText(weightChange);

        long timeDiff = selectedPhoto2.getFechaTomada().getTime() - selectedPhoto1.getFechaTomada().getTime();
        long daysDiff = timeDiff / (1000 * 60 * 60 * 24);

        String timePeriod = "Time Period: " + daysDiff + " days";
        tvTimeDifference.setText(timePeriod);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPhotos();
    }
}