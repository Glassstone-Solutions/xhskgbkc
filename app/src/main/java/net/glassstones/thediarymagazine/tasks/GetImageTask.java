package net.glassstones.thediarymagazine.tasks;

import android.content.Context;
import android.util.Log;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.interfaces.network.TDMAPIClient;
import net.glassstones.thediarymagazine.models.Post;
import net.glassstones.thediarymagazine.models.WPMedia;
import net.glassstones.thediarymagazine.network.Request;
import net.glassstones.thediarymagazine.network.ServiceGenerator;
import net.glassstones.thediarymagazine.services.UpdateLocalPostsImageService;
import net.glassstones.thediarymagazine.utils.RealmUtils;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Thompson on 19/06/2016.
 * For The Diary Magazine
 */
public class GetImageTask implements RealmUtils.RealmInterface {

    private Context mContext;

    TDMAPIClient client;
    Realm realm;
    RealmUtils realmUtils;

    ServiceGenerator sg;

    public GetImageTask() {
    }

    public void execute(){

        Common app = (Common) mContext.getApplicationContext();

        realm = Realm.getDefaultInstance();

        realmUtils = new RealmUtils(realm, this);

        final List<Post> posts = realmUtils.getPosts(Post.IMAGE_SAVED, false,Post.CREATED_AT, Sort.DESCENDING);

        sg = new ServiceGenerator(app);
        client = sg.createService(TDMAPIClient.class);

        for (final Post p : posts){
            Call<WPMedia> call = client.getMedia(p.getFeatured_media());
            call.enqueue(new Callback<WPMedia>() {
                @Override
                public void onResponse(Response<WPMedia> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        Log.e("TAG", "Media received");
                        WPMedia media = response.body();
                        realmUtils.saveMedia(media, p);
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }
    }

    public GetImageTask mContext(Context mContext) {
        this.mContext = mContext;
        return this;
    }

    @Override
    public void realmChange(List<Post> posts) {

    }

    @Override
    public void realmChange(Post post) {

    }

    @Override
    public void postSaveFailed(Post post, Throwable t) {

    }
}
