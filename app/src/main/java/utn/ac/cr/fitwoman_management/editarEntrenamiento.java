package utn.ac.cr.fitwoman_management;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import utn.ac.cr.fitwoman_management.Entities.Entrenamiento;
import utn.ac.cr.fitwoman_management.data.MemoryDataManager;

public class editarEntrenamiento extends AppCompatActivity {

    // UI Components
    private EditText etNombreEditar, etGrupoMuscularEditar, etDuracionEditar;
    private EditText etDescripcionEditar, etNotasEditar;
    private Spinner spinnerCategoriaEditar;
    private Button btnGuardarCambios, btnCancelarEditar;

    // Data
    private MemoryDataManager dataManager;
    private Entrenamiento workoutToEdit;
    private String workoutId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_entrenamiento);

        initializeViews();
        setupSpinner();
        dataManager = MemoryDataManager.getInstance();

        // Get workout data from intent
        getWorkoutData();

        // Load workout data into form
        loadWorkoutData();

        setupListeners();
    }

    private void initializeViews() {
        etNombreEditar = findViewById(R.id.etNombreEditar);
        etGrupoMuscularEditar = findViewById(R.id.etGrupoMuscularEditar);
        etDuracionEditar = findViewById(R.id.etDuracionEditar);
        etDescripcionEditar = findViewById(R.id.etDescripcionEditar);
        etNotasEditar = findViewById(R.id.etNotasEditar);

        spinnerCategoriaEditar = findViewById(R.id.spinnerCategoriaEditar);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        btnCancelarEditar = findViewById(R.id.btnCancelarEditar);
    }

    private void setupSpinner() {
        String[] categories = {
                "Select Category",
                "Cardio",
                "Strength",
                "Flexibility",
                "Legs",
                "Glutes",
                "Arms",
                "Back",
                "Abs",
                "Full Body"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoriaEditar.setAdapter(adapter);
    }

    private void getWorkoutData() {
        // Get data from Intent
        workoutId = getIntent().getStringExtra("WORKOUT_ID");
        workoutToEdit = (Entrenamiento) getIntent().getSerializableExtra("WORKOUT_OBJECT");

        // If no object passed, try to get by ID
        if (workoutToEdit == null && workoutId != null) {
            workoutToEdit = dataManager.getEntrenamientoById(workoutId);
        }

        if (workoutToEdit == null) {
            Toast.makeText(this, "Error: Workout not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadWorkoutData() {
        if (workoutToEdit != null) {
            etNombreEditar.setText(workoutToEdit.getNombre());
            etGrupoMuscularEditar.setText(workoutToEdit.getGrupoMuscular());
            etDuracionEditar.setText(String.valueOf(workoutToEdit.getDuracionMinutos()));

            if (workoutToEdit.getDescripcion() != null) {
                etDescripcionEditar.setText(workoutToEdit.getDescripcion());
            }

            if (workoutToEdit.getNotas() != null) {
                etNotasEditar.setText(workoutToEdit.getNotas());
            }

            // Set spinner selection
            setSpinnerSelection(workoutToEdit.getCategoria());
        }
    }

    private void setSpinnerSelection(String category) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerCategoriaEditar.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equalsIgnoreCase(category)) {
                spinnerCategoriaEditar.setSelection(i);
                break;
            }
        }
    }

    private void setupListeners() {
        // Save changes button - shows confirmation dialog
        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateConfirmationDialog();
            }
        });

        // Cancel button
        btnCancelarEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showUpdateConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate custom dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_confirm, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        // Get dialog views
        TextView tvTitle = dialogView.findViewById(R.id.tvDialogTitle);
        TextView tvMessage = dialogView.findViewById(R.id.tvDialogMessage);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        // Set dialog content
        tvTitle.setText("Update Workout");
        tvMessage.setText("Are you sure you want to save the changes to '" +
                workoutToEdit.getNombre() + "'?");

        // Cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Confirm button
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWorkout();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updateWorkout() {
        // Get values from inputs
        String nombre = etNombreEditar.getText().toString().trim();
        String categoria = spinnerCategoriaEditar.getSelectedItem().toString();
        String grupoMuscular = etGrupoMuscularEditar.getText().toString().trim();
        String duracionStr = etDuracionEditar.getText().toString().trim();
        String descripcion = etDescripcionEditar.getText().toString().trim();
        String notas = etNotasEditar.getText().toString().trim();

        // Validate inputs
        if (!validateInputs(nombre, categoria, grupoMuscular, duracionStr)) {
            return;
        }

        // Convert duration to int
        int duracion = Integer.parseInt(duracionStr);

        // Update the workout object
        workoutToEdit.setNombre(nombre);
        workoutToEdit.setCategoria(categoria);
        workoutToEdit.setGrupoMuscular(grupoMuscular);
        workoutToEdit.setDuracionMinutos(duracion);
        workoutToEdit.setDescripcion(descripcion);
        workoutToEdit.setNotas(notas);

        // Save changes to data manager
        boolean updated = dataManager.updateEntrenamiento(workoutToEdit);

        if (updated) {
            Toast.makeText(this, "Workout updated successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Return to previous screen
        } else {
            Toast.makeText(this, "Error updating workout", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String nombre, String categoria, String grupoMuscular, String duracionStr) {
        // Validate name
        if (nombre.isEmpty()) {
            etNombreEditar.setError("Name is required");
            etNombreEditar.requestFocus();
            return false;
        }

        // Validate category
        if (categoria.equals("Select Category")) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate muscle group
        if (grupoMuscular.isEmpty()) {
            etGrupoMuscularEditar.setError("Muscle group is required");
            etGrupoMuscularEditar.requestFocus();
            return false;
        }

        // Validate duration
        if (duracionStr.isEmpty()) {
            etDuracionEditar.setError("Duration is required");
            etDuracionEditar.requestFocus();
            return false;
        }

        try {
            int duracion = Integer.parseInt(duracionStr);
            if (duracion <= 0) {
                etDuracionEditar.setError("Duration must be greater than 0");
                etDuracionEditar.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etDuracionEditar.setError("Please enter a valid number");
            etDuracionEditar.requestFocus();
            return false;
        }

        return true;
    }
}