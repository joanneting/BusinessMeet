package tw.com.businessmeet.service.Impl;

import java.util.List;

import retrofit2.Call;
import tw.com.businessmeet.bean.LoginBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.service.UserInformationService;

import static tw.com.businessmeet.network.RetrofitConfig.retrofit;

public class UserInformationServiceImpl {
    private static final UserInformationService userInformationAPI = retrofit.create(UserInformationService.class);

    public static Call<ResponseBody<List<UserInformationBean>>> search(UserInformationBean userInformationBean) {
        return userInformationAPI.search(userInformationBean);
    }

    public static Call<ResponseBody<UserInformationBean>> add(UserInformationBean userInformationBean) {
        return userInformationAPI.add(userInformationBean);
    }

    public static Call<ResponseBody<UserInformationBean>> update(UserInformationBean userinformationBean) {
        return userInformationAPI.update(userinformationBean);
    }

    public static Call<ResponseBody<UserInformationBean>> getById(String userId) {
        return userInformationAPI.getById(userId);
    }

    public static Call<ResponseBody<UserInformationBean>> getByIdentifier(String identifier) {
        return userInformationAPI.getByIdentifier(identifier);
    }

    public static Call<ResponseBody<LoginBean>> login(UserInformationBean userInformationBean) {
        return userInformationAPI.login(userInformationBean);
    }
}
