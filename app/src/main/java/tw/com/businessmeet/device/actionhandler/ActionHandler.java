package tw.com.businessmeet.device.actionhandler;

import android.content.Context;
import android.content.Intent;

import tw.com.businessmeet.device.ActionListener;

public interface ActionHandler {
    void handle(Context context, Intent intent);

    void executeListener(ActionListener listener, Context context, Intent intent);
}
