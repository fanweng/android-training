package com.windfan;

import android.app.Application;
import android.util.Log;
import com.windfan.Cpu;
import android.content.IntentFilter;

public class WindfanServices extends Application {
    private static final String TAG = "WindfanServices";

    WindfanBroadcastReceiver windfanBroadcastReceiver = new WindfanBroadcastReceiver();
    private static Cpu cpu; // JAVA -> JNI -> HIDL

    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate()");
        cpu = new Cpu();

        Log.d(TAG, "setScalingGovernor(performance) => " + cpu.setScalingGovernor("performance"));

        IntentFilter filter = new IntentFilter("com.windfan.CPU");
        registerReceiver(windfanBroadcastReceiver, filter);
    }

    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "Terminated");
    }

    public static Cpu getCpu() {
        return cpu;
    }
}