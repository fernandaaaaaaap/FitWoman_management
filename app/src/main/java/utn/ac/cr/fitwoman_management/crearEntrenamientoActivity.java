package utn.ac.cr.fitwoman_management;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import utn.ac.cr.fitwoman_management.Entities.Entrenamiento;
import utn.ac.cr.fitwoman_management.data.MemoryDataManager;

public class crearEntrenamientoActivity extends AppCompatActivity {

    // UI Components
    private EditText etNombre, etGrupoMuscular, etDuracion, etDescripcion, etNotas;
    private Spinner spinnerCategoria;
    private Button btnGuardar, btnCancelar;

    // Data Manager
    private MemoryDataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_entrenamiento);

        initializeViews();
        setupSpinner();
        setupListeners();

        dataManager = MemoryDataManager.getInstance();
    }

    private void initializeViews() {
        // Initialize all EditText fields
        etNombre = findViewById(R.id.etNombre);
        etGrupoMuscular = findViewById(R.id.etGrupoMuscular);
        etDuracion = findViewById(R.id.etDuracion);
        etDescripcion = findViewById(R.id.etDescripcion);
        etNotas = findViewById(R.id.etNotas);

        // Initialize Spinner and Buttons
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);
    }

    private void setupSpinner() {
        // Categories for workouts
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
        spinnerCategoria.setAdapter(adapter);
    }

    private void setupListeners() {
        // Save button
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWorkout();
            }
        });

        // Cancel button
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close activity and return to previous screen
            }
        });
    }

    private void saveWorkout() {
        // Get values from inputs
        String nombre = etNombre.getText().toString().trim();
        String categoria = spinnerCategoria.getSelectedItem().toString();
        String grupoMuscular = etGrupoMuscular.getText().toString().trim();
        String duracionStr = etDuracion.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String notas = etNotas.getText().toString().trim();

        // Validate inputs
        if (!validateInputs(nombre, categoria, grupoMuscular, duracionStr)) {
            return;
        }

        // Convert duration to int
        int duracion = Integer.parseInt(duracionStr);

        // Create new Entrenamiento object
        Entrenamiento nuevoEntrenamiento = new Entrenamiento(
                nombre,
                categoria,
                descripcion,
                duracion,
                grupoMuscular,
                notas
        );

        // Save to data manager
        boolean saved = dataManager.createEntrenamiento(nuevoEntrenamiento);

        if (saved) {
            Toast.makeText(this, "Workout saved successfully!", Toast.LENGTH_SHORT).show();
            clearForm();
            finish(); // Return to previous screen
        } else {
            Toast.makeText(this, "Error saving workout", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String nombre, String categoria, String grupoMuscular, String duracionStr) {
        // Validate name
        if (nombre.isEmpty()) {
            etNombre.setError("Name is required");
            etNombre.requestFocus();
            return false;
        }

        // Validate category
        if (categoria.equals("Select Category")) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate muscle group
        if (grupoMuscular.isEmpty()) {
            etGrupoMuscular.setError("Muscle group is required");
            etGrupoMuscular.requestFocus();
            return false;
        }

        // Validate duration
        if (duracionStr.isEmpty()) {
            etDuracion.setError("Duration is required");
            etDuracion.requestFocus();
            return false;
        }

        try {
            int duracion = Integer.parseInt(duracionStr);
            if (duracion <= 0) {
                etDuracion.setError("Duration must be greater than 0");
                etDuracion.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etDuracion.setError("Please enter a valid number");
            etDuracion.requestFocus();
            return false;
        }

        return true;
    }

    private void clearForm() {
        etNombre.setText("");
        etGrupoMuscular.setText("");
        etDuracion.setText("");
        etDescripcion.setText("");
        etNotas.setText("");
        spinnerCategoria.setSelection(0);
    }
}