package com.androidmocklocation;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
 
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
                if (isRunning) { 
                    mangoJOU();
                    handler.postDelayed(this, 5000); 
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
         Log.i("ReactNative", "BINDANDO"); 
        return null;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    private void mangoJOU() {
         
    }
}