package com.moko.lifex.activity;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;

import com.elvishew.xlog.XLog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.moko.lib.mqtt.MQTTSupport;
import com.moko.lib.mqtt.event.MQTTMessageArrivedEvent;
import com.moko.lib.mqtt.event.MQTTPublishFailureEvent;
import com.moko.lib.mqtt.event.MQTTPublishSuccessEvent;
import com.moko.lib.scannerui.utils.ToastUtils;
import com.moko.lifex.AppConstants;
import com.moko.lifex.R;
import com.moko.lifex.base.BaseActivity;
import com.moko.lifex.databinding.ActivityOverloadValueBinding;
import com.moko.lifex.entity.MokoDevice;
import com.moko.lifex.utils.SPUtiles;
import com.moko.support.MQTTConstants;
import com.moko.support.entity.MQTTConfig;
import com.moko.support.entity.MsgCommon;
import com.moko.support.entity.OverloadInfo;
import com.moko.support.entity.OverloadValue;
import com.moko.support.event.DeviceOnlineEvent;
import com.moko.support.handler.MQTTMessageAssembler;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;

public class OverloadValueActivity extends BaseActivity<ActivityOverloadValueBinding> {

    private MokoDevice mMokoDevice;
    private MQTTConfig appMqttConfig;
    private Handler mHandler;

    @Override
    protected ActivityOverloadValueBinding getViewBinding() {
        return ActivityOverloadValueBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate() {
        if (getIntent().getExtras() != null) {
            mMokoDevice = (MokoDevice) getIntent().getSerializableExtra(AppConstants.EXTRA_KEY_DEVICE);
        }
        String mqttConfigAppStr = SPUtiles.getStringValue(OverloadValueActivity.this, AppConstants.SP_KEY_MQTT_CONFIG_APP, "");
        appMqttConfig = new Gson().fromJson(mqttConfigAppStr, MQTTConfig.class);
        mBind.etOverloadValue.setText(String.valueOf(mMokoDevice.overloadValue));
        mHandler = new Handler(Looper.getMainLooper());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMQTTMessageArrivedEvent(MQTTMessageArrivedEvent event) {
        // 更新所有设备的网络状态
        final String topic = event.getTopic();
        final String message = event.getMessage();
        if (TextUtils.isEmpty(message))
            return;
        MsgCommon<JsonObject> msgCommon;
        try {
            Type type = new TypeToken<MsgCommon<JsonObject>>() {
            }.getType();
            msgCommon = new Gson().fromJson(message, type);
        } catch (Exception e) {
            return;
        }
        if (!mMokoDevice.uniqueId.equals(msgCommon.id)) {
            return;
        }
        mMokoDevice.isOnline = true;
        if (msgCommon.msg_id == MQTTConstants.NOTIFY_MSG_ID_OVERLOAD) {
            Type infoType = new TypeToken<OverloadInfo>() {
            }.getType();
            OverloadInfo overLoadInfo = new Gson().fromJson(msgCommon.data, infoType);
            mMokoDevice.isOverload = overLoadInfo.overload_state == 1;
            mMokoDevice.overloadValue = overLoadInfo.overload_value;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeviceOnlineEvent(DeviceOnlineEvent event) {
        String deviceId = event.getDeviceId();
        if (!mMokoDevice.deviceId.equals(deviceId)) {
            return;
        }
        boolean online = event.isOnline();
        if (!online) {
            mMokoDevice.isOnline = false;
            mMokoDevice.on_off = false;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMQTTPublishSuccessEvent(MQTTPublishSuccessEvent event) {
        int msgId = event.getMsgId();
        if (msgId == MQTTConstants.CONFIG_MSG_ID_OVERLOAD_VALUE) {
            ToastUtils.showToast(this, "Set up succeed");
            if (mHandler.hasMessages(0)) {
                dismissLoadingProgressDialog();
                mHandler.removeMessages(0);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMQTTPublishFailureEvent(MQTTPublishFailureEvent event) {
        int msgId = event.getMsgId();
        if (msgId == MQTTConstants.CONFIG_MSG_ID_OVERLOAD_VALUE) {
            ToastUtils.showToast(this, "Set up failed");
            if (mHandler.hasMessages(0)) {
                dismissLoadingProgressDialog();
                mHandler.removeMessages(0);
            }
        }
    }

    public void back(View view) {
        finish();
    }

    public void onSaveSettingsClick(View view) {
        if (isWindowLocked())
            return;
        if (!MQTTSupport.getInstance().isConnected()) {
            ToastUtils.showToast(this, R.string.network_error);
            return;
        }
        if (!mMokoDevice.isOnline) {
            ToastUtils.showToast(this, R.string.device_offline);
            return;
        }
        String overloadValue = mBind.etOverloadValue.getText().toString();
        if (TextUtils.isEmpty(overloadValue)) {
            ToastUtils.showToast(this, "Para Error");
            return;
        }
        int value = Integer.parseInt(overloadValue);
        if (value < 10 || value > 3795) {
            ToastUtils.showToast(this, "Para Error");
            return;
        }
        mHandler.postDelayed(() -> {
            dismissLoadingProgressDialog();
            ToastUtils.showToast(this, "Set up failed");
        }, 30 * 1000);
        showLoadingProgressDialog();
        XLog.i("设置过载值");
        OverloadValue overload = new OverloadValue();
        overload.overload_value = value;
        String appTopic;
        if (TextUtils.isEmpty(appMqttConfig.topicPublish)) {
            appTopic = mMokoDevice.topicSubscribe;
        } else {
            appTopic = appMqttConfig.topicPublish;
        }
        String message = MQTTMessageAssembler.assembleConfigOverloadValue(mMokoDevice.uniqueId, overload);
        try {
            MQTTSupport.getInstance().publish(appTopic, message, MQTTConstants.CONFIG_MSG_ID_OVERLOAD_VALUE, appMqttConfig.qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
