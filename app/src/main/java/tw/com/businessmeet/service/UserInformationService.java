package tw.com.businessmeet.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.LoginBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.UserInformationBean;

public interface UserInformationService {
    String baseRoute = "userinformation/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<UserInformationBean>>> search(@Body UserInformationBean userInformationBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<UserInformationBean>> add(@Body UserInformationBean userInformationBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<UserInformationBean>> update (@Body UserInformationBean userinformationBean);
    @POST(baseRoute+"get/{userId}")
    Call<ResponseBody<UserInformationBean>> getById(@Path("userId") String userId);
    @POST(baseRoute+"getbyidentifier/{identifier}")
    Call<ResponseBody<UserInformationBean>> getByIdentifier(@Path("identifier") String identifier);
    @POST("login")
    Call<ResponseBody<LoginBean>> login(@Body UserInformationBean userInformationBean);
}
