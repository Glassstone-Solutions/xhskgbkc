package net.glassstones.thediarymagazine.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thompson on 11/02/2016.
 * For The Diary Magazine
 */
public class NewsCluster extends NewsItem implements Parcelable {
    public static final Parcelable.Creator<NewsCluster> CREATOR = new Parcelable.Creator<NewsCluster>() {
        @Override
        public NewsCluster createFromParcel(Parcel source) {
            return new NewsCluster(source);
        }

        @Override
        public NewsCluster[] newArray(int size) {
            return new NewsCluster[size];
        }
    };
    private List<NewsItem> items;

    public NewsCluster() {
        items = new ArrayList<>();
    }

    protected NewsCluster(Parcel in) {
        this.items = new ArrayList<>();
        in.readList(this.items, NewsItem.class.getClassLoader());
    }

    public List<NewsItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "NewsCluster{" +
                "items=" + items +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.items);
    }
}
