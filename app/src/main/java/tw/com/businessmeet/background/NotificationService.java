package tw.com.businessmeet.background;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

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

    @Override
    public IBinder onBind(Intent intent) {
        return mLocBin;
    }

    @Override
    public void onCreate() {
        Log.e("service ", "serviceStart");
        try {
            connectServer = new BluetoothConnectServer(this);
            connectServer.start();
        } catch (IOException e) {
            throw new BluetoothServerStartException(e);
        }
        server = DiscoverServerCompat.getInstance(this);
        server.start();
        finder = DeviceFinderCompat.getBackgroundFinder(this);
        finder.find(new BackgroundActionHandlerSupplier(this, finder));

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        try {
            connectServer.close();
        } catch (IOException e) {
            throw new BluetoothServerStartException(e);
        }
        server.stop();
        finder.cancel();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        try {
            connectServer.close();
        } catch (IOException e) {
            throw new BluetoothServerStartException(e);
        }
        server.stop();
        finder.cancel();
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        public NotificationService getService() {
            return NotificationService.this;
        }
    }
}
