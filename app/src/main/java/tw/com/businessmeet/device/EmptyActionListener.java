package tw.com.businessmeet.device;

import android.content.Context;
import android.content.Intent;

import tw.com.businessmeet.device.enumerate.FindState;

public class EmptyActionListener implements ActionListener {
    @Override
    public void onSearch(Context context, Intent intent) {

    }

    @Override
    public void onConnected(String bluetooth, Context context, Intent intent) {

    }

    @Override
    public void onDisconnected(String bluetooth, Context context, Intent intent) {

    }

    @Override
    public void onChange(FindState state, Context context, Intent intent) {

    }

    @Override
    public void onFinish(Context context, Intent intent) {

    }

    @Override
    public void unknownAction(Context context, Intent intent) {

    }
}
