package tw.com.businessmeet.helper;

import android.content.Context;
import android.os.Build;

import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.device.beacon.BeaconSharedPreferences;

public class DeviceHelper {

    public static String getIdentifier(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BeaconSharedPreferences beaconSharedPreferences = new BeaconSharedPreferences(context);
            return beaconSharedPreferences.getIdentifier();
        } else {
            return BluetoothHelper.getDeviceBluetoothMacAddress(context.getContentResolver());
        }
    }

    public static String getUserId(Context context) {
        return getUserId(context, new UserInformationDAO(new DBHelper(context)));
    }

    public static String getUserId(Context context, UserInformationDAO userInformationDAO) {
        String bluetoothMacAddress = getIdentifier(context);
        String userId = "";
        if (bluetoothMacAddress != null && !bluetoothMacAddress.equals("02:00:00:00:00:00")) {
            userId = userInformationDAO.getId(bluetoothMacAddress);
        }
        return userId;
    }

    public static String getUserName(Context context) {
        return getUserName(context, new UserInformationDAO(new DBHelper(context)));
    }

    public static String getUserName(Context context, UserInformationDAO userInformationDAO) {
        String identifier = getIdentifier(context);
        String result = "";
        if (identifier != null && !identifier.equals("02:00:00:00:00:00")) {
            result = userInformationDAO.getName(identifier);
        }
        return result;
    }
}
