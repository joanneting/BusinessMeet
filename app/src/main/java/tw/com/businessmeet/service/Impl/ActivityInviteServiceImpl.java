package tw.com.businessmeet.service.Impl;

import java.util.List;

import retrofit2.Call;
import tw.com.businessmeet.bean.ActivityInviteBean;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.service.ActivityInviteService;

import static tw.com.businessmeet.network.RetrofitConfig.retrofit;

public class ActivityInviteServiceImpl {
    private static ActivityInviteService ActivityInviteApi = retrofit.create(ActivityInviteService.class);

    public static Call<ResponseBody<List<ActivityInviteBean>>> search(ActivityInviteBean activityInviteBean) {
        return ActivityInviteApi.search(activityInviteBean);
    }

    public static Call<ResponseBody<ActivityInviteBean>> add(ActivityInviteBean activityInviteBean) {
        return ActivityInviteApi.add(activityInviteBean);
    }

    public static Call<ResponseBody<ActivityInviteBean>> update(ActivityInviteBean activityInviteBean) {
        return ActivityInviteApi.update(activityInviteBean);
    }

    public static Call<ResponseBody<Empty>> delete(Integer activityInviteNo) {
        return ActivityInviteApi.delete(activityInviteNo);
    }
}
