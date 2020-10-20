package tw.com.businessmeet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import tw.com.businessmeet.background.FriendInviteService;
import tw.com.businessmeet.background.NotificationService;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.BluetoothHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.PermissionHelper;
import tw.com.businessmeet.network.ApplicationContext;

//https://codertw.com/android-%E9%96%8B%E7%99%BC/332688/
public class MainActivity extends AppCompatActivity {
    private boolean permission = false;
    private UserInformationDAO userInformationDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, NotificationService.class));
        startService(new Intent(this, FriendInviteService.class));
        ApplicationContext.getInstance().init(getApplicationContext());
        userInformationDAO = new UserInformationDAO(new DBHelper(this));

        BluetoothHelper.startBluetooth(this);
        PermissionHelper.requestGPSPermission(this);
        PermissionHelper.requestBluetoothAddressPermission(this);
        permission = false;
        Thread checkPermission = new Thread() {
            @Override
            public void run() {
                super.run();
                while (!permission) {
                    permission = PermissionHelper.hasAccessCoarseLocation(MainActivity.this, userInformationDAO);
                    if (permission) {
                        Thread.interrupted();
                    }
                }
                Log.d("resultthread", String.valueOf(permission));
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
