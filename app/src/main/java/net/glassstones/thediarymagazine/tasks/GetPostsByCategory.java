package net.glassstones.thediarymagazine.tasks;

import android.util.Log;

import net.glassstones.thediarymagazine.network.models.NI;
import net.glassstones.thediarymagazine.network.models.Post;
import net.glassstones.thediarymagazine.utils.RealmUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thompson on 21/06/2016.
 * For The Diary Magazine
 */
public class GetPostsByCategory implements RealmUtils.RealmInterface {
    private Call<ArrayList<NI>> mCall;
    private RealmUtils realmUtils;
    private GetPostsByCategoryInterface listener;

    public GetPostsByCategory (Call<ArrayList<NI>> call) {
        this.mCall = call;
        this.realmUtils = new RealmUtils(Realm.getDefaultInstance(), this);
    }

    public void execute () {
        mCall.enqueue(new Callback<ArrayList<NI>>() {
            @Override
            public void onResponse (Call<ArrayList<NI>> call, Response<ArrayList<NI>> response) {
                if (response.isSuccessful()) {
                    final List<NI> posts = response.body();
                    final List<Post> rPosts = new ArrayList<>();
                    Realm r = Realm.getDefaultInstance();
                    for (NI p : posts) {
                        Log.e("TAG", "" + p.getTitle().title());
                        Post post = r.where(Post.class).equalTo(Post.TITLE, p.getTitle().title
                                ()).findFirst();
                        if (post != null) {
                            rPosts.add(realmUtils.getPost(Post.ID, p.getId()));
                        } else {
                            Post posttt = realmUtils.NI2Post(p, null);
                            rPosts.add(posttt);
                        }
                    }

                    r.executeTransactionAsync(realm -> {
                        realm.copyToRealmOrUpdate(rPosts);
                        realmUtils.savePosts(posts, rPosts);
                    });
                } else {
                    listener.responseFailed(response.errorBody());
                }
            }

            @Override
            public void onFailure (Call<ArrayList<NI>> call, Throwable t) {
                listener.callFailed(t);
            }
        });
    }

    public void closeRealm () {
        realmUtils.closeRealm();
    }

    public void setListeners (GetPostsByCategoryInterface l) {
        this.listener = l;
    }

    @Override
    public void realmChange (Post post) {

    }

    @Override
    public void realmChange (List<NI> p) {
        List<Post> posts = new ArrayList<>();
        for (NI pos : p) {
            Post post = realmUtils.getPost(Post.ID, pos.getId());
            realmUtils.updatePost(pos, post);
            posts.add(post);
        }
        listener.getPosts(posts);
    }

    @Override
    public void postSaveFailed (Post post, Throwable t) {

    }

    public interface GetPostsByCategoryInterface {
        void getPosts (List<Post> posts);

        void callFailed (Throwable t);

        void responseFailed (ResponseBody responseBody);
    }

}
