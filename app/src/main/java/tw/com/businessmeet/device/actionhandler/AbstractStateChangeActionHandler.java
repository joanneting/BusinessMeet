package tw.com.businessmeet.device.actionhandler;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;

import tw.com.businessmeet.device.ActionListener;
import tw.com.businessmeet.device.enumerate.FindState;

public abstract class AbstractStateChangeActionHandler implements ActionHandler {
    @Override
    public void executeListener(ActionListener listener, Context context, Intent intent) {
        FindState state = getState(intent);
        listener.onChange(state, context, intent);
    }

    public FindState getState(Intent intent) {
        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
        return FindState.getInstance(state);
    }
}
