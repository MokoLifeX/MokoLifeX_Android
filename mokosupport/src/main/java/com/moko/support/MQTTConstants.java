package com.moko.support;

public class MQTTConstants {
//    // CONFIG
//    public static final int CONFIG_MSG_ID_RESET = 1001;
//    public static final int CONFIG_MSG_ID_OTA = 1002;
//    public static final int CONFIG_MSG_ID_DEVICE_INFO = 1003;
//    public static final int CONFIG_MSG_ID_UTC = 1004;
//    public static final int CONFIG_MSG_ID_INDICATOR_STATUS = 1005;
//    public static final int CONFIG_MSG_ID_NETWORK_REPORT_PERIOD = 1006;
//    public static final int CONFIG_MSG_ID_SCAN_CONFIG = 1007;
//    public static final int CONFIG_MSG_ID_DATA_REPORT_TIMEOUT = 1008;
//    public static final int CONFIG_MSG_ID_UPLOAD_DATA_OPTION = 1009;
//    public static final int CONFIG_MSG_ID_DUPLICATE_DATA_FILTER = 1010;
//    public static final int CONFIG_MSG_ID_BEACON_TYPE_FILTER = 1011;
//    public static final int CONFIG_MSG_ID_FILTER_RELATION = 1012;
//    public static final int CONFIG_MSG_ID_FILTER_A = 1013;
//    public static final int CONFIG_MSG_ID_FILTER_B = 1014;
//    public static final int CONFIG_MSG_ID_CONN_TIMEOUT = 1016;
//    public static final int CONFIG_MSG_ID_BLE_SCAN_TIMEOUT = 1017;
//    public static final int CONFIG_MSG_ID_REBOOT = 1018;
//    // READ
//    public static final int READ_MSG_ID_BLE_PASSWORD = 2000;
//    public static final int READ_MSG_ID_DEVICE_INFO = 2003;
//    public static final int READ_MSG_ID_UTC = 2004;
//    public static final int READ_MSG_ID_INDICATOR_STATUS = 2005;
//    public static final int READ_MSG_ID_NETWORK_REPORT_PERIOD = 2006;
//    public static final int READ_MSG_ID_SCAN_CONFIG = 2007;
//    public static final int READ_MSG_ID_DATA_REPORT_TIMEOUT = 2008;
//    public static final int READ_MSG_ID_UPLOAD_DATA_OPTION = 2009;
//    public static final int READ_MSG_ID_DUPLICATE_DATA_FILTER = 2010;
//    public static final int READ_MSG_ID_BEACON_TYPE_FILTER = 2011;
//    public static final int READ_MSG_ID_FILTER_RELATION = 2012;
//    public static final int READ_MSG_ID_FILTER_A = 2013;
//    public static final int READ_MSG_ID_FILTER_B = 2014;
//    public static final int READ_MSG_ID_CONFIG_INFO = 2015;
//    public static final int READ_MSG_ID_CONN_TIMEOUT = 2016;
//    public static final int READ_MSG_ID_BLE_SCAN_TIMEOUT = 2017;
//    // NOTIFY
//    public static final int NOTIFY_MSG_ID_OTA_RESULT = 3001;
//    public static final int NOTIFY_MSG_ID_RESET_RESULT = 3002;
//    public static final int NOTIFY_MSG_ID_NETWORKING_STATUS = 3003;
//    public static final int NOTIFY_MSG_ID_BLE_SCAN_RESULT = 3004;

