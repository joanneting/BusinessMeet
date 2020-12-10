package tw.com.businessmeet.service.Impl;

import java.util.List;

import retrofit2.Call;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.FriendCustomizationBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.service.FriendCustomizationService;

import static tw.com.businessmeet.network.RetrofitConfig.retrofit;

public class FriendCustomizationServiceImpl {
    private static FriendCustomizationService FriendCustomizationApi = retrofit.create(FriendCustomizationService.class);

    public static Call<ResponseBody<List<FriendCustomizationBean>>> search(FriendCustomizationBean friendCustomizationBean) {
        return FriendCustomizationApi.search(friendCustomizationBean);
    }

    public static Call<ResponseBody<FriendCustomizationBean>> add(FriendCustomizationBean friendCustomizationBean) {
        return FriendCustomizationApi.add(friendCustomizationBean);
    }

    public static Call<ResponseBody<FriendCustomizationBean>> update(FriendCustomizationBean friendCustomizationBean) {
        return FriendCustomizationApi.update(friendCustomizationBean);
    }

    public static Call<ResponseBody<Empty>> delete(Integer friendCustomizationNo) {
        return FriendCustomizationApi.delete(friendCustomizationNo);
    }
}
