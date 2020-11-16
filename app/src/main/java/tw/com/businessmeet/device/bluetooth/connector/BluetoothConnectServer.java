package tw.com.businessmeet.device.bluetooth.connector;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

import tw.com.businessmeet.R;
import tw.com.businessmeet.activity.FriendsIntroductionActivity;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.dao.FriendDAO;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.BluetoothHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.helper.NotificationHelper;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;

public class BluetoothConnectServer extends Thread implements Closeable {
    private static final String ACTION_GRANT_ADD_FRIEND = "tw.com.businessmeet.action.GRANT_ADD_FRIEND";
    private static final String ACTION_OK = "tw.com.businessmeet.action.notification.bluetooth.ok";
    private static final String ACTION_DENIED = "tw.com.businessmeet.action.notification.bluetooth.denied";

    private final Context context;
    private boolean isListening = true;
    private final UserInformationDAO userInformationDAO;
    private final FriendDAO friendDAO;
    private final BluetoothServerSocket server;
    private BluetoothSocket connection;
    private BufferedReader input;

    public BluetoothConnectServer(Context context) throws IOException {
        this.context = context;
        DBHelper dbHelper = new DBHelper(context);
        this.userInformationDAO = new UserInformationDAO(dbHelper);
        this.friendDAO = new FriendDAO(dbHelper);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.server =
                bluetoothAdapter.listenUsingRfcommWithServiceRecord(BluetoothHelper.BLUETOOTH_NAME, BluetoothHelper.BLUETOOTH_UUID);
    }

    @Override
    public void run() {
        super.run();
        try {
            while (isListening) {
                this.connection = server.accept();
                this.input = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

                String deviceJsonString = input.readLine();
                JSONObject device = new JSONObject(deviceJsonString);
                String action = device.getString("action");
                switch (action) {
                    case BluetoothConnectClient.ACTION_REQUEST_ADD_FRIEND:
                        String name = device.getString("name");
                        String address = device.getString("address");
                        Intent okIntent = new Intent(context, BluetoothFriendBroadcastReceiver.class);
                        okIntent.setAction(ACTION_OK);
                        okIntent.putExtra("address", address);
                        PendingIntent okPendingIntent =
                                PendingIntent.getBroadcast(context, 0, okIntent, 0);
                        Intent deniedIntent = new Intent(context, BluetoothFriendBroadcastReceiver.class);
                        deniedIntent.setAction(ACTION_OK);
                        PendingIntent deniedPendingIntent =
                                PendingIntent.getBroadcast(context, 1, deniedIntent, 0);
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
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
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationHelper.CHANNEL_1_ID)
                                .setSmallIcon(R.drawable.applogo)
                                .setContentTitle("好友確認")
                                .setContentText(name + "傳來了好友邀請")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .addAction(R.drawable.applogo, "確認", okPendingIntent)
                                .addAction(R.drawable.applogo, "取消", deniedPendingIntent);
                        notificationManager.notify(Integer.MAX_VALUE, builder.build());
                        break;
                    case ACTION_GRANT_ADD_FRIEND:
                        if (device.getBoolean("result")) {
                            String friendId = device.getString("userId");
                            String userId = DeviceHelper.getUserId(context, userInformationDAO);
                            FriendBean friendBean = new FriendBean();
                            friendBean.setMatchmakerId(userId);
                            friendBean.setFriendId(friendId);
                            AsyncTaskHelper.execute(
                                    () -> FriendServiceImpl.add(friendBean),
                                    responseFriendBean -> {
                                        friendDAO.add(responseFriendBean);
                                        Intent intent = new Intent(context, FriendsIntroductionActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("friendId", friendId);
                                        intent.putExtras(bundle);
                                        context.startActivity(intent);
                                    }
                            );
                        } else {
                            Toast.makeText(context, "對方已拒絕您的好友邀請", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                connection.close();
                input.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        isListening = false;
        server.close();
        if (connection.isConnected()) {
            connection.close();
        }
        input.close();
    }

    public static class BluetoothFriendBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String address = intent.getStringExtra("address");
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            try (BluetoothConnector connector = new BluetoothConnector(bluetoothAdapter.getRemoteDevice(address))) {
                connector.connect();
                JSONObject response = new JSONObject();
                response.put("action", ACTION_GRANT_ADD_FRIEND);
                response.put("result", ACTION_OK.equals(action));
                response.put("userId", DeviceHelper.getUserId(context));
                connector.write(response.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
