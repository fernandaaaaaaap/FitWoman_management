package utn.ac.cr.fitwoman_management;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import utn.ac.cr.fitwoman_management.Entities.Goal;
import utn.ac.cr.fitwoman_management.data.MemoryDataManager;

public class GoalsActivity extends AppCompatActivity {
    private ListView lvGoals;
    private Button btnAddGoal, btnActiveGoals, btnCompletedGoals;
    private LinearLayout layoutNoGoals;
    private MemoryDataManager dataManager;
    private List<Goal> goalsList;
    private boolean showingActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        initializeViews();
        dataManager = MemoryDataManager.getInstance();
        setupListeners();
        loadGoals();
    }

    private void initializeViews() {
        lvGoals = findViewById(R.id.lvGoals);
        btnAddGoal = findViewById(R.id.btnAddGoal);
        btnActiveGoals = findViewById(R.id.btnActiveGoals);
        btnCompletedGoals = findViewById(R.id.btnCompletedGoals);
        layoutNoGoals = findViewById(R.id.layoutNoGoals);
    }

    private void setupListeners() {
        btnAddGoal.setOnClickListener(v -> {
            Intent intent = new Intent(GoalsActivity.this, CreateGoalActivity.class);
            startActivity(intent);
        });

        btnActiveGoals.setOnClickListener(v -> {
            showingActive = true;
            btnActiveGoals.setBackgroundColor(0xFF8B4789);
            btnCompletedGoals.setBackgroundColor(0xFFCCCCCC);
            loadGoals();
        });

        btnCompletedGoals.setOnClickListener(v -> {
            showingActive = false;
            btnCompletedGoals.setBackgroundColor(0xFF8B4789);
            btnActiveGoals.setBackgroundColor(0xFFCCCCCC);
            loadGoals();
        });
    }

    private void loadGoals() {
        goalsList = showingActive ? dataManager.getActiveGoals() : dataManager.getCompletedGoals();

        if (goalsList.isEmpty()) {
            lvGoals.setVisibility(View.GONE);
            layoutNoGoals.setVisibility(View.VISIBLE);
        } else {
            lvGoals.setVisibility(View.VISIBLE);
            layoutNoGoals.setVisibility(View.GONE);
            Toast.makeText(this, goalsList.size() + " goals loaded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGoals();
    }
}