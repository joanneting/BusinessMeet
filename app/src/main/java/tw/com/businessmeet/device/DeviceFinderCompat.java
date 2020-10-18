package tw.com.businessmeet.device;

import android.app.Activity;
import android.os.Build;

import tw.com.businessmeet.background.NotificationService;
import tw.com.businessmeet.device.beacon.finder.BeaconFinder;
import tw.com.businessmeet.device.bluetooth.finder.BackgroundBluetoothDeviceFinder;
import tw.com.businessmeet.device.bluetooth.finder.ForegroundBluetoothDeviceFinder;

public class DeviceFinderCompat {

    public static DeviceFinder getForegroundFinder(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new BeaconFinder(activity);
        } else {
            return new ForegroundBluetoothDeviceFinder(activity);
        }
    }

    public static DeviceFinder getBackgroundFinder(NotificationService notificationService) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new BeaconFinder(notificationService);
        } else {
            return new BackgroundBluetoothDeviceFinder(notificationService);
        }
    }
}
