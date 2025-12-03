package utn.ac.cr.fitwoman_management.data;

import utn.ac.cr.fitwoman_management.Entities.Entrenamiento;
import utn.ac.cr.fitwoman_management.Entities.FotoProgreso;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import utn.ac.cr.fitwoman_management.Entities.Goal;

public class MemoryDataManager implements IDataManager {

    private static MemoryDataManager instance;
    private List<Entrenamiento> entrenamientos;
    private List<FotoProgreso> fotosProgreso;
    private List<Goal> goals;

    private MemoryDataManager() {
        this.entrenamientos = new ArrayList<>();
        this.fotosProgreso = new ArrayList<>();
        this.goals = new ArrayList<>();
    }

    public static MemoryDataManager getInstance() {
        if (instance == null) {
            instance = new MemoryDataManager();
        }
        return instance;
    }

    @Override
    public boolean createEntrenamiento(Entrenamiento entrenamiento) {
        if (entrenamiento == null || entrenamiento.getId() == null) {
            return false;
        }
        return entrenamientos.add(entrenamiento);
    }

    @Override
    public List<Entrenamiento> getAllEntrenamientos() {
        return new ArrayList<>(entrenamientos);
    }

    @Override
    public Entrenamiento getEntrenamientoById(String id) {
        for (Entrenamiento e : entrenamientos) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public List<Entrenamiento> getEntrenamientosByCategoria(String categoria) {
        List<Entrenamiento> resultado = new ArrayList<>();
        for (Entrenamiento e : entrenamientos) {
            if (e.getCategoria() != null && e.getCategoria().equalsIgnoreCase(categoria)) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    @Override
    public List<Entrenamiento> getEntrenamientosByFecha(Date fechaInicio, Date fechaFin) {
        List<Entrenamiento> resultado = new ArrayList<>();
        for (Entrenamiento e : entrenamientos) {
            Date fecha = e.getFecha();
            if (fecha != null && !fecha.before(fechaInicio) && !fecha.after(fechaFin)) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    @Override
    public boolean updateEntrenamiento(Entrenamiento entrenamiento) {
        if (entrenamiento == null || entrenamiento.getId() == null) {
            return false;
        }

        for (int i = 0; i < entrenamientos.size(); i++) {
            if (entrenamientos.get(i).getId().equals(entrenamiento.getId())) {
                entrenamientos.set(i, entrenamiento);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteEntrenamiento(String id) {
        return entrenamientos.removeIf(e -> e.getId().equals(id));
    }

    @Override
    public int deleteEntrenamientosAntiguos() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date unAnioAtras = calendar.getTime();

        List<Entrenamiento> entrenamientosAEliminar = new ArrayList<>();

        for (Entrenamiento e : entrenamientos) {
            if (e.getFecha() != null && e.getFecha().before(unAnioAtras)) {
                entrenamientosAEliminar.add(e);
            }
        }

        int cantidadEliminada = entrenamientosAEliminar.size();
        entrenamientos.removeAll(entrenamientosAEliminar);

        return cantidadEliminada;
    }

    @Override
    public boolean createFotoProgreso(FotoProgreso foto) {
        if (foto == null || foto.getId() == null) {
            return false;
        }
        return fotosProgreso.add(foto);
    }

    @Override
    public List<FotoProgreso> getAllFotosProgreso() {
        return new ArrayList<>(fotosProgreso);
    }

    @Override
    public FotoProgreso getFotoProgresoById(String id) {
        for (FotoProgreso f : fotosProgreso) {
            if (f.getId().equals(id)) {
                return f;
            }
        }
        return null;
    }

    @Override
    public List<FotoProgreso> getFotosProgresoByMes(String mes) {
        List<FotoProgreso> resultado = new ArrayList<>();
        for (FotoProgreso f : fotosProgreso) {
            if (f.getMes() != null && f.getMes().equalsIgnoreCase(mes)) {
                resultado.add(f);
            }
        }
        return resultado;
    }

    @Override
    public List<FotoProgreso> getFotosProgresoByCategoria(String categoria) {
        List<FotoProgreso> resultado = new ArrayList<>();
        for (FotoProgreso f : fotosProgreso) {
            if (f.getCategoria() != null && f.getCategoria().equalsIgnoreCase(categoria)) {
                resultado.add(f);
            }
        }
        return resultado;
    }

    @Override
    public boolean updateFotoProgreso(FotoProgreso foto) {
        if (foto == null || foto.getId() == null) {
            return false;
        }

        for (int i = 0; i < fotosProgreso.size(); i++) {
            if (fotosProgreso.get(i).getId().equals(foto.getId())) {
                fotosProgreso.set(i, foto);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteFotoProgreso(String id) {
        return fotosProgreso.removeIf(f -> f.getId().equals(id));
    }

    @Override
    public void clearAllData() {
        entrenamientos.clear();
        fotosProgreso.clear();
    }

    @Override
    public int getTotalEntrenamientos() {
        return entrenamientos.size();
    }

    @Override
    public int getTotalFotosProgreso() {
        return fotosProgreso.size();
    }
    // ==================== GOALS CRUD ====================

    @Override
    public boolean createGoal(Goal goal) {
        if (goal == null || goal.getId() == null) {
            return false;
        }
        return goals.add(goal);
    }

    @Override
    public List<Goal> getAllGoals() {
        return new ArrayList<>(goals);
    }

    @Override
    public Goal getGoalById(String id) {
        for (Goal g : goals) {
            if (g.getId().equals(id)) {
                return g;
            }
        }
        return null;
    }

    @Override
    public List<Goal> getActiveGoals() {
        List<Goal> activeGoals = new ArrayList<>();
        for (Goal g : goals) {
            if (!g.isCompleted()) {
                activeGoals.add(g);
            }
        }
        return activeGoals;
    }

    @Override
    public List<Goal> getCompletedGoals() {
        List<Goal> completedGoals = new ArrayList<>();
        for (Goal g : goals) {
            if (g.isCompleted()) {
                completedGoals.add(g);
            }
        }
        return completedGoals;
    }

    @Override
    public boolean updateGoal(Goal goal) {
        if (goal == null || goal.getId() == null) {
            return false;
        }

        for (int i = 0; i < goals.size(); i++) {
            if (goals.get(i).getId().equals(goal.getId())) {
                goals.set(i, goal);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteGoal(String id) {
        return goals.removeIf(g -> g.getId().equals(id));
    }

    @Override
    public int getTotalGoals() {
        return goals.size();
    }
}