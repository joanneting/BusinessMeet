package tw.com.businessmeet.device.enumerate;

import android.bluetooth.BluetoothAdapter;

public enum FindState {
    STATE_ON, STATE_OFF, STATE_TURNING_ON, STATE_TURNING_OFF, UNKNOWN;

    public static FindState getInstance(int state) {
        switch (state) {
            case BluetoothAdapter.STATE_ON:
                return STATE_ON;
            case BluetoothAdapter.STATE_OFF:
                return STATE_OFF;
            case BluetoothAdapter.STATE_TURNING_ON:
                return STATE_TURNING_ON;
            case BluetoothAdapter.STATE_TURNING_OFF:
                return STATE_TURNING_OFF;
            default:
                return UNKNOWN;
        }
    }
}
