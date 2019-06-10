package com.spikeboost.bluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.spikeboost.MainActivity;

import java.util.ArrayList;

import static android.content.Context.BLUETOOTH_SERVICE;
//import static com.spikeboost.MainActivity.getMainContext;

public class DeviceConnector  extends ReactContextBaseJavaModule {

    // Bluetooth section
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattService variableService;
    private BluetoothGattCallback miBandGattCallBack;
    private BluetoothGattCharacteristic heartRateControlPointCharacteristic;
    private static final String MI_BAND_3_NAME = "Mi Band 3";

    // UI components section
    private ListView deviceListView;

    // Miscellaneous
    private ArrayAdapter<?> genericListAdapter;
    private ArrayList<BluetoothDevice> deviceArrayList;



    DeviceConnector(ReactApplicationContext reactContext) {
        super(reactContext);
    }


    private String enableBTAndDiscover() {
        Context mainContext = null;//getMainContext();
        final BluetoothAdapter bluetoothAdapter = ((BluetoothManager) mainContext.getSystemService(BLUETOOTH_SERVICE)).getAdapter();

        final ProgressDialog searchProgress = new ProgressDialog(mainContext);
        searchProgress.setIndeterminate(true);
        searchProgress.setTitle("BlueTooth LE Device");
        searchProgress.setMessage("Searching...");
        searchProgress.setCancelable(false);
        searchProgress.show();

        deviceArrayList = new ArrayList<>();
        genericListAdapter = new ArrayAdapter<>(mainContext, android.R.layout.simple_list_item_1, deviceArrayList);
        deviceListView.setAdapter(genericListAdapter);

        if (bluetoothAdapter == null) {
            return "Bluetooth not supported on this device";
        }

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
                        genericListAdapter.notifyDataSetChanged();
                        bluetoothAdapter.getBluetoothLeScanner().stopScan(this);
                        searchProgress.dismiss();
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

        return "Device has been connected";
    }

    @Override
    public String getName() {
        return "DeviceConnector";
    }


}
