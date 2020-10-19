package tw.com.businessmeet.service.Impl;

import retrofit2.Call;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.FriendGroupBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.service.FriendGroupService;

import java.util.List;

import static tw.com.businessmeet.network.RetrofitConfig.retrofit;

public class FriendGroupServiceImpl implements FriendGroupService{
    private static FriendGroupService FriendGroupApi = retrofit.create(FriendGroupService.class);

    @Override
    public Call<ResponseBody<List<FriendGroupBean>>> search(FriendGroupBean friendGroupBean) {
        return FriendGroupApi.search(friendGroupBean);
    }

    @Override
    public Call<ResponseBody<FriendGroupBean>> add(FriendGroupBean friendGroupBean) {
        return FriendGroupApi.add(friendGroupBean);
    }

    @Override
    public Call<ResponseBody<FriendGroupBean>> update(FriendGroupBean friendGroupBean) {
        return FriendGroupApi.update(friendGroupBean);
    }

    @Override
    public Call<ResponseBody<Empty>> delete(Integer friendGroupNo) {
        return FriendGroupApi.delete(friendGroupNo);
    }

    @Override
    public Call<ResponseBody<List<FriendGroupBean>>> searchCount() {
        return FriendGroupApi.searchCount();
    }
}
