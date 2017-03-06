package net.glassstones.thediarymagazine.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class WPAuthor implements Parcelable {
    /**
     * id : 1
     * name : admin
     * url :
     * description :
     * link : http://192.168.8.100/author/admin/
     * slug : admin
     * avatar_urls : {"24":"http://1.gravatar.com/avatar/4ababb69b03c28723fa5ee4be01b5520?s=24&d=mm&r=g","48":"http://1.gravatar.com/avatar/4ababb69b03c28723fa5ee4be01b5520?s=48&d=mm&r=g","96":"http://1.gravatar.com/avatar/4ababb69b03c28723fa5ee4be01b5520?s=96&d=mm&r=g"}
     * _links : {"self":[{"href":"http://192.168.8.100/wp-json/wp/v2/users/1"}],"collection":[{"href":"http://192.168.8.100/wp-json/wp/v2/users"}]}
     */

    private int id;
    private String name;
    private String url;
    private String description;
    private String link;
    private String slug;
    private AvatarUrlsBean avatar_urls;

    protected WPAuthor(Parcel in) {
        id = in.readInt();
        name = in.readString();
        url = in.readString();
        description = in.readString();
        link = in.readString();
        slug = in.readString();
    }

    public static final Creator<WPAuthor> CREATOR = new Creator<WPAuthor>() {
        @Override
        public WPAuthor createFromParcel(Parcel in) {
            return new WPAuthor(in);
        }

        @Override
        public WPAuthor[] newArray(int size) {
            return new WPAuthor[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(url);
        parcel.writeString(description);
        parcel.writeString(link);
        parcel.writeString(slug);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public AvatarUrlsBean getAvatar_urls() {
        return avatar_urls;
    }

    public void setAvatar_urls(AvatarUrlsBean avatar_urls) {
        this.avatar_urls = avatar_urls;
    }

    public static class AvatarUrlsBean implements Parcelable {
        /**
         * 24 : http://1.gravatar.com/avatar/4ababb69b03c28723fa5ee4be01b5520?s=24&d=mm&r=g
         * 48 : http://1.gravatar.com/avatar/4ababb69b03c28723fa5ee4be01b5520?s=48&d=mm&r=g
         * 96 : http://1.gravatar.com/avatar/4ababb69b03c28723fa5ee4be01b5520?s=96&d=mm&r=g
         */

        @SerializedName("24")
        private String _$24;
        @SerializedName("48")
        private String _$48;
        @SerializedName("96")
        private String _$96;

        protected AvatarUrlsBean(Parcel in) {
            _$24 = in.readString();
            _$48 = in.readString();
            _$96 = in.readString();
        }

        public static final Creator<AvatarUrlsBean> CREATOR = new Creator<AvatarUrlsBean>() {
            @Override
            public AvatarUrlsBean createFromParcel(Parcel in) {
                return new AvatarUrlsBean(in);
            }

            @Override
            public AvatarUrlsBean[] newArray(int size) {
                return new AvatarUrlsBean[size];
            }
        };

        public String get_$24() {
            return _$24;
        }

        public void set_$24(String _$24) {
            this._$24 = _$24;
        }

        public String get_$48() {
            return _$48;
        }

        public void set_$48(String _$48) {
            this._$48 = _$48;
        }

        public String get_$96() {
            return _$96;
        }

        public void set_$96(String _$96) {
            this._$96 = _$96;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(_$24);
            parcel.writeString(_$48);
            parcel.writeString(_$96);
        }
    }
}
