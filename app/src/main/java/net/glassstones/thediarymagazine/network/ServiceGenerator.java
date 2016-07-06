package net.glassstones.thediarymagazine.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.glassstones.thediarymagazine.Common;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Thompson on 08/06/2016.
 * For The Diary Magazine
 */
public class ServiceGenerator {


    public static final String API_BASE_URL = "http://www.thediarymagazine.com";

    int cacheSize = 20 * 1024 * 1024; // 10 MiB

    private Retrofit retrofit;

    public ServiceGenerator (Common mApp) {

        Cache mCache = createCache(mApp);

        Gson gson = createGson();

        OkHttpClient okHttpClient = createClient(mCache);

        retrofit = createRetrofit(gson, okHttpClient);
    }

    private Cache createCache (Common mApp) {
        return new Cache(mApp.getCacheDir(), cacheSize);
    }

    private Gson createGson () {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    private OkHttpClient createClient (Cache cache) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(chain -> {
            // Do anything with response here
            //if we ant to grab a specific cookie or something..
            return chain.proceed(chain.request());
        });

        builder.addInterceptor(chain -> {
            //this is where we will add whatever we want to our request headers.
            Request request = chain.request().newBuilder().addHeader("Accept",
                    "application/json").build();
            return chain.proceed(request);
        });

        builder.cache(cache);
        return builder.build();
    }

    private Retrofit createRetrofit (Gson gson, OkHttpClient okHttpClient) {
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.create();
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxAdapter)
                .baseUrl(API_BASE_URL)
                .client(okHttpClient)
                .build();
    }

    public static TDMAPIClient createGithubService () {
        Retrofit.Builder builder = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(API_BASE_URL);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request request = chain.request();
            Request newReq = request.newBuilder()
                    .build();
            return chain.proceed(newReq);
        }).build();

        builder.client(client);

        return builder.build().create(TDMAPIClient.class);
    }



    public <S> S createService (Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
