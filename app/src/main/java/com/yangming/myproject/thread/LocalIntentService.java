package com.yangming.myproject.thread;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author yangming
 */
public class LocalIntentService extends IntentService {

    private static final String TAG = "LocalIntentService";

    public LocalIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Log.e(TAG, "onStart: ");
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getStringExtra("task");
        Log.e(TAG, "receive task = " + action);
        SystemClock.sleep(3000);
        if ("yang.task".equals(action)) {
            Log.e(TAG, "handle task: " + action);
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "Service Destroy. ");
        super.onDestroy();
    }
}
