package tw.com.businessmeet.helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import tw.com.businessmeet.RequestCode;
import tw.com.businessmeet.dao.UserInformationDAO;

public class PermissionHelper {
    private static boolean login = false;

    public static void requestBluetoothAddressPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, RequestCode.REQUEST_BLUETOOTH);
    }

    public static void onRequestBluetoothResult(Context context, int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "本App需要同意此權限才可使用", Toast.LENGTH_LONG).show();
        }
    }

    public static void requestGPSPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RequestCode.REQUEST_LOCATION);
    }

    public static boolean hasAccessCoarseLocation(Activity activity) {
        return hasAccessCoarseLocation(activity, new UserInformationDAO(new DBHelper(activity)));
    }

    public static boolean hasAccessCoarseLocation(Activity activity, UserInformationDAO userInformationDAO) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return permissionCheck == PackageManager.PERMISSION_GRANTED && bluetoothAdapter.isEnabled();
    }

    public static void onRequestLocationResult(Activity activity, int[] grantResults) {
        onRequestLocationResult(activity, new UserInformationDAO(new DBHelper(activity)), grantResults);
    }

    public static void onRequestLocationResult(Activity activity, UserInformationDAO userInformationDAO, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            // 不等於 PERMISSION_GRANTED 代表被按下拒絕
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                Toast.makeText(activity, "這些權限是為了搜尋附近藍芽裝置，拒絕將無法使用本應用程式。", Toast.LENGTH_LONG).show();
            } else {
                new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setTitle("這些權限是為了搜尋附近藍芽裝置，拒絕將無法使用本應用程式。")
                        .setPositiveButton("我需要此權限!", (dialog, which) -> requestGPSPermission(activity))
                        .show();
            }
        } else {
            PermissionHelper.hasAccessCoarseLocation(activity, userInformationDAO);
        }
    }

    public static void onCheckOpenBluetoothResult(Activity activity, int requestCode) {
        if (requestCode == RequestCode.REQUEST_DISCOVERABLE) {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!bluetoothAdapter.isEnabled()) {
                Toast.makeText(activity, "開啟藍芽位置才可搜尋附近藍牙裝置，拒絕將無法使用本應用程式。", Toast.LENGTH_LONG).show();
                BluetoothHelper.startBluetooth(activity);
            } else {
                Toast.makeText(activity, "已開啟藍牙", Toast.LENGTH_LONG).show();
            }
        }
    }
}
