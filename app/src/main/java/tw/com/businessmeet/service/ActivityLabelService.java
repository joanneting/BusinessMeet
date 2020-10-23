package tw.com.businessmeet.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tw.com.businessmeet.bean.ActivityLabelBean;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.ResponseBody;

import java.util.List;

public interface ActivityLabelService {
    String baseRoute = "activitylabel/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<ActivityLabelBean>>> search(@Body ActivityLabelBean activityLabelBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<ActivityLabelBean>> add(@Body ActivityLabelBean activityLabelBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<ActivityLabelBean>> update (@Body ActivityLabelBean activityLabelBean);
    @POST(baseRoute+"{activityLabelNo}/delete")
    Call<ResponseBody<Empty>> delete (@Path("activityLabelNo") Integer activityLabelNo);
}
