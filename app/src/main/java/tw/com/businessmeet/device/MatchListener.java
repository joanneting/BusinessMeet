package tw.com.businessmeet.device;

import tw.com.businessmeet.bean.UserInformationBean;

public interface MatchListener {
    void onMatched(UserInformationBean userInformationBean);

    void onUnmatched(UserInformationBean userInformationBean);
}
