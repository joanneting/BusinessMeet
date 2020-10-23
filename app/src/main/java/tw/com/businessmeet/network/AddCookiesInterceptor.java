package tw.com.businessmeet.network;

import android.content.Context;

import java.io.IOException;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();
        Set<String> cookieSet = ApplicationContext.get()
                .getSharedPreferences("cookieData", Context.MODE_PRIVATE)
                .getStringSet("cookie", null);
        if (cookieSet != null) {
            for (String cookie : cookieSet) {
                builder.addHeader("Cookie", cookie);
            }
        }
        return chain.proceed(builder.build());
    }
}
