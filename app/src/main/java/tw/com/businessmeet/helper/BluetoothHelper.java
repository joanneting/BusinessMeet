package tw.com.businessmeet.helper;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.UUID;

import tw.com.businessmeet.RequestCode;

public class BluetoothHelper {
    public static final String BLUETOOTH_NAME = "BeMet";
    public static final UUID BLUETOOTH_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String SECURE_SETTINGS_BLUETOOTH_ADDRESS = "bluetooth_address";

    public static void startBluetooth(Activity activity) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            //裝置不支援藍芽
            Toast.makeText(activity, "裝置不支援藍芽", Toast.LENGTH_SHORT).show();
            activity.finish();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                enable.putExtra(
                        BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                        3600);
                activity.startActivityForResult(enable, RequestCode.REQUEST_DISCOVERABLE);
            } else {
                bluetoothAdapter.enable();
            }
        }
    }

    public static String getDeviceBluetoothMacAddress(ContentResolver contentResolver) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        String result = "F0:EE:10:FC:C5:2D";
        String result = "02:00:00:00:00:00";
        //確認版本號
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            result = Settings.Secure.getString(contentResolver, SECURE_SETTINGS_BLUETOOTH_ADDRESS);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Field mServiceField = bluetoothAdapter.getClass().getDeclaredField("mService");
                mServiceField.setAccessible(true);

                Object btManagerService = mServiceField.get(bluetoothAdapter);

                if (btManagerService != null) {
                    result = (String) btManagerService.getClass().getMethod("getAddress").invoke(btManagerService);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        } else {
            result = bluetoothAdapter.getAddress();
        }

        return result != null && !result.startsWith("02:00:00") ? result : "F0:EE:10:38:DA:77";
    }

    public static String getBluetoothAddress(Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        return device.getAddress();
    }

    public static double getDistance(Intent intent) {
        short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
        int iRssi = Math.abs(rssi);
        // 將藍芽訊號強度換算為距離
        double power = (iRssi - 59) / 25.0;
        return Math.pow(10, power);
    }

    public static IntentFilter getFilter() {
        IntentFilter result = new IntentFilter();
        result.addAction(BluetoothDevice.ACTION_FOUND);
        result.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        result.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        result.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        return result;
    }

    public static void startDiscovery() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.startDiscovery();
        }
    }

    public static void cancelDiscovery() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
    }
}
