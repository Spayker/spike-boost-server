package com.spikeboost.common;

import com.facebook.react.shell.MainReactPackage;
import com.spikeboost.bluetooth.DeviceConnectorPackage;
import com.spikeboost.metric.HeartBeatMeasurerPackage;

public class ModuleStorage {

    private static ModuleStorage instance;

    private MainReactPackage mainReactPackage;

    private DeviceConnectorPackage deviceConnectorPackage;

    private HeartBeatMeasurerPackage heartBeatMeasurerPackage;

    private ModuleStorage(){
        mainReactPackage = new MainReactPackage();
        deviceConnectorPackage = new DeviceConnectorPackage();
        heartBeatMeasurerPackage = new HeartBeatMeasurerPackage();
    }

    public static ModuleStorage getModuleStorage(){
        if(instance == null){
            instance = new ModuleStorage();
        }
        return instance;
    }

    public MainReactPackage getMainReactPackage() {
        return mainReactPackage;
    }

    public DeviceConnectorPackage getDeviceConnectorPackage() {
        return deviceConnectorPackage;
    }

    public HeartBeatMeasurerPackage getHeartBeatMeasurerPackage() {
        return heartBeatMeasurerPackage;
    }
}
