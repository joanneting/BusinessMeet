package tw.com.businessmeet.device;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import tw.com.businessmeet.R;
import tw.com.businessmeet.device.enumerate.FindState;

public class ActionListenerImpl implements ActionListener {
    private final Activity activity;

    public ActionListenerImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onSearch(Context context, Intent intent) {
        TextView search_title = activity.findViewById(R.id.search_title);
        search_title.setText("搜尋中...");
    }

    @Override
    public void onConnected(String bluetooth, Context context, Intent intent) {

    }

    @Override
    public void onDisconnected(String bluetooth, Context context, Intent intent) {

    }

    @Override
    public void onChange(FindState state, Context context, Intent intent) {
        if (state == FindState.STATE_ON) {
            activity.setProgressBarIndeterminateVisibility(true);
            TextView search_title = activity.findViewById(R.id.search_title);
            Log.e("search", String.valueOf(activity));
            Log.e("search", String.valueOf(search_title));
            search_title.setText("正在搜尋...");
        }
    }

    @Override
    public void onFinish(Context context, Intent intent) {
        TextView search_title = activity.findViewById(R.id.search_title);
        search_title.setText("搜尋完成!");
    }

    @Override
    public void unknownAction(Context context, Intent intent) {

    }
}
