package tw.com.businessmeet.device.actionhandler;

import android.content.Context;
import android.content.Intent;

import tw.com.businessmeet.device.ActionListener;

public abstract class AbstractFoundActionHandler implements ActionHandler {
    @Override
    public void executeListener(ActionListener listener, Context context, Intent intent) {
        listener.onSearch(context, intent);
    }
}
