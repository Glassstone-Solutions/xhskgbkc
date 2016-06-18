package net.glassstones.thediarymagazine.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Thompson on 18/06/2016.
 * For The Diary Magazine
 */
public class Post extends RealmObject {

    @PrimaryKey
    private int id;
    private String created_at;
    private String slug;
    private String type;
    private String link;
    private String title;
    private String content;
    private String excerpt;
    private int authorId;
    private int featured_media;
    private int mediaId;
    private String media_type;
    private String mime_type;
    private String source_url;
    private byte[] image_byte;
    private boolean mediaSaved;

    public boolean isMediaSaved() {
        return mediaSaved;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getSlug() {
        return slug;
    }

    public String getLink() {
        return link;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getFeatured_media() {
        return featured_media;
    }

    public int getMediaId() {
        return mediaId;
    }

    public String getMedia_type() {
        return media_type;
    }

    public String getMime_type() {
        return mime_type;
    }

    public String getSource_url() {
        return source_url;
    }

    public byte[] getImageByte() {
        return image_byte;
    }

    public int getId() {
        return id;
    }

    public void setMediaSaved(boolean mediaSaved) {
        this.mediaSaved = mediaSaved;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
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

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public void setFeatured_media(int featured_media) {
        this.featured_media = featured_media;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public void setImageByte(byte[] image_byte) {
        this.image_byte = image_byte;
    }
}
