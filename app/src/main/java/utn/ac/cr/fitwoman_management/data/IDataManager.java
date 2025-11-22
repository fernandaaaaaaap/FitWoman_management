package utn.ac.cr.fitwoman_management.data;

import utn.ac.cr.fitwoman_management.Entities.Entrenamiento;
import utn.ac.cr.fitwoman_management.Entities.FotoProgreso;
import java.util.Date;
import java.util.List;

public interface IDataManager {

   //CRUD//

    boolean createEntrenamiento(Entrenamiento entrenamiento);
    List<Entrenamiento> getAllEntrenamientos();
    Entrenamiento getEntrenamientoById(String id);
    List<Entrenamiento> getEntrenamientosByCategoria(String categoria);
    List<Entrenamiento> getEntrenamientosByFecha(Date fechaInicio, Date fechaFin);
    boolean updateEntrenamiento(Entrenamiento entrenamiento);
    boolean deleteEntrenamiento(String id);
    int deleteEntrenamientosAntiguos();



    boolean createFotoProgreso(FotoProgreso foto);
    List<FotoProgreso> getAllFotosProgreso();
    FotoProgreso getFotoProgresoById(String id);
    List<FotoProgreso> getFotosProgresoByMes(String mes);
    List<FotoProgreso> getFotosProgresoByCategoria(String categoria);
    boolean updateFotoProgreso(FotoProgreso foto);
    boolean deleteFotoProgreso(String id);



    void clearAllData();
    int getTotalEntrenamientos();
    int getTotalFotosProgreso();
}