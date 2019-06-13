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
import com.spikeboost.metric.UUIDs;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static android.content.Context.BLUETOOTH_SERVICE;
import static com.spikeboost.MainActivity.getMainContext;
import static com.spikeboost.common.ModuleStorage.getModuleStorage;
import static com.spikeboost.metric.UUIDs.CUSTOM_SERVICE_AUTH_CHARACTERISTIC_STRING;
import static com.spikeboost.metric.UUIDs.HEART_RATE_MEASUREMENT_CHARACTERISTIC_STRING;

public class DeviceConnector  extends ReactContextBaseJavaModule {

    // Bluetooth section
    private BluetoothGatt bluetoothGatt;
    private AppBluetoothGattCallback miBandGattCallBack;
    private static final String MI_BAND_3_NAME = "Mi Band 3";

    // Miscellaneous
    private ArrayList<BluetoothDevice> deviceArrayList;
    private HeartBeatMeasurer heartBeatMeasurer;

    // Android settings section
    private SharedPreferences sharedPreferences;
    private String sharedPreferencesAppName = "MiBandConnectPreferences";
    private String sharedPreferencesDeviceMac = "lastMiBandConnectedDeviceMac";

    private BluetoothDevice miBand;

    DeviceConnector(ReactApplicationContext reactContext) {
        super(reactContext);
        sharedPreferences = getMainContext()
                .getSharedPreferences(sharedPreferencesAppName, Context.MODE_PRIVATE);
        heartBeatMeasurer = getModuleStorage().getHeartBeatMeasurerPackage().getHeartBeatMeasurer();
        miBandGattCallBack = new AppBluetoothGattCallback(sharedPreferences, heartBeatMeasurer);
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
                        miBand = result.getDevice();
                        bluetoothAdapter.getBluetoothLeScanner().stopScan(this);
                        searchProgress.dismiss();
                        connectDevice(successCallback);
                    }
                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
            }
        };

        String lastMiBandConnectedDeviceMac = sharedPreferences.getString(sharedPreferencesDeviceMac, null);
        if (lastMiBandConnectedDeviceMac != null) {
            miBand = bluetoothAdapter.getRemoteDevice(lastMiBandConnectedDeviceMac);
            bluetoothGatt = miBand.connectGatt(mainContext, true, miBandGattCallBack);
            getDeviceBondLevel(successCallback);
            getModuleStorage().getHeartBeatMeasurerPackage().getHeartBeatMeasurer().updateBluetoothConfig(bluetoothGatt);
            miBandGattCallBack.updateBluetoothGatt(bluetoothGatt);
            searchProgress.dismiss();
        } else {
            bluetoothAdapter.getBluetoothLeScanner().startScan(leDeviceScanCallback);
        }
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
        miBandGattCallBack.updateBluetoothGatt(bluetoothGatt);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sharedPreferencesDeviceMac, miBand.getAddress());
        editor.apply();

        getDeviceBondLevel(successCallback);
    }

    @ReactMethod
    private void getDeviceBondLevel(Callback successCallback){
        successCallback.invoke(null, bluetoothGatt.getDevice().getBondState());
    }

    @Override
    public String getName() {
        return "DeviceConnector";
    }

}
