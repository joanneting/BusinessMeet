package tw.com.businessmeet.device.bluetooth.connector;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import tw.com.businessmeet.helper.BluetoothHelper;

public class BluetoothConnector implements Closeable {
    private BluetoothSocket connection;
    private BufferedReader input;
    private PrintWriter output;

    public BluetoothConnector(BluetoothDevice device) throws IOException {
        this(device.createRfcommSocketToServiceRecord(BluetoothHelper.BLUETOOTH_UUID));
    }

    public BluetoothConnector(BluetoothSocket connection) throws IOException {
        this.connection = connection;
        resetIO(connection);
    }

    private void resetIO(BluetoothSocket connection) throws IOException {
        this.input = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        this.output = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"), true);
    }

    public void connect() {
        try {
            connection.connect();
        } catch (IOException e) {
            try {
                connection = getReflectSocket();
                connection.connect();
                resetIO(connection);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private BluetoothSocket getReflectSocket() {
        try {
            Class<? extends BluetoothDevice> deviceClass = connection.getRemoteDevice().getClass();
            Method method = deviceClass.getMethod("createRfcommSocket", int.class);
            return (BluetoothSocket) method.invoke(connection.getRemoteDevice(), 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isConnected() {
        return connection.isConnected();
    }

    public String readLine() throws IOException {
        return input.readLine();
    }

    public void write(String output) {
        this.output.println(output);
    }

    @Override
    public void close() throws IOException {
        if (connection.isConnected()) {
            connection.close();
        }
        input.close();
        output.close();
    }
}
