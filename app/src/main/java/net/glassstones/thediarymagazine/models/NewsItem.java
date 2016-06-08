package net.glassstones.thediarymagazine.models;

/**
 * Created by Thompson on 11/02/2016.
 * For The Diary Magazine
 */
public class NewsItem {
    String imageUrl;

    NI post;

    public NewsItem() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public NI getPost() {
        return post;
    }

    public void setPost(NI post) {
        this.post = post;
    }
}
