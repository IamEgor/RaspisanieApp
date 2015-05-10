package db;

import java.util.concurrent.ExecutorService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class UpdatingService extends Service {
    ExecutorService es;

    @Override
    public void onCreate() {
        Toast.makeText(this, "Служба создана",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        //MySQLiteClass mySQLiteClass = new MySQLiteClass(getApplicationContext());

        //mySQLiteClass.fillingDB(getResources());
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Служба остановлена",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}
