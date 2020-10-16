package tw.com.businessmeet.helper;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import tw.com.businessmeet.bean.ResponseBody;

public class AsyncTasKHelper<P, R> extends AsyncTask<P, Void, Response<ResponseBody<R>>> {

    private OnResponseListener<P, R> onResponseListener;

    public AsyncTasKHelper(OnResponseListener<P, R> onResponseListener) {
        this.onResponseListener = onResponseListener;
    }

    public static <P, R> void execute(OnResponseListener<P,R> onResponseListener, P... ps) {
        new AsyncTasKHelper<>(onResponseListener).execute(ps);
    }

    @Override
    protected Response<ResponseBody<R>> doInBackground(P... ps) {
        try {
            return onResponseListener.request(ps).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Response<ResponseBody<R>> response) {
        super.onPostExecute(response);
        if (response != null && response.isSuccessful()) {
            ResponseBody<R> body = response.body();
            System.out.println("body.getMessage() : " + body.getMessage());
            System.out.println("body.getSuccess() = " + body.getSuccess());
            if (body.getSuccess()) {
                onResponseListener.onSuccess(body.getData());
            } else {
                onResponseListener.onFail(1,body.getMessage());
            }
        } else {
            try {
                JSONObject errorBody = new JSONObject(response.errorBody().string());
                System.out.println("errorBody = " + errorBody);

                System.out.println("errorBody.getJSONObject(\"text\").getString(\"message\") = " + errorBody.getString("message"));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            onResponseListener.onFail(response != null ? response.code() : 500,(response!=null?"":"") );
        }
    }



    public interface OnResponseListener<P, R> {
        Call<ResponseBody<R>> request(P... ps);

        void onSuccess(R r);

        void onFail(int status,String message);
    }
}

