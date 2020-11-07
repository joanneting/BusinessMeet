package tw.com.businessmeet.device.actionhandler;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.FriendDAO;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.device.DeviceFinder;
import tw.com.businessmeet.device.FoundedDeviceDetail;
import tw.com.businessmeet.device.MatchListener;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;
import tw.com.businessmeet.service.Impl.UserInformationServiceImpl;

public class ForegroundFoundActionHandler extends AbstractFoundActionHandler {
    private final Set<String> blueToothSet = new HashSet<>();
    private DBHelper dbHelper;
    private MatchListener matchListener;
    private UserInformationDAO userInformationDAO;
    private FriendDAO friendDAO;

    public ForegroundFoundActionHandler(MatchListener matchListener) {
        this(null, matchListener);
    }

    public ForegroundFoundActionHandler(DBHelper dbHelper, MatchListener matchListener) {
        this.dbHelper = dbHelper;
        this.matchListener = matchListener;
        if (dbHelper != null) {
            userInformationDAO = new UserInformationDAO(dbHelper);
            friendDAO = new FriendDAO(dbHelper);
        }
    }

    @Override
    public void handle(Context context, Intent intent) {
        FoundedDeviceDetail deviceDetail = intent.getParcelableExtra(DeviceFinder.EXTRA_FOUNDED_DEVICE_DETAIL);
        if (deviceDetail != null && blueToothSet.add(deviceDetail.getIdentifier())) {
            if (dbHelper == null) {
                dbHelper = new DBHelper(context);
                userInformationDAO = new UserInformationDAO(dbHelper);
                friendDAO = new FriendDAO(dbHelper);
            }
            String userId = userInformationDAO.getId(deviceDetail.getIdentifier());
            if (userId != null) {
                Cursor cursor = userInformationDAO.getById(userId);
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String avatar = cursor.getString(cursor.getColumnIndex("avatar"));
                String profession = cursor.getString(cursor.getColumnIndex("profession"));
                UserInformationBean userInformationBean = new UserInformationBean();
                userInformationBean.setUserId(userId);
                userInformationBean.setProfession(profession);
                userInformationBean.setName(name);
                userInformationBean.setAvatar(avatar);
                searchFriend(userInformationBean);
            } else {
                AsyncTaskHelper.execute(
                        () -> UserInformationServiceImpl.getByIdentifier(deviceDetail.getIdentifier()), userInformationBean -> {
                            userInformationDAO.add(userInformationBean);
                            searchFriend(userInformationBean);
                        }
                );
            }
        }
    }

    private void searchFriend(UserInformationBean userInformationBean) {
        FriendBean friendBean = new FriendBean();
        friendBean.setMatchmakerId(DeviceHelper.getUserId(dbHelper.getContext(), userInformationDAO));
        friendBean.setFriendId(userInformationBean.getUserId());
        Cursor cursor = friendDAO.search(friendBean);
        if (cursor != null) {
            String friendNo = cursor.getString(cursor.getColumnIndex("friend_no"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            friendBean.setFriendNo(Integer.parseInt(friendNo));
            friendBean.setRemark(remark);
            List<FriendBean> friendBeanList = new ArrayList<>();
            friendBeanList.add(friendBean);
            checkFriendMatched(userInformationBean, friendBeanList);
        } else {
            AsyncTaskHelper.execute(
                    () -> FriendServiceImpl.search(friendBean),
                    friendBeanList -> checkFriendMatched(userInformationBean, friendBeanList)
            );
        }
    }

    private void checkFriendMatched(UserInformationBean userInformationBean, List<FriendBean> friendBeanList) {
        FriendBean friendBean = friendBeanList.get(0);
        if (friendBeanList.size() > 1 ||
                (friendBeanList.size() == 1 && friendBean.getCreateDate() != null)
        ) {
            matchListener.onMatched(userInformationBean);
        } else {
            matchListener.onUnmatched(userInformationBean);
        }
    }
}
