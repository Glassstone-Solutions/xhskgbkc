package net.glassstones.thediarymagazine.tasks;

import net.glassstones.thediarymagazine.network.models.NI;
import net.glassstones.thediarymagazine.network.NetworkService;

import rx.Observable;
import rx.Observable.Transformer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Thompson on 01/07/2016.
 * For The Diary Magazine
 */
public class GetPostTask {
    NetworkService service;

    public GetPostTask (NetworkService service) {
        this.service = service;
    }

    public Subscription getPostSubscription (Observable<NI> postObservable) {
        return null;
    }

    <T> Transformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
