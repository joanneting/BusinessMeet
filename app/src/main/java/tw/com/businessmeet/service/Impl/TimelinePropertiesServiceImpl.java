package tw.com.businessmeet.service.Impl;

import retrofit2.Call;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.TimelinePropertiesBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.service.TimelinePropertiesService;

import java.util.List;

import static tw.com.businessmeet.network.RetrofitConfig.retrofit;

public class TimelinePropertiesServiceImpl implements TimelinePropertiesService{
    private static TimelinePropertiesService TimelinePropertiesApi = retrofit.create(TimelinePropertiesService.class);

    @Override
    public Call<ResponseBody<List<TimelinePropertiesBean>>> search(TimelinePropertiesBean timelinePropertiesBean) {
        return TimelinePropertiesApi.search(timelinePropertiesBean);
    }

    @Override
    public Call<ResponseBody<TimelinePropertiesBean>> add(TimelinePropertiesBean timelinePropertiesBean) {
        return TimelinePropertiesApi.add(timelinePropertiesBean);
    }

    @Override
    public Call<ResponseBody<TimelinePropertiesBean>> update(TimelinePropertiesBean timelinePropertiesBean) {
        return TimelinePropertiesApi.update(timelinePropertiesBean);
    }

    @Override
    public Call<ResponseBody<Empty>> delete(Integer timelinePropertiesNo) {
        return TimelinePropertiesApi.delete(timelinePropertiesNo);
    }
}
