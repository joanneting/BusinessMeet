package tw.com.businessmeet.device.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import tw.com.businessmeet.device.ActionListener;
import tw.com.businessmeet.device.DeviceFinder;
import tw.com.businessmeet.device.EmptyActionListener;
import tw.com.businessmeet.device.FoundedDeviceDetail;
import tw.com.businessmeet.device.actionhandler.ActionHandler;
import tw.com.businessmeet.device.actionhandler.supplier.ActionHandlerSupplier;
import tw.com.businessmeet.device.enumerate.FindAction;

public class BluetoothBroadcast extends BroadcastReceiver {
    private final ActionHandlerSupplier actionHandlerSupplier;
    private final ActionListener actionListener;

    public BluetoothBroadcast(ActionHandlerSupplier actionHandlerSupplier, ActionListener actionListener) {
        this.actionHandlerSupplier = actionHandlerSupplier;
        this.actionListener = actionListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        FindAction bluetoothAction = FindAction.getInstance(action);
        ActionHandler actionHandler = actionHandlerSupplier.get(bluetoothAction);
        if (actionHandler != null) {
            if (bluetoothAction == FindAction.FOUND) {
                FoundedDeviceDetail deviceDetail = new BluetoothDeviceDetail(intent);
                intent.putExtra(DeviceFinder.EXTRA_FOUNDED_DEVICE_DETAIL, deviceDetail);
            }
            actionHandler.handle(context, intent);
            actionHandler.executeListener(actionListener != null ? actionListener : new EmptyActionListener(), context, intent);
        }
    }
}
