package net.glassstones.thediarymagazine.models;

/**
 * Created by Thompson on 11/02/2016.
 * For The Diary Magazine
 */
public class NewsItem {
    String title, text, imageUrl;

    public NewsItem() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
