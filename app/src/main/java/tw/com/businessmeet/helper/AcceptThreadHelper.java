package tw.com.businessmeet.helper;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class AcceptThreadHelper extends Thread {
    private BluetoothServerSocket serverSocket;
    private BluetoothSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    // 为其链接创建一个名称
    private final String NAME = "Bluetooth_Socket";
    private BluetoothAdapter mBlueToothAdapter;
    private Activity activity;
    private Handler handler;

    public AcceptThreadHelper(BluetoothAdapter mBlueToothAdapter, UUID UUID, Activity activity, Handler handler) {
        this.mBlueToothAdapter = mBlueToothAdapter;
        this.activity = activity;
        this.handler = handler;
        try {
            serverSocket = this.mBlueToothAdapter.listenUsingRfcommWithServiceRecord(NAME, UUID);
        } catch (Exception e) {

        }
    }

    @Override
    public void run() {
        super.run();
        try {
            socket = serverSocket.accept();
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            while (true) {
                byte[] buffer = new byte[128];
                int count = inputStream.read(buffer);
                Message message = new Message();
                message.obj = new String(buffer, 0, count, "utf-8");
                handler.sendMessage(message);
            }

        } catch (Exception e) {

        }
    }
}
