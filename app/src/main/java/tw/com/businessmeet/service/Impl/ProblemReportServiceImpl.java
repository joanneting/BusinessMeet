package tw.com.businessmeet.service.Impl;

import retrofit2.Call;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.ProblemReportBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.service.ProblemReportService;

import java.util.List;

import static tw.com.businessmeet.network.RetrofitConfig.retrofit;

public class ProblemReportServiceImpl implements ProblemReportService{
    private static ProblemReportService ProblemReportApi = retrofit.create(ProblemReportService.class);

    @Override
    public Call<ResponseBody<List<ProblemReportBean>>> search(ProblemReportBean problemReportBean) {
        return ProblemReportApi.search(problemReportBean);
    }

    @Override
    public Call<ResponseBody<ProblemReportBean>> add(ProblemReportBean problemReportBean) {
        return ProblemReportApi.add(problemReportBean);
    }

    @Override
    public Call<ResponseBody<ProblemReportBean>> update(ProblemReportBean problemReportBean) {
        return ProblemReportApi.update(problemReportBean);
    }

    @Override
    public Call<ResponseBody<Empty>> delete(Integer problemReportNo) {
        return ProblemReportApi.delete(problemReportNo);
    }
}
