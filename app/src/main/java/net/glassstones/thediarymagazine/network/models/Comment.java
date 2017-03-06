package net.glassstones.thediarymagazine.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Comment implements Parcelable {
    /**
     * id : 1
     * post : 1
     * parent : 0
     * author : 0
     * author_name : A WordPress Commenter
     * author_url : https://wordpress.org/
     * date : 2016-11-21T20:44:44
     * date_gmt : 2016-11-21T20:44:44
     * content : {"rendered":"<p>Hi, this is a comment.<br />\nTo get started with moderating, editing, and deleting comments, please visit the Comments screen in the dashboard.<br />\nCommenter avatars come from <a href=\"https://gravatar.com\">Gravatar<\/a>.<\/p>\n"}
     * link : http://192.168.8.100/2016/11/21/hello-world/#comment-1
     * status : approved
     * type : comment
     * author_avatar_urls : {"24":"http://1.gravatar.com/avatar/d7a973c7dab26985da5f961be7b74480?s=24&d=mm&r=g","48":"http://1.gravatar.com/avatar/d7a973c7dab26985da5f961be7b74480?s=48&d=mm&r=g","96":"http://1.gravatar.com/avatar/d7a973c7dab26985da5f961be7b74480?s=96&d=mm&r=g"}
     * _links : {"self":[{"href":"http://192.168.8.100/wp-json/wp/v2/comments/1"}],"collection":[{"href":"http://192.168.8.100/wp-json/wp/v2/comments"}],"up":[{"embeddable":true,"post_type":"post","href":"http://192.168.8.100/wp-json/wp/v2/posts/1"}]}
     */

    private int id;
    private int post;
    private int author;
    private String author_name;
    @SerializedName("date_gmt")
    private String date;
    private ContentBean content;
    private String status;
    private String type;
    @SerializedName("author_avatar_urls")
    private AuthorAvatarUrlsBean authorAvatarUrls;

    protected Comment(Parcel in) {
        id = in.readInt();
        post = in.readInt();
        author = in.readInt();
        author_name = in.readString();
        date = in.readString();
        content = in.readParcelable(ContentBean.class.getClassLoader());
        status = in.readString();
        type = in.readString();
        authorAvatarUrls = in.readParcelable(AuthorAvatarUrlsBean.class.getClassLoader());
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AuthorAvatarUrlsBean getAuthorAvatarUrls() {
        return authorAvatarUrls;
    }

    public void setAuthorAvatarUrls(AuthorAvatarUrlsBean authorAvatarUrls) {
        this.authorAvatarUrls = authorAvatarUrls;
    }

    @Override
    public int describeContents() {
        return id;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(post);
        parcel.writeInt(author);
        parcel.writeString(author_name);
        parcel.writeString(date);
        parcel.writeParcelable(content, i);
        parcel.writeString(status);
        parcel.writeString(type);
        parcel.writeParcelable(authorAvatarUrls, i);
    }

    public static class AuthorAvatarUrlsBean implements Parcelable {

        /**
         * 24 : http://1.gravatar.com/avatar/d7a973c7dab26985da5f961be7b74480?s=24&d=mm&r=g
         * 48 : http://1.gravatar.com/avatar/d7a973c7dab26985da5f961be7b74480?s=48&d=mm&r=g
         * 96 : http://1.gravatar.com/avatar/d7a973c7dab26985da5f961be7b74480?s=96&d=mm&r=g
         */

        @SerializedName("24")
        private String small;
        @SerializedName("48")
        private String normal;
        @SerializedName("96")
        private String large;

        protected AuthorAvatarUrlsBean(Parcel in) {
            small = in.readString();
            normal = in.readString();
            large = in.readString();
        }

        public static final Creator<AuthorAvatarUrlsBean> CREATOR = new Creator<AuthorAvatarUrlsBean>() {
            @Override
            public AuthorAvatarUrlsBean createFromParcel(Parcel in) {
                return new AuthorAvatarUrlsBean(in);
            }

            @Override
            public AuthorAvatarUrlsBean[] newArray(int size) {
                return new AuthorAvatarUrlsBean[size];
            }
        };

        public void setSmall(String small) {
            this.small = small;
        }

        public void setNormal(String normal) {
            this.normal = normal;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getSmall() {
            return small;
        }

        public String getNormal() {
            return normal;
        }

        public String getLarge() {
            return large;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(small);
            parcel.writeString(normal);
            parcel.writeString(large);
        }
    }

    public static class ContentBean implements Parcelable {
        /**
         * rendered : <p>Hi, this is a comment.<br />
         To get started with moderating, editing, and deleting comments, please visit the Comments screen in the dashboard.<br />
         Commenter avatars come from <a href="https://gravatar.com">Gravatar</a>.</p>

         */

        @SerializedName("rendered")
        private String comment;

        protected ContentBean(Parcel in) {
            comment = in.readString();
        }

        public static final Creator<ContentBean> CREATOR = new Creator<ContentBean>() {
            @Override
            public ContentBean createFromParcel(Parcel in) {
                return new ContentBean(in);
            }

            @Override
            public ContentBean[] newArray(int size) {
                return new ContentBean[size];
            }
        };

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getComment() {
            return comment;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(comment);
        }
    }
}
