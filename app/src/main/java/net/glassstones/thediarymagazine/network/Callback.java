package net.glassstones.thediarymagazine.network;

import net.glassstones.thediarymagazine.network.models.NewsItem;

/**
 * Created by Thompson on 21/02/2016.
 * For The Diary Magazine
 */
public interface Callback {
    void onPageRequested(NewsItem newsItem);
    void onMoreRequest(int offset);
    void onShowAd();


}
