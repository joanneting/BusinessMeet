package tw.com.businessmeet.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.FriendGroupBean;
import tw.com.businessmeet.bean.ResponseBody;

import java.util.List;

public interface FriendGroupService {
    String baseRoute = "friendgroup/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<FriendGroupBean>>> search(@Body FriendGroupBean friendGroupBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<FriendGroupBean>> add(@Body FriendGroupBean friendGroupBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<FriendGroupBean>> update (@Body FriendGroupBean friendGroupBean);
    @POST(baseRoute+"{friendGroupNo}/delete")
    Call<ResponseBody<Empty>> delete (@Path("friendGroupNo") Integer friendGroupNo);
    @POST(baseRoute+"search/count")
    Call<ResponseBody<List<FriendGroupBean>>> searchCount();
}
