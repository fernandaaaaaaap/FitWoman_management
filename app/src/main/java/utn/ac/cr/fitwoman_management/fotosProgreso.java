package utn.ac.cr.fitwoman_management;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import utn.ac.cr.fitwoman_management.Entities.FotoProgreso;
import utn.ac.cr.fitwoman_management.adapters.FotoProgresoAdapter;
import utn.ac.cr.fitwoman_management.data.MemoryDataManager;

public class fotosProgreso extends AppCompatActivity {

    private GridView gridViewFotos;
    private Button btnComparar, fabAgregarFoto;
    private LinearLayout layoutNoFotos;

    private MemoryDataManager dataManager;
    private List<FotoProgreso> fotosList;
    private FotoProgresoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotos_progreso);

        initializeViews();
        dataManager = MemoryDataManager.getInstance();
        setupListeners();
        loadPhotos();
    }

    private void initializeViews() {
        gridViewFotos = findViewById(R.id.gridViewFotos);
        btnComparar = findViewById(R.id.btnComparar);
        fabAgregarFoto = findViewById(R.id.fabAgregarFoto);
        layoutNoFotos = findViewById(R.id.layoutNoFotos);
    }

    private void setupListeners() {
        fabAgregarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fotosProgreso.this, TomarFotoActivity.class);
                startActivity(intent);
            }
        });

        btnComparar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fotosList != null && fotosList.size() >= 2) {
                    Intent intent = new Intent(fotosProgreso.this, ComparadorFotosActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(fotosProgreso.this,
                            "You need at least 2 photos to compare",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        gridViewFotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FotoProgreso foto = fotosList.get(position);
                Toast.makeText(fotosProgreso.this,
                        foto.getMes() + " - " + foto.getPeso() + " kg - " + foto.getCategoria(),
                        Toast.LENGTH_LONG).show();
            }
        });

        gridViewFotos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FotoProgreso foto = fotosList.get(position);
                showDeleteConfirmationDialog(foto);
                return true;
            }
        });
    }

    private void loadPhotos() {
        fotosList = new ArrayList<>();
        List<FotoProgreso> allPhotos = dataManager.getAllFotosProgreso();
        fotosList.addAll(allPhotos);

        if (fotosList.isEmpty()) {
            gridViewFotos.setVisibility(View.GONE);
            layoutNoFotos.setVisibility(View.VISIBLE);
            btnComparar.setEnabled(false);
        } else {
            gridViewFotos.setVisibility(View.VISIBLE);
            layoutNoFotos.setVisibility(View.GONE);
            btnComparar.setEnabled(true);

            adapter = new FotoProgresoAdapter(this, fotosList);
            gridViewFotos.setAdapter(adapter);
        }
    }

    private void showDeleteConfirmationDialog(FotoProgreso foto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_confirm, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        TextView tvTitle = dialogView.findViewById(R.id.tvDialogTitle);
        TextView tvMessage = dialogView.findViewById(R.id.tvDialogMessage);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        tvTitle.setText("Delete Photo");
        tvMessage.setText("Are you sure you want to delete this progress photo from '" +
                foto.getMes() + "'? This action cannot be undone.");

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            boolean deleted = dataManager.deleteFotoProgreso(foto.getId());
            if (deleted) {
                Toast.makeText(this, "Photo deleted successfully", Toast.LENGTH_SHORT).show();
                loadPhotos();
            } else {
                Toast.makeText(this, "Error deleting photo", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPhotos();
    }
}