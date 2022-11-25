package com.windfan;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CpuActivity extends Activity {
    private TextView cppHIDLRetTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu);

        cppHIDLRetTV = findViewById(R.id.cppHIDLRetTV);
    }


    public void onClick(View view) {
        String setValue = ((Button)view).getText().toString();
        setSuccess(cppHIDLRetTV, WindfanServices.getCpu().setScalingGovernor(setValue));
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
