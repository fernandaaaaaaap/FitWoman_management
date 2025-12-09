package utn.ac.cr.fitwoman_management;

import android.app.Application;
import utn.ac.cr.fitwoman_management.data.MemoryDataManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Inicializa el MemoryDataManager cuando la app inicia
        MemoryDataManager.getInstance();
    }
}