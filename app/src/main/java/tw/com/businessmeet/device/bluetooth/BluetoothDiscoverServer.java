package tw.com.businessmeet.device.bluetooth;

import tw.com.businessmeet.device.discover.DiscoverServer;
import tw.com.businessmeet.helper.BluetoothHelper;

public class BluetoothDiscoverServer implements DiscoverServer {
    @Override
    public void start() {
        BluetoothHelper.startDiscovery();
    }

    @Override
    public void stop() {
        BluetoothHelper.cancelDiscovery();
    }
}
