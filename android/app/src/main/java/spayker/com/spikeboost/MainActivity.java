package spayker.com.spikeboost;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

//import com.zhaoxiaodan.miband.ActionCallback;
//import com.zhaoxiaodan.miband.MiBand;
//import com.zhaoxiaodan.miband.listeners.RealtimeStepsNotifyListener;
//import com.zhaoxiaodan.miband.model.UserInfo;

import io.reactivex.disposables.Disposable;
import zhaoxiaodan.miband.ActionCallback;
import zhaoxiaodan.miband.MiBand;
import zhaoxiaodan.miband.model.UserInfo;

import static java.lang.Thread.sleep;
import static zhaoxiaodan.miband.model.VibrationMode.VIBRATION_WITHOUT_LED;

public class MainActivity extends AppCompatActivity {

    private String targetBandDeviceMac = "F7:E2:C8:FF:9A:B8";
    private String targetBandDeviceName = "";

    //private RxBleDevice device;
    //private BluetoothDevice device;

    private MiBand miband;

    private ScanCallback scanCallback;

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        miband = new MiBand(this);

        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                BluetoothDevice device = result.getDevice();


                if(device.getAddress().equalsIgnoreCase(targetBandDeviceMac)){
                    stopScan();
                    miband.connect(device, new ActionCallback() {

                        @Override
                        public void onSuccess(Object data) {
                            Log.d(TAG,"connect success");
                            //UserInfo userInfo = new UserInfo(35649876, 1, 30, 180, 55, "alex", 0);
                            //miband.setUserInfo(userInfo);

                            //miband.enableSensorDataNotify();
                            //miband.enableRealtimeStepsNotify();

                            miband.startVibration(VIBRATION_WITHOUT_LED);

                            miband.readRssi(new ActionCallback() {
                                @Override
                                public void onSuccess(Object data) {
                                    Log.d(TAG,"data: " + data);
                                }

                                @Override
                                public void onFail(int errorCode, String msg) {
                                    Log.d(TAG,"errorCode: " + errorCode);
                                    Log.d(TAG,"msg: " + msg);
                                }
                            });

                            runHeartBeatProcessing();

                        }

                        @Override
                        public void onFail(int errorCode, String msg) {
                            Log.d(TAG,"connect fail, code:"+errorCode+",mgs:"+msg);
                        }
                    });
                }


            }
        };
        MiBand.startScan(scanCallback);
    }

    private void runHeartBeatProcessing() {

//        try {
//            sleep(30000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        miband.setHeartRateScanListener(heartRate -> {
            Log.d(TAG, "heart rate: "+ heartRate);
            Log.d(TAG, "heart rate: "+ heartRate);
            Log.d(TAG, "heart rate: "+ heartRate);
        });

        //miband.startHeartRateScan();


    }

    private void disposeScanning(Disposable scanSubscription) {
        scanSubscription.dispose();
    }

    private void disposeConnection(Disposable connectionSubscription){
        connectionSubscription.dispose();

    }

    private void stopScan(){
        MiBand.stopScan(scanCallback);
    }

}
