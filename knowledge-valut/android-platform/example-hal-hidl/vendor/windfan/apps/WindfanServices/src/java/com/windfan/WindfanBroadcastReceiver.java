package com.windfan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WindfanBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "WindfanBR";

    @Override
    public void onReceive(Context context, Intent intent) {
        String scalingGovernor = intent.getStringExtra("setScalingGovernor");
        if(scalingGovernor != null) {
            if(WindfanServices.getCpu().setScalingGovernor(scalingGovernor)) {
                Log.d(TAG, "Successfully setScalingGovernor(" + scalingGovernor + ")");
            } else {
                Log.e(TAG, "Failed calling setScalingGovernor(" + scalingGovernor + ")");
            }
        }
    }
}
