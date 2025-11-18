package utn.ac.cr.fitwoman_management;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utn.ac.cr.fitwoman_management.Entities.Entrenamiento;
import utn.ac.cr.fitwoman_management.data.MemoryDataManager;

public class Estadisticas_Activity extends AppCompatActivity {

    private MemoryDataManager dataManager;

    private TextView tvTotalWorkouts;
    private TextView tvTotalTime;
    private TextView tvTotalPhotos;
    private TextView tvFavoriteCategory;
    private Button btnBackToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadistica);

        initializeViews();
        dataManager = MemoryDataManager.getInstance();
        loadStatistics();
        setupListeners();
    }

    private void initializeViews() {
        tvTotalWorkouts = findViewById(R.id.tvTotalWorkouts);
        tvTotalTime = findViewById(R.id.tvTotalTime);
        tvTotalPhotos = findViewById(R.id.tvTotalPhotos);
        tvFavoriteCategory = findViewById(R.id.tvFavoriteCategory);
        btnBackToMenu = findViewById(R.id.btnBackToMenu);
    }

    private void loadStatistics() {
        // Get total workouts
        int totalWorkouts = dataManager.getTotalEntrenamientos();
        tvTotalWorkouts.setText(String.valueOf(totalWorkouts));

        // Calculate total training time
        List<Entrenamiento> workouts = dataManager.getAllEntrenamientos();
        int totalMinutes = 0;
        for (Entrenamiento workout : workouts) {
            totalMinutes += workout.getDuracionMinutos();
        }

        // Format time display
        if (totalMinutes >= 60) {
            int hours = totalMinutes / 60;
            int minutes = totalMinutes % 60;
            tvTotalTime.setText(hours + "h " + minutes + "m");
        } else {
            tvTotalTime.setText(totalMinutes + " min");
        }

        // Get total photos
        int totalPhotos = dataManager.getTotalFotosProgreso();
        tvTotalPhotos.setText(String.valueOf(totalPhotos));

        // Find favorite category
        String favoriteCategory = getFavoriteCategory(workouts);
        tvFavoriteCategory.setText(favoriteCategory);
    }

    private String getFavoriteCategory(List<Entrenamiento> workouts) {
        if (workouts.isEmpty()) {
            return "None yet";
        }

        // Count workouts by category
        Map<String, Integer> categoryCount = new HashMap<>();

        for (Entrenamiento workout : workouts) {
            String category = workout.getCategoria();
            if (category != null && !category.isEmpty()) {
                categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
            }
        }

        // Find category with most workouts
        String favorite = "None yet";
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : categoryCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                favorite = entry.getKey() + " (" + entry.getValue() + ")";
            }
        }

        return favorite;
    }

    private void setupListeners() {
        btnBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Return to previous screen (Menu)
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh statistics when returning to this screen
        loadStatistics();
    }
}