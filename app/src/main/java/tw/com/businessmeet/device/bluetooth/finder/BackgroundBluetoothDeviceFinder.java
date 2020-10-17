package tw.com.businessmeet.device.bluetooth.finder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import tw.com.businessmeet.background.NotificationService;
import tw.com.businessmeet.device.ActionListener;
import tw.com.businessmeet.device.DeviceFinder;
import tw.com.businessmeet.device.actionhandler.supplier.ActionHandlerSupplier;
import tw.com.businessmeet.device.bluetooth.BluetoothBroadcast;
import tw.com.businessmeet.helper.BluetoothHelper;

public class BackgroundBluetoothDeviceFinder implements DeviceFinder {
    private final NotificationService notificationService;

    public BackgroundBluetoothDeviceFinder(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void find(ActionHandlerSupplier actionHandlerSupplier, ActionListener actionListener) {
        BluetoothHelper.startDiscovery();

        Log.e("service ", "searchBlueToothInBackground");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel CHANNEL_1_ID =
                    new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = notificationService.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(CHANNEL_1_ID);
        }
        IntentFilter filter = BluetoothHelper.getFilter();
        notificationService.registerReceiver(new BluetoothBroadcast(actionHandlerSupplier, actionListener), filter);
        BluetoothHelper.startDiscovery();
    }

    @Override
    public void cancel() {
        BluetoothHelper.cancelDiscovery();
    }
}
