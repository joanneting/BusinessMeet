package tw.com.businessmeet.device.actionhandler;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.Locale;

import tw.com.businessmeet.background.NotificationService;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.TimelineBean;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.TimelineDAO;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.device.DeviceFinder;
import tw.com.businessmeet.device.FoundedDeviceDetail;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.helper.NotificationHelper;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;
import tw.com.businessmeet.service.Impl.TimelineServiceImpl;
import tw.com.businessmeet.service.Impl.UserInformationServiceImpl;

public class BackgroundFoundActionHandler extends AbstractFoundActionHandler {
    private int distance;
    private DBHelper dbHelper;
    private NotificationService notificationService;
    private NotificationHelper notificationHelper;
    private LocationManager locationManager;
    private LocationListener locationListener = new MyLocationListener();
    private double longitude;
    private double latitude;

    public BackgroundFoundActionHandler(NotificationService notificationService) {
        this(notificationService, null);
    }

    public BackgroundFoundActionHandler(NotificationService notificationService, DBHelper dbHelper) {
        this.notificationService = notificationService;
        this.notificationHelper = new NotificationHelper(notificationService);
        this.locationManager = (LocationManager) notificationService.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(
                notificationService,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(notificationService, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(notificationService, "請至設定開啟定位功能", Toast.LENGTH_SHORT).show();
            return;
        }
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1800000, 0, locationListener);
        this.dbHelper = dbHelper;
    }

    @Override
    public void handle(Context context, Intent intent) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
        FoundedDeviceDetail deviceDetail = intent.getParcelableExtra(DeviceFinder.EXTRA_FOUNDED_DEVICE_DETAIL);
        if (deviceDetail != null) {
            String identifier = deviceDetail.getIdentifier();
            distance = (int) deviceDetail.getDistance();
            AsyncTaskHelper.execute(
                    () -> UserInformationServiceImpl.getByIdentifier(identifier),
                    this::searchFriend
            );
        }
    }

    private void searchFriend(UserInformationBean userInformationBean) {
        String searchId = userInformationBean.getUserId();
        FriendBean friendBean = new FriendBean();
        UserInformationDAO userInformationDAO = new UserInformationDAO(dbHelper);
        friendBean.setMatchmakerId(DeviceHelper.getUserId(dbHelper.getContext(), userInformationDAO));
        friendBean.setFriendId(searchId);
        AsyncTaskHelper.execute(
                () -> FriendServiceImpl.search(friendBean),
                friendBeanList -> checkFriendMatched(userInformationBean, friendBeanList)
        );
    }

    private void checkFriendMatched(UserInformationBean userInformationBean, List<FriendBean> friendBeanList) {
        FriendBean friendBean = friendBeanList.get(0);
        if (friendBeanList.size() > 1 ||
                (friendBeanList.size() == 1 && friendBeanList.get(0).getCreateDate() != null)
        ) {

//            if (distance <= 100000) {
            if (ActivityCompat.checkSelfPermission(notificationService, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(notificationService, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }

            //更新位置
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            TimelineBean timelineBean = new TimelineBean();
            timelineBean.setFriendId(friendBean.getFriendId());
            timelineBean.setMatchmakerId(friendBean.getMatchmakerId());
            Geocoder gc = new Geocoder(notificationService, Locale.TRADITIONAL_CHINESE);


            try {
                longitude = location.getLongitude();        //取得經度
                latitude = location.getLatitude();
                List<Address> lstAddress = gc.getFromLocation(latitude, longitude, 1);
//                    Toast.makeText(
//                            notificationService.getBaseContext(),
//                            lstAddress.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                timelineBean.setPlace(lstAddress.get(0).getAddressLine(0));
                locationManager.removeUpdates(locationListener);
            } catch (Exception e) {
                e.printStackTrace();
                timelineBean.setPlace("室內");
            }
//                    if (!Geocoder.isPresent()){ //Since: API Level 9
//                        returnAddress = "Sorry! Geocoder service not Present.";
//                    }
            timelineBean.setTimelinePropertiesNo(2);

            timelineBean.setTitle(timelineBean.getPlace());
            TimelineDAO timelineDAO = new TimelineDAO(dbHelper);
            TimelineBean searchBean = new TimelineBean();
            searchBean.setFriendId(friendBean.getFriendId());
            searchBean.setMatchmakerId(friendBean.getMatchmakerId());
            Cursor cursor = timelineDAO.search(searchBean);
            String lastMeetPlace = "";
            if (cursor != null && cursor.moveToLast()) {
                lastMeetPlace = cursor.getString(cursor.getColumnIndex("place"));
            }
            AsyncTaskHelper.execute(
                    () -> TimelineServiceImpl.add(timelineBean),
                    timelineDAO::add
            );

            notificationHelper.sendBackgroundMessage(userInformationBean, lastMeetPlace);
        }
//        }
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
//            Toast.makeText(
//                    notificationService,
//                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
//                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            longitude = loc.getLongitude();
            latitude = loc.getLatitude();
            /*------- To get city name from coordinates -------- */
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}
