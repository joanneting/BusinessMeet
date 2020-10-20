package tw.com.businessmeet.device.actionhandler;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import tw.com.businessmeet.helper.BluetoothHelper;

public class BackgroundStateChangeActionHandler extends AbstractStateChangeActionHandler {

    @Override
    public void handle(Context context, Intent intent) {
        switch (getState(intent)) {
            case STATE_ON:
                BluetoothHelper.startDiscovery();
                break;
            case STATE_OFF:
//                Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//                enable.putExtra(
//                        BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
//                        3600);
//                notificationService.startActivityForResult(enable, RequestCode.REQUEST_DISCOVERABLE);
                Toast.makeText(context, "檢測到您的藍芽已關閉，請至設定開啟。", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
