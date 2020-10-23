package tw.com.businessmeet.device.bluetooth.finder;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.Build;

import java.lang.reflect.Method;
import java.util.Set;

import tw.com.businessmeet.device.ActionListener;
import tw.com.businessmeet.device.DeviceFinder;
import tw.com.businessmeet.device.actionhandler.supplier.ActionHandlerSupplier;
import tw.com.businessmeet.device.bluetooth.BluetoothBroadcast;
import tw.com.businessmeet.helper.BluetoothHelper;

public class ForegroundBluetoothDeviceFinder implements DeviceFinder {
    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final Activity activity;

    public ForegroundBluetoothDeviceFinder(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void find(ActionHandlerSupplier actionHandlerSupplier, ActionListener actionListener) {
        BluetoothHelper.startDiscovery();

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                removeMatched(device.getAddress());
            }
        }
        IntentFilter filter = BluetoothHelper.getFilter();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel CHANNEL_1_ID =
                    new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = activity.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(CHANNEL_1_ID);
        }
        activity.registerReceiver(new BluetoothBroadcast(actionHandlerSupplier, actionListener), filter);
    }

    @Override
    public void cancel() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
    }

    private void removeMatched(String matchedAddress) {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(matchedAddress);
        try {
            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
