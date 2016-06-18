package net.glassstones.thediarymagazine.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Thompson on 08/06/2016.
 * For The Diary Magazine
 */
public class NI {

    private int id;
    @SerializedName("date")
    private String created_at;
    private String slug;
    private String type;
    private String link;

    private WPTitle title;

    private WPContent content;

    private WPExcerpt excerpt;
    @SerializedName("author")
    private int authorId;
    private int featured_media;
    private WPMedia media;

    public NI() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public WPTitle getTitle() {
        return title;
    }

    public void setTitle(WPTitle title) {
        this.title = title;
    }

    public WPContent getContent() {
        return content;
    }

    public void setContent(WPContent content) {
        this.content = content;
    }

    public WPExcerpt getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(WPExcerpt excerpt) {
        this.excerpt = excerpt;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getFeatured_media() {
        return featured_media;
    }

    public void setFeatured_media(int featured_media) {
        this.featured_media = featured_media;
    }

    public WPMedia getMedia() {
        return media;
    }

    public void setMedia(WPMedia media) {
        this.media = media;
    }

}
