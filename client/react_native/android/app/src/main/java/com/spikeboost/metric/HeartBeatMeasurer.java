package com.spikeboost.metric;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import javax.annotation.Nonnull;

public class HeartBeatMeasurer extends ReactContextBaseJavaModule {


    HeartBeatMeasurer(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Nonnull
    @Override
    public String getName() {
        return "HeartBeatMeasurer";
    }
}
