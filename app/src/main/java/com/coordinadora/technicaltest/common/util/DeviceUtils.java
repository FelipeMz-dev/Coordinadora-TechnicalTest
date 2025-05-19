package com.coordinadora.technicaltest.common.util;

import android.provider.Settings;

import com.coordinadora.technicaltest.App;

public class DeviceUtils {
    public static String getSerial() {
        return Settings.Secure.getString(
                App.getAppContext().getContentResolver(),
                Settings.Secure.ANDROID_ID
        );
    }
}