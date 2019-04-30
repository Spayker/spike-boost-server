package spayker.com.spikeboost;

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
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static java.lang.Thread.sleep;

/**
* Main activity class for testing of main SpikeBoost concepts.
 * Contains some basic logic for connecting to target device by bluetooth and
 * further data processing from device's sensors
*
* @author  spayker
* @version 1.0
* @since   30/04/19
*/
public class MainActivity extends AppCompatActivity {

    // Bluetooth section
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattService variableService;
    private BluetoothGattCallback miBandGattCallBack;
    private BluetoothGattCharacteristic heartRateControlPointCharacteristic;
    private static final String MI_BAND_3_NAME = "Mi Band 3";

    // UI components section
    private ListView deviceListView;
    private TextView heartRate;

    // Android settings section
    private SharedPreferences sharedPreferences;

    // Miscellaneous
    private ArrayAdapter<?> genericListAdapter;
    private ArrayList<BluetoothDevice> deviceArrayList;
    private final Object object = new Object();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        heartRate = findViewById(R.id.heartRate);
        initialiseViewsAndComponents();
        enableBTAndDiscover();
    }

    @Override
    protected void onDestroy() {
        bluetoothGatt.disconnect();
        super.onDestroy();
    }

    private void handleHeartRateData(final BluetoothGattCharacteristic characteristic) {

        Log.e("Heart", String.valueOf(characteristic.getValue()[1]));
        runOnUiThread(() -> {
            BluetoothGattCharacteristic heartRateMeasurementCharacteristic
                    = variableService.getCharacteristic(UUID.fromString(UUIDs.HEART_RATE_MEASUREMENT_CHARACTERISTIC_STRING));

            bluetoothGatt.readCharacteristic(heartRateMeasurementCharacteristic);
            synchronized (object) {
                try {
                    object.wait(250);
                    heartRateControlPointCharacteristic.setValue(new byte[]{0x16});
                    bluetoothGatt.writeCharacteristic(heartRateControlPointCharacteristic);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            heartRate.setText(String.valueOf(characteristic.getValue()[1]));
        });
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

    private void getHeartRate() {
        variableService = bluetoothGatt.getService(UUIDs.HEART_RATE_SERVICE);

        BluetoothGattCharacteristic heartRateCharacteristic = variableService.getCharacteristic(UUIDs.HEART_RATE_MEASUREMENT_CHARACTERISTIC);
        BluetoothGattDescriptor heartRateDescriptor = heartRateCharacteristic.getDescriptor(UUIDs.HEART_RATE_MEASURMENT_DESCRIPTOR);

        bluetoothGatt.setCharacteristicNotification(heartRateCharacteristic, true);
        heartRateDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        bluetoothGatt.writeDescriptor(heartRateDescriptor);

        heartRateControlPointCharacteristic = variableService
                .getCharacteristic(UUIDs.HEART_RATE_CONTROL_POINT_CHARACTERISTIC);
        pause();

        BluetoothGattService variableSensorService = bluetoothGatt.getService(UUIDs.SENSOR_SERVICE);
        BluetoothGattCharacteristic heartCharacteristicSensor
                = variableSensorService.getCharacteristic(UUIDs.CHARACTER_SENSOR_CHARACTERISTIC);
        pause();

        heartRateControlPointCharacteristic.setValue(new byte[]{0x15, 0x02, 0x00});
        bluetoothGatt.writeCharacteristic(heartRateControlPointCharacteristic);
        pause();


        heartRateControlPointCharacteristic.setValue(new byte[]{0x15, 0x01, 0x00});
        bluetoothGatt.writeCharacteristic(heartRateControlPointCharacteristic);
        pause();

        heartCharacteristicSensor.setValue(new byte[]{0x01, 0x03, 0x19});
        bluetoothGatt.writeCharacteristic(heartRateControlPointCharacteristic);
        pause();

        heartRateControlPointCharacteristic.setValue(new byte[]{0x01, 0x00});
        bluetoothGatt.writeCharacteristic(heartRateControlPointCharacteristic);
        pause();

        heartRateControlPointCharacteristic.setValue(new byte[]{0x15, 0x01, 0x01});
        bluetoothGatt.writeCharacteristic(heartRateControlPointCharacteristic);

        heartCharacteristicSensor.setValue(new byte[]{0x2});
        bluetoothGatt.writeCharacteristic(heartRateControlPointCharacteristic);

    }

    private void pause(){
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void enableBTAndDiscover() {
        final BluetoothAdapter bluetoothAdapter = ((BluetoothManager) getSystemService(BLUETOOTH_SERVICE)).getAdapter();

        final ProgressDialog searchProgress = new ProgressDialog(MainActivity.this);
        searchProgress.setIndeterminate(true);
        searchProgress.setTitle("BlueTooth LE Device");
        searchProgress.setMessage("Searching...");
        searchProgress.setCancelable(false);
        searchProgress.show();

        deviceArrayList = new ArrayList<>();
        genericListAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, deviceArrayList);
        deviceListView.setAdapter(genericListAdapter);

        if (bluetoothAdapter == null) {
            Toast.makeText(MainActivity.this, "Bluetooth not supported on this device", Toast.LENGTH_LONG).show();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
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
    }

    private void connectDevice(BluetoothDevice miBand) {
        if (miBand.getBondState() == BluetoothDevice.BOND_NONE) {
            miBand.createBond();
            Log.d("Bond", "Created with Device");
        }
        bluetoothGatt = miBand.connectGatt(getApplicationContext(), true, miBandGattCallBack);
    }

    private void initialiseViewsAndComponents() {
        deviceListView = findViewById(R.id.deviceListView);
        Button getHRDetails = findViewById(R.id.getHRDetails);

        sharedPreferences = getSharedPreferences("MiBandConnectPreferences", Context.MODE_PRIVATE);
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

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                switch (characteristic.getUuid().toString()) {
                    case UUIDs.CUSTOM_SERVICE_AUTH_CHARACTERISTIC_STRING:
                        executeAuthorisationSequence(characteristic);
                        break;
                    case UUIDs.HEART_RATE_MEASUREMENT_CHARACTERISTIC_STRING:
                        handleHeartRateData(characteristic);
                        break;
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

        deviceListView.setOnItemClickListener(
                (parent, view, position, id) -> connectDevice((BluetoothDevice) parent.getItemAtPosition(position)));

        getHRDetails.setOnClickListener(v -> getHeartRate());
    }

}