    // device_to_app
    public static final int MSG_ID_D_2_A_DEVICE_INFO = 1002;
    public static final int MSG_ID_D_2_A_TIMER_INFO = 1003;
    public static final int MSG_ID_D_2_A_OTA_INFO = 1004;
    public static final int MSG_ID_D_2_A_POWER_STATUS = 1008;
    public static final int MSG_ID_D_2_A_LED_STATUS_COLOR = 1009;
    public static final int MSG_ID_D_2_A_LOAD_INSERTION = 1011;
    public static final int MSG_ID_D_2_A_REPORT_INTERVAL = 1012;
    public static final int MSG_ID_D_2_A_ENERGY_STORAGE_PARAMS = 1013;
    public static final int MSG_ID_D_2_A_ENERGY_HISTORY_MONTH = 1014;
    public static final int MSG_ID_D_2_A_ENERGY_HISTORY_TODAY = 1015;
    public static final int MSG_ID_D_2_A_ENERGY_TOTAL = 1017;
    public static final int MSG_ID_D_2_A_ENERGY_CURRENT = 1018;
    public static final int MSG_ID_D_2_A_ENERGY_REPORT_INTERVAL = 1019;
    // app_to_device
    public static final int MSG_ID_A_2_D_SWITCH_STATE = 2001;
    public static final int MSG_ID_A_2_D_SET_TIMER = 2002;
    public static final int MSG_ID_A_2_D_RESET = 2003;
    public static final int MSG_ID_A_2_D_SET_OTA = 2004;
    public static final int MSG_ID_A_2_D_DEVICE_INFO = 2005;
    public static final int MSG_ID_A_2_D_SET_POWER_STATUS = 2006;
    public static final int MSG_ID_A_2_D_GET_POWER_STATUS = 2007;
    public static final int MSG_ID_A_2_D_GET_LED_STATUS_COLOR = 2008;
    public static final int MSG_ID_A_2_D_SET_LED_STATUS_COLOR = 2010;
    public static final int MSG_ID_A_2_D_GET_OVERLOAD = 2012;
    public static final int MSG_ID_A_2_D_SET_OVERLOAD_VALUE = 2013;
    public static final int MSG_ID_A_2_D_GET_REPORT_INTERVAL = 2014;
    public static final int MSG_ID_A_2_D_SET_REPORT_INTERVAL = 2015;
    public static final int MSG_ID_A_2_D_GET_ENERGY_STORAGE_PARAMS = 2016;
    public static final int MSG_ID_A_2_D_SET_ENERGY_STORAGE_PARAMS = 2017;
    public static final int MSG_ID_A_2_D_GET_ENERGY_HISTORY_MONTH = 2018;
    public static final int MSG_ID_A_2_D_GET_ENERGY_HISTORY_TODAY = 2019;
    public static final int MSG_ID_A_2_D_GET_ENERGY_TOTAL = 2021;
    public static final int MSG_ID_A_2_D_SET_SYSTEM_TIME = 2022;
    public static final int MSG_ID_A_2_D_SET_ENERGY_CLEAR = 2023;
    public static final int MSG_ID_A_2_D_SET_ENERGY_REPORT_INTERVAL = 2024;
    public static final int MSG_ID_A_2_D_GET_ENERGY_REPORT_INTERVAL = 2025;
    // READ
    public static final int READ_MSG_ID_DEVICE_INFO = 2005;
    public static final int READ_MSG_ID_POWER_STATUS = 2007;
    public static final int READ_MSG_ID_LED_STATUS_COLOR = 2008;
    public static final int READ_MSG_ID_OVERLOAD = 2012;
    public static final int READ_MSG_ID_POWER_REPORT_INTERVAL = 2014;
    public static final int READ_MSG_ID_ENERGY_REPORT_PARAMS = 2016;
    public static final int READ_MSG_ID_ENERGY_HISTORY_MONTH = 2018;
    public static final int READ_MSG_ID_ENERGY_HISTORY_TODAY = 2019;
    public static final int READ_MSG_ID_ENERGY_TOTAL = 2021;
    public static final int READ_MSG_ID_SWITCH_INFO = 2026;
    public static final int READ_MSG_ID_INDICATOR_STATUS = 2119;
    public static final int READ_MSG_ID_DEVICE_STATUS_REPORT_INTERVAL = 2101;
    public static final int READ_MSG_ID_CONNECT_TIMEOUT = 2121;
    public static final int READ_MSG_ID_LOAD_STATUS_NOTIFY = 2117;
    public static final int READ_MSG_ID_SETTINGS_FOR_DEVICE = 2123;
    public static final int READ_MSG_ID_SYSTEM_TIME_WITH_UTC = 2122;
    public static final int READ_MSG_ID_SYNC_TIME_FROM_NTP = 2115;
    public static final int READ_MSG_ID_OVERLOAD_PROTECTION = 2103;
    public static final int READ_MSG_ID_OVER_VOLTAGE_PROTECTION = 2107;
    public static final int READ_MSG_ID_OVER_CURRENT_PROTECTION = 2111;
    // CONFIG
    public static final int CONFIG_MSG_ID_SWITCH_STATE = 2001;
    public static final int CONFIG_MSG_ID_TIMER = 2002;
    public static final int CONFIG_MSG_ID_RESET = 2003;
    public static final int CONFIG_MSG_ID_OTA = 2004;
    public static final int CONFIG_MSG_ID_POWER_STATUS = 2006;
    public static final int CONFIG_MSG_ID_LED_STATUS_COLOR = 2010;
    public static final int CONFIG_MSG_ID_OVERLOAD_VALUE = 2013;
    public static final int CONFIG_MSG_ID_POWER_REPORT_INTERVAL = 2015;
    public static final int CONFIG_MSG_ID_ENERGY_REPORT_PARAMS = 2017;
    public static final int CONFIG_MSG_ID_SYSTEM_TIME = 2022;
    public static final int CONFIG_MSG_ID_ENERGY_CLEAR = 2023;
    public static final int CONFIG_MSG_ID_INDICATOR_STATUS = 2118;
    public static final int CONFIG_MSG_ID_DEVICE_STATUS_REPORT_INTERVAL = 2100;
    public static final int CONFIG_MSG_ID_CONNECT_TIMEOUT = 2120;
    public static final int CONFIG_MSG_ID_LOAD_STATUS_NOTIFY = 2116;
    public static final int CONFIG_MSG_ID_SYSTEM_TIME_WITH_UTC = 2124;
    public static final int CONFIG_MSG_ID_SYSTEM_TIME_WITH_UTC_PRO = 2125;
    public static final int CONFIG_MSG_ID_MQTT_SETTINGS = 2126;
    public static final int CONFIG_MSG_ID_MQTT_RECONNECT = 2127;
    public static final int CONFIG_MSG_ID_OTA_FIRMWARE = 2128;
    public static final int CONFIG_MSG_ID_OTA_ONE_WAY = 2129;
    public static final int CONFIG_MSG_ID_OTA_BOTH_WAY = 2130;
    public static final int CONFIG_MSG_ID_SYNC_TIME_FROM_NTP = 2114;
    public static final int CONFIG_MSG_ID_OVERLOAD_PROTECTION = 2102;
    public static final int CONFIG_MSG_ID_CLEAR_OVERLOAD_PROTECTION = 2104;
    public static final int CONFIG_MSG_ID_OVER_VOLTAGE_PROTECTION = 2106;
    public static final int CONFIG_MSG_ID_CLEAR_OVER_VOLTAGE_PROTECTION = 2108;
    public static final int CONFIG_MSG_ID_OVER_CURRENT_PROTECTION = 2110;
    public static final int CONFIG_MSG_ID_CLEAR_OVER_CURRENT_PROTECTION = 2112;
    // NOTIFY
    public static final int NOTIFY_MSG_ID_SWITCH_STATE = 1001;
    public static final int NOTIFY_MSG_ID_DEVICE_INFO = 1002;
    public static final int NOTIFY_MSG_ID_TIMER_INFO = 1003;
    public static final int NOTIFY_MSG_ID_OTA = 1004;
    public static final int NOTIFY_MSG_ID_OVERLOAD = 1005;
    public static final int NOTIFY_MSG_ID_POWER_INFO = 1006;
    public static final int NOTIFY_MSG_ID_POWER_STATUS = 1008;
    public static final int NOTIFY_MSG_ID_LED_STATUS_COLOR = 1009;
    public static final int NOTIFY_MSG_ID_LOAD_INSERTION = 1011;
    public static final int NOTIFY_MSG_ID_POWER_REPORT_INTERVAL = 1012;
    public static final int NOTIFY_MSG_ID_ENERGY_REPORT_PARAMS = 1013;
    public static final int NOTIFY_MSG_ID_ENERGY_HISTORY_MONTH = 1014;
    public static final int NOTIFY_MSG_ID_ENERGY_HISTORY_TODAY = 1015;
    public static final int NOTIFY_MSG_ID_ENERGY_TOTAL = 1017;
    public static final int NOTIFY_MSG_ID_ENERGY_CURRENT = 1018;
    public static final int NOTIFY_MSG_ID_ENERGY_HISTORY_MONTH_NEW = 1020;
    public static final int NOTIFY_MSG_ID_INDICATOR_STATUS = 1119;
    public static final int NOTIFY_MSG_ID_DEVICE_STATUS_REPORT_INTERVAL = 1101;
    public static final int NOTIFY_MSG_ID_CONNECT_TIMEOUT = 1121;
    public static final int NOTIFY_MSG_ID_LOAD_STATUS_NOTIFY = 1117;
    public static final int NOTIFY_MSG_ID_SETTINGS_FOR_DEVICE = 1123;
    public static final int NOTIFY_MSG_ID_SYSTEM_TIME_WITH_UTC = 1122;
    public static final int NOTIFY_MSG_ID_MQTT_SETTINGS = 1124;
    public static final int NOTIFY_MSG_ID_MQTT_RECONNECT = 1125;
    public static final int NOTIFY_MSG_ID_OTA_RESULT = 1126;
    public static final int NOTIFY_MSG_ID_SYNC_TIME_FROM_NTP = 1115;
    public static final int NOTIFY_MSG_ID_OVERLOAD_PROTECTION = 1103;
    public static final int NOTIFY_MSG_ID_OVERLOAD_OCCUR = 1105;
    public static final int NOTIFY_MSG_ID_OVER_VOLTAGE_PROTECTION = 1107;
    public static final int NOTIFY_MSG_ID_OVER_VOLTAGE_OCCUR = 1109;
    public static final int NOTIFY_MSG_ID_OVER_CURRENT_PROTECTION = 1111;
    public static final int NOTIFY_MSG_ID_OVER_CURRENT_OCCUR = 1113;

}
