package tw.com.businessmeet.device.actionhandler;

import android.content.Context;
import android.content.Intent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.UserInformationBean;
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

    public ForegroundFoundActionHandler(MatchListener matchListener) {
        this(null, matchListener);
    }

    public ForegroundFoundActionHandler(DBHelper dbHelper, MatchListener matchListener) {
        this.dbHelper = dbHelper;
        this.matchListener = matchListener;
    }

    @Override
    public void handle(Context context, Intent intent) {
        FoundedDeviceDetail deviceDetail = intent.getParcelableExtra(DeviceFinder.EXTRA_FOUNDED_DEVICE_DETAIL);
        if (deviceDetail != null && blueToothSet.add(deviceDetail.getIdentifier())) {
            if (dbHelper == null) {
                dbHelper = new DBHelper(context);
            }
            AsyncTaskHelper.execute(
                    () -> UserInformationServiceImpl.getByIdentifier(deviceDetail.getIdentifier()),
                    this::searchFriend
            );
        }
    }

    private void searchFriend(UserInformationBean userInformationBean) {
        UserInformationDAO userInformationDAO = new UserInformationDAO(dbHelper);
        String userId = userInformationDAO.getId(userInformationBean.getIdentifier());
        if (userId == null) {
            userInformationDAO.add(userInformationBean);
        }
        FriendBean friendBean = new FriendBean();
        friendBean.setMatchmakerId(DeviceHelper.getUserId(dbHelper.getContext(), userInformationDAO));
        friendBean.setFriendId(userInformationBean.getUserId());
        AsyncTaskHelper.execute(
                () -> FriendServiceImpl.search(friendBean),
                friendBeanList -> checkFriendMatched(userInformationBean, friendBeanList)
        );
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
