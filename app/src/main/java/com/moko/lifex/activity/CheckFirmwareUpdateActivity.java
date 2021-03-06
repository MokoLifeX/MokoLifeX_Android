package com.moko.lifex.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.moko.lifex.AppConstants;
import com.moko.lifex.R;
import com.moko.lifex.base.BaseActivity;
import com.moko.lifex.dialog.UpdateTypeDialog;
import com.moko.lifex.entity.MQTTConfig;
import com.moko.lifex.entity.MokoDevice;
import com.moko.lifex.entity.MsgCommon;
import com.moko.lifex.entity.OTAInfo;
import com.moko.lifex.entity.OverloadInfo;
import com.moko.lifex.entity.SetOTA;
import com.moko.lifex.service.MokoService;
import com.moko.lifex.utils.SPUtiles;
import com.moko.lifex.utils.ToastUtils;
import com.moko.support.MokoConstants;
import com.moko.support.log.LogModule;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.reflect.Type;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @Date 2018/6/7
 * @Author wenzheng.liu
 * @Description
 * @ClassPath com.moko.lifex.activity.CheckFirmwareUpdateActivity
 */
public class CheckFirmwareUpdateActivity extends BaseActivity {

    public static String TAG = "CheckFirmwareUpdateActivity";
    @Bind(R.id.et_host_content)
    EditText etHostContent;
    @Bind(R.id.et_host_port)
    EditText etHostPort;
    @Bind(R.id.et_host_catalogue)
    EditText etHostCatalogue;
    @Bind(R.id.tv_update_type)
    TextView tvUpdateType;


    private MokoDevice mokoDevice;
    private MQTTConfig appMqttConfig;

    private String[] mUpdateType;

