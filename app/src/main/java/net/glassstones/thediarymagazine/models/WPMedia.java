package net.glassstones.thediarymagazine.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Thompson on 08/06/2016.
 * For The Diary Magazine
 */
public class WPMedia implements Parcelable {

    /**
     * id : 15750
     * date : 2016-02-24T08:19:29
     * date_gmt : 2016-02-24T07:19:29
     * guid : {"rendered":"http://www.thediarymagazine.com/wp-content/uploads/2016/02/fayose-1.jpg"}
     * modified : 2016-02-24T08:19:29
     * modified_gmt : 2016-02-24T07:19:29
     * slug : fayose-8
     * type : attachment
     * link : http://www.thediarymagazine.com/we-have-no-rift-with-fayose-over-aluko-ekiti-obas/fayose-8/
     * title : {"rendered":"fayose"}
     * author : 50
     * comment_status : open
     * ping_status : closed
     * alt_text :
     * caption :
     * description :
     * media_type : image
     * mime_type : image/jpeg
     * media_details : {"width":270,"height":200,"file":"2016/02/fayose-1.jpg","sizes":{"thumbnail":{"file":"fayose-1-150x150.jpg","width":150,"height":150,"mime_type":"image/jpeg","source_url":"http://i0.wp.com/www.thediarymagazine.com/wp-content/uploads/2016/02/fayose-1.jpg?resize=150%2C150"},"wptouch-new-thumbnail":{"file":"fayose-1-144x144.jpg","width":144,"height":144,"mime_type":"image/jpeg","source_url":"http://i0.wp.com/www.thediarymagazine.com/wp-content/uploads/2016/02/fayose-1.jpg?resize=144%2C144"},"article-list-sm-image-size":{"file":"fayose-1-120x120.jpg","width":120,"height":120,"mime_type":"image/jpeg","source_url":"http://i0.wp.com/www.thediarymagazine.com/wp-content/uploads/2016/02/fayose-1.jpg?resize=120%2C120"},"entry-list-image-size":{"file":"fayose-1-227x182.jpg","width":227,"height":182,"mime_type":"image/jpeg","source_url":"http://i0.wp.com/www.thediarymagazine.com/wp-content/uploads/2016/02/fayose-1.jpg?resize=227%2C182"},"full":{"file":"fayose-1.jpg?fit=270%2C200","width":270,"height":200,"mime_type":"image/jpeg","source_url":"http://i0.wp.com/www.thediarymagazine.com/wp-content/uploads/2016/02/fayose-1.jpg?fit=270%2C200"}},"image_meta":{"aperture":"0","credit":"","camera":"","caption":"","created_timestamp":"0","copyright":"","focal_length":"0","iso":"0","shutter_speed":"0","title":"","orientation":"0","keywords":[]}}
     * post : 15747
     * source_url : http://www.thediarymagazine.com/wp-content/uploads/2016/02/fayose-1.jpg
     * _links : {"self":[{"href":"http://www.thediarymagazine.com/wp-json/wp/v2/media/15750"}],"collection":[{"href":"http://www.thediarymagazine.com/wp-json/wp/v2/media"}],"about":[{"href":"http://www.thediarymagazine.com/wp-json/wp/v2/types/attachment"}],"author":[{"embeddable":true,"href":"http://www.thediarymagazine.com/wp-json/wp/v2/users/50"}],"replies":[{"embeddable":true,"href":"http://www.thediarymagazine.com/wp-json/wp/v2/comments?post=15750"}]}
     */

    private int id;
    private String media_type;
    private String mime_type;
    private String source_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.media_type);
        dest.writeString(this.mime_type);
        dest.writeString(this.source_url);
    }

    public WPMedia() {
    }

    protected WPMedia(Parcel in) {
        this.id = in.readInt();
        this.media_type = in.readString();
        this.mime_type = in.readString();
        this.source_url = in.readString();
    }

    public static final Parcelable.Creator<WPMedia> CREATOR = new Parcelable.Creator<WPMedia>() {
        @Override
        public WPMedia createFromParcel(Parcel source) {
            return new WPMedia(source);
        }

        @Override
        public WPMedia[] newArray(int size) {
            return new WPMedia[size];
        }
    };
}
