package tw.com.businessmeet.service.Impl;

import java.util.List;

import retrofit2.Call;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.TimelineBean;
import tw.com.businessmeet.service.TimelineService;

import static tw.com.businessmeet.network.RetrofitConfig.retrofit;

public class TimelineServiceImpl {
    private static TimelineService TimelineApi = retrofit.create(TimelineService.class);

    public static Call<ResponseBody<List<TimelineBean>>> search(TimelineBean timelineBean) {
        return TimelineApi.search(timelineBean);
    }

    public static Call<ResponseBody<List<TimelineBean>>> searchList(TimelineBean timelineBean) {
        return TimelineApi.searchList(timelineBean);
    }

    public static Call<ResponseBody<TimelineBean>> add(TimelineBean timelineBean) {
        return TimelineApi.add(timelineBean);
    }

    public static Call<ResponseBody<TimelineBean>> update(TimelineBean timelineBean) {
        return TimelineApi.update(timelineBean);
    }

    public static Call<ResponseBody<Empty>> delete(Integer timelineNo) {
        return TimelineApi.delete(timelineNo);
    }

    public static Call<ResponseBody<TimelineBean>> getById(Integer timelineNo) {
        return TimelineApi.getById(timelineNo);
    }
}
