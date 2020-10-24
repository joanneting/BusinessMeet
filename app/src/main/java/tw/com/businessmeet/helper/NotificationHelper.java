package tw.com.businessmeet.helper;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import java.io.ByteArrayOutputStream;

import androidx.core.app.NotificationCompat;
import tw.com.businessmeet.FriendsTimelineActivity;
import tw.com.businessmeet.R;
import tw.com.businessmeet.background.NotificationService;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.service.Impl.UserInformationServiceImpl;

public class NotificationHelper {
    private Activity activity;
    private UserInformationServiceImpl userInformationService = new UserInformationServiceImpl();
    private AvatarHelper avatarHelper = new AvatarHelper();
    private static int NOTIFICATION_ID = 0;
    public static final String CHANNEL_1_ID = "channel1";
    private int position;
    //private static int NOTIFICATION_ID = 0x30001; //196610
    private static int SUMMARY_ID = 1;
    private String GROUP_KEY_MEET = "tw.com.bemet.meet";
    private NotificationService notificationService;
    private NotificationManager notificationManager;

    public NotificationHelper(Activity activity) {
        this.activity = activity;
        notificationManager = (NotificationManager) activity.getSystemService(
                Context.NOTIFICATION_SERVICE
        );
    }

    public NotificationHelper(NotificationService notificationService) {
        this.notificationService = notificationService;
        notificationManager = (NotificationManager) notificationService.getSystemService(
                Context.NOTIFICATION_SERVICE
        );
    }


    //public static final String CHANNEL_2_ID = "channel2";


    public void sendBackgroundMessage(UserInformationBean userInformationBean, String lastMeetPlace) {
        String userName = userInformationBean.getName();
        String profession = userInformationBean.getProfession();
        String mail = userInformationBean.getMail();
        String tel = userInformationBean.getTel();

        //String avatar = userInformationBean.getAvatar();
        Notification groupBuilder =
                new NotificationCompat.Builder(notificationService, CHANNEL_1_ID)
                        .setContentTitle(userName + " " + profession)
                        .setContentText(lastMeetPlace)
                        //set content text to support devices running API level < 24
                        .setSmallIcon(R.drawable.ic_insert_comment_black_24dp)
                        //build summary info into InboxStyle template
                        .setStyle(new NotificationCompat.InboxStyle()
                                .setSummaryText("附近好友"))
                        //specify which group this notification belongs to
                        .setGroup(GROUP_KEY_MEET)
                        //set this notification as the summary for the group
                        .setGroupSummary(true)
                        .build();



                    /*String title2 = "李赫宰";
                    String message2 = " SM娛樂公司";*/
        /**/

//        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
//        bigStyle.bigText(memo);
        NotificationCompat.Builder notification1 = new NotificationCompat.Builder(
                notificationService, CHANNEL_1_ID
        )
                .setSmallIcon(R.drawable.ic_insert_comment_black_24dp)
                .setContentTitle(userName + " " + profession)
                .setContentText(lastMeetPlace)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setColor(Color.rgb(4, 42, 88))
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setLargeIcon(avatarHelper.getImageResource(userInformationBean.getAvatar()))
                //.setStyle(bigStyle)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(memo))
                .setGroup(GROUP_KEY_MEET);


        //宣告Intent物件 跳至friends_introduction
/*                    Intent intent = new Intent(notificationService,
                            FriendsIntroductionActivity.class);*/

        Intent intent = new Intent();
        intent.setClass(notificationService, FriendsTimelineActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("friendId", userInformationBean.getUserId());
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //大頭貼---------------------
        AvatarHelper avatarHelper = new AvatarHelper();
        Bitmap profilePhoto = avatarHelper.getImageResource(userInformationBean.getAvatar());
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        profilePhoto.compress(Bitmap.CompressFormat.PNG, 100, bs);
        intent.putExtra("avatar", bs.toByteArray());
        //.大頭貼---------------------

        // Since android Oreo notification channel is needed. 添加通道分配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 =
                    new NotificationChannel(
                            CHANNEL_1_ID,
                            "channel1",
                            NotificationManager.IMPORTANCE_HIGH
                    );
            channel1.setDescription("This is channel 1");
            channel1.enableLights(true);
            channel1.enableVibration(true);

            NotificationManager manager = notificationService.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

        }
        //.添加通道分配

        // 宣告一個 PendingIntent 的物件(執行完並不會馬上啟動,點訊息的時候才會跳到別的 Activity)
        PendingIntent pendingIntent = PendingIntent.getActivity(notificationService,
                NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification1.setContentIntent(pendingIntent);
                    /*notification2.setContentIntent(pendingIntent);
                    summaryNotification.setContentIntent(pendingIntent);*/

        //定義一個訊息管理者 和系統要 取得訊息管理者的物件
//                    NotificationManagerCompat.from(activity)
                    /*手機端有摘要通知，又有一般的通知的話改寫
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);*/

        //要求傳送一個訊息
        //id若一樣，則為更新通知，之前的通知會不見
        notificationManager.notify(NOTIFICATION_ID++, notification1.build());
        if (NOTIFICATION_ID > 0) {
//            notificationManager.notify(SUMMARY_ID, groupBuilder);
        }

        //notificationManager.notify(SUMMARY_ID, summaryNotification.build());
        if (NOTIFICATION_ID >= 10) {
            notificationManager.cancel(NOTIFICATION_ID - 10);// 取消之前的通知消息;
        }
    }

}
