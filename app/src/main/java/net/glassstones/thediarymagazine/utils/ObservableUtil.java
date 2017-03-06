package net.glassstones.thediarymagazine.utils;

import android.support.v4.util.Pair;

import net.glassstones.thediarymagazine.network.TDMAPIClient;
import net.glassstones.thediarymagazine.network.models.NI;
import net.glassstones.thediarymagazine.network.models.WPAuthor;
import net.glassstones.thediarymagazine.network.models.WPMedia;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import co.kaush.core.util.CoreNullnessUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.glassstones.thediarymagazine.utils.Error.exponentialBackoffForExceptions;

public class ObservableUtil {

    static int retryCount = 3;
    static int initialDelay = 3;
    TDMAPIClient client;

    public ObservableUtil(TDMAPIClient client){
        this.client = client;
    }

    public Observable<NI> GetPost(String id) {
        return client.getPostObservable(id)
                .flatMap(p -> filterPost(client, Observable.just(p)));
    }

    public Observable<List<NI>> GetListObservable(Integer perPage, Integer offset, Integer page, String slug) {
        return client.getObservablePosts(perPage, offset, page, slug)
                .flatMap(Observable::from)
                .flatMap(o -> filterPost(client, Observable.just(o)))
                .toList();
    }

    public Subscription GetSubscription(Observable<List<NI>> postsObservable, Observer<List<NI>> observer) {
        return postsObservable
                .retryWhen(exponentialBackoffForExceptions(initialDelay, retryCount, TimeUnit.SECONDS, IOException.class))
                .distinct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public Subscription GetPostSubscription(Observable<NI> postObservable, Observer<NI> postObserver){
        return postObservable
                .retryWhen(exponentialBackoffForExceptions(initialDelay, retryCount, TimeUnit.SECONDS, IOException.class))
                .distinct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(postObserver);
    }

    private Observable<NI> filterPost(TDMAPIClient client, Observable<NI> p){
        return  p.flatMap(o -> getPairObservable(client, o))
                 .filter(pair -> pair.second != null)
                 .flatMap(this::getObservable);
    }

    private Observable<Pair<NI, WPMedia>> getPairObservable(TDMAPIClient client, NI ni){
        Observable<WPMedia> media = client.getMediaObservable(ni.getFeatured_media()).onErrorReturn(e -> {
            e.printStackTrace();
            return null;
        });
        return Observable.zip(Observable.just(ni), media, Pair::new);
    }

    private Observable<NI> getObservable(Pair<NI, WPMedia> pair){
        NI post = pair.first;
        if (pair.second != null) {
            post.setMedia(pair.second);
        }
        return Observable.just(post);
    }

    public Observable<WPAuthor> getAuthor(NI ni) {
        return client.getAuthorObservable(ni.getAuthorId()).onErrorReturn(e ->{
            e.printStackTrace();
            return null;
        });
    }

}
