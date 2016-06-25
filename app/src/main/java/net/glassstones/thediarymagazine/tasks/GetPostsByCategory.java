package net.glassstones.thediarymagazine.tasks;

import android.util.Log;

import com.squareup.okhttp.ResponseBody;

import net.glassstones.thediarymagazine.models.NI;
import net.glassstones.thediarymagazine.models.Post;
import net.glassstones.thediarymagazine.utils.RealmUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Thompson on 21/06/2016.
 * For The Diary Magazine
 */
public class GetPostsByCategory implements RealmUtils.RealmInterface {
    private Call<ArrayList<NI>> mCall;
    private RealmUtils realmUtils;
    private GetPostsByCategoryInterface listener;

    public GetPostsByCategory(Call<ArrayList<NI>> call) {
        this.mCall = call;
        this.realmUtils = new RealmUtils(Realm.getDefaultInstance(), this);
    }

    public void execute() {
        mCall.enqueue(new Callback<ArrayList<NI>>() {
            @Override
            public void onResponse(Response<ArrayList<NI>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    final List<NI> posts = response.body();
                    final List<Post> rPosts = new ArrayList<>();
                    for (NI p : posts) {
                        Log.e("TAG", ""+p.getTitle().getTitle());
                        rPosts.add(realmUtils.getPost(Post.ID, p.getId()));
                    }
                    Realm r = Realm.getDefaultInstance();
                    r.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(rPosts);
                            realmUtils.savePosts(posts, rPosts);
                        }
                    });
                } else {
                    listener.responseFailed(response.errorBody());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                listener.callFailed(t);
            }
        });
    }

    public void closeRealm() {
        realmUtils.closeRealm();
    }

    public void setListeners(GetPostsByCategoryInterface l) {
        this.listener = l;
    }

    @Override
    public void realmChange(Post post) {

    }

    @Override
    public void realmChange(List<NI> p) {
        List<Post> posts = new ArrayList<>();
        for (NI pos : p) {
            Post post = realmUtils.getPost(Post.ID, pos.getId());
            realmUtils.updatePost(pos, post);
            posts.add(post);
        }
        listener.getPosts(posts);
    }

    @Override
    public void postSaveFailed(Post post, Throwable t) {

    }

    public interface GetPostsByCategoryInterface {
        void getPosts(List<Post> posts);
        void callFailed(Throwable t);
        void responseFailed(ResponseBody responseBody);
    }

}
