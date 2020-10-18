package tw.com.businessmeet.device.beacon;

import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.Collections;
import java.util.UUID;

import tw.com.businessmeet.device.discover.DiscoverServer;

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public class BeaconDiscoverServer implements DiscoverServer {
    private final String beaconIdentifier;
    private final BeaconTransmitter transmitter;

    public BeaconDiscoverServer(Context context) {
        BeaconSharedPreferences beaconSharedPreferences = new BeaconSharedPreferences(context);
        String beaconIdentifier = beaconSharedPreferences.getIdentifier();
        if (beaconIdentifier == null) {
            beaconIdentifier = UUID.randomUUID().toString();
            beaconSharedPreferences.setIdentifier(beaconIdentifier);
        }
        this.beaconIdentifier = beaconIdentifier;
        BeaconParser beaconParser = new BeaconParser();
        beaconParser.setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT);
        this.transmitter = new BeaconTransmitter(context, beaconParser);
    }

    @Override
    public void start() {
        Beacon beacon = new Beacon.Builder()
                .setId1(beaconIdentifier)
                .setId2("1")
                .setId3("1")
                .setManufacturer(0x0118)
                .setTxPower(-59)
                .setDataFields(Collections.singletonList(0L))
                .build();
        transmitter.startAdvertising(beacon, new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
            }

            @Override
            public void onStartFailure(int errorCode) {
                super.onStartFailure(errorCode);
            }
        });
    }

    @Override
    public void stop() {
        transmitter.stopAdvertising();
    }
}
