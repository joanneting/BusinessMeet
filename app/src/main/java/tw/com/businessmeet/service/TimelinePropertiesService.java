package tw.com.businessmeet.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.TimelinePropertiesBean;

import java.util.List;

public interface TimelinePropertiesService {
    String baseRoute = "timelinepropertirs/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<TimelinePropertiesBean>>> search(@Body TimelinePropertiesBean timelinePropertiesBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<TimelinePropertiesBean>> add(@Body TimelinePropertiesBean timelinePropertiesBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<TimelinePropertiesBean>> update (@Body TimelinePropertiesBean timelinePropertiesBean);
    @POST(baseRoute+"{timelinePropertiesNo}/delete")
    Call<ResponseBody<Empty>> delete (@Path("timelinePropertiesNo") Integer timelinePropertiesNo);
}
