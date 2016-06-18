package net.glassstones.thediarymagazine.models;

/**
 * Created by Thompson on 11/02/2016.
 * For The Diary Magazine
 */
public class NewsItem {
    String imageUrl;

    Post post;

    public NewsItem() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
