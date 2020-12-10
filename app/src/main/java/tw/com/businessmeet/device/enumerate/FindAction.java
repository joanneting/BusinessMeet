package tw.com.businessmeet.device.enumerate;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public enum FindAction {//列舉
    UNKNOWN, FOUND, CHANGE, FINISH;

    public static FindAction getInstance(String action) {
        switch (action) {
            case BluetoothDevice.ACTION_FOUND:
                return FOUND;
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                return CHANGE;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                return FINISH;
            default:
                return UNKNOWN;
        }
    }
}
