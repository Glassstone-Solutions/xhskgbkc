package net.glassstones.thediarymagazine.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thompson on 11/02/2016.
 * For The Diary Magazine
 */
public class NewsCluster extends NewsItem {
    private List<NewsItem> items;

    public NewsCluster() {
        items = new ArrayList<>();
    }

    public List<NewsItem> getItems() {
        return items;
    }

    public void setItems(List<NewsItem> items) {
        this.items = items;
    }
}
