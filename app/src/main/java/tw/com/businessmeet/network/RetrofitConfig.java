package tw.com.businessmeet.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {
    private static final String BASE_URL = ApiConfig.API_URL;
    private static  Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .setLenient()
            .create();
    private static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new ReceivedCookiesInterceptor())
            .addInterceptor(new AddCookiesInterceptor())
            .build();


    public static  Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson)) // 使用 Gson 解析
            .client(client)
            .baseUrl(BASE_URL)
            .build();


}
