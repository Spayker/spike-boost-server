package zhaoxiaodan.miband.model;

import java.util.UUID;

public class Profile {

    // MiBand Main Service
    public static final UUID UUID_SERVICE_MILI = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");

    // Immediate Alert Service
    public static final UUID UUID_SERVICE_VIBRATION = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");

    // Heart Rate Service
    public static final UUID UUID_SERVICE_HEART_RATE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");

    // Heart Rate Measurement Characteristic
    public static final UUID UUID_NOTIFICATION_HEART_RATE = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");

    // Characteristic Configuration Descriptor
    public static final UUID UUID_DESCRIPTOR_UPDATE_NOTIFICATION = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    // Heart Rate Control Point Characteristic
    public static final UUID UUID_CHAR_HEART_RATE = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");


    // Service Info Characteristic (does not exist in mi band 3)
    public static final UUID UUID_CHAR_DEVICE_INFO = UUID.fromString("0000ff01-0000-1000-8000-00805f9b34fb");

    // Device Name Characteristic
    public static final UUID UUID_CHAR_DEVICE_NAME = UUID.fromString("0000ff02-0000-1000-8000-00805f9b34fb");

    // Current time Characteristic (does not exist in mi band 3)
    public static final UUID UUID_CHAR_NOTIFICATION = UUID.fromString("00002a2b-0000-1000-8000-00805f9b34fb");

    // User Info Characteristic (does not exist in mi band 3)
    public static final UUID UUID_CHAR_USER_INFO = UUID.fromString("0000ff04-0000-1000-8000-00805f9b34fb");

    // Control Point Characteristic (does not exist in mi band 3)
    public static final UUID UUID_CHAR_CONTROL_POINT = UUID.fromString("0000ff05-0000-1000-8000-00805f9b34fb");

    // Current Time Characteristic
    public static final UUID UUID_CHAR_REALTIME_STEPS = UUID.fromString("00002a2b-0000-1000-8000-00805f9b34fb");

    // Activity data Characteristic (does not exist in mi band 3)
    public static final UUID UUID_CHAR_ACTIVITY = UUID.fromString("0000ff07-0000-1000-8000-00805f9b34fb");

    // Firmware data Characteristic (does not exist in mi band 3)
    public static final UUID UUID_CHAR_FIRMWARE_DATA = UUID.fromString("0000ff08-0000-1000-8000-00805f9b34fb");

    // LE params Characteristic (does not exist in mi band 3)
    public static final UUID UUID_CHAR_LE_PARAMS = UUID.fromString("0000ff09-0000-1000-8000-00805f9b34fb");

    // Date&time Characteristic (does not exist in mi band 3)
    public static final UUID UUID_CHAR_DATA_TIME = UUID.fromString("0000ff0a-0000-1000-8000-00805f9b34fb");

    // Statistics Characteristic (does not exist in mi band 3)
    public static final UUID UUID_CHAR_STATISTICS = UUID.fromString("0000ff0b-0000-1000-8000-00805f9b34fb");

    // Battery Level Characteristic (does not exist in mi band 3)
    public static final UUID UUID_CHAR_BATTERY = UUID.fromString("0000ff0c-0000-1000-8000-00805f9b34fb");

    // Test Characteristic (does not exist in mi band 3)
    public static final UUID UUID_CHAR_TEST = UUID.fromString("0000ff0d-0000-1000-8000-00805f9b34fb");

    // Sensor Data Characteristic ()
    public static final UUID UUID_CHAR_SENSOR_DATA = UUID.fromString("00000002-0000-3512-2118-0009af100700");

    // Pair device Characteristic (does not exist in mi band 3)
    public static final UUID UUID_CHAR_PAIR = UUID.fromString("0000ff0f-0000-1000-8000-00805f9b34fb");

    // Alert Level Characteristic
    public static final UUID UUID_CHAR_VIBRATION = UUID.fromString("00002a06-0000-1000-8000-00805f9b34fb");

    //// Service Unknown Section
    // Generic access service
    public static final UUID UUID_SERVICE_UNKNOWN1 = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");

    // generic attribute
    public static final UUID UUID_SERVICE_UNKNOWN2 = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");

    // unknown service
    public static final UUID UUID_SERVICE_UNKNOWN4 = UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb");

    // unknown service
    public static final UUID UUID_SERVICE_UNKNOWN5 = UUID.fromString("0000fee7-0000-1000-8000-00805f9b34fb");
}
