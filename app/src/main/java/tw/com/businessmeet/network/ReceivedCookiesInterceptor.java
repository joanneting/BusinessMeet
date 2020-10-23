package tw.com.businessmeet.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceivedCookiesInterceptor implements Interceptor {
    private final static String TAG = "CookiesInterceptor";
    public ReceivedCookiesInterceptor() {
//        this.context = context;
        super();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        //這里獲取请求返回的cookie
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {

            HashSet<String> cookies = new HashSet<>();
            for(String header: originalResponse.headers("Set-Cookie"))
            {
                Log.e(TAG, "攔截的cookie是："+header);
                cookies.add(header);
            }
            //保存的sharepreference文件名為cookieData
            SharedPreferences sharedPreferences = ApplicationContext.get().getSharedPreferences("cookieData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet("cookie", cookies);

            editor.commit();
        }

        return originalResponse;

    }
}