    private MokoService mokoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_firmware);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            mokoDevice = (MokoDevice) getIntent().getSerializableExtra(AppConstants.EXTRA_KEY_DEVICE);
        }
        mUpdateType = getResources().getStringArray(R.array.update_type);
        tvUpdateType.setText(mUpdateType[0]);
        String mqttConfigAppStr = SPUtiles.getStringValue(CheckFirmwareUpdateActivity.this, AppConstants.SP_KEY_MQTT_CONFIG_APP, "");
        appMqttConfig = new Gson().fromJson(mqttConfigAppStr, MQTTConfig.class);
        bindService(new Intent(this, MokoService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mokoService = ((MokoService.LocalBinder) service).getService();
            // 注册广播接收器
            IntentFilter filter = new IntentFilter();
            filter.addAction(MokoConstants.ACTION_MQTT_CONNECTION);
            filter.addAction(MokoConstants.ACTION_MQTT_RECEIVE);
            filter.addAction(MokoConstants.ACTION_MQTT_SUBSCRIBE);
            filter.addAction(MokoConstants.ACTION_MQTT_UNSUBSCRIBE);
            filter.addAction(AppConstants.ACTION_DEVICE_STATE);
            registerReceiver(mReceiver, filter);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (MokoConstants.ACTION_MQTT_CONNECTION.equals(action)) {
                int state = intent.getIntExtra(MokoConstants.EXTRA_MQTT_CONNECTION_STATE, 0);
            }
            if (MokoConstants.ACTION_MQTT_RECEIVE.equals(action)) {
                String topic = intent.getStringExtra(MokoConstants.EXTRA_MQTT_RECEIVE_TOPIC);
                String message = intent.getStringExtra(MokoConstants.EXTRA_MQTT_RECEIVE_MESSAGE);
                MsgCommon<JsonObject> msgCommon;
                try {
                    Type type = new TypeToken<MsgCommon<JsonObject>>() {
                    }.getType();
                    msgCommon = new Gson().fromJson(message, type);
                } catch (Exception e) {
                    return;
                }
                if (mokoDevice.uniqueId.equals(msgCommon.id)) {
                    mokoDevice.isOnline = true;
                    if (msgCommon.msg_id == MokoConstants.MSG_ID_D_2_A_OTA_INFO) {
                        dismissLoadingProgressDialog();
                        Type infoType = new TypeToken<OTAInfo>() {
                        }.getType();
                        OTAInfo otaInfo = new Gson().fromJson(msgCommon.data, infoType);
                        String ota_result = otaInfo.ota_result;
                        if ("R1".equals(ota_result)) {
                            ToastUtils.showToast(CheckFirmwareUpdateActivity.this, R.string.update_success);
                        } else {
                            ToastUtils.showToast(CheckFirmwareUpdateActivity.this, R.string.update_failed);
                        }
                    }
                    if (msgCommon.msg_id == MokoConstants.MSG_ID_D_2_A_OVERLOAD) {
                        Type infoType = new TypeToken<OverloadInfo>() {
                        }.getType();
                        OverloadInfo overLoadInfo = new Gson().fromJson(msgCommon.data, infoType);
                        mokoDevice.isOverload = overLoadInfo.overload_state == 1;
                        mokoDevice.overloadValue = overLoadInfo.overload_value;
                    }
                }
            }
            if (MokoConstants.ACTION_MQTT_SUBSCRIBE.equals(action)) {
                int state = intent.getIntExtra(MokoConstants.EXTRA_MQTT_STATE, 0);
            }
            if (MokoConstants.ACTION_MQTT_UNSUBSCRIBE.equals(action)) {
                int state = intent.getIntExtra(MokoConstants.EXTRA_MQTT_STATE, 0);
            }
            if (AppConstants.ACTION_DEVICE_STATE.equals(action)) {
                String topic = intent.getStringExtra(MokoConstants.EXTRA_MQTT_RECEIVE_TOPIC);
                if (topic.equals(mokoDevice.topicPublish)) {
                    mokoDevice.isOnline = false;
                }
            }
        }
    };

    public void back(View view) {
        finish();
    }

    public void startUpdate(View view) {
        if (!mokoService.isConnected()) {
            ToastUtils.showToast(this, R.string.network_error);
            return;
        }
        if (!mokoDevice.isOnline) {
            ToastUtils.showToast(this, R.string.device_offline);
            return;
        }
        if (mokoDevice.isOverload) {
            ToastUtils.showToast(this, R.string.device_offline);
            return;
        }
        String hostStr = etHostContent.getText().toString();
        String portStr = etHostPort.getText().toString();
        String catalogueStr = etHostCatalogue.getText().toString();
        if (TextUtils.isEmpty(hostStr)) {
            ToastUtils.showToast(this, R.string.mqtt_verify_host);
            return;
        }
        if (!TextUtils.isEmpty(portStr) && Integer.parseInt(portStr) > 65535) {
            ToastUtils.showToast(this, R.string.mqtt_verify_port_empty);
            return;
        }
        if (TextUtils.isEmpty(catalogueStr)) {
            ToastUtils.showToast(this, R.string.mqtt_verify_catalogue);
            return;
        }
        LogModule.i("升级固件");
        showLoadingProgressDialog(getString(R.string.wait));
        MsgCommon<SetOTA> msgCommon = new MsgCommon();
        msgCommon.msg_id = MokoConstants.MSG_ID_A_2_D_SET_OTA;
        msgCommon.id = mokoDevice.uniqueId;
        SetOTA setOTA = new SetOTA();
        setOTA.file_type = mSelected;
        setOTA.domain_name = hostStr;
        setOTA.port = Integer.parseInt(portStr);
        setOTA.file_way = catalogueStr;
        msgCommon.data = setOTA;
        MqttMessage message = new MqttMessage();
        message.setPayload(new Gson().toJson(msgCommon).getBytes());
        message.setQos(appMqttConfig.qos);
        String appTopic;
        if (TextUtils.isEmpty(appMqttConfig.topicPublish)) {
            appTopic = mokoDevice.topicSubscribe;
        } else {
            appTopic = appMqttConfig.topicPublish;
        }
        try {
            mokoService.publish(appTopic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        unbindService(serviceConnection);
    }

    private int mSelected;

    public void updateType(View view) {
        UpdateTypeDialog dialog = new UpdateTypeDialog();
        dialog.setSelected(mSelected);
        dialog.setListener(new UpdateTypeDialog.OnDataSelectedListener() {
            @Override
            public void onDataSelected(int selected) {
                mSelected = selected;
                tvUpdateType.setText(mUpdateType[mSelected]);
            }
        });
        dialog.show(getSupportFragmentManager());
    }
}
