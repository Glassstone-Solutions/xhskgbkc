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
    /**
     * id : 20749
     * date : 2016-05-02T10:49:28
     * date_gmt : 2016-05-02T09:49:28
     * guid : {"rendered":"http://www.thediarymagazine.com/?p=20749"}
     * modified : 2016-05-02T10:49:28
     * modified_gmt : 2016-05-02T09:49:28
     * slug : catholic-church-sues-ekiti-govt-school-shutdown
     * type : post
     * link : http://www.thediarymagazine.com/catholic-church-sues-ekiti-govt-school-shutdown/
     * title : {"rendered":"Catholic church sues Ekiti Govt over school shutdown"}
     * content : {"rendered":"<p>The Government of Ekiti state has shut down seven schools operated by the Catholic Diocese of Ekiti state over non payment of Education Development levy.<\/p>\n<p>The Church <a href=\"https://www.bellanaija.com/2016/04/ekiti-catholic-church-threatens-to-sue-state-government-over-proposed-education-tax/\" onclick=\"__gaTracker('send', 'event', 'outbound-article', 'https://www.bellanaija.com/2016/04/ekiti-catholic-church-threatens-to-sue-state-government-over-proposed-education-tax/', 'as it promised');\">as it promised<\/a> has taken the state government to court as a result of the development. The suit was filed by the Incorporated Trustees of the Catholic Diocese of Ekiti, and the defendants are the state Attorney-General and Commissioner for Justice as well as the Commissioner for Education.<\/p>\n<p>The church said that the action violated Section 2 of Compulsory Free Universal Basic Education Act 2004 and Section 19 of the state Universal Basic Education Board Law, asked the court to declare the imposition of such levies as illegal and unconstitutional.<\/p>\n<p>The church asked the court to determine \u201cwhether every child of primary school and junior secondary school age in the state is not entitled to free and compulsory basic education under Section 2 of Compulsory Free Universal Education Act, 2004 and Section 19 of Ekiti State Universal Basic Education Board Law\u201d.<\/p>\n<p>The state Commissioner for Education, Science and Technology, <strong>Jide Egunjobi<\/strong>, who confirmed the closure of schools said: \u201cIt is true, we shut the schools that failed to pay. Schools that paid are in operation. We only shut those that refused to pay. We all agreed to the development levy at the education summit, so, there is no going back.\u201d<\/p>\n<p>Source: Bella Naija\n<div class=\"pvc_clear\"><\/div>\n<p class=\"pvc_stats\" element-id=\"20749\">62,513&nbsp;total views, 2,166&nbsp;views today<\/p>\n<div class=\"pvc_clear\"><\/div>\n"}
     * excerpt : {"rendered":"<p>The Government of Ekiti state has shut down seven schools operated by the Catholic Diocese of Ekiti state over non&#8230;\n<div class=\"pvc_clear\"><\/div>\n<p class=\"pvc_stats\" element-id=\"20749\">62,513&nbsp;total views, 2,166&nbsp;views today<\/p>\n<div class=\"pvc_clear\"><\/div>\n"}
     * author : 93
     * featured_media : 15750
     * comment_status : open
     * ping_status : open
     * sticky : false
     * format : standard
     * categories : [612,24,20]
     * tags : [6355,256,2216]
     * _links : {"self":[{"href":"http://www.thediarymagazine.com/wp-json/wp/v2/posts/20749"}],"collection":[{"href":"http://www.thediarymagazine.com/wp-json/wp/v2/posts"}],"about":[{"href":"http://www.thediarymagazine.com/wp-json/wp/v2/types/post"}],"author":[{"embeddable":true,"href":"http://www.thediarymagazine.com/wp-json/wp/v2/users/93"}],"replies":[{"embeddable":true,"href":"http://www.thediarymagazine.com/wp-json/wp/v2/comments?post=20749"}],"version-history":[{"href":"http://www.thediarymagazine.com/wp-json/wp/v2/posts/20749/revisions"}],"wp:featuredmedia":[{"embeddable":true,"href":"http://www.thediarymagazine.com/wp-json/wp/v2/media/15750"}],"wp:attachment":[{"href":"http://www.thediarymagazine.com/wp-json/wp/v2/media?parent=20749"}],"wp:term":[{"taxonomy":"category","embeddable":true,"href":"http://www.thediarymagazine.com/wp-json/wp/v2/categories?post=20749"},{"taxonomy":"post_tag","embeddable":true,"href":"http://www.thediarymagazine.com/wp-json/wp/v2/tags?post=20749"}],"curies":[{"name":"wp","href":"https://api.w.org/{rel}","templated":true}]}
     */

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
    /**
     * rendered : <p>The Government of Ekiti state has shut down seven schools operated by the Catholic Diocese of Ekiti state over non payment of Education Development levy.</p>
     * <p>The Church <a href="https://www.bellanaija.com/2016/04/ekiti-catholic-church-threatens-to-sue-state-government-over-proposed-education-tax/" onclick="__gaTracker('send', 'event', 'outbound-article', 'https://www.bellanaija.com/2016/04/ekiti-catholic-church-threatens-to-sue-state-government-over-proposed-education-tax/', 'as it promised');">as it promised</a> has taken the state government to court as a result of the development. The suit was filed by the Incorporated Trustees of the Catholic Diocese of Ekiti, and the defendants are the state Attorney-General and Commissioner for Justice as well as the Commissioner for Education.</p>
     * <p>The church said that the action violated Section 2 of Compulsory Free Universal Basic Education Act 2004 and Section 19 of the state Universal Basic Education Board Law, asked the court to declare the imposition of such levies as illegal and unconstitutional.</p>
     * <p>The church asked the court to determine “whether every child of primary school and junior secondary school age in the state is not entitled to free and compulsory basic education under Section 2 of Compulsory Free Universal Education Act, 2004 and Section 19 of Ekiti State Universal Basic Education Board Law”.</p>
     * <p>The state Commissioner for Education, Science and Technology, <strong>Jide Egunjobi</strong>, who confirmed the closure of schools said: “It is true, we shut the schools that failed to pay. Schools that paid are in operation. We only shut those that refused to pay. We all agreed to the development levy at the education summit, so, there is no going back.”</p>
     * <p>Source: Bella Naija
     * <div class="pvc_clear"></div>
     * <p class="pvc_stats" element-id="20749">62,513&nbsp;total views, 2,166&nbsp;views today</p>
     * <div class="pvc_clear"></div>
     */

    private WPContent content;
    /**
     * rendered : <p>The Government of Ekiti state has shut down seven schools operated by the Catholic Diocese of Ekiti state over non&#8230;
     * <div class="pvc_clear"></div>
     * <p class="pvc_stats" element-id="20749">62,513&nbsp;total views, 2,166&nbsp;views today</p>
     * <div class="pvc_clear"></div>
     */

    private WPExcerpt excerpt;
    @SerializedName("author")
    private int authorId;
    private int featured_media;
    private WPMedia media;

    public NI() {
    }

    protected NI(Parcel in) {
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
