package tw.com.businessmeet.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.ActivityInviteBean;
import tw.com.businessmeet.bean.ResponseBody;

import java.util.List;

public interface ActivityInviteService {
    String baseRoute = "activityinvite/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<ActivityInviteBean>>> search(@Body ActivityInviteBean activityInviteBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<ActivityInviteBean>> add(@Body ActivityInviteBean activityInviteBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<ActivityInviteBean>> update (@Body ActivityInviteBean activityInviteBean);
    @POST(baseRoute+"{activityInviteNo}/delete")
    Call<ResponseBody<Empty>> delete (@Path("activityInviteNo") Integer activityInviteNo);
}
