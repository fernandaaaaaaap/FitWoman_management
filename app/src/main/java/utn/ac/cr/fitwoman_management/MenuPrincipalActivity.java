package utn.ac.cr.fitwoman_management;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;



public class MenuPrincipalActivity extends AppCompatActivity {

    private Button btnMisEntrenamientos;
    private Button btnFotosProgreso;
    private Button btnEstadisticas;
    private Button btnGoals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        initializeViews();
        setupListeners();
        // Button
        btnGoals = findViewById(R.id.btnGoals);
    }

    private void initializeViews() {
        btnMisEntrenamientos = findViewById(R.id.btnMisentrenamientos);
        btnFotosProgreso = findViewById(R.id.btnfotosProgreso);
        btnEstadisticas = findViewById(R.id.btnestadisticas);
    }

    private void setupListeners() {
        // Button: My Workouts
        btnMisEntrenamientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipalActivity.this, ListaEntrenamientoActivity.class);
                startActivity(intent);

                // Button: Goals
                btnGoals.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MenuPrincipalActivity.this, GoalsActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        // Button: Progress Photos
        btnFotosProgreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipalActivity.this, fotosProgreso.class);
                startActivity(intent);
            }
        });

        // Button: Statistics
        btnEstadisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipalActivity.this, Estadisticas_Activity.class);
                startActivity(intent);
            }
        });
    }
}