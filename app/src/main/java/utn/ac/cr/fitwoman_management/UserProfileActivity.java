package utn.ac.cr.fitwoman_management;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {

    private EditText etNombreCompleto, etEdad, etPeso, etAltura, etCondiciones;
    private RadioGroup rgGenero;
    private Spinner spinnerObjetivo, spinnerNivelActividad;
    private Button btnGuardarPerfil, btnOmitir;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UserProfile";
    private static final String KEY_PROFILE_COMPLETED = "profile_completed";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_EDAD = "edad";
    private static final String KEY_GENERO = "genero";
    private static final String KEY_PESO = "peso";
    private static final String KEY_ALTURA = "altura";
    private static final String KEY_OBJETIVO = "objetivo";
    private static final String KEY_NIVEL_ACTIVIDAD = "nivel_actividad";
    private static final String KEY_CONDICIONES = "condiciones";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initializeViews();
        setupSpinners();
        setupListeners();

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Si ya completó el perfil, cargar datos
        if (sharedPreferences.getBoolean(KEY_PROFILE_COMPLETED, false)) {
            loadProfileData();
        }
    }

    private void initializeViews() {
        etNombreCompleto = findViewById(R.id.etNombreCompleto);
        etEdad = findViewById(R.id.etEdad);
        etPeso = findViewById(R.id.etPeso);
        etAltura = findViewById(R.id.etAltura);
        etCondiciones = findViewById(R.id.etCondiciones);
        rgGenero = findViewById(R.id.rgGenero);
        spinnerObjetivo = findViewById(R.id.spinnerObjetivo);
        spinnerNivelActividad = findViewById(R.id.spinnerNivelActividad);
        btnGuardarPerfil = findViewById(R.id.btnGuardarPerfil);
        btnOmitir = findViewById(R.id.btnOmitir);
    }

    private void setupSpinners() {
        // Spinner de Objetivos
        String[] objetivos = {
                "Select Goal",
                "Lose Weight",
                "Gain Muscle",
                "Tone Body",
                "Improve Endurance",
                "Stay Healthy",
                "Increase Flexibility"
        };
        ArrayAdapter<String> objetivosAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                objetivos
        );
        objetivosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerObjetivo.setAdapter(objetivosAdapter);

        // Spinner de Nivel de Actividad
        String[] nivelesActividad = {
                "Select Activity Level",
                "Sedentary (Little or no exercise)",
                "Light (Exercise 1-3 days/week)",
                "Moderate (Exercise 3-5 days/week)",
                "Active (Exercise 6-7 days/week)",
                "Very Active (Intense exercise daily)"
        };
        ArrayAdapter<String> actividadAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nivelesActividad
        );
        actividadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNivelActividad.setAdapter(actividadAdapter);
    }

    private void setupListeners() {
        btnGuardarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        btnOmitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainMenu();
            }
        });
    }

    private void saveProfile() {
        String nombre = etNombreCompleto.getText().toString().trim();
        String edadStr = etEdad.getText().toString().trim();
        String pesoStr = etPeso.getText().toString().trim();
        String alturaStr = etAltura.getText().toString().trim();
        String condiciones = etCondiciones.getText().toString().trim();
        String objetivo = spinnerObjetivo.getSelectedItem().toString();
        String nivelActividad = spinnerNivelActividad.getSelectedItem().toString();

        // Validaciones básicas
        if (nombre.isEmpty()) {
            etNombreCompleto.setError("Name is required");
            etNombreCompleto.requestFocus();
            return;
        }

        if (edadStr.isEmpty()) {
            etEdad.setError("Age is required");
            etEdad.requestFocus();
            return;
        }

        // Obtener género seleccionado
        int selectedGenderId = rgGenero.getCheckedRadioButtonId();
        String genero = "";
        if (selectedGenderId != -1) {
            RadioButton selectedRadio = findViewById(selectedGenderId);
            genero = selectedRadio.getText().toString();
        } else {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if (objetivo.equals("Select Goal")) {
            Toast.makeText(this, "Please select a goal", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nivelActividad.equals("Select Activity Level")) {
            Toast.makeText(this, "Please select an activity level", Toast.LENGTH_SHORT).show();
            return;
        }

        // Guardar en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NOMBRE, nombre);
        editor.putString(KEY_EDAD, edadStr);
        editor.putString(KEY_GENERO, genero);
        editor.putString(KEY_PESO, pesoStr);
        editor.putString(KEY_ALTURA, alturaStr);
        editor.putString(KEY_OBJETIVO, objetivo);
        editor.putString(KEY_NIVEL_ACTIVIDAD, nivelActividad);
        editor.putString(KEY_CONDICIONES, condiciones);
        editor.putBoolean(KEY_PROFILE_COMPLETED, true);
        editor.apply();

        Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show();
        goToMainMenu();
    }

    private void loadProfileData() {
        etNombreCompleto.setText(sharedPreferences.getString(KEY_NOMBRE, ""));
        etEdad.setText(sharedPreferences.getString(KEY_EDAD, ""));
        etPeso.setText(sharedPreferences.getString(KEY_PESO, ""));
        etAltura.setText(sharedPreferences.getString(KEY_ALTURA, ""));
        etCondiciones.setText(sharedPreferences.getString(KEY_CONDICIONES, ""));

        // Cargar género
        String genero = sharedPreferences.getString(KEY_GENERO, "");
        if (genero.equals("Female")) {
            rgGenero.check(R.id.rbFemenino);
        } else if (genero.equals("Male")) {
            rgGenero.check(R.id.rbMasculino);
        } else if (genero.equals("Other")) {
            rgGenero.check(R.id.rbOtro);
        }

        // Cargar spinners
        String objetivo = sharedPreferences.getString(KEY_OBJETIVO, "");
        setSpinnerValue(spinnerObjetivo, objetivo);

        String nivelActividad = sharedPreferences.getString(KEY_NIVEL_ACTIVIDAD, "");
        setSpinnerValue(spinnerNivelActividad, nivelActividad);
    }

    private void setSpinnerValue(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void goToMainMenu() {
        Intent intent = new Intent(UserProfileActivity.this, MenuPrincipalActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // Evitar que regrese al login
        moveTaskToBack(true);
    }
}