package tw.com.businessmeet.service.Impl;

import retrofit2.Call;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.TimelineBean;
import tw.com.businessmeet.service.TimelineService;

import java.util.List;

import static tw.com.businessmeet.network.RetrofitConfig.retrofit;

public class TimelineServiceImpl implements TimelineService{
    private static TimelineService TimelineApi = retrofit.create(TimelineService.class);

    @Override
    public Call<ResponseBody<List<TimelineBean>>> search(TimelineBean timelineBean) {
        return TimelineApi.search(timelineBean);
    }
    @Override
    public Call<ResponseBody<List<TimelineBean>>> searchList(TimelineBean timelineBean) {
        return TimelineApi.searchList(timelineBean);
    }

    @Override
    public Call<ResponseBody<TimelineBean>> add(TimelineBean timelineBean) {
        return TimelineApi.add(timelineBean);
    }

    @Override
    public Call<ResponseBody<TimelineBean>> update(TimelineBean timelineBean) {
        return TimelineApi.update(timelineBean);
    }

    @Override
    public Call<ResponseBody<Empty>> delete(Integer timelineNo) {
        return TimelineApi.delete(timelineNo);
    }
    @Override
    public Call<ResponseBody<TimelineBean>> getById(Integer timelineNo) {
        return TimelineApi.getById(timelineNo);
    }
}
