package tw.com.businessmeet.background;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;

import java.io.IOException;

import tw.com.businessmeet.device.DeviceFinder;
import tw.com.businessmeet.device.DeviceFinderCompat;
import tw.com.businessmeet.device.actionhandler.supplier.BackgroundActionHandlerSupplier;
import tw.com.businessmeet.device.bluetooth.connector.BluetoothConnectServer;
import tw.com.businessmeet.device.discover.DiscoverServer;
import tw.com.businessmeet.device.discover.DiscoverServerCompat;
import tw.com.businessmeet.exception.BluetoothServerStartException;

public class NotificationService extends Service {
    private final LocalBinder mLocBin = new LocalBinder();
    private BluetoothConnectServer connectServer;
    private DiscoverServer server;
    private DeviceFinder finder;
    private static final String KEY_NOTIFICATION = "notification";

    @Override
    public IBinder onBind(Intent intent) {
        return mLocBin;
    }

    @Override
    public void onCreate() {
//        try {
//            connectServer = new BluetoothConnectServer(this);
//            connectServer.start();
//        } catch (IOException e) {
//            throw new BluetoothServerStartException(e);
//        }
        boolean isOpen = getSharedPreferences(KEY_NOTIFICATION, Context.MODE_PRIVATE)
                .getBoolean(KEY_NOTIFICATION, false);
        if (!isOpen) {
            server = DiscoverServerCompat.getInstance(this);
            server.start();
            finder = DeviceFinderCompat.getBackgroundFinder(this);
            finder.find(new BackgroundActionHandlerSupplier(this, finder));

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sharedPreferences.edit().putBoolean("KEY_NOTIFICATION", true).apply();
        }

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        try {
            connectServer.close();
        } catch (IOException e) {
            throw new BluetoothServerStartException(e);
        }

        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        try {
            connectServer.close();
        } catch (IOException e) {
            throw new BluetoothServerStartException(e);
        }

        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        public NotificationService getService() {
            return NotificationService.this;
        }
    }
}
