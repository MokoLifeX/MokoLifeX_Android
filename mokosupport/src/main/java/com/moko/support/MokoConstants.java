package com.moko.support;

public class MokoConstants {
    // header
    public static final int HEADER_GET_DEVICE_INFO = 4001;
    public static final int HEADER_SET_MQTT_INFO = 4002;
    public static final int HEADER_SET_MQTT_SSL = 4003;
    public static final int HEADER_SET_TOPIC = 4004;
    public static final int HEADER_SET_SWITCH_STATUS = 4005;
    public static final int HEADER_SET_WIFI_INFO = 4006;
    public static final int HEADER_SET_DEVICE_ID = 4007;
    public static final int HEADER_SET_NTP_URL = 4008;
    public static final int HEADER_SET_TIMEZONE = 4009;
    public static final int HEADER_SET_TIMEZONE_PRO = 4010;
    public static final int HEADER_SET_CHANNEL_DOMAIN = 4011;
    public static final int HEADER_GET_TIMEZONE_PRO = 4012;
    public static final int HEADER_GET_CHANNEL_DOMAIN = 4013;
    // response
    public static final int RESPONSE_SUCCESS = 0;
    public static final int RESPONSE_FAILED_LENGTH = 1;
    public static final int RESPONSE_FAILED_DATA_FORMAT = 2;
    public static final int RESPONSE_FAILED_MQTT_WIFI = 3;
    // conn status
    public static final int CONN_STATUS_SUCCESS = 0;
    public static final int CONN_STATUS_FAILED = 1;
    public static final int CONN_STATUS_TIMEOUT = 2;
    public static final int CONN_STATUS_CLOSED = 3;
    // mqtt conn status
    public static final int MQTT_CONN_STATUS_LOST = 0;
    public static final int MQTT_CONN_STATUS_SUCCESS = 1;
    public static final int MQTT_CONN_STATUS_FAILED = 2;
    // mqtt state
    public static final int MQTT_STATE_SUCCESS = 1;
    public static final int MQTT_STATE_FAILED = 0;
    // action
    public static final String ACTION_AP_CONNECTION = "com.moko.lifex.action.ACTION_AP_CONNECTION";
    public static final String ACTION_AP_SET_DATA_RESPONSE = "com.moko.lifex.action.ACTION_AP_SET_DATA_RESPONSE";
    public static final String ACTION_MQTT_CONNECTION = "com.moko.lifex.action.ACTION_MQTT_CONNECTION";
    public static final String ACTION_MQTT_RECEIVE = "com.moko.lifex.action.ACTION_MQTT_RECEIVE";
    public static final String ACTION_MQTT_SUBSCRIBE = "com.moko.lifex.action.ACTION_MQTT_SUBSCRIBE";
    public static final String ACTION_MQTT_PUBLISH = "com.moko.lifex.action.ACTION_MQTT_PUBLISH";
    public static final String ACTION_MQTT_UNSUBSCRIBE = "com.moko.lifex.action.ACTION_MQTT_UNSUBSCRIBE";
    // extra
    public static final String EXTRA_AP_CONNECTION = "EXTRA_AP_CONNECTION";
    public static final String EXTRA_AP_SET_DATA_RESPONSE = "EXTRA_AP_SET_DATA_RESPONSE";
    public static final String EXTRA_MQTT_CONNECTION_STATE = "EXTRA_MQTT_CONNECTION_STATE";
    public static final String EXTRA_MQTT_RECEIVE_TOPIC = "EXTRA_MQTT_RECEIVE_TOPIC";
    public static final String EXTRA_MQTT_RECEIVE_MESSAGE = "EXTRA_MQTT_RECEIVE_MESSAGE";
    public static final String EXTRA_MQTT_STATE = "EXTRA_MQTT_STATE";

}
