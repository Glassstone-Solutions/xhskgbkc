package net.glassstones.thediarymagazine.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thompson on 16/06/2016.
 * For The Diary Magazine
 */
public class WPContent {
    @SerializedName("rendered")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
