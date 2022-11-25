package com.windfan;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CpuActivity extends Activity {
    private TextView javaAIDLRetTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu);

        javaAIDLRetTV = findViewById(R.id.javaAIDLRetTV);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onClick(View view) {
        String setValue = ((Button)view).getText().toString();

        // [---------------- com.windfan ----------------------------]
        // [ android_app: /system_ext/app/WindfanServices/           ]
        // [                     |/\/\|                               ]
        // [ aidl_interface: apk:classes.dex:vendor.windfan.cpu.ICpu ]
        // [----------------------------------------------------------]
        //                         ||
        //                    /dev/vndbinder
        //                         ||
        // [------------- vendor.windfan.cpu@1.0-service ----------------------]
        // [ aidl_interface: /vendor/lib/vendor.windfan.cpu-V1-ndk_platform.so ]
        // [                     |/\/\|                                         ]
        // [ cc_binary: /vendor/bin/hw/vendor.windfan.cpu-service              ]
        // [--------------------------------------------------------------------]
        try {
            setSuccess(javaAIDLRetTV, WindfanServices.getCpuAJ().setScalingGovernor(setValue));
        } catch (android.os.RemoteException e) {
            Log.e("CpuActivity", "cpu AIDL Java proxy returned error", e);
        }
    }

    private void setValue(TextView textValue, String value){
        textValue.setText(value);
        textValue.setTextColor(getColor(R.color.darkGray));
    }

    private void setSuccess(TextView textValue, boolean success){
        if (success) {
            textValue.setText("Success");
            textValue.setTextColor(getColor(R.color.success));
        } else {
            textValue.setText("Error");
            textValue.setTextColor(getColor(R.color.error));
        }
    }
}
