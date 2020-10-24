package tw.com.businessmeet.device.beacon.finder;

import android.content.Context;
import android.content.Intent;

import org.altbeacon.beacon.Beacon;

import java.util.HashSet;
import java.util.Set;

import tw.com.businessmeet.device.ActionListener;
import tw.com.businessmeet.device.DeviceFinder;
import tw.com.businessmeet.device.FoundedDeviceDetail;
import tw.com.businessmeet.device.actionhandler.ActionHandler;
import tw.com.businessmeet.device.actionhandler.supplier.ActionHandlerSupplier;
import tw.com.businessmeet.device.beacon.BeaconDeviceDetail;
import tw.com.businessmeet.device.beacon.BeaconHelper;
import tw.com.businessmeet.device.enumerate.FindAction;

public class BeaconFinder implements DeviceFinder {
    private final Context context;
    private final BeaconHelper beaconHelper;
    private final Set<String> foundedBeacon = new HashSet<>();
    private ActionHandler finishActionHandler;

    public BeaconFinder(Context context) {
        this.context = context;
        this.beaconHelper = new BeaconHelper(context);
    }

    @Override
    public void find(ActionHandlerSupplier actionHandlerSupplier, ActionListener actionListener) {
        ActionHandler foundActionHandler = actionHandlerSupplier.get(FindAction.FOUND);
        
        beaconHelper.addRangeNotifier((beaconCollection, region) -> {
            for (Beacon beacon : beaconCollection) {
                if (foundedBeacon.add(beacon.getId1().toString())) {
                    Intent intent = new Intent();
                    FoundedDeviceDetail deviceDetail = new BeaconDeviceDetail(beacon);
                    intent.putExtra(DeviceFinder.EXTRA_FOUNDED_DEVICE_DETAIL, deviceDetail);
                    foundActionHandler.handle(context, intent);
                }
            }
        });
        finishActionHandler = actionHandlerSupplier.get(FindAction.FINISH);
        beaconHelper.bind();
    }

    @Override
    public void cancel() {
        beaconHelper.unbind();
        finishActionHandler.handle(context, new Intent());
    }
}
