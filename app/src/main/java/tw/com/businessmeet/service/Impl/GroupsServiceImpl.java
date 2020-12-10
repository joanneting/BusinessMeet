package tw.com.businessmeet.service.Impl;

import androidx.annotation.Nullable;

import retrofit2.Call;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.GroupsBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.service.GroupsService;

import java.util.List;

import static tw.com.businessmeet.network.RetrofitConfig.retrofit;

public class GroupsServiceImpl {
    private static GroupsService GroupsApi = retrofit.create(GroupsService.class);

    @Nullable
    public static Call<ResponseBody<List<GroupsBean>>> search(GroupsBean groupsBean) {
        return GroupsApi.search(groupsBean);
    }

    @Nullable
    public static Call<ResponseBody<GroupsBean>> add(GroupsBean groupsBean) {
        return GroupsApi.add(groupsBean);
    }

    @Nullable
    public static Call<ResponseBody<GroupsBean>> update(GroupsBean groupsBean) {
        return GroupsApi.update(groupsBean);
    }

    @Nullable
    public static Call<ResponseBody<Empty>> delete(Integer groupsNo) {
        return GroupsApi.delete(groupsNo);
    }
}
