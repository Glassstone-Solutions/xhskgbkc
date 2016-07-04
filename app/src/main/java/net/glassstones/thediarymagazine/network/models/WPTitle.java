package net.glassstones.thediarymagazine.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thompson on 16/06/2016.
 * For The Diary Magazine
 */
public class WPTitle {

    @SerializedName("rendered")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
