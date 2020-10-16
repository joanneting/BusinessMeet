package tw.com.businessmeet.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.ResponseBody;

public interface FriendService {
    String baseRoute = "friend/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<FriendBean>>> search(@Body FriendBean friendBean);
    @POST(baseRoute+"search/invitelist")
    Call<ResponseBody<List<FriendBean>>> searchInviteList(@Body FriendBean friendBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<FriendBean>> add(@Body FriendBean friendBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<FriendBean>> update (@Body FriendBean friendBean);
    @POST(baseRoute+"delete/{friendNo}")
    Call<ResponseBody<Empty>> delete (@Path("friendNo") Integer friendNo);
}
