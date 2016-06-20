package net.glassstones.thediarymagazine.utils;

import android.util.Log;

import net.glassstones.thediarymagazine.models.NI;
import net.glassstones.thediarymagazine.models.Post;
import net.glassstones.thediarymagazine.models.WPMedia;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by Thompson on 16/06/2016.
 * For The Diary Magazine
 */
public class RealmUtils {

    private final Realm mRealm;
    private RealmInterface listner;

    public RealmUtils(final Realm realm, RealmInterface realmInterface) {
        this.mRealm = realm;
        this.listner = realmInterface;
    }

    public void savePosts(final List<Post> posts) {
        Log.e("SavePosts", "Saving Posts");
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(posts);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.e("SavePosts", "Saved Posts");
                listner.realmChange(posts);
            }
        });
    }

    public List<Post> getPosts(String filterKey, String filterValue, String sortKey, Sort sort) {
        return mRealm.where(Post.class).equalTo(filterKey, filterValue).findAllSorted(sortKey, sort);
    }

    public List<Post> getPosts(String filterKey, boolean filterValue, String sortKey, Sort sort) {
        return mRealm.where(Post.class).equalTo(filterKey, filterValue).findAllSorted(sortKey, sort);
    }

    public List<Post> getPosts(String filterKey, int filterValue, String sortKey, Sort sort) {
        return mRealm.where(Post.class).equalTo(filterKey, filterValue).findAllSorted(sortKey, sort);
    }

    public void save(final Post post) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(post);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                listner.realmChange(post);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                listner.postSaveFailed(post, error);
            }
        });
    }

    public void closeRealm(){
        mRealm.close();
    }

    public Post getPost(String filterKey, int filterValue) {
        return mRealm.where(Post.class).equalTo(filterKey, filterValue).findFirst();
    }

    public Post NI2Post(NI ni, byte[] media){
        Post p = new Post();

        mRealm.beginTransaction();
        p.setId(ni.getId());
        p.setTitle(ni.getTitle().getTitle());
        p.setExcerpt(ni.getExcerpt().getExcerpt());
        p.setCreated_at(ni.getCreated_at());
        p.setSlug(ni.getSlug());
        p.setType(ni.getType());
        p.setLink(ni.getLink());
        p.setContent(ni.getContent().getContent());
        p.setAuthorId(ni.getAuthorId());
        p.setFeatured_media(ni.getFeatured_media());
        if (media != null){
            p.setImageByte(media);
            p.setMediaSaved(true);
            p.setMediaId(ni.getMedia().getId());
            p.setMedia_type(ni.getMedia().getMedia_type());
            p.setMime_type(ni.getMedia().getMime_type());
            p.setSource_url(ni.getMedia().getSourceUrl());
        } else {
            p.setImageByte(null);
            p.setMediaSaved(false);
        }
        mRealm.commitTransaction();

        return p;
    }

    public void updatePostMedia(Post post, byte[] byteArray) {
        if (byteArray != null) {
            mRealm.beginTransaction();
            post.setImageByte(byteArray);
            post.setMediaSaved(true);
            mRealm.commitTransaction();
        }
    }

    public void saveMedia(final WPMedia media, final Post p) {
        final String url = media.getSourceUrl();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                p.setImageByte(BitmapUtils.getByteArray(BitmapUtils.getBitmapFromURL(url)));
                p.setMime_type(media.getMime_type());
                p.setMedia_type(media.getMedia_type());
                p.setMediaId(media.getId());
                p.setSource_url(media.getSourceUrl());
                p.setMediaSaved(true);
                Log.e("TAG", "Media Saved");
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                listner.realmChange(p);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                listner.postSaveFailed(p, error);
            }
        });
    }

    public interface RealmInterface {
        void realmChange(List<Post> posts);

        void realmChange(Post post);

        void postSaveFailed(Post post, Throwable t);
    }

}
