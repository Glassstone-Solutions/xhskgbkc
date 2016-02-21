package net.glassstones.thediarymagazine.interfaces;

import net.glassstones.thediarymagazine.models.NewsItem;

/**
 * Created by Thompson on 21/02/2016.
 * For The Diary Magazine
 */
public interface Callback {
    void onPageRequested(NewsItem newsItem);
}
