package tw.com.businessmeet.background;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tw.com.businessmeet.FriendsIntroductionActivity;
import tw.com.businessmeet.R;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.TimelineBean;
import tw.com.businessmeet.dao.TimelineDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.helper.NotificationHelper;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;
import tw.com.businessmeet.service.Impl.TimelineServiceImpl;

public class InviteMessageService extends FirebaseMessagingService {
    private static final String ACTION_OK = "tw.com.businessmeet.action.notification.bluetooth.ok";
    private static final String ACTION_DENIED = "tw.com.businessmeet.action.notification.bluetooth.denied";
    private static Notification ACTIVE_NOTIFICATION;
    //    private static Notification Accept_NOTIFICATION;
    private int notificationId = 0;
    private NotificationManagerCompat notificationManager;
    private static RemoteMessage remoteMessage = null;
    private LocationManager locationManager;
    private double longitude;
    private double latitude;
    private LocationListener locationListener = new MyLocationListener();
    private static final LinkedList<FriendBean> inviteRequestList = new LinkedList<>();

    @Override
    public void onCreate() {
        super.onCreate();

    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            longitude = loc.getLongitude();
            latitude = loc.getLatitude();
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        this.remoteMessage = remoteMessage;
        notificationManager = NotificationManagerCompat.from(this);
        Map<String, String> data = remoteMessage.getData();
        System.out.println("data = " + data);
        if (data != null) {
            switch (data.get("type")) {
                case "friendInvite":
//                    Looper.prepare();
                    FriendBean friendBean = new FriendBean();
                    String friendId = data.get("friendId");
                    String friendName = data.get("friendName");
                    System.out.println("friendName = " + friendName);
                    friendBean.setFriendId(friendId);
                    friendBean.setFriendName(friendName);
                    addTimeline(friendId, friendName);
//                    inviteRequestList.add(friendBean);
                    createInviteFriendNotification(friendBean);
//                    Looper.loop();
                    break;
                case "acceptFriendInvite":

                    String acceptFriendId = data.get("friendId");
                    String acceptFriendName = data.get("friendName");
                    addTimeline(acceptFriendId, acceptFriendName);
                    createAccrptFriendNotification(acceptFriendId, acceptFriendName);
            }

        }

    }

    private void addTimeline(String acceptFriendId, String acceptFriendName) {
        TimelineBean timelineBean = new TimelineBean();
        timelineBean.setMatchmakerId(DeviceHelper.getUserId(this));
        timelineBean.setFriendId(acceptFriendId);
        Geocoder gc = new Geocoder(this, Locale.TRADITIONAL_CHINESE);
        //更新位置
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        try {
            longitude = location.getLongitude();        //取得經度
            latitude = location.getLatitude();
            List<Address> lstAddress = gc.getFromLocation(latitude, longitude, 1);
//                    Toast.makeText(
//                            notificationService.getBaseContext(),
//                            lstAddress.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
            timelineBean.setPlace(lstAddress.get(0).getAddressLine(0));
            locationManager.removeUpdates(locationListener);
        } catch (Exception e) {
            e.printStackTrace();
            timelineBean.setPlace("室內");
        }
        timelineBean.setTimelinePropertiesNo(2);

        timelineBean.setTitle(timelineBean.getPlace());
        DBHelper dbHelper = new DBHelper(this);
        TimelineDAO timelineDAO = new TimelineDAO(dbHelper);
        TimelineBean searchBean = new TimelineBean();
        searchBean.setFriendId(acceptFriendId);
        searchBean.setMatchmakerId(DeviceHelper.getUserId(this));
        AsyncTaskHelper.execute(
                () -> TimelineServiceImpl.add(timelineBean),
                timelineDAO::add
        );
    }

    private void createInviteFriendNotification(FriendBean friendBean) {
        Intent okIntent = new Intent(this, FriendInviteService.FriendInviteBroadcastReceiver.class);
        okIntent.setAction(ACTION_OK);
        System.out.println("friendBean.getFriendId() = " + friendBean.getFriendId());
        okIntent.putExtra("friendId", friendBean.getFriendId());
        System.out.println("okIntent.getStringExtra(\"friendId\") = " + okIntent.getStringExtra("friendId"));
        PendingIntent okPendingIntent =
                PendingIntent.getBroadcast(this, 0, okIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Intent deniedIntent = new Intent(this, FriendInviteService.FriendInviteBroadcastReceiver.class);
        deniedIntent.setAction(ACTION_DENIED);
        deniedIntent.putExtra("friendId", friendBean.getFriendId());
        PendingIntent deniedPendingIntent =
                PendingIntent.getBroadcast(this, 0, deniedIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationHelper.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle("好友確認")
                .setContentText(friendBean.getFriendName() + "傳來了好友邀請")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.applogo, "確認", okPendingIntent)
                .addAction(R.drawable.applogo, "取消", deniedPendingIntent);

        ACTIVE_NOTIFICATION = builder.build();
        notificationManager.notify(notificationId--, ACTIVE_NOTIFICATION);
    }

    private void createAccrptFriendNotification(String friendId, String friendName) {
        Intent intent = new Intent(this, FriendsIntroductionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("friendId", friendId);
        intent.putExtras(bundle);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, notificationId, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationHelper.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle("好友同意")
                .setContentText(friendName + "同意了好友邀請")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        ACTIVE_NOTIFICATION = builder.build();
        notificationManager.notify(notificationId--, ACTIVE_NOTIFICATION);
        ACTIVE_NOTIFICATION = null;
    }

    @Override
    public void onNewToken(String token) {
        Log.d("activitymessage", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
    }

    public static class FriendInviteBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancelAll();
            String action = intent.getAction();
            String friend = intent.getStringExtra("friend");
            System.out.println("friendId = " + friend);
            FriendBean friendBean = new FriendBean();
            if (friend == null) {
                friend = remoteMessage.getData().get("friendId");
            }
            friendBean.setFriendId(friend);
            friendBean.setStatus(action.equals(ACTION_OK) ? 2 : null);
            AsyncTaskHelper.execute(
                    () -> FriendServiceImpl.createInviteNotification(friendBean),
                    newFriendBean -> {
//                        inviteRequestList.removeFirst();
                        InviteMessageService.ACTIVE_NOTIFICATION = null;
                        if (action.equals(ACTION_OK)) {
                            Intent startActivityIntent = new Intent(context, FriendsIntroductionActivity.class);
                            startActivityIntent.putExtra("friendId", friendBean.getFriendId());
                            startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(startActivityIntent);
                        }
                    },
                    (status, message) -> {
//                        inviteRequestList.removeFirst();
                        InviteMessageService.ACTIVE_NOTIFICATION = null;
                    }
            );

        }
    }

}