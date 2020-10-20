package tw.com.businessmeet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import tw.com.businessmeet.background.FriendInviteService;
import tw.com.businessmeet.background.NotificationService;

public class BootCompletedReceiver extends BroadcastReceiver {
    private static final String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_BOOT_COMPLETED)) {
            //Service
            Intent serviceIntent = new Intent(context, NotificationService.class);
            context.startService(serviceIntent);
            Intent friendInvite = new Intent(context, FriendInviteService.class);
            context.startService(friendInvite);
        }
    }
}
