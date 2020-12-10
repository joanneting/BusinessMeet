package tw.com.businessmeet.device.discover;

import android.content.Context;
import android.os.Build;

import tw.com.businessmeet.device.beacon.BeaconDiscoverServer;
import tw.com.businessmeet.device.bluetooth.BluetoothDiscoverServer;

public class DiscoverServerCompat {

    public static DiscoverServer getInstance(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new BeaconDiscoverServer(context);
        } else {
            return new BluetoothDiscoverServer();
        }
    }
}
