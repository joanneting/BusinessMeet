package tw.com.businessmeet.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.GroupsBean;
import tw.com.businessmeet.bean.ResponseBody;

import java.util.List;

public interface GroupsService {
    String baseRoute = "groups/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<GroupsBean>>> search(@Body GroupsBean groupsBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<GroupsBean>> add(@Body GroupsBean groupsBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<GroupsBean>> update (@Body GroupsBean groupsBean);
    @POST(baseRoute+"{groupsNo}/delete")
    Call<ResponseBody<Empty>> delete (@Path("groupsNo") Integer groupsNo);
}
