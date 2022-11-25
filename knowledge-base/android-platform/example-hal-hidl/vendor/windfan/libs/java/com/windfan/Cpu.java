package com.windfan;

public class Cpu {
    private native void nativeInit();
    private static native boolean nativeSetScalingGovernor(String governor);

    public Cpu() {
        synchronized (this) {
            nativeInit();
        }
    }

    public boolean setScalingGovernor(String governor) {
        return nativeSetScalingGovernor(governor);
    }

    static {
        System.loadLibrary("windfan"); // libwindfan.so
    }
}
