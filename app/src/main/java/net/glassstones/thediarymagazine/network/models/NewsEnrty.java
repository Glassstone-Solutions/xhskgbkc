package net.glassstones.thediarymagazine.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.glassstones.thediarymagazine.utils.DateDeserializer;
import net.glassstones.thediarymagazine.utils.DateSerializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.annotations.PrimaryKey;

/**
 * Created by Thompson on 10/07/2016.
 * For The Diary Magazine
 */
public class NewsEnrty implements Parcelable {
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final Parcelable.Creator<NewsEnrty> CREATOR = new Parcelable.Creator<NewsEnrty>() {
        @Override
        public NewsEnrty createFromParcel (Parcel source) {
            return new NewsEnrty(source);
        }

        @Override
        public NewsEnrty[] newArray (int size) {
            return new NewsEnrty[size];
        }
    };
    public static String IMAGE_SAVED = "mediaSaved";
    public static String CREATED_AT = "created_at";
    @PrimaryKey
    private int id;
    private Date created_at;
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
    private List<Integer> categories;

    public NewsEnrty () {
    }

    protected NewsEnrty (Parcel in) {
        this.id = in.readInt();
        long tmpCreated_at = in.readLong();
        this.created_at = tmpCreated_at == -1 ? null : new Date(tmpCreated_at);
        this.slug = in.readString();
        this.type = in.readString();
        this.link = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.excerpt = in.readString();
        this.authorId = in.readInt();
        this.featured_media = in.readInt();
        this.mediaId = in.readInt();
        this.media_type = in.readString();
        this.mime_type = in.readString();
        this.source_url = in.readString();
        this.categories = new ArrayList<>();
        in.readList(this.categories, Integer.class.getClassLoader());
    }

    public String toJson () {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
        Gson gson = gsonBuilder.create();

        return gson.toJson(this);
    }

    @Override
    public int describeContents () {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeLong(this.created_at != null ? this.created_at.getTime() : -1);
        dest.writeString(this.slug);
        dest.writeString(this.type);
        dest.writeString(this.link);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.excerpt);
        dest.writeInt(this.authorId);
        dest.writeInt(this.featured_media);
        dest.writeInt(this.mediaId);
        dest.writeString(this.media_type);
        dest.writeString(this.mime_type);
        dest.writeString(this.source_url);
        dest.writeList(this.categories);
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public Date getCreated_at () {
        return created_at;
    }

    public void setCreated_at (Date created_at) {
        this.created_at = created_at;
    }

    public String getSlug () {
        return slug;
    }

    public void setSlug (String slug) {
        this.slug = slug;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public String getLink () {
        return link;
    }

    public void setLink (String link) {
        this.link = link;
    }

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String getContent () {
        return content;
    }

    public void setContent (String content) {
        this.content = content;
    }

    public String getExcerpt () {
        return excerpt;
    }

    public void setExcerpt (String excerpt) {
        this.excerpt = excerpt;
    }

    public int getAuthorId () {
        return authorId;
    }

    public void setAuthorId (int authorId) {
        this.authorId = authorId;
    }

    public int getFeatured_media () {
        return featured_media;
    }

    public void setFeatured_media (int featured_media) {
        this.featured_media = featured_media;
    }

    public int getMediaId () {
        return mediaId;
    }

    public void setMediaId (int mediaId) {
        this.mediaId = mediaId;
    }

    public String getMedia_type () {
        return media_type;
    }

    public void setMedia_type (String media_type) {
        this.media_type = media_type;
    }

    public String getMime_type () {
        return mime_type;
    }

    public void setMime_type (String mime_type) {
        this.mime_type = mime_type;
    }

    public String getSource_url () {
        return source_url;
    }

    public void setSource_url (String source_url) {
        this.source_url = source_url;
    }
}
