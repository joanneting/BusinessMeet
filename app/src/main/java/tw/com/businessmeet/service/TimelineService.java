package tw.com.businessmeet.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.TimelineBean;
import tw.com.businessmeet.bean.ResponseBody;

import java.util.List;

public interface TimelineService {
    String baseRoute = "timeline/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<TimelineBean>>> search(@Body TimelineBean timelineBean);
    @POST(baseRoute+"searchlist")
    Call<ResponseBody<List<TimelineBean>>> searchList(@Body TimelineBean timelineBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<TimelineBean>> add(@Body TimelineBean timelineBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<TimelineBean>> update (@Body TimelineBean timelineBean);
    @POST(baseRoute+"{timelineNo}/delete")
    Call<ResponseBody<Empty>> delete (@Path("timelineNo") Integer timelineNo);
    @POST(baseRoute+"get/{timelineNo}")
    Call<ResponseBody<TimelineBean>> getById (@Path("timelineNo") Integer timelineNo);
}
