package tw.com.businessmeet.service.Impl;

import retrofit2.Call;
import tw.com.businessmeet.bean.ActivityRemindBean;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.service.ActivityRemindService;

import java.util.List;

import static tw.com.businessmeet.network.RetrofitConfig.retrofit;

public class ActivityRemindServiceImpl implements ActivityRemindService{
    private static ActivityRemindService ActivityRemindApi = retrofit.create(ActivityRemindService.class);

    @Override
    public Call<ResponseBody<List<ActivityRemindBean>>> search(ActivityRemindBean activityRemindBean) {
        return ActivityRemindApi.search(activityRemindBean);
    }

    @Override
    public Call<ResponseBody<ActivityRemindBean>> add(ActivityRemindBean activityRemindBean) {
        return ActivityRemindApi.add(activityRemindBean);
    }

    @Override
    public Call<ResponseBody<ActivityRemindBean>> update(ActivityRemindBean activityRemindBean) {
        return ActivityRemindApi.update(activityRemindBean);
    }

    @Override
    public Call<ResponseBody<Empty>> delete(Integer activityRemindNo) {
        return ActivityRemindApi.delete(activityRemindNo);
    }
}
