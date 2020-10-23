package tw.com.businessmeet;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import tw.com.businessmeet.adapter.MatchedDeviceRecyclerViewAdapter;
import tw.com.businessmeet.adapter.UnmatchedDeviceRecyclerViewAdapter;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.device.DeviceFinder;
import tw.com.businessmeet.device.actionhandler.supplier.ForegroundActionHandlerSupplier;
import tw.com.businessmeet.device.bluetooth.finder.ForegroundBluetoothDeviceFinder;
import tw.com.businessmeet.helper.DBHelper;

public class NotificationActivity extends AppCompatActivity {

    //Button btNotification;
    private UserInformationDAO userInformationDAO;
    private MatchedDeviceRecyclerViewAdapter matchedRecyclerViewAdapter;
    private UnmatchedDeviceRecyclerViewAdapter unmatchedRecyclerViewAdapter;
    private DBHelper DH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        //search bluetooth
        DeviceFinder deviceFinder = new ForegroundBluetoothDeviceFinder(this);
        deviceFinder.find(new ForegroundActionHandlerSupplier(this));
        sendMessage();
        openDB();
        //setting channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
    }

    private void openDB() {
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
    }

    private void sendMessage() {

        String message = "This is a notific.";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                NotificationActivity.this, "MyNotifications"
        )
                .setSmallIcon(R.drawable.ic_insert_comment_black_24dp)
                .setContentText("New Notification")
                .setContentText(message)
                //.setAutoCancel(true)
                .setGroup("example_group");

/*        //宣告Intent物件 跳至friends_introduction
        Intent intent = new Intent(NotificationActivity.this,
                FriendsIntroductionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message",message);

        // 宣告一個 PendingIntent 的物件(執行完並不會馬上啟動,點訊息的時候才會跳到別的 Activity)
        PendingIntent pendingIntent = PendingIntent.getActivity(NotificationActivity.this,
                0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);*/

        //定義一個訊息管理者 和系統要 取得訊息管理者的物件
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE
        );

        //要求傳送一個訊息
        notificationManager.notify(0, builder.build());
    }
}


//要指定通知的UI和操作，請使用NotificationCompat.Builder。
//要創建通知，請使用NotificationCompat.Builder.build()。
//要發出通知，請使用NotificationManager.notify()將該通知對像傳遞給Android運行時系統。