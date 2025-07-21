package com.moko.lifex.activity;

import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.moko.lib.mqtt.event.MQTTMessageArrivedEvent;
import com.moko.lifex.AppConstants;
import com.moko.lifex.base.BaseActivity;
import com.moko.lifex.databinding.ActivityElectricityManagerBinding;
import com.moko.lifex.entity.MokoDevice;
import com.moko.lifex.utils.Utils;
import com.moko.support.MQTTConstants;
import com.moko.support.entity.MsgCommon;
import com.moko.support.entity.OverloadOccur;
import com.moko.support.entity.PowerInfo;
import com.moko.support.event.DeviceOnlineEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;

public class ElectricityActivity extends BaseActivity<ActivityElectricityManagerBinding> {


    private MokoDevice mMokoDevice;

    @Override
    protected ActivityElectricityManagerBinding getViewBinding() {
        return ActivityElectricityManagerBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate() {
        mMokoDevice = (MokoDevice) getIntent().getSerializableExtra(AppConstants.EXTRA_KEY_DEVICE);
        if ("4".equals(mMokoDevice.type) || "5".equals(mMokoDevice.type)) {
            mBind.rlPowerFactor.setVisibility(View.VISIBLE);
        }
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
        if (msgCommon.msg_id == MQTTConstants.NOTIFY_MSG_ID_POWER_INFO) {
            Type infoType = new TypeToken<PowerInfo>() {
            }.getType();
            PowerInfo powerInfo = new Gson().fromJson(msgCommon.data, infoType);
            if ("4".equals(mMokoDevice.type) || "5".equals(mMokoDevice.type)) {
                float voltage = powerInfo.voltage;
                float current = powerInfo.current;
                float power = powerInfo.power;
                float power_factor = powerInfo.power_factor * 0.001f;
                mBind.tvCurrent.setText(Utils.getDecimalFormat("0.#").format(current));
                mBind.tvVoltage.setText(Utils.getDecimalFormat("0.#").format(voltage));
                mBind.tvPower.setText(Utils.getDecimalFormat("0.#").format(power));
                mBind.tvPowerFactor.setText(Utils.getDecimalFormat("0.###").format(power_factor));
            } else if ("2".equals(mMokoDevice.type) || "3".equals(mMokoDevice.type)) {
                float voltage = powerInfo.voltage;
                float current = powerInfo.current;
                float power = powerInfo.power;
                mBind.tvCurrent.setText(Utils.getDecimalFormat("0.#").format(current));
                mBind.tvVoltage.setText(Utils.getDecimalFormat("0.#").format(voltage));
                mBind.tvPower.setText(Utils.getDecimalFormat("0.#").format(power));
            } else {
                int voltage = (int) powerInfo.voltage;
                int current = (int) powerInfo.current;
                int power = (int) powerInfo.power;
                mBind.tvCurrent.setText(current + "");
                mBind.tvVoltage.setText(Utils.getDecimalFormat("0.#").format(voltage * 0.1));
                mBind.tvPower.setText(power + "");
            }
        }
        if (msgCommon.msg_id == MQTTConstants.NOTIFY_MSG_ID_OVERLOAD_OCCUR
                || msgCommon.msg_id == MQTTConstants.NOTIFY_MSG_ID_OVER_VOLTAGE_OCCUR
                || msgCommon.msg_id == MQTTConstants.NOTIFY_MSG_ID_OVER_CURRENT_OCCUR) {
            Type infoType = new TypeToken<OverloadOccur>() {
            }.getType();
            OverloadOccur overloadOccur = new Gson().fromJson(msgCommon.data, infoType);
            if (overloadOccur.state == 1)
                finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeviceOnlineEvent(DeviceOnlineEvent event) {
        String deviceId = event.getDeviceId();
        if (!mMokoDevice.deviceId.equals(deviceId)) {
            return;
        }
        boolean online = event.isOnline();
        if (!online)
            finish();
    }

    public void back(View view) {
        finish();
    }
}
