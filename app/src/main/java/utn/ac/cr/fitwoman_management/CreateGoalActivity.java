package utn.ac.cr.fitwoman_management;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

import utn.ac.cr.fitwoman_management.Entities.Goal;
import utn.ac.cr.fitwoman_management.data.MemoryDataManager;

public class CreateGoalActivity extends AppCompatActivity {

    private Spinner spinnerGoalType;
    private EditText etTargetValue, etGoalDescription;
    private Button btnSelectDate, btnSaveGoal, btnCancelGoal;

    private MemoryDataManager dataManager;
    private Date selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);

        initializeViews();
        setupSpinner();
        setupListeners();

        dataManager = MemoryDataManager.getInstance();
    }

    private void initializeViews() {
        spinnerGoalType = findViewById(R.id.spinnerGoalType);
        etTargetValue = findViewById(R.id.etTargetValue);
        etGoalDescription = findViewById(R.id.etGoalDescription);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSaveGoal = findViewById(R.id.btnSaveGoal);
        btnCancelGoal = findViewById(R.id.btnCancelGoal);
    }

    private void setupSpinner() {
        String[] goalTypes = {
                "Select Goal Type",
                "Target Weight (kg)",
                "Weekly Workouts",
                "Total Workouts"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                goalTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGoalType.setAdapter(adapter);
    }

    private void setupListeners() {
        btnSelectDate.setOnClickListener(v -> showDatePicker());
        btnSaveGoal.setOnClickListener(v -> saveGoal());
        btnCancelGoal.setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    selectedDate = calendar.getTime();
                    btnSelectDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void saveGoal() {
        String goalType = spinnerGoalType.getSelectedItem().toString();
        String targetValueStr = etTargetValue.getText().toString().trim();
        String description = etGoalDescription.getText().toString().trim();

        if (!validateInputs(goalType, targetValueStr, description)) {
            return;
        }

        double targetValue = Double.parseDouble(targetValueStr);

        String type = getGoalTypeCode(goalType);

        Goal newGoal = new Goal(type, targetValue, selectedDate, description);

        boolean saved = dataManager.createGoal(newGoal);

        if (saved) {
            Toast.makeText(this, "Goal created successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error creating goal", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String goalType, String targetValue, String description) {
        if (goalType.equals("Select Goal Type")) {
            Toast.makeText(this, "Please select a goal type", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (targetValue.isEmpty()) {
            etTargetValue.setError("Target value is required");
            etTargetValue.requestFocus();
            return false;
        }

        try {
            double value = Double.parseDouble(targetValue);
            if (value <= 0) {
                etTargetValue.setError("Value must be greater than 0");
                etTargetValue.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etTargetValue.setError("Please enter a valid number");
            etTargetValue.requestFocus();
            return false;
        }

        if (description.isEmpty()) {
            etGoalDescription.setError("Description is required");
            etGoalDescription.requestFocus();
            return false;
        }

        if (selectedDate == null) {
            Toast.makeText(this, "Please select a target date", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private String getGoalTypeCode(String goalType) {
        if (goalType.contains("Weight")) {
            return "weight";
        } else if (goalType.contains("Weekly")) {
            return "workouts_weekly";
        } else if (goalType.contains("Total")) {
            return "workouts_total";
        }
        return "weight";
    }
}