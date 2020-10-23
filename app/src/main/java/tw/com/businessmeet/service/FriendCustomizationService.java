package tw.com.businessmeet.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tw.com.businessmeet.bean.FriendCustomizationBean;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.ResponseBody;

import java.util.List;

public interface FriendCustomizationService {
    String baseRoute = "friendcustomization/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<FriendCustomizationBean>>> search(@Body FriendCustomizationBean friendCustomizationBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<FriendCustomizationBean>> add(@Body FriendCustomizationBean friendCustomizationBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<FriendCustomizationBean>> update (@Body FriendCustomizationBean friendCustomizationBean);
    @POST(baseRoute+"{friendCustomizationNo}/delete")
    Call<ResponseBody<Empty>> delete (@Path("friendCustomizationNo") Integer friendCustomizationNo);
}
