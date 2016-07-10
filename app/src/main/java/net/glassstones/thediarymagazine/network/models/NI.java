package net.glassstones.thediarymagazine.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thompson on 08/06/2016.
 * For The Diary Magazine
 */
public class NI implements Parcelable {

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
    List<Integer> categories;

    public NI() {
    }

    public static String POST_LIST_PARCEL_KEY = "_posts";

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

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "NI{" +
                "id=" + id +
                ", created_at='" + created_at + '\'' +
                ", slug='" + slug + '\'' +
                ", type='" + type + '\'' +
                ", link='" + link + '\'' +
                ", title=" + title +
                ", content=" + content +
                ", excerpt=" + excerpt +
                ", authorId=" + authorId +
                ", featured_media=" + featured_media +
                ", media=" + media +
                ", categories=" + categories +
                '}';
    }

    @Override
    public int describeContents () {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.created_at);
        dest.writeString(this.slug);
        dest.writeString(this.type);
        dest.writeString(this.link);
        dest.writeParcelable(this.title, flags);
        dest.writeParcelable(this.content, flags);
        dest.writeParcelable(this.excerpt, flags);
        dest.writeInt(this.authorId);
        dest.writeInt(this.featured_media);
        dest.writeParcelable(this.media, flags);
        dest.writeList(this.categories);
    }

    protected NI (Parcel in) {
        this.id = in.readInt();
        this.created_at = in.readString();
        this.slug = in.readString();
        this.type = in.readString();
        this.link = in.readString();
        this.title = in.readParcelable(WPTitle.class.getClassLoader());
        this.content = in.readParcelable(WPContent.class.getClassLoader());
        this.excerpt = in.readParcelable(WPExcerpt.class.getClassLoader());
        this.authorId = in.readInt();
        this.featured_media = in.readInt();
        this.media = in.readParcelable(WPMedia.class.getClassLoader());
        this.categories = new ArrayList<Integer>();
        in.readList(this.categories, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<NI> CREATOR = new Parcelable.Creator<NI>() {
        @Override
        public NI createFromParcel (Parcel source) {
            return new NI(source);
        }

        @Override
        public NI[] newArray (int size) {
            return new NI[size];
        }
    };
}
