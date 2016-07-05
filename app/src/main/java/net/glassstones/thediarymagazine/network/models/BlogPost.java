package net.glassstones.thediarymagazine.network.models;

/**
 * Created by Thompson on 05/07/2016.
 * For The Diary Magazine
 */
public class BlogPost {
    NI newsItem;
    WPMedia media;

    public NI getNewsItem () {
        return newsItem;
    }

    public void setNewsItem (NI newsItem) {
        this.newsItem = newsItem;
    }

    public WPMedia getMedia () {
        return media;
    }

    public void setMedia (WPMedia media) {
        this.media = media;
    }
}
