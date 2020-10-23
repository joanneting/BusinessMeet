package tw.com.businessmeet.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.UserCustomizationBean;

import java.util.List;

public interface UserCustomizationService {
    String baseRoute = "usercustomization/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<UserCustomizationBean>>> search(@Body UserCustomizationBean userCustomizationBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<UserCustomizationBean>> add(@Body UserCustomizationBean userCustomizationBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<UserCustomizationBean>> update (@Body UserCustomizationBean userCustomizationBean);
    @POST(baseRoute+"{userCustomizationNo}/delete")
    Call<ResponseBody<Empty>> delete(@Path("userCustomizationNo") Integer userCustomizationNo);
}
