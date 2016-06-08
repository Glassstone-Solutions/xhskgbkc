package net.glassstones.thediarymagazine.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import net.glassstones.thediarymagazine.Common;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Thompson on 08/06/2016.
 * For The Diary Magazine
 */
public class ServiceGenerator {


    public static final String API_BASE_URL = "http://www.thediarymagazine.com";

    int cacheSize = 10 * 1024 * 1024; // 10 MiB

    private Retrofit retrofit;

    public ServiceGenerator(Common mApp) {

        Cache mCache = createCache(mApp);

        Gson gson = createGson();

        OkHttpClient okHttpClient = createClient(mCache);

        retrofit = createRetrofit(gson, okHttpClient);
    }

    private Retrofit createRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(API_BASE_URL)
                .client(okHttpClient)
                .build();
    }

    private OkHttpClient createClient(Cache cache) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setCache(cache);
        return okHttpClient;
    }

    private Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    private Cache createCache(Common mApp) {
        return new Cache(mApp.getCacheDir(), cacheSize);
    }

    public <S> S createService(Class<S> serviceClass){
        return retrofit.create(serviceClass);
    }
}
