package net.glassstones.thediarymagazine.network;

import net.glassstones.thediarymagazine.network.models.NI;
import net.glassstones.thediarymagazine.network.models.WPAuthor;
import net.glassstones.thediarymagazine.network.models.WPMedia;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Thompson on 08/06/2016.
 * For The Diary Magazine
 */
public interface TDMAPIClient {
    @GET("wp-json/wp/v2/posts")
    Call<ArrayList<NI>> getPosts (@Query("per_page") int limit, @Query("page") int skip, @Query
            ("slug") String slug);

    @GET("wp-json/wp/v2/posts")
    Observable<List<NI>> getObservablePosts (@Query("per_page") Integer perPage,
                                             @Query("offset") Integer offset,
                                             @Query("page") Integer page,
                                             @Query("slug") String slug);

    @GET("wp-json/wp/v2/posts")
    Observable<List<NI>> getPostsObservableByCategory (
            @Query("categories") Integer category,
            @Query("per_page") Integer perPage,
            @Query("offset") Integer offset,
            @Query("page") Integer page);

    @GET("wp-json/wp/v2/posts/{id}")
    Call<NI> getPost (@Path("id") int id);

    @GET("wp-json/wp/v2/posts/{id}")
    Observable<NI> getPostObservable(@Path("id") String id);

    @GET("wp-json/wp/v2/posts")
    Call<NI> getPostFromSlug (@Query("slug") String slug);

    @GET("wp-json/wp/v2/posts")
    Call<List<NI>> getPostsByCategory (@Query("categories") int category, @Query("per_page")
    int limit, @Query("page") int skip);

    @GET("wp-json/wp/v2/posts")
    @Headers("Cache-Control: no-cache")
    Observable<List<NI>> searchPostsObservable (@Query("search") String q);

    @GET("wp-json/wp/v2/media/{id}")
    Call<WPMedia> getMedia (@Path("id") int id);

    @GET("wp-json/wp/v2/media/{id}")
    Observable<WPMedia> getMediaObservable (@Path("id") int id);

    @GET("wp-json/wp/v2/users/{id}")
    Observable<WPAuthor> getAuthorObservable(@Path("id") int authorId);
}
