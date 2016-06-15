package net.glassstones.thediarymagazine.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Thompson on 08/06/2016.
 * For The Diary Magazine
 */
public class NI implements Parcelable {

    public static final Parcelable.Creator<NI> CREATOR = new Parcelable.Creator<NI>() {
        @Override
        public NI createFromParcel(Parcel source) {
            return new NI(source);
        }

        @Override
        public NI[] newArray(int size) {
            return new NI[size];
        }
    };

    private int id;
    @SerializedName("date")
    private String created_at;
    private String slug;
    private String type;
    private String link;
    /**
     * rendered : Catholic church sues Ekiti Govt over school shutdown
     */

    private WPTitle title;

    private WPContent content;

    private WPExcerpt excerpt;
    @SerializedName("author")
    private int authorId;
    private int featured_media;
    private WPMedia media;

    public NI() {
    }

    public NI(Parcel in) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
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
    }

    public static class WPTitle implements Parcelable {
        public static final Creator<WPTitle> CREATOR = new Creator<WPTitle>() {
            @Override
            public WPTitle createFromParcel(Parcel source) {
                return new WPTitle(source);
            }

            @Override
            public WPTitle[] newArray(int size) {
                return new WPTitle[size];
            }
        };
        @SerializedName("rendered")
        private String title;

        public WPTitle() {
        }

        protected WPTitle(Parcel in) {
            this.title = in.readString();
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.title);
        }
    }

    public static class WPContent implements Parcelable {
        public static final Creator<WPContent> CREATOR = new Creator<WPContent>() {
            @Override
            public WPContent createFromParcel(Parcel source) {
                return new WPContent(source);
            }

            @Override
            public WPContent[] newArray(int size) {
                return new WPContent[size];
            }
        };
        @SerializedName("rendered")
        private String content;

        public WPContent() {
        }

        protected WPContent(Parcel in) {
            this.content = in.readString();
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.content);
        }
    }

    public static class WPExcerpt implements Parcelable {
        public static final Creator<WPExcerpt> CREATOR = new Creator<WPExcerpt>() {
            @Override
            public WPExcerpt createFromParcel(Parcel source) {
                return new WPExcerpt(source);
            }

            @Override
            public WPExcerpt[] newArray(int size) {
                return new WPExcerpt[size];
            }
        };
        @SerializedName("rendered")
        private String excerpt;

        public WPExcerpt() {
        }

        protected WPExcerpt(Parcel in) {
            this.excerpt = in.readString();
        }

        public String getExcerpt() {
            return excerpt;
        }

        public void setExcerpt(String excerpt) {
            this.excerpt = excerpt;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.excerpt);
        }
    }
}
