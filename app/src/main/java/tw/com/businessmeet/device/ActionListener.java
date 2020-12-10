package tw.com.businessmeet.device;

import android.content.Context;
import android.content.Intent;

import tw.com.businessmeet.device.enumerate.FindState;

public interface ActionListener {
    void onSearch(Context context, Intent intent);

    void onConnected(String bluetooth, Context context, Intent intent);

    void onDisconnected(String bluetooth, Context context, Intent intent);

    void onChange(FindState state, Context context, Intent intent);

    void onFinish(Context context, Intent intent);

    void unknownAction(Context context, Intent intent);
}
