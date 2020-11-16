package tw.com.businessmeet.background;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import tw.com.businessmeet.activity.FriendsIntroductionActivity;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.dao.FriendDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.NotificationHelper;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;

public class FriendInviteService extends Service {
    private static final String ACTION_OK = "tw.com.businessmeet.action.notification.bluetooth.ok";
    private static final String ACTION_DENIED = "tw.com.businessmeet.action.notification.bluetooth.denied";

    private static Notification ACTIVE_NOTIFICATION;
    private int notificationId = 0;
    private NotificationManagerCompat notificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = NotificationManagerCompat.from(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 =
                    new NotificationChannel(
                            NotificationHelper.CHANNEL_1_ID,
                            "channel1",
                            NotificationManager.IMPORTANCE_HIGH
                    );
            channel1.setDescription("This is channel 1");
            channel1.enableLights(true);
            channel1.enableVibration(true);
            notificationManager.createNotificationChannel(channel1);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static class FriendInviteBroadcastReceiver extends BroadcastReceiver {
        private FriendDAO friendDAO;
        private DBHelper dbHelper;

        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancelAll();
            dbHelper = new DBHelper(context);
            friendDAO = new FriendDAO(dbHelper);
            String action = intent.getAction();
            String friendId = intent.getStringExtra("friendId");
            FriendBean friendBean = new FriendBean();
            friendBean.setFriendId(friendId);
            friendBean.setStatus(action.equals(ACTION_OK) ? 2 : null);
            AsyncTaskHelper.execute(
                    () -> FriendServiceImpl.createInviteNotification(friendBean), newFriendBean -> {
                        friendDAO.add(newFriendBean);
                        FriendInviteService.ACTIVE_NOTIFICATION = null;
                        if (action.equals(ACTION_OK)) {
                            Intent startActivityIntent = new Intent(context, FriendsIntroductionActivity.class);
                            startActivityIntent.putExtra("friendId", friendBean.getFriendId());
                            startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(startActivityIntent);
                        }
                    },
                    (status, message) -> {
                        FriendInviteService.ACTIVE_NOTIFICATION = null;
                    }
            );
        }
    }
}
