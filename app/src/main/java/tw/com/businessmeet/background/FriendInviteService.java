package tw.com.businessmeet.background;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import tw.com.businessmeet.FriendsIntroductionActivity;
import tw.com.businessmeet.R;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.NotificationHelper;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;

public class FriendInviteService extends Service {
    private static final String ACTION_OK = "tw.com.businessmeet.action.notification.bluetooth.ok";
    private static final String ACTION_DENIED = "tw.com.businessmeet.action.notification.bluetooth.denied";

    private static Notification ACTIVE_NOTIFICATION;

    private static final LinkedList<FriendBean> inviteRequestList = new LinkedList<>();
    private final Timer timer = new Timer(true);
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
        Thread invite = new Thread() {
            @Override
            public void run() {
                super.run();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        SharedPreferences sharedPreferences = getSharedPreferences("cookieData", Context.MODE_PRIVATE);
                        Set<String> cookieSet = sharedPreferences.getStringSet("cookie", new HashSet<>());
                        boolean isLogin = false;
                        for (String cookie : cookieSet) {
                            if (cookie.contains("JSESSIONID")) {
                                isLogin = true;
                                break;
                            }
                        }
                        if (isLogin) {
                            if (ACTIVE_NOTIFICATION == null && !inviteRequestList.isEmpty()) {
                                createNotification(inviteRequestList.getFirst());
                            }
                            if (inviteRequestList.size() == 0) {
                                AsyncTaskHelper.execute(FriendServiceImpl::searchInviteNotification, friendBeans -> {
                                    for (FriendBean friendBean : friendBeans) {
                                        if (!inviteRequestList.contains(friendBean)) {
                                            inviteRequestList.addLast(friendBean);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }, 1000, 1000);
            }
        };
        invite.start();

    }

    private void createNotification(FriendBean friendBean) {
        Intent okIntent = new Intent(this, FriendInviteBroadcastReceiver.class);
        okIntent.setAction(ACTION_OK);
        okIntent.putExtra("matchmakerId", friendBean.getFriendId());
        okIntent.putExtra("friendId", friendBean.getMatchmakerId());
        PendingIntent okPendingIntent =
                PendingIntent.getBroadcast(this, 0, okIntent, 0);
        Intent deniedIntent = new Intent(this, FriendInviteBroadcastReceiver.class);
        deniedIntent.setAction(ACTION_DENIED);
        deniedIntent.putExtra("matchmakerId", friendBean.getFriendId());
        deniedIntent.putExtra("friendId", friendBean.getMatchmakerId());
        PendingIntent deniedPendingIntent =
                PendingIntent.getBroadcast(this, 0, deniedIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationHelper.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle("好友確認")
                .setContentText(friendBean.getMatchmakerId() + "傳來了好友邀請")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.applogo, "確認", okPendingIntent)
                .addAction(R.drawable.applogo, "取消", deniedPendingIntent);

        ACTIVE_NOTIFICATION = builder.build();
        notificationManager.notify(notificationId++, ACTIVE_NOTIFICATION);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    public static class FriendInviteBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancelAll();
            String action = intent.getAction();
            String matchmakerId = intent.getStringExtra("matchmakerId");
            String friendId = intent.getStringExtra("friendId");
            FriendBean friendBean = new FriendBean();
            friendBean.setFriendId(friendId);
            friendBean.setMatchmakerId(matchmakerId);
            friendBean.setStatus(action.equals(ACTION_OK) ? 2 : null);
            AsyncTaskHelper.execute(
                    () -> FriendServiceImpl.createInviteNotification(friendBean),
                    newFriendBean -> {
                        inviteRequestList.removeFirst();
                        FriendInviteService.ACTIVE_NOTIFICATION = null;
                        if (action.equals(ACTION_OK)) {
                            Intent startActivityIntent = new Intent(context, FriendsIntroductionActivity.class);
                            startActivityIntent.putExtra("friendId", friendBean.getFriendId());
                            startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(startActivityIntent);
                        }
                    },
                    (status, message) -> {
                        inviteRequestList.removeFirst();
                        FriendInviteService.ACTIVE_NOTIFICATION = null;
                    }
            );
        }
    }
}
