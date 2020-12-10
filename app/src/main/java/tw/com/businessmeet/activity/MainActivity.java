package tw.com.businessmeet.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import tw.com.businessmeet.R;
import tw.com.businessmeet.RequestCode;
import tw.com.businessmeet.background.FriendInviteService;
import tw.com.businessmeet.background.NotificationService;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.BluetoothHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.helper.PermissionHelper;
import tw.com.businessmeet.network.ApplicationContext;
import tw.com.businessmeet.service.Impl.UserInformationServiceImpl;

//https://codertw.com/android-%E9%96%8B%E7%99%BC/332688/
public class MainActivity extends AppCompatActivity {
    private boolean permission = false;
    private UserInformationDAO userInformationDAO;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApplicationContext.getInstance().init(getApplicationContext());
        userInformationDAO = new UserInformationDAO(new DBHelper(this));

        BluetoothHelper.startBluetooth(this);
        PermissionHelper.requestGPSPermission(this);
        PermissionHelper.requestBluetoothAddressPermission(this);
        permission = false;
        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("MainActivity", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        sharedPreferences.edit().putString("firebaseToken", token).apply();
                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        Thread checkPermission = new Thread() {
            @Override
            public void run() {
                super.run();
                while (!permission) {
                    permission = PermissionHelper.hasAccessCoarseLocation(MainActivity.this, userInformationDAO);
                    if (permission) {
                        String userId = DeviceHelper.getUserId(MainActivity.this, userInformationDAO);
                        Intent intent = new Intent();
                        startService(new Intent(MainActivity.this, NotificationService.class));
                        startService(new Intent(MainActivity.this, FriendInviteService.class));
                        if (userId == "" || userId == null) {
                            intent.setClass(MainActivity.this, LoginActivity.class);
                            MainActivity.this.startActivity(intent);
                            MainActivity.this.finish();
                        } else {
                            AsyncTaskHelper.execute(
                                    () -> UserInformationServiceImpl.getById(userId),
                                    userInformationBean -> {

                                        intent.setClass(MainActivity.this, SelfIntroductionActivity.class);
                                        MainActivity.this.startActivity(intent);
                                        MainActivity.this.finish();
                                    },
                                    (status, message) -> {
                                        intent.setClass(MainActivity.this, LoginActivity.class);
                                        MainActivity.this.startActivity(intent);
                                        MainActivity.this.finish();
                                    }
                            );
                        }
                        Thread.interrupted();

                    }
                }
            }
        };
        checkPermission.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestCode.REQUEST_LOCATION:
                PermissionHelper.onRequestLocationResult(this, userInformationDAO, grantResults);
                break;
            case RequestCode.REQUEST_BLUETOOTH:
                PermissionHelper.onRequestBluetoothResult(this, grantResults);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionHelper.onCheckOpenBluetoothResult(this, requestCode);
    }//onActivityResult
}
