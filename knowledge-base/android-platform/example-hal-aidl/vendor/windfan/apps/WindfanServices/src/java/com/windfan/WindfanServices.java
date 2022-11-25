package com.windfan;

import android.app.Application;
import android.util.Log;
import com.windfan.Cpu;
import android.content.IntentFilter;
import vendor.windfan.cpu.V1_0.ICpu;
import android.os.ServiceManager;
import android.os.IBinder;

public class WindfanServices extends Application {
    private static final String TAG = "WindfanServices";

    private static final String ICPU_AIDL_INTERFACE = "vendor.windfan.cpu.ICpu/default";

    WindfanBroadcastReceiver windfanBroadcastReceiver = new WindfanBroadcastReceiver();
    private static Cpu cpu; // JAVA -> JNI -> HIDL Cpp Proxy
    private static ICpu cpuHJ; // HIDL Java Proxy
    private static vendor.windfan.cpu.ICpu cpuAJ; // AIDL Java Proxy

    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate()");
        cpu = new Cpu();

        Log.d(TAG, "HIDL-Cpp setScalingGovernor(performance) => " + cpu.setScalingGovernor("performance"));

        try {
            cpuHJ = ICpu.getService(true);
            Log.d(TAG, "HIDL-Java setScalingGovernor(powersave) => " + cpuHJ.setScalingGovernor("powersave"));
        } catch (android.os.RemoteException e) {
            Log.e(TAG, "ICpu error", e);
        }

        IBinder binder = ServiceManager.getService(ICPU_AIDL_INTERFACE);
        if (binder == null) {
            Log.e(TAG, "Getting " + ICPU_AIDL_INTERFACE + " service daemon binder failed");
        } else {
            cpuAJ = vendor.windfan.cpu.ICpu.Stub.asInterface(binder);
            if (cpuAJ == null) {
                Log.e(TAG, "Getting ICpu Aidl daemon interface failed");
            } else {
                try {
                    Log.d(TAG, "AIDL-Java setScalingGovernor(conservative) => " +
                         cpuAJ.setScalingGovernor("conservative"));
                } catch (android.os.RemoteException e) {
                    Log.e(TAG, "ICPU-AIDL error", e);
                }
            }
        }

        IntentFilter filter = new IntentFilter("com.windfan.CPU");
        registerReceiver(windfanBroadcastReceiver, filter);
    }

    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "Terminated");
    }

    // JAVA -> JNI -> HIDL Cpp Proxy
    public static Cpu getCpu() {
        return cpu;
    }

    // HIDL Java Proxy
    public static ICpu getCpuHJ() {
        return cpuHJ;
    }

    // AIDL Java Proxy
    public static vendor.windfan.cpu.ICpu getCpuAJ() {
        return cpuAJ;
    }
}
