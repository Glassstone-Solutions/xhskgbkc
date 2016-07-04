package net.glassstones.thediarymagazine.tasks;

import android.content.Context;
import android.util.Log;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.network.TDMAPIClient;
import net.glassstones.thediarymagazine.network.models.NI;
import net.glassstones.thediarymagazine.network.models.Post;
import net.glassstones.thediarymagazine.network.models.WPMedia;
import net.glassstones.thediarymagazine.network.ServiceGenerator;
import net.glassstones.thediarymagazine.utils.RealmUtils;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thompson on 19/06/2016.
 * For The Diary Magazine
 */
public class GetImageTask implements RealmUtils.RealmInterface {

    private static final String TAG = GetImageTask.class.getSimpleName();
    TDMAPIClient client;
    Realm realm;
    RealmUtils realmUtils;
    ServiceGenerator sg;
    private Context mContext;

    public GetImageTask () {
    }

    public void execute () {

        Common app = (Common) mContext.getApplicationContext();

        realm = Realm.getDefaultInstance();

        realmUtils = new RealmUtils(realm, this);

        final List<Post> posts = realmUtils.getPosts(Post.IMAGE_SAVED, false, Post.CREATED_AT, Sort.DESCENDING);

        sg = new ServiceGenerator(app);
        client = sg.createService(TDMAPIClient.class);

        for (final Post p : posts) {
            if (!p.isMediaSaved()) {
                Call<WPMedia> call = client.getMedia(p.getFeatured_media());
                call.enqueue(new Callback<WPMedia>() {
                    @Override
                    public void onResponse (Call<WPMedia> call, Response<WPMedia> response) {
                        if (response.isSuccessful()) {
                            Log.e("TAG", "Media received");
                            WPMedia media = response.body();
                            realmUtils.saveMedia(media, p);
                        }
                    }

                    @Override
                    public void onFailure (Call<WPMedia> call, Throwable t) {

                    }
                });
            } else {
                Log.e("TAG", "Media already set");
            }
        }
    }

    public GetImageTask mContext (Context mContext) {
        this.mContext = mContext;
        return this;
    }

    @Override
    public void realmChange (Post post) {

    }

    @Override
    public void realmChange (List<NI> p) {

    }

    @Override
    public void postSaveFailed (Post post, Throwable t) {

    }
}
