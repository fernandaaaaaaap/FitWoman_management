package utn.ac.cr.fitwoman_management;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import utn.ac.cr.fitwoman_management.Entities.Entrenamiento;
import utn.ac.cr.fitwoman_management.data.MemoryDataManager;

public class ListaEntrenamientoActivity extends AppCompatActivity {

    private ListView lvEntrenamientos;
    private EditText etBuscar;
    private Button fabAgregarEntrenamiento;
    private TextView tvNoEntrenamientos;

    private MemoryDataManager dataManager;
    private List<Entrenamiento> workoutList;
    private List<Entrenamiento> workoutListFull;
    private WorkoutListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_entrenamiento);

        initializeViews();
        initializeData();
        setupListeners();
        loadWorkouts();
    }

    private void initializeViews() {
        lvEntrenamientos = findViewById(R.id.lvEntrenamientos);
        etBuscar = findViewById(R.id.etBuscar);
        fabAgregarEntrenamiento = findViewById(R.id.fabAgregarEntrenamiento);
        tvNoEntrenamientos = findViewById(R.id.tvNoEntrenamientos);
    }

    private void initializeData() {
        dataManager = MemoryDataManager.getInstance();
        workoutList = new ArrayList<>();
        workoutListFull = new ArrayList<>();
    }

    private void setupListeners() {
        // Add workout button
        fabAgregarEntrenamiento.setOnClickListener(v -> {
            Intent intent = new Intent(ListaEntrenamientoActivity.this, crearEntrenamientoActivity.class);
            startActivity(intent);
        });

        // Search functionality
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterWorkouts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Item click - show options
        lvEntrenamientos.setOnItemClickListener((parent, view, position, id) -> {
            Entrenamiento workout = workoutList.get(position);
            showOptionsDialog(workout);
        });

        // Long click - quick delete
        lvEntrenamientos.setOnItemLongClickListener((parent, view, position, id) -> {
            Entrenamiento workout = workoutList.get(position);
            showDeleteConfirmationDialog(workout);
            return true;
        });
    }

    private void loadWorkouts() {
        workoutList.clear();
        workoutListFull.clear();

        List<Entrenamiento> allWorkouts = dataManager.getAllEntrenamientos();
        workoutList.addAll(allWorkouts);
        workoutListFull.addAll(allWorkouts);

        if (workoutList.isEmpty()) {
            lvEntrenamientos.setVisibility(View.GONE);
            tvNoEntrenamientos.setVisibility(View.VISIBLE);
        } else {
            lvEntrenamientos.setVisibility(View.VISIBLE);
            tvNoEntrenamientos.setVisibility(View.GONE);

            adapter = new WorkoutListAdapter(this, workoutList);
            lvEntrenamientos.setAdapter(adapter);
        }
    }

    private void filterWorkouts(String query) {
        workoutList.clear();

        if (query.isEmpty()) {
            workoutList.addAll(workoutListFull);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Entrenamiento workout : workoutListFull) {
                if (workout.getNombre().toLowerCase().contains(lowerCaseQuery) ||
                        workout.getCategoria().toLowerCase().contains(lowerCaseQuery) ||
                        workout.getGrupoMuscular().toLowerCase().contains(lowerCaseQuery)) {
                    workoutList.add(workout);
                }
            }
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        if (workoutList.isEmpty()) {
            lvEntrenamientos.setVisibility(View.GONE);
            tvNoEntrenamientos.setVisibility(View.VISIBLE);
            tvNoEntrenamientos.setText("No workouts found");
        } else {
            lvEntrenamientos.setVisibility(View.VISIBLE);
            tvNoEntrenamientos.setVisibility(View.GONE);
        }
    }

    private void showOptionsDialog(Entrenamiento workout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(workout.getNombre());

        String[] options = {"View Details", "Edit", "Delete"};

        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    showWorkoutDetails(workout);
                    break;
                case 1:
                    editWorkout(workout);
                    break;
                case 2:
                    showDeleteConfirmationDialog(workout);
                    break;
            }
        });

        builder.show();
    }

    private void showWorkoutDetails(Entrenamiento workout) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());

        String details = "Name: " + workout.getNombre() + "\n\n" +
                "Category: " + workout.getCategoria() + "\n\n" +
                "Muscle Group: " + workout.getGrupoMuscular() + "\n\n" +
                "Duration: " + workout.getDuracionMinutos() + " minutes\n\n" +
                "Date: " + dateFormat.format(workout.getFecha()) + "\n\n" +
                "Description: " + (workout.getDescripcion() != null ? workout.getDescripcion() : "N/A") + "\n\n" +
                "Notes: " + (workout.getNotas() != null ? workout.getNotas() : "N/A");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Workout Details");
        builder.setMessage(details);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void editWorkout(Entrenamiento workout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Workout");
        builder.setMessage("Do you want to edit '" + workout.getNombre() + "'?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            Intent intent = new Intent(ListaEntrenamientoActivity.this, editarEntrenamiento.class);
            intent.putExtra("WORKOUT_ID", workout.getId());
            intent.putExtra("WORKOUT_OBJECT", workout);
            startActivity(intent);
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDeleteConfirmationDialog(Entrenamiento workout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_confirm, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        TextView tvTitle = dialogView.findViewById(R.id.tvDialogTitle);
        TextView tvMessage = dialogView.findViewById(R.id.tvDialogMessage);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        tvTitle.setText("Delete Workout");
        tvMessage.setText("Are you sure you want to delete '" + workout.getNombre() + "'? This action cannot be undone.");

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            boolean deleted = dataManager.deleteEntrenamiento(workout.getId());
            if (deleted) {
                workoutList.remove(workout);
                workoutListFull.remove(workout);

                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }

                Toast.makeText(this, "Workout deleted successfully", Toast.LENGTH_SHORT).show();
                loadWorkouts();
            } else {
                Toast.makeText(this, "Error deleting workout", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWorkouts();
    }

    private class WorkoutListAdapter extends android.widget.ArrayAdapter<Entrenamiento> {

        public WorkoutListAdapter(android.content.Context context, List<Entrenamiento> workouts) {
            super(context, R.layout.item_entrenamiento, workouts);
        }

        @Override
        public View getView(int position, View convertView, android.view.ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_entrenamiento, parent, false);
            }

            Entrenamiento workout = getItem(position);

            TextView tvNombre = convertView.findViewById(R.id.tvNombreEntrenamiento);
            TextView tvCategoria = convertView.findViewById(R.id.tvCategoriaEntrenamiento);
            TextView tvDuracion = convertView.findViewById(R.id.tvDuracionEntrenamiento);
            TextView tvFecha = convertView.findViewById(R.id.tvFechaEntrenamiento);

            if (workout != null) {
                tvNombre.setText(workout.getNombre());
                tvCategoria.setText("🍑 " + workout.getCategoria());
                tvDuracion.setText("⏱ " + workout.getDuracionMinutos() + " min");

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                tvFecha.setText("📅 " + dateFormat.format(workout.getFecha()));
            }

            return convertView;
        }
    }
}