package tw.com.businessmeet.device.bluetooth.connector;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.BluetoothHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;

public class BluetoothConnectClient extends Thread {
    public static final String ACTION_REQUEST_ADD_FRIEND = "tw.com.businessmeet.action.REQUEST_ADD_FRIEND";

    private final Context context;
    private final BluetoothDevice connectDevice;

    public BluetoothConnectClient(Context context, String bluetooth) throws IOException {
        this(context, BluetoothAdapter.getDefaultAdapter().getRemoteDevice(bluetooth));
    }

    public BluetoothConnectClient(Context context, BluetoothDevice connectDevice) throws IOException {
        this.context = context;
        this.connectDevice = connectDevice;
    }

    @Override
    public void run() {
        super.run();
        BluetoothHelper.cancelDiscovery();
        try (BluetoothConnector connector = new BluetoothConnector(connectDevice)) {
            connector.connect();
            JSONObject message = new JSONObject();
            message.put("address", DeviceHelper.getIdentifier(context));
            DBHelper dbHelper = new DBHelper(context);
            UserInformationDAO userInformationDAO = new UserInformationDAO(dbHelper);
            message.put("name", DeviceHelper.getUserName(context, userInformationDAO));
            connector.write(message.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
