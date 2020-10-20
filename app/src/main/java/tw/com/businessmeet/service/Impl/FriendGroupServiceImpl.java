package tw.com.businessmeet.service.Impl;

import androidx.annotation.Nullable;

import retrofit2.Call;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.FriendGroupBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.service.FriendGroupService;

import java.util.List;

import static tw.com.businessmeet.network.RetrofitConfig.retrofit;

public class FriendGroupServiceImpl{
    private static FriendGroupService FriendGroupApi = retrofit.create(FriendGroupService.class);

    @Nullable
    public static Call<ResponseBody<List<FriendGroupBean>>> search(FriendGroupBean friendGroupBean) {
        return FriendGroupApi.search(friendGroupBean);
    }

    @Nullable
    public static Call<ResponseBody<FriendGroupBean>> add(FriendGroupBean friendGroupBean) {
        return FriendGroupApi.add(friendGroupBean);
    }

    @Nullable
    public static Call<ResponseBody<FriendGroupBean>> update(FriendGroupBean friendGroupBean) {
        return FriendGroupApi.update(friendGroupBean);
    }

    @Nullable
    public static Call<ResponseBody<Empty>> delete(Integer friendGroupNo) {
        return FriendGroupApi.delete(friendGroupNo);
    }

    @Nullable
    public static Call<ResponseBody<List<FriendGroupBean>>> searchCount() {
        return FriendGroupApi.searchCount();
    }

    @Nullable
    public static Call<ResponseBody<List<FriendGroupBean>>> searchFriendByGroup(Integer groupNo) {
        return FriendGroupApi.searchFriendByGroup(groupNo);
    }
}
