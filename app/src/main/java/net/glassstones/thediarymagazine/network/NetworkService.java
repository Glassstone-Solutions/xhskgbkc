package net.glassstones.thediarymagazine.network;

import android.support.v4.util.LruCache;

import net.glassstones.thediarymagazine.network.models.NI;
import net.glassstones.thediarymagazine.network.models.WPMedia;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Thompson on 01/07/2016.
 * For The Diary Magazine
 */
public class NetworkService {

    public static final String API_BASE_URL = "http://www.thediarymagazine.com";

    private NetworkAPI networkAPI;
    private OkHttpClient okHttpClient;
    private LruCache<Class<?>, Observable<?>> apiObservables;

    public NetworkService () {
        this(API_BASE_URL);
    }

    public NetworkService (String baseUrl) {
        okHttpClient = buildClient();
        apiObservables = new LruCache<>(10);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        networkAPI = retrofit.create(NetworkAPI.class);
    }

    /**
     * Method to build and return an OkHttpClient so we can set/get
     * headers quickly and efficiently.
     *
     * @return
     */
    public OkHttpClient buildClient () {

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

        return builder.build();
    }

    /**
     * Method to return the API interface.
     *
     * @return NetworkApi
     */
    public NetworkAPI getAPI () {
        return networkAPI;
    }

    /**
     * Method to clear the entire cache of observables
     */
    public void clearCache () {
        apiObservables.evictAll();
    }

    /**
     * Method to either return a cached observable or prepare a new one.
     *
     * @param unPreparedObservable
     * @param clazz
     * @param cacheObservable
     * @param useCache
     * @return Observable ready to be subscribed to
     */
    public Observable<?> getPreparedObservable (Observable<?> unPreparedObservable, Class<?> clazz, boolean cacheObservable, boolean useCache) {

        Observable<?> preparedObservable = null;

        if (useCache)//this way we don't reset anything in the cache if this is the only instance of us not wanting to use it.
            preparedObservable = apiObservables.get(clazz);

        if (preparedObservable != null)
            return preparedObservable;


        //we are here because we have never created this observable before or we didn't want to use the cache...

        preparedObservable = unPreparedObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        if (cacheObservable) {
            preparedObservable = preparedObservable.cache();
            apiObservables.put(clazz, preparedObservable);
        }


        return preparedObservable;
    }

    /**
     * all the Service alls to use for the retrofit requests.
     */
    public interface NetworkAPI {
        @GET("wp-json/wp/v2/posts")
        Call<ArrayList<NI>> getPosts (@Query("per_page") int limit, @Query("page") int skip, @Query
                ("slug") String slug);

        @GET("wp-json/wp/v2/posts")
        Observable<ArrayList<NI>> getObservablePosts (@Query("per_page") int limit, @Query("page") int
                skip, @Query
                                                              ("slug") String slug);

        @GET("wp-json/wp/v2/posts/{id}")
        Call<NI> getPost (@Path("id") int id);

        @GET("wp-json/wp/v2/posts")
        Observable<NI> getObservablePost (@Query("per_page") int limit, @Query("page") int skip);

        @GET("wp-json/wp/v2/posts")
        Call<NI> getPostFromSlug (@Query("slug") String slug);

        @GET("wp-json/wp/v2/posts")
        Call<ArrayList<NI>> getPostsByCategory (@Query("categories") int category, @Query("per_page")
        int limit, @Query("page") int skip);

        @GET("wp-json/wp/v2/media/{id}")
        Call<WPMedia> getMedia (@Path("id") int id);
    }
}
