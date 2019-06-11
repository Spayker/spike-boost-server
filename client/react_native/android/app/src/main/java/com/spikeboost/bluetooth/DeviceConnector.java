package com.spikeboost.bluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.spikeboost.metric.HeartBeatMeasurer;
import com.spikeboost.metric.HeartBeatMeasurerPackage;
import com.spikeboost.metric.UUIDs;

import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.ArrayUtils;

import static android.content.Context.BLUETOOTH_SERVICE;
import static com.spikeboost.MainActivity.getMainContext;
import static com.spikeboost.common.ModuleStorage.getModuleStorage;
import static com.spikeboost.metric.UUIDs.CUSTOM_SERVICE_AUTH_CHARACTERISTIC_STRING;
import static com.spikeboost.metric.UUIDs.HEART_RATE_MEASUREMENT_CHARACTERISTIC_STRING;

public class DeviceConnector  extends ReactContextBaseJavaModule {

    // Bluetooth section
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCallback miBandGattCallBack;
    private static final String MI_BAND_3_NAME = "Mi Band 3";

    // Miscellaneous
    private ArrayList<BluetoothDevice> deviceArrayList;
    private HeartBeatMeasurer heartBeatMeasurer;

    // Android settings section
    private SharedPreferences sharedPreferences;
    private String sharedPreferencesAppName = "MiBandConnectPreferences";

    private static String foundDeviceName;
    private BluetoothDevice miBand;

    DeviceConnector(ReactApplicationContext reactContext) {
        super(reactContext);
        sharedPreferences = getMainContext()
                .getSharedPreferences(sharedPreferencesAppName, Context.MODE_PRIVATE);
        heartBeatMeasurer = getModuleStorage().getHeartBeatMeasurerPackage().getHeartBeatMeasurer();
        initMiBandGattCallBack();
    }

    private void initMiBandGattCallBack() {
        miBandGattCallBack = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                switch (newState) {
                    case BluetoothGatt.STATE_DISCONNECTED:
                        Log.d("Info", "Device disconnected");

                        break;
                    case BluetoothGatt.STATE_CONNECTED: {
                        Log.d("Info", "Connected with device");
                        Log.d("Info", "Discovering services");
                        gatt.discoverServices();
                    }
                    break;
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (!sharedPreferences.getBoolean("isAuthenticated", false)) {
                    authoriseMiBand();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isAuthenticated", true);
                    editor.apply();
                } else {
                    Log.i("Device", "Already authenticated");
                }
            }

            /*------Methods to send requests to the device------*/
            private void authoriseMiBand() {
                BluetoothGattService service = bluetoothGatt.getService(UUIDs.CUSTOM_SERVICE_FEE1);

                BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUIDs.CUSTOM_SERVICE_AUTH_CHARACTERISTIC);
                bluetoothGatt.setCharacteristicNotification(characteristic, true);
                for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                    if (descriptor.getUuid().equals(UUIDs.CUSTOM_SERVICE_AUTH_DESCRIPTOR)) {
                        Log.d("INFO", "Found NOTIFICATION BluetoothGattDescriptor: " + descriptor.getUuid().toString());
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    }
                }

                characteristic.setValue(new byte[]{0x01, 0x8, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x40, 0x41, 0x42, 0x43, 0x44, 0x45});
                bluetoothGatt.writeCharacteristic(characteristic);
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                heartBeatMeasurer = getModuleStorage().getHeartBeatMeasurerPackage().getHeartBeatMeasurer();
                switch (characteristic.getUuid().toString()) {
                    case CUSTOM_SERVICE_AUTH_CHARACTERISTIC_STRING:
                        executeAuthorisationSequence(characteristic);
                        break;
                    case HEART_RATE_MEASUREMENT_CHARACTERISTIC_STRING:
                        heartBeatMeasurer.handleHeartRateData(characteristic);
                        break;
                }
            }

            private void executeAuthorisationSequence(BluetoothGattCharacteristic characteristic) {
                byte[] value = characteristic.getValue();
                if (value[0] == 0x10 && value[1] == 0x01 && value[2] == 0x01) {
                    characteristic.setValue(new byte[]{0x02, 0x8});
                    bluetoothGatt.writeCharacteristic(characteristic);
                } else if (value[0] == 0x10 && value[1] == 0x02 && value[2] == 0x01) {
                    try {
                        byte[] tmpValue = Arrays.copyOfRange(value, 3, 19);
                        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

                        SecretKeySpec key = new SecretKeySpec(new byte[]{0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x40, 0x41, 0x42, 0x43, 0x44, 0x45}, "AES");

                        cipher.init(Cipher.ENCRYPT_MODE, key);
                        byte[] bytes = cipher.doFinal(tmpValue);


                        byte[] rq = ArrayUtils.addAll(new byte[]{0x03, 0x8}, bytes);
                        characteristic.setValue(rq);
                        bluetoothGatt.writeCharacteristic(characteristic);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                Log.d("Descriptor", descriptor.getUuid().toString() + " Read");
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                Log.d("Descriptor", descriptor.getUuid().toString() + " Written");
            }
        };
    }

    @ReactMethod
    public void enableBTAndDiscover(Callback successCallback) {
        Context mainContext = getMainContext();
        final BluetoothAdapter bluetoothAdapter = ((BluetoothManager) mainContext.getSystemService(BLUETOOTH_SERVICE)).getAdapter();

        final ProgressDialog searchProgress = new ProgressDialog(mainContext);
        searchProgress.setIndeterminate(true);
        searchProgress.setTitle("MiBand Bluetooth Scanner");
        searchProgress.setMessage("Searching...");
        searchProgress.setCancelable(false);
        searchProgress.show();

        deviceArrayList = new ArrayList<>();

        if (!bluetoothAdapter.isEnabled()) {
            ((AppCompatActivity)mainContext).startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
        }

        final ScanCallback leDeviceScanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                String deviceName = result.getDevice().getName();
                if (deviceName != null && deviceName.equalsIgnoreCase(MI_BAND_3_NAME)){
                    Log.d("TAG", "Device found" + " " + result.getDevice().getAddress() + " " + deviceName);
                    if (!deviceArrayList.contains(result.getDevice())) {
                        deviceArrayList.add(result.getDevice());
                        foundDeviceName = result.getDevice().getName();
                        miBand = result.getDevice();
                        bluetoothAdapter.getBluetoothLeScanner().stopScan(this);
                        searchProgress.dismiss();
                        successCallback.invoke(null, foundDeviceName);
                    }
                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
            }
        };

        bluetoothAdapter.getBluetoothLeScanner().startScan(leDeviceScanCallback);
        new Handler().postDelayed(() -> {
            bluetoothAdapter.getBluetoothLeScanner().stopScan(leDeviceScanCallback);
            searchProgress.dismiss();
        }, 120000);
    }

    @ReactMethod
    private void connectDevice(Callback successCallback) {
        if (miBand.getBondState() == BluetoothDevice.BOND_NONE) {
            miBand.createBond();
            Log.d("Bond", "Created with Device");
        }
        Context mainContext = getMainContext();
        bluetoothGatt = miBand.connectGatt(mainContext, true, miBandGattCallBack);
        getModuleStorage().getHeartBeatMeasurerPackage().getHeartBeatMeasurer().updateBluetoothConfig(bluetoothGatt);
        successCallback.invoke(null, bluetoothGatt.getDevice().getBondState());
    }

    private void sendDataToHeartMeasureModule(){

    }



    @Override
    public String getName() {
        return "DeviceConnector";
    }

}
