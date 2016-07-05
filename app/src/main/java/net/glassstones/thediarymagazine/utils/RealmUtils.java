package net.glassstones.thediarymagazine.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import net.glassstones.thediarymagazine.network.models.Categories;
import net.glassstones.thediarymagazine.network.models.NI;
import net.glassstones.thediarymagazine.network.models.Post;
import net.glassstones.thediarymagazine.network.models.PostEvent;
import net.glassstones.thediarymagazine.network.models.WPMedia;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;

/**
 * Created by Thompson on 16/06/2016.
 * For The Diary Magazine
 */
public class RealmUtils {

    private final Realm mRealm;
    private RealmInterface listner;

    public RealmUtils () {
        this.mRealm = Realm.getDefaultInstance();
    }

    public RealmUtils (Realm mRealm) {
        this.mRealm = mRealm;
    }

    public RealmUtils (final Realm realm, RealmInterface realmInterface) {
        this.mRealm = realm;
        this.listner = realmInterface;
    }

    public void savePosts (final List<NI> nis, final List<Post> posts) {
        mRealm.executeTransaction(realm -> {
            realm.copyToRealmOrUpdate(posts);
            EventBus.getDefault().post(new PostEvent()
                    .id(PostEvent.LIST_CHANGE)
                    .type(PostEvent.POST_LIST)
                    .status(PostEvent.SAVED)
            );
            listner.realmChange(nis);
        });
    }

    public List<Post> getPosts (String filterKey, String filterValue, String sortKey, Sort sort) {
        return mRealm.where(Post.class).equalTo(filterKey, filterValue).findAllSorted(sortKey, sort);
    }

    public List<Post> getPosts (String filterKey, boolean filterValue, String sortKey, Sort sort) {
        return mRealm.where(Post.class).equalTo(filterKey, filterValue).findAllSorted(sortKey, sort);
    }

    public List<Post> getPosts (String filterKey, int filterValue, String sortKey, Sort sort) {
        return mRealm.where(Post.class).equalTo(filterKey, filterValue).findAllSorted(sortKey, sort);
    }

    public void save (final Post post) {
        mRealm.executeTransactionAsync(realm -> realm.copyToRealmOrUpdate(post),
                () -> listner.realmChange(post),
                error -> listner.postSaveFailed(post, error));
    }

    public void closeRealm () {
        mRealm.close();
    }

    public Post getPost (String filterKey, int filterValue) {
        return mRealm.where(Post.class).equalTo(filterKey, filterValue).findFirst();
    }

    public Post NI2Post (NI ni, byte[] media) {
        Post p = new Post();

        List<Post> posts = mRealm.where(Post.class).equalTo(Post.ID, ni.getId()).equalTo(Post
                .TITLE, ni.getTitle().getTitle()).findAll();

        if (posts.size() > 0) {
            return null;
        }

        mRealm.beginTransaction();
        RealmList<Categories> categories = new RealmList<>();
        for (Integer i : ni.getCategories()) {
            if (mRealm.where(Categories.class).equalTo("id", i).count() < 1) {
                Categories cat = mRealm.createObject(Categories.class);
                cat.setId(i);
                categories.add(cat);
            } else {
                Categories cat = mRealm.where(Categories.class).equalTo("id", i).findFirst();
                categories.add(cat);
            }
        }
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
        if (media != null) {
            p.setMediaSaved(true);
            p.setMediaId(ni.getMedia().getId());
            p.setMedia_type(ni.getMedia().getMedia_type());
            p.setMime_type(ni.getMedia().getMime_type());
            p.setSource_url(ni.getMedia().getSourceUrl());
        } else {
            p.setMediaSaved(false);
        }
        p.setCategories(categories);
        mRealm.commitTransaction();

        return p;
    }

    public void updatePostMedia (Post post, byte[] byteArray) {
        if (byteArray != null) {
            mRealm.beginTransaction();
            post.setMediaSaved(true);
            mRealm.commitTransaction();
        }
    }

    public void saveMedia (final WPMedia media, final Post p) {
        final String url = media.getSourceUrl();
        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground (Void... params) {
                return BitmapUtils.getBitmapFromURL(url);
            }

            @Override
            protected void onPostExecute (final Bitmap bitmap) {
                super.onPostExecute(bitmap);
                mRealm.executeTransaction(realm -> {
                    p.setMime_type(media.getMime_type());
                    p.setMedia_type(media.getMedia_type());
                    p.setMediaId(media.getId());
                    p.setSource_url(media.getSourceUrl());
                    p.setMediaSaved(true);

                    listner.realmChange(p);
                });

            }
        }.execute();
    }

    public void updatePost (final NI niPost, final Post post) {
        if (post.getCategories().size() == 0 || post.getCategories().size() < post.getCategories().size()) {
            mRealm.executeTransactionAsync(realm -> {
                for (int cat : niPost.getCategories()) {
                    Categories categories = realm.createObject(Categories.class);
                    categories.setId(cat);
                    post.getCategories().add(categories);
                }
            }, () -> listner.realmChange(post), error -> listner.postSaveFailed(post, error));
        }
    }

    public interface RealmInterface {

        void realmChange (Post post);

        void realmChange (List<NI> p);

        void postSaveFailed (Post post, Throwable t);
    }

}
