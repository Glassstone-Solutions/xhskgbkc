package net.glassstones.thediarymagazine.interfaces.network;

import net.glassstones.thediarymagazine.models.NI;
import net.glassstones.thediarymagazine.models.WPMedia;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Thompson on 08/06/2016.
 * For The Diary Magazine
 */
public interface TDMAPIClient {
    @GET("wp-json/wp/v2/posts")
    Call<ArrayList<NI>> getPosts(@Query("limit") int limit, @Query("skip") int skip);
    @GET("wp-json/wp/v2/media/{id}")
    Call<WPMedia> getMedia(@Path("id") int id);
}
