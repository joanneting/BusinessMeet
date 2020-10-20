package tw.com.businessmeet.helper;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.function.BiConsumer;
import tw.com.businessmeet.function.Consumer;
import tw.com.businessmeet.function.Supplier;

public class AsyncTaskHelper<R> extends AsyncTask<Void, Void, Response<ResponseBody<R>>> {
    private final Supplier<Call<ResponseBody<R>>> requestSender;
    private final Consumer<R> onSuccess;
    private final BiConsumer<Integer, String> onFail;

    public AsyncTaskHelper(Supplier<Call<ResponseBody<R>>> requestSender, Consumer<R> onSuccess, BiConsumer<Integer, String> onFail) {
        this.requestSender = requestSender;
        this.onSuccess = onSuccess;
        this.onFail = onFail;
    }

    public static <R> void execute(Supplier<Call<ResponseBody<R>>> requestSender) {
        execute(requestSender, data -> {
        });
    }

    public static <R> void execute(Supplier<Call<ResponseBody<R>>> requestSender, Consumer<R> onSuccess) {
        new AsyncTaskHelper<>(requestSender, onSuccess, (status, message) -> {
        }).execute();
    }

    public static <R> void execute(Supplier<Call<ResponseBody<R>>> requestSender, Consumer<R> onSuccess, BiConsumer<Integer, String> onFail) {
        new AsyncTaskHelper<>(requestSender, onSuccess, onFail).execute();
    }

    @Override
    protected Response<ResponseBody<R>> doInBackground(Void... ps) {
        try {
            return requestSender.get().execute();
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
            System.out.println("response : " + body.getMessage());
            if (body.getSuccess()) {
                onSuccess.accept(body.getData());
            } else {
                onFail.accept(1, body.getMessage());
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
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            onFail.accept(response != null ? response.code() : 500, (response != null ? "" : ""));
        }
    }
}

