package com.moko.lifex.activity;

import android.os.Bundle;
import android.view.View;

import com.moko.lib.mqtt.event.MQTTUnSubscribeSuccessEvent;
import com.moko.lifex.R;
import com.moko.lifex.base.BaseActivity;
import com.moko.lifex.databinding.ActivityPlugOperationStepsBinding;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class OperationPlugStepsActivity extends BaseActivity<ActivityPlugOperationStepsBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plug_operation_steps);
    }

    @Override
    protected ActivityPlugOperationStepsBinding getViewBinding() {
        return ActivityPlugOperationStepsBinding.inflate(getLayoutInflater());
    }

    public void back(View view) {
        finish();
    }

    public void plugBlinking(View view) {
        setResult(RESULT_OK);
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMQTTUnSubscribeSuccessEvent(MQTTUnSubscribeSuccessEvent event) {
    }
}
