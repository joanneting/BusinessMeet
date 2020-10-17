package tw.com.businessmeet.device.actionhandler;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;

import tw.com.businessmeet.RequestCode;
import tw.com.businessmeet.device.ActionListener;
import tw.com.businessmeet.helper.BluetoothHelper;

public class ForegroundStateChangeActionHandler extends AbstractStateChangeActionHandler {
    private final Activity activity;

    public ForegroundStateChangeActionHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void handle(Context context, Intent intent) {

    }

    @Override
    public void executeListener(ActionListener listener, Context context, Intent intent) {
        super.executeListener(listener, context, intent);
        switch (getState(intent)) {
            case STATE_ON:
                BluetoothHelper.startDiscovery();
                break;
            case STATE_OFF:
                Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                enable.putExtra(
                        BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                        3600
                );
                activity.startActivityForResult(enable, RequestCode.REQUEST_DISCOVERABLE);
                break;
            case STATE_TURNING_ON:
            case STATE_TURNING_OFF:
                break;
        }
    }
}
