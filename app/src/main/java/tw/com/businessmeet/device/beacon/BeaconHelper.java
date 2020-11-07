package tw.com.businessmeet.device.beacon;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

public class BeaconHelper implements BeaconConsumer {
    public static final String EXTRA_IDENTIFIER = "identifier";
    public static final String EXTRA_DISTANCE = "distance";

    private final Context context;
    private final BeaconManager beaconManager;

    public BeaconHelper(Context context) {
        this.context = context;
        this.beaconManager = BeaconManager.getInstanceForApplication(context);
        this.beaconManager.getBeaconParsers().add(new BeaconParser(BeaconParser.ALTBEACON_LAYOUT));
        beaconManager.setBackgroundBetweenScanPeriod(500L);
    }

    @Override
    public void onBeaconServiceConnect() {
        try {
            beaconManager.startRangingBeaconsInRegion(new Region(
                    "BeMet",
                    null,
                    Identifier.fromInt(1),
                    Identifier.fromInt(1)
            ));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Context getApplicationContext() {
        return context.getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        context.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return context.bindService(intent, serviceConnection, i);
    }

    public void addRangeNotifier(RangeNotifier notifier) {
        beaconManager.removeRangeNotifier(notifier);
        beaconManager.addRangeNotifier(notifier);
    }

    public void addMonitorNotifier(MonitorNotifier notifier) {
        beaconManager.removeMonitorNotifier(notifier);
        beaconManager.addMonitorNotifier(notifier);
    }

    public void bind() {
        beaconManager.bind(this);
    }

    public void unbind() {
        beaconManager.unbind(this);
    }

    public BeaconManager getBeaconManager() {
        return beaconManager;
    }
}
