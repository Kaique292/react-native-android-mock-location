import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class AndroidMockLocationTask extends Service {
    private Handler handler;
    private Runnable runnable;
    private boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // Coloque sua lógica de execução periódica aqui
                if (isRunning) {
                    // Execute a tarefa
                    doSomething();
                    handler.postDelayed(this, 5000); // Executa a cada 5 segundos
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;
        handler.post(runnable);
        return START_STICKY;
    }

     @Override
    public IBinder onBind(Intent intent) {
        // Este método é necessário, mesmo que você não planeje utilizá-lo
        // Você pode deixá-lo vazio
        return null;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    private void doSomething() {
        // Implemente a lógica da tarefa aqui
    }
}
