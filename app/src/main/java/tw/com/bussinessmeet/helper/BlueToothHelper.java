package tw.com.bussinessmeet.helper;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Set;


import tw.com.bussinessmeet.Bean.UserInformationBean;
import tw.com.bussinessmeet.DAO.UserInformationDAO;
import tw.com.bussinessmeet.MatchedDeviceRecyclerViewAdapter;
import tw.com.bussinessmeet.UnmatchedDeviceRecyclerViewAdapter;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class BlueToothHelper {
    private final static int REQUEST_ENABLE_BT = 1;
    private Activity activity;
    private BluetoothAdapter mBluetoothAdapter;
    private IntentFilter filter;
    private UserInformationDAO userInformationDAO;
    private List<UserInformationBean> unmatchedBeanList;
    private List<UserInformationBean> matchedBeanList;

    private MatchedDeviceRecyclerViewAdapter matchedDeviceRecyclerViewAdapter;
    private UnmatchedDeviceRecyclerViewAdapter unmatchedDeviceRecyclerViewAdapter;
    public BlueToothHelper(Activity activity) {
        this.activity = activity;
    }
    public void searchBlueTooth(UserInformationDAO userInformationDAO, MatchedDeviceRecyclerViewAdapter matchedDeviceRecyclerViewAdapter, UnmatchedDeviceRecyclerViewAdapter unmatchedDeviceRecyclerViewAdapter){
        this.userInformationDAO = userInformationDAO;
        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.matchedDeviceRecyclerViewAdapter = matchedDeviceRecyclerViewAdapter;
        this.unmatchedDeviceRecyclerViewAdapter = unmatchedDeviceRecyclerViewAdapter;
        Log.d("resultMainAdapter", String.valueOf(matchedDeviceRecyclerViewAdapter.getItemCount()));
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                UserInformationBean ufb = new UserInformationBean();
                ufb.setBlueTooth(device.getAddress());
                // 遍歷
//                matchedBeanList.add(ufb);
                matchedDeviceRecyclerViewAdapter.dataInsert(ufb);
            }
        }
        activity.registerReceiver(receiver, filter);
    }
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 收到的廣播型別
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 從intent中獲取裝置
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String aa = "";
//                for(int i = 0; i < deviceItems.size(); i++) {
////                    matched = recyclerViewThrmatic.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.matched);
//                    if(matched.getText() !=null) {
//                        aa = matched.getText().toString();
//                        break;
//
//                    }
//                }
                if (aa.contains(device.getAddress())) {
                    return;
                } else {
                    // 判斷是否配對過
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        // 新增到列表
                        short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                        int iRssi = abs(rssi);
                        // 將藍芽訊號強度換算為距離
                        double power = (iRssi - 59) / 25.0;
                        String mm = new Formatter().format("%.2f", pow(10, power)).toString();
                        UserInformationBean ufb = new UserInformationBean();
                        ufb.setBlueTooth(device.getAddress());
//                        Cursor result = userInformationDAO.searchAll(ufb);
//                        result.moveToFirst();
//                        ufb.setAvatar(result.getString(result.getColumnIndex("avatar")));
                        unmatchedDeviceRecyclerViewAdapter.dataInsert(ufb);
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // 關閉進度條
//                activity.setProgressBarIndeterminateVisibility(true);

//                activity.setTitle("搜尋完成！");
//                Log.d("MainActivity",String.valueOf(deviceItems.size()));
//                createRecyclerViewWeather();
                // 用於迴圈掃描藍芽的handler
//                mBLHandler.sendEmptyMessageDelayed(1, 10000);
            }else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                switch (state) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        scanBluth();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        openGPS(activity);
                        bluetooth(activity);
                        break;
                }
            }
        }
    };

    public void startBuleTooth(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            //裝置不支援藍芽
            Toast.makeText(activity, "裝置不支援藍芽", Toast.LENGTH_SHORT).show();
            activity.finish();
        } else{
//            while(!isGpsEnable(this) && !mBluetoothAdapter.isEnabled()){
                openGPS(activity);
            if (!mBluetoothAdapter.isEnabled()) {
                bluetooth(activity);
//                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//                   intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 200);
//        startActivityForResult(intent, 2);
                ActivityCompat.requestPermissions( activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3 );
            } else {
                mBluetoothAdapter.enable();
            }
//            }
            matchedDevices();
            scanBluth();
        }
    }
    public static final boolean isGpsEnable(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }
    public void bluetooth(Context context){
        Intent enableBtIntent = new Intent((BluetoothAdapter.ACTION_REQUEST_ENABLE));
        activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void scanBluth() {
// 設定進度條
        activity.setProgressBarIndeterminateVisibility(true);
        activity.setTitle("正在搜尋...");
// 判斷是否在搜尋,如果在搜尋，就取消搜尋
//        if (mBluetoothAdapter.isDiscovering()) {
//            mBluetoothAdapter.cancelDiscovery();
//        }
// 開始搜尋
//        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
//
//        // 这个可以用来设置时间

        mBluetoothAdapter.startDiscovery();
    }
    public void matchedDevices(){
//        matched.setText("");
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//
//            // 判斷是否有配對過的裝置
        List<String> mDevicesList = new ArrayList<>();
//
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                // 遍歷
//                DeviceItem deviceItem = new DeviceItem();

//                deviceItem.setDeviceAddress(device.getAddress());
//                deviceItems.add(deviceItem);
                mDevicesList.add(device.getAddress());
//                matched.append(device.getName() + " - " + device.getAddress() + "\n");
            }
        }
    }

    public String getMyBuleTooth(){
        return mBluetoothAdapter.getAddress();
    }

    Handler mBLHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    matchedDevices();
                    scanBluth();
                    break;
                default:
                    break;
            }
        }
    };
//    private void createRecyclerViewSearch() {
//        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
//        mainRecyclerViewAdapter = new SearchRecyclerViewAdapter(this, this.userInformationBeanList);
//        mainRecyclerViewAdapter.setClickListener(this);
//        recyclerViewSearch.setAdapter(mainRecyclerViewAdapter);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewSearch.getContext(), DividerItemDecoration.VERTICAL);
//        recyclerViewSearch.addItemDecoration(dividerItemDecoration);
//    }

}